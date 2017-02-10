package bxlx.awt;

import bxlx.graphics.Color;
import bxlx.graphics.Cursor;
import bxlx.graphics.Font;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.system.CommonError;
import bxlx.system.IMouseEventListener;
import bxlx.system.IRenderer;
import bxlx.system.SystemSpecific;
import bxlx.system.Timer;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by qqcs on 2016.12.23..
 */
public class AwtSystemSpecific extends SystemSpecific {
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
    public void readTextFileAsync(String fileName, Consumer<String> consumer) {
        Thread t = new Thread(() -> {
            try {
                consumer.accept(Files.readAllLines(Paths.get(fileName)).stream().collect(Collectors.joining("\n")));
            } catch (IOException e) {
                consumer.accept(null);
            }
        });
        t.setDaemon(true);
        t.start();
    }

    @Override
    public Size preLoad(String src, boolean img) {
        if (img) {
            BufferedImage imgBuff = GraphicsCanvas.imageCaches.get(src);
            if (imgBuff == null) {
                return Size.NULL;
            }
            return new Size(imgBuff.getWidth(), imgBuff.getHeight());
        }

        return null;
    }

    @Override
    public Color getColor(String pic, double x, double y) {
        BufferedImage img = GraphicsCanvas.imageCaches.get(pic);
        int rgba = img.getRGB((int) Math.min(img.getWidth() - 1, Math.round(x * img.getWidth())), (int) Math.min(img.getHeight() - 1, Math.round(y * img.getHeight())));
        return new Color(rgba);
    }

    @Override
    public <T> boolean equals(T first, T second) {
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
    public void setMinimumSize(Size size) {
        super.setMinimumSize(size);
        if (frame != null) {
            frame.setMinimumSize(new Dimension((int) size.getWidth(), (int) size.getHeight()));
        }
    }
}
