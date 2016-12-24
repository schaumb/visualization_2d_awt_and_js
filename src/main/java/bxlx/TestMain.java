package bxlx;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Polygon;

import java.util.function.Consumer;

/**
 * Created by qqcs on 2016.12.24..
 */
public class TestMain {
    public static class ConsumerX implements MyConsumer<ICanvas> {
        long a = 0;
        @Override
        public void accept(ICanvas c) {
            c.clearCanvas();
            c.setColor(Color.CYAN);
            Size size = c.getBoundingRectangle().getSize();
            double percent = 0;
            long time = SystemSpecific.get().getTime();
            if(a == 0 || time - a > 1000) {
                a = time;
            } else {
                percent = (time - a) / 1000.0;
            }
            c.fillArc(new Arc(new Point(size.getWidth() / 2.0, size.getHeight() / 2.0),
                    Math.min(size.getHeight(), size.getWidth()) / 2,
                    0, Math.PI * percent));
        }
    }
    public TestMain() {
        SystemSpecific.get().setDrawFunction(new ConsumerX());
    }
}
