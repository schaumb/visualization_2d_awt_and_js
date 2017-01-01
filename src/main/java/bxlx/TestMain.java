package bxlx;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;
import bxlx.pipe.Rect;

/**
 * Created by qqcs on 2016.12.24..
 */
public class TestMain {
    public static class ConsumerX implements IRenderer {
        long a = 0;

        @Override
        public boolean render(ICanvas c) {
            c.clearCanvas(Color.WHITE);

            c.clip(new Rectangle(new Point(20, 20), c.getBoundingRectangle().getSize().asPoint().add(-40).asSize()));
            new Rect().draw(c);
            c.restore();
            c.setColor(Color.GREEN);
            c.setFont("sans-serif", 30, false, false);
            c.fillText("Szia", new Point(40, 40));
            return false;
        }
    }

    public TestMain() {
        SystemSpecific.get().setDrawFunction(new ConsumerX());
    }
}
