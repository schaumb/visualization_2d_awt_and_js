package bxlx;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;

/**
 * Created by qqcs on 2017.01.03..
 */
public class FPS implements IDrawable {
    private Timer timer;
    private long drawedFrames;

    @Override
    public void draw(ICanvas canvas) {
        double fps = getFps();
        if (timer == null) {
            drawedFrames = 0;
            timer = new Timer(60000);
        }
        ++drawedFrames;
        canvas.setColor(Color.BLUE);
        canvas.fill(new Rectangle(0, 0, 115, 30));
        canvas.setColor(Color.RED.getScale(Color.GREEN, (Math.max(10, Math.min(60, fps)) - 10.0) / 50.0));
        canvas.setFont("sans-serif", 20, false, false);
        canvas.fillText(((int) (fps * 100)) / 100.0 + " fps", new Point(10, 20));
        if (timer.elapsed()) {
            timer = null;
        }
    }

    public double getFps() {
        return timer == null ? 0.0 : drawedFrames * 1000.0 / timer.elapsedTime();
    }
}
