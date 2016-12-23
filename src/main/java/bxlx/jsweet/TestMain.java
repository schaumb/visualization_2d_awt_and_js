package bxlx.jsweet;


import bxlx.graphics.Color;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Arc;

import static jsweet.dom.Globals.console;

/**
 * Created by qqcs on 2016.12.22..
 */
public class TestMain {

    public static void main(String[] args) {
        JSweetSystemSpecific.create().setDrawFunction(c -> {
            c.setColor(Color.GREEN);
            Size size = c.getBoundingRectangle().getSize();
            c.fillArc(Arc.circle(c.getBoundingRectangle().getCenter(), Math.min(size.getHeight(), size.getWidth()) / 2));
        });
    }
}
