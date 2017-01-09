package bxlx.awt;

import bxlx.graphics.Point;
import bxlx.system.CommonError;
import bxlx.system.Consumer;
import bxlx.system.IMouseEventListener;
import bxlx.system.IRenderer;
import bxlx.system.SystemSpecific;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by qqcs on 2016.12.23..
 */
public class AwtSystemSpecific extends SystemSpecific {
    private JFrame frame;
    private JPanel panel;

    private AwtSystemSpecific() {
    }

    public static SystemSpecific create() {
        if (INSTANCE != null) return get();
        return new AwtSystemSpecific();
    }

    private void refresh() {
        if (panel != null) {
            panel.revalidate();
            panel.repaint();
        }
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDrawFunction(IRenderer renderer) {
        if (frame == null) {
            frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 500);
            frame.setVisible(true);
            frame.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    if (!isRendering()) {
                        EventQueue.invokeLater(AwtSystemSpecific.this::refresh);
                    }
                }
            });
        } else {
            frame.remove(panel);
        }
        frame.add(panel = new JPanel() {
            private BufferedImage img;
            private GraphicsCanvas canvas;

            @Override
            protected void paintComponent(Graphics graphics) {
                //super.paintComponent(graphics);

                Rectangle rect = graphics.getClipBounds();
                if (img == null || img.getWidth() != rect.width || img.getHeight() != rect.height) {
                    img = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_ARGB);
                    canvas = new GraphicsCanvas((Graphics2D) img.getGraphics(), rect);
                }

                if (rendering = renderer.render(canvas)) {
                    EventQueue.invokeLater(AwtSystemSpecific.this::refresh);
                }

                graphics.drawImage(img, 0, 0, null);
            }
        });
        if (!isRendering()) {
            EventQueue.invokeLater(this::refresh);
        }
    }

    @Override
    public void setMouseEventListener(IMouseEventListener listener) {
        if (panel != null) {
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
            Player player = Manager.createPlayer(new MediaLocator(new File(src).toURI().toURL()));
            player.start();
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
}
