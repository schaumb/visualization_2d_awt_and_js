package bxlx.conticup17;

import bxlx.graphics.*;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.combined.Stick;
import bxlx.graphics.container.Container;
import bxlx.graphics.drawable.ShapeDrawable;
import bxlx.graphics.fill.DrawNGon;
import bxlx.graphics.shapes.Polygon;
import bxlx.system.SystemSpecific;
import bxlx.system.Timer;

import java.util.Arrays;

/**
 * Created by qqcs on 5/8/17.
 */
public class RobotDrawable extends ChangeableDrawable {
    private final RobotStates.RobotPlayer player;

    private final ChangeableTimer timer = new ChangeableTimer(this, new Timer(10000));

    public RobotDrawable(RobotStates.RobotPlayer player) {
        this.player = player;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        double time = timer.get();

        double angle = time * 2 * Math.PI;
        Point dir = Direction.fromRadian(angle).getVector();
        Point othDir = new Point(-dir.getY(), dir.getX());
        double length = canvas.getBoundingRectangle().getSize().getShorterDimension();
        Point center = canvas.getBoundingRectangle().getCenter();

        Builder.container()
                .add(Builder.circle(true)
                        .makeColored(Color.BLACK)
                        .makeMargin(0.8).get())
                .add(Builder.circle(true)
                        .makeColored(Color.YELLOW)
                        .makeMargin(2)
                        .makeMargin(0.8).get())
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(othDir.multiple(length * 0.5 * 0.2 * 0.7 * 0.5 * 0.707)).add(dir.multiple(-50)),
                        center.add(othDir.multiple(length * 0.5 * 0.2 * 0.7 * 0.5 * 0.707)).add(dir.multiple(50)),
                        center.add(othDir.multiple(0)).add(dir.multiple(50)),
                        center.add(othDir.multiple(0)).add(dir.multiple(-50)))))
                        .makeColored(Color.GREEN)
                        .get())
                .add(Builder.nGon(true, false, 4, angle + Math.PI / 4, true)
                        .makeColored(Color.BLACK)
                        .makeMargin(0.3)
                        .makeMargin(0.8).get())
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(othDir.multiple(length * 0.5 * 0.2 * 0.7 * 0.5 * 0.707)).add(dir.multiple(-30)),
                        center.add(othDir.multiple(length * 0.5 * 0.2 * 0.7 * 0.5 * 0.707 - 4)).add(dir.multiple(-30)),
                        center.add(othDir.multiple(length * 0.5 * 0.2 * 0.7 * 0.5 * 0.707 - 4)).add(dir.multiple(130)),
                        center.add(othDir.multiple(length * 0.5 * 0.2 * 0.7 * 0.5 * 0.707)).add(dir.multiple(130)))))
                        .makeColored(Color.BLACK)
                        .get())
                .makeBackgrounded(Color.WHITE)
                .makeMargin(0.5)
                .get().forceDraw(canvas);

        if(time >= 1.0) {
            timer.getTimer().setStart();
        }
    }
}
