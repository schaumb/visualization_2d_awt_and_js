package bxlx;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphics.shapes.Rectangle;
import bxlx.pipe.Rect;

/**
 * Created by qqcs on 2016.12.24..
 */
public class TestMain {
    public static class ConsumerX implements IRenderer {
        Timer timer = new Timer(6000);

        @Override
        public boolean render(ICanvas c) {
            c.clearCanvas(Color.WHITE);

            c.clip(new Rectangle(new Point(20, 20), c.getBoundingRectangle().getSize().asPoint().add(-40).asSize()));
            int n = (int) (timer.percent() * 0.99999 * 8 + 3);
            Polygon p = Polygon.nGon(n, c.getBoundingRectangle().getCenter(),
                    Math.min(c.getBoundingRectangle().getSize().getHeight(),
                            c.getBoundingRectangle().getSize().getWidth()) / 2, Math.PI / 4.0 * (n-3));

            c.setColor(Color.LIGHT_GRAY);
            c.fillRectangle(p.getBoundingRectangle());
            c.setColor(Color.RED);
            c.fillPolygon(p);
            c.restore();
            if(timer.elapsed()) {
                timer.setStart();
            }
            return true;
        }
    }

    public TestMain() {
        SystemSpecific.get().setDrawFunction(new ConsumerX());
    }
}
