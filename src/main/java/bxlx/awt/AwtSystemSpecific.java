package bxlx.awt;

import bxlx.graphics.Color;
import bxlx.graphics.Cursor;
import bxlx.graphics.Font;
import bxlx.graphics.Point;
import bxlx.graphics.*;
import bxlx.system.*;
import bxlx.system.Timer;
import bxlx.system.functional.MySocket;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by qqcs on 2016.12.23..
 */
public class AwtSystemSpecific extends SystemSpecific {
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(5, runnable -> {
        Thread t = Executors.defaultThreadFactory().newThread(runnable);
        t.setDaemon(true);
        return t;
    });
    private static final int MAX_LENGTH = 1024 * 5 * 20;
    private final HashMap<String, ObservableValue<Size>> imageSizes = new HashMap<>();
    private JFrame frame;
    private JPanel panel;
    private IRenderer renderer;
    private boolean newRenderer = true;
    private GraphicsCanvas graphicsCanvas;

    private AwtSystemSpecific() {
    }

    public static SystemSpecific create() {
        if (INSTANCE != null) return get();
        return new AwtSystemSpecific();
    }

    private void refresh() {
        if (panel != null) {
            panel.repaint();
        }
    }

    @Override
    public void setDrawFunction(IRenderer renderer) {
        this.newRenderer = this.renderer != renderer;
        this.renderer = renderer;
        if (frame == null) {
            frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setSize(d.width, d.height);
            frame.setMinimumSize(new Dimension((int) minimumSize.getWidth(), (int) minimumSize.getHeight()));
            frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    if (!isRendering()) {
                        EventQueue.invokeLater(AwtSystemSpecific.this::refresh);
                    }
                }
            });
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowIconified(WindowEvent windowEvent) {
                    rendering = false;
                }

                @Override
                public void windowDeiconified(WindowEvent windowEvent) {
                    if (!isRendering()) {
                        EventQueue.invokeLater(AwtSystemSpecific.this::refresh);
                    }
                }
            });

            frame.add(panel = new JPanel() {
                private BufferedImage img;

                @Override
                protected void paintComponent(Graphics graphics) {
                    Timer timer = new Timer(1000 / 60);
                    Rectangle rect = graphics.getClipBounds();
                    if (img == null || img.getWidth() != rect.width || img.getHeight() != rect.height || newRenderer) {
                        img = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_ARGB);
                        renderer.setCanvas(graphicsCanvas = new GraphicsCanvas((Graphics2D) img.getGraphics(), rect));
                        newRenderer = false;
                    }

                    if (rendering = renderer.render()) {
                        EventQueue.invokeLater(AwtSystemSpecific.this::refresh);
                    }

                    graphics.drawImage(img, 0, 0, null);

                    try {
                        long need = timer.need();
                        if (need > 0) {
                            Thread.sleep(need);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            frame.setVisible(true);
            setMouseEventListeners();
        }
        if (!isRendering()) {
            EventQueue.invokeLater(this::refresh);
        }
    }

    @Override
    public void setMouseEventListeners() {
        if (panel != null) {
            for (IMouseEventListener listener : listeners) {
                MouseAdapter adapter = new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {
                        java.awt.Point p = mouseEvent.getPoint();
                        listener.down(new Point(p.getX(), p.getY()), mouseEvent.getButton() == MouseEvent.BUTTON1);
                    }

                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {
                        java.awt.Point p = mouseEvent.getPoint();
                        listener.up(new Point(p.getX(), p.getY()), mouseEvent.getButton() == MouseEvent.BUTTON1);
                    }

                    @Override
                    public void mouseMoved(MouseEvent mouseEvent) {
                        java.awt.Point p = mouseEvent.getPoint();
                        listener.move(new Point(p.getX(), p.getY()));
                    }

                    @Override
                    public void mouseDragged(MouseEvent mouseEvent) {
                        java.awt.Point p = mouseEvent.getPoint();
                        listener.move(new Point(p.getX(), p.getY()));
                    }
                };
                panel.addMouseListener(adapter);
                panel.addMouseMotionListener(adapter);
            }
            listeners.clear();
        }
    }

    @Override
    public boolean isEqual(double d1, double d2) {
        return Double.compare(d1, d2) == 0;
    }

    @Override
    public long getTime() {
        return new Date().getTime();
    }

    @Override
    public void log(String message) {
        System.out.println(message);
    }

    @Override
    public void log(CommonError commonError, String message) {
        System.out.println("ERROR: " + commonError.name + " - " + commonError.message + " - " + message);
    }

    @Override
    public void playMusic(String src) {
        // TODO mp3? only wav files!
        try {
            Manager.createPlayer(new MediaLocator(new File(src).toURI().toURL())).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void readTextFileAsync(String from, String fileName, Consumer<String> consumer) {
        executor.submit(() -> {
            try {
                consumer.accept(Files.readAllLines(Paths.get(from, fileName)).stream().collect(Collectors.joining("\n")));
            } catch (IOException e) {
                consumer.accept(null);
            }
        });
    }

    @Override
    public void preLoad(String src, boolean img) {
        if (img) {
            GraphicsCanvas.imageCaches.get(src);
        }
        // TODO music preload
    }

    public ObservableValue<Size> imageSize(String src) {
        BufferedImage imgBuff = GraphicsCanvas.imageCaches.get(src);

        if (imgBuff == null) {
            return imageSizes.computeIfAbsent(src, s -> new ObservableValue<>())
                    .setValue(Size.ZERO);
        }
        return imageSizes.computeIfAbsent(src, s -> new ObservableValue<>())
                .setValue(new Size(imgBuff.getWidth(), imgBuff.getHeight()));
    }

    @Override
    public Color getColor(String pic, double x, double y) {
        BufferedImage img = GraphicsCanvas.imageCaches.get(pic);
        int rgba = img.getRGB((int) Math.min(img.getWidth() - 1, Math.round(x * img.getWidth())), (int) Math.min(img.getHeight() - 1, Math.round(y * img.getHeight())));
        return new Color(rgba);
    }

    @Override
    public <T> boolean isEquals(T first, T second) {
        return Objects.equals(first, second);
    }

    @Override
    public int stringLength(Font font, String string) {
        if (graphicsCanvas != null) {
            Font tmp = graphicsCanvas.getFont();
            if (font != null) {
                graphicsCanvas.setFont(font);
            }
            int res = graphicsCanvas.textWidth(string);
            graphicsCanvas.setFont(tmp);
            return res;
        }
        return -1;
    }

    @Override
    public void open(String thing) {
        String message = "Open is not supported";
        boolean success = false;
        if (Desktop.isDesktopSupported()) {
            try {
                if (thing.startsWith("http:") || thing.startsWith("www.")) {
                    Desktop.getDesktop().browse(URI.create(thing));
                } else {
                    File file = new File(thing);
                    Desktop.getDesktop().open(file);
                }
                success = true;
            } catch (IOException | IllegalArgumentException e) {
                message = e.getLocalizedMessage();
            }
        }

        if (!success) {
            JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void logout() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void setCursor(Cursor cursor) {
        java.awt.Cursor awtCursor = java.awt.Cursor.getDefaultCursor();
        switch (cursor) {
            case DEFAULT:
                break;
            case HAND:
                awtCursor = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR);
                break;
        }
        panel.setCursor(awtCursor);
    }

    @Override
    public void runAfter(Runnable run, int millisec) {
        executor.schedule(run, millisec, TimeUnit.MILLISECONDS);
    }

    @Override
    public void setMinimumSize(Size size) {
        super.setMinimumSize(size);
        if (frame != null) {
            frame.setMinimumSize(new Dimension((int) size.getWidth(), (int) size.getHeight()));
        }
    }

    @Override
    public MySocket openSocket(String to, Consumer<String> atRead, Consumer<String> atError, Consumer<MySocket> atReady) {
        try {
            AsynchronousSocketChannel open = AsynchronousSocketChannel.open();
            String[] split = to.split(":");

            open.connect(new InetSocketAddress(split[0], split.length > 1 ? Integer.parseUnsignedInt(split[1]) : 80));
            ByteBuffer allocate = ByteBuffer.allocate(1024);

            CompletionHandler<Integer, Void> ch = new CompletionHandler<Integer, Void>() {
                @Override
                public void completed(Integer result, Void attachment) {
                    if (result == -1) {
                        failed(new RuntimeException("Connection closed by server"), attachment);
                        return;
                    }


                    byte[] decoded = decodeMessage(Arrays.copyOf(allocate.array(), result), open);

                    if (decoded != null) {
                        atRead.accept(new String(decoded, 0, decoded.length));
                    }

                    allocate.clear();

                    open.read(allocate, attachment, this);
                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    atError.accept(exc.getMessage());
                }
            };

            StringBuilder str = new StringBuilder();
            final Random random = new Random();
            for (int i = 0; i < 16; ++i) {
                str.append((char) random.nextInt());
            }

            open.write(ByteBuffer.wrap(("GET / HTTP/1.1\r\n" +
                    "Host: " + to + "\r\n" +
                    "Sec-WebSocket-Key: " + Arrays.toString(Base64.getEncoder()
                    .encode(str.toString().getBytes(StandardCharsets.ISO_8859_1))) + "\r\n" +
                    "Connection: Upgrade\r\n" +
                    "Upgrade: websocket\r\n\r\n").getBytes()));

            final MySocket mySocket = new MySocket() {
                @Override
                public void write(String what) {
                    open.write(ByteBuffer.wrap(encodeMessage(what.getBytes())));
                }

                @Override
                public void close() {
                    try {
                        open.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            open.read(allocate, null, new CompletionHandler<Integer, Void>() {
                @Override
                public void completed(Integer integer, Void attachment) {
                    if (integer < 0) {
                        failed(new RuntimeException("Not got first message"), attachment);
                        return;
                    }

                    // we assume that is OK :)
                    allocate.clear();

                    open.read(allocate, attachment, ch);

                    atReady.accept(mySocket);
                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    atError.accept(exc.getMessage());
                }
            });

            return mySocket;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] decodeMessage(byte[] in, AsynchronousSocketChannel channel) {
        AtomicInteger start = new AtomicInteger(0);
        AtomicInteger size = new AtomicInteger(MAX_LENGTH);
        WebSocketFrameType type = getFrame(in, start, size);

        switch (type) {
            case PING_FRAME:
                channel.write(ByteBuffer.wrap(makeFrame(WebSocketFrameType.PONG_FRAME, new byte[0])));
            case INCOMPLETE_FRAME:
            case OPENING_FRAME:
            case CLOSING_FRAME:
            case INCOMPLETE_TEXT_FRAME:
            case INCOMPLETE_BINARY_FRAME:
            case BINARY_FRAME:
            case PONG_FRAME:
                // OK :)
                return new byte[0];
            case TEXT_FRAME:
                return Arrays.copyOfRange(in, start.get(), size.get() + start.get());
            case ERROR_FRAME:
            default:
                return null;
        }
    }

    private WebSocketFrameType getFrame(byte[] in, AtomicInteger start, AtomicInteger size) {
        if (in.length < 3) return WebSocketFrameType.INCOMPLETE_FRAME;

        int messageLength;
        int pos = 2;
        int wholeLength = (in[1] & 0xFF) & (~0x80);

        if (wholeLength <= 125) {
            messageLength = wholeLength;
        } else if (wholeLength == 126) {
            messageLength = (in[2] & 0xFF) + ((in[3] & 0xFF) << 8);
            pos += 2;
        } else {

            messageLength = (in[2] & 0xFF) + ((in[3] & 0xFF) << 8) +
                    ((in[4] & 0xFF) << 16);
            pos += 8;
        }
        if (in.length < messageLength + pos) {
            return WebSocketFrameType.INCOMPLETE_FRAME;
        }

        if ((((in[1] & 0xFF) >> 7) & 0x01) > 0) { // masked message
            pos += 4;

            for (int i = 0; i < messageLength; ++i) {
                in[pos + i] = (byte) (in[pos + i] ^ in[pos - 4 + i % 4]);
            }
        }

        start.set(pos);
        size.set(messageLength);

        boolean completedMessage = (((in[0] & 0xFF) >> 7) & 0x01) > 0;
        switch (in[0] & 0xFF & 0x0F) {
            case 0x0:
            case 0x1:
                return completedMessage ? WebSocketFrameType.TEXT_FRAME : WebSocketFrameType.INCOMPLETE_TEXT_FRAME;
            case 0x2:
                return completedMessage ? WebSocketFrameType.BINARY_FRAME : WebSocketFrameType.INCOMPLETE_BINARY_FRAME;
            case 0x9:
                return WebSocketFrameType.PING_FRAME;
            case 0xA:
                return WebSocketFrameType.PONG_FRAME;
            default:
                return WebSocketFrameType.ERROR_FRAME;
        }
    }

    private byte[] encodeMessage(byte[] out) {
        if (out.length == 0) {
            return out;
        }
        return makeFrame(WebSocketFrameType.TEXT_FRAME, out);
    }

    private byte[] makeFrame(WebSocketFrameType frameType, byte[] from) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(frameType.getNum() & 0xFF);
        if (from.length <= 125) {
            byteArrayOutputStream.write(from.length);
        } else if (from.length <= 65535) {
            byteArrayOutputStream.write(126);
            byteArrayOutputStream.write((from.length >> 8) & 0xFF);
            byteArrayOutputStream.write(from.length & 0xFF);
        } else {
            byteArrayOutputStream.write(127);
            byteArrayOutputStream.write(new byte[]{0, 0, 0, 0}, 0, 4);
            for (int i = 0; i < 4; ++i) {
                byteArrayOutputStream.write((from.length >> 8 * (4 - i)) & 0xFF);
            }
        }
        byteArrayOutputStream.write(from, 0, from.length);
        return byteArrayOutputStream.toByteArray();
    }

    private enum WebSocketFrameType {
        ERROR_FRAME(0xFF00),
        INCOMPLETE_FRAME(0xFE00),

        OPENING_FRAME(0x3300),
        CLOSING_FRAME(0x3400),

        INCOMPLETE_TEXT_FRAME(0x01),
        INCOMPLETE_BINARY_FRAME(0x02),

        TEXT_FRAME(0x81),
        BINARY_FRAME(0x82),

        PING_FRAME(0x19),
        PONG_FRAME(0x1A);

        private final int num;

        WebSocketFrameType(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    }
}
