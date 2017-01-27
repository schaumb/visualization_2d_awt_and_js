package bxlx.system;

import bxlx.graphics.Color;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;

/**
 * Created by qqcs on 2017.01.03..
 */
public class FPS implements IDrawable {
    private Timer timer;
    private long drewFrames;

    @Override
    public Redraw needRedraw() {
        return new Redraw(Redraw.I_NEED_REDRAW);
    }

    @Override
    public void setRedraw() {

    }

    @Override
    public void forceDraw(ICanvas canvas) {
        Color tmp = canvas.getColor();
        double fps = getFps();
        if (timer == null) {
            drewFrames = 0;
            timer = new Timer(60000);
        }
        ++drewFrames;
        canvas.setColor(Color.BLUE);
        canvas.fill(new Rectangle(0, 0, 115, 30));
        canvas.setColor(Color.RED.getScale(Color.GREEN, (Math.max(10, Math.min(60, fps)) - 10.0) / 50.0));
        canvas.setFont("sans-serif", 20, false, false);
        canvas.fillText(((int) (Math.min(100, fps) * 100)) / 100.0 + " fps", new Point(10, 20));
        if (timer.elapsed()) {
            timer = null;
        }

        canvas.setColor(tmp);
    }

    public double getFps() {
        return timer == null ? 0.0 : drewFrames * 1000.0 / timer.elapsedTime();
    }

    public void reset() {
        timer = null;
        drewFrames = 0;
    }
}
