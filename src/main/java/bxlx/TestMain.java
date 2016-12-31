package bxlx;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
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

            c.clip(new Rectangle(new Point(20, 20), new Size(40, 40)));
            new Rect().draw(c);
            c.restore();
            c.setColor(Color.GREEN);
            c.setFont("sans-serif", 30, false, false);
            c.fillText("Szia", new Point(40, 40));
        }
    }
    public TestMain() {
        SystemSpecific.get().setDrawFunction(new ConsumerX());
    }
}
