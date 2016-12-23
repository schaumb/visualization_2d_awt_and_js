package bxlx.awt;

import bxlx.SystemSpecific;
import bxlx.graphics.*;
import bxlx.graphics.Color;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Arc;

import java.awt.*;

/**
 * Created by qqcs on 2016.12.22..
 */
public class TestMain {


    public static void main(String[] args) {
        AwtSystemSpecific.create().setDrawFunction(c -> {
            c.setColor(Color.GREEN);
            Size size = c.getBoundingRectangle().getSize();
            c.fillArc(Arc.circle(c.getBoundingRectangle().getCenter(), Math.min(size.getHeight(), size.getWidth()) / 2));
        });
    }
}
