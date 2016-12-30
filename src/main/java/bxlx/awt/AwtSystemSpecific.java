package bxlx.awt;

import bxlx.CommonError;
import bxlx.MyConsumer;
import bxlx.SystemSpecific;
import bxlx.graphics.ICanvas;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.io.File;
import java.util.Date;

/**
 * Created by qqcs on 2016.12.23..
 */
public class AwtSystemSpecific extends SystemSpecific {
    private JFrame frame;
    private JPanel panel;
    private ICanvas canvas;

    private AwtSystemSpecific() {}

    public static SystemSpecific create() {
        if(INSTANCE != null) return get();
        return new AwtSystemSpecific();
    }

    private void refresh() {
        if(panel != null) {
            panel.revalidate();
            panel.repaint();
        }
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(this::refresh);
    }

    @Override
    public void setDrawFunction(MyConsumer<ICanvas> canvasConsumer) {
        if(frame == null) {
            frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 500);
            frame.setVisible(true);
            EventQueue.invokeLater(this::refresh);
        } else {
            frame.remove(panel);
        }

        frame.add(panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                //super.paintComponent(graphics);

                canvasConsumer.accept(canvas = new GraphicsCanvas(graphics));
            }
        });
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
        try{
            Player player = Manager.createPlayer(new MediaLocator(new File(src).toURI().toURL()));
            player.start();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
