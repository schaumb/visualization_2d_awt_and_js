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
    public static class ConsumerX implements MyConsumer<ICanvas> {
        long a = 0;
        @Override
        public void accept(ICanvas c) {
            c.clearCanvas(Color.WHITE);
            c.clip(new Rectangle(new Point(20, 20), c.getBoundingRectangle().getSize().asPoint().add(-40).asSize()));
            new Rect().draw(c);
            c.restore();
        }
    }
    public TestMain() {
        SystemSpecific.get().setDrawFunction(new ConsumerX());
    }
}
