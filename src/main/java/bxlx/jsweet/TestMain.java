package bxlx.jsweet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Panel;
import java.util.HashMap;

import static jsweet.dom.Globals.console;
import static jsweet.lang.Object.freeze;

/**
 * Created by qqcs on 2016.12.22..
 */
public class TestMain extends JFrame {

    public TestMain(String s) {
        super(s);
    }

    public static void main(String[] args) {
        JFrame frame = new TestMain("JFrame Demo");
        JPanel panel1 = new JPanel() {


            @Override
            public void paint(Graphics graphics) {
                super.paint(graphics);

                setBackground(Color.GRAY);
                graphics.setColor(Color.BLACK);
                graphics.fillRect(10, 10, 20, 20);
                if(console != null) {
                    console.log("We are young,we are free" + graphics.toString());
                }
            }
        };
        frame.getContentPane().setSize(0, 0);
        BorderLayout lo = (BorderLayout) frame.getLayout();
        lo.setHgap(1);
        lo.setVgap(1);
        frame.setContentPane(panel1);
        frame.getContentPane().setSize(400, 500);
        frame.getContentPane().setBackground(Color.GRAY);

        if(console != null) {
            console.log(frame.getContentPane());
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.validate();
        frame.setVisible(true);
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(10, 10, 20, 20);
        if(console != null) {
            console.log("At beginning");
        }
    }
}
