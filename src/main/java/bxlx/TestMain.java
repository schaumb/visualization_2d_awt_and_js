package bxlx;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.shapes.Polygon;

/**
 * Created by qqcs on 2016.12.24..
 */
public class TestMain {
    public static class ConsumerX implements IRenderer {
        Timer timer = new Timer(6000);
        FPS fps = new FPS();

        @Override
        public boolean render(ICanvas c) {

            c.setColor(Color.CYAN);
            c.fill(Polygon.nGon(5, c.getBoundingRectangle().getCenter(),
                    c.getBoundingRectangle().getSize().getShorterDimension() / 2, 0));
            if (timer.elapsed()) {
                timer.setStart();
            }

            fps.draw(c);

            return true;
        }
    }

    public TestMain() {
        SystemSpecific.get().setDrawFunction(new ConsumerX());
    }
}
