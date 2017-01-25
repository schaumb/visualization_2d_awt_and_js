package bxlx;

import bxlx.graphics.Color;
import bxlx.graphics.Direction;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.combined.Navigator;
import bxlx.graphics.fill.Splitter;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphisoft.Game;
import bxlx.system.IRenderer;
import bxlx.system.MouseInfo;
import bxlx.system.SystemSpecific;
import bxlx.system.Timer;
import bxlx.system.ValueOrSupplier;
import bxlx.system.input.Button;

import java.util.Arrays;

/**
 * Created by qqcs on 2016.12.24..
 */
public class TestMain implements IRenderer {
    private ICanvas c;
    private Splitter splitter = new Splitter(true, 0, null, null);

    private static class Waiter implements IDrawable {
        private Timer timer = new Timer(3000);

        @Override
        public Redraw needRedraw() {
            return new Redraw(Redraw.PARENT_NEED_REDRAW);
        }

        @Override
        public void forceDraw(ICanvas canvas) {
            Point center = canvas.getBoundingRectangle().getCenter();
            double size = canvas.getBoundingRectangle().getSize().getShorterDimension() / 2;
            final int max = 8;
            double percent = timer.percent();
            double prevAngle = -1.0 / max * Math.PI * 2;
            for (int i = 0; i < max; ++i) {
                double percentPlus = percent + i / (double) max;
                if (percentPlus > 1)
                    percentPlus -= 1;
                double angle = i / (double) max * Math.PI * 2;

                Point from = Direction.fromRadian(prevAngle).getVector();
                Point to = Direction.fromRadian(angle).getVector();

                canvas.setColor(Color.GREEN.getScale(Color.RED, percentPlus));
                canvas.fill(new Polygon(Arrays.asList(
                        center.add(from.multiple(size * (1.0 - percentPlus * 0.5))),
                        center.add(from.multiple(size * (0.6 - percentPlus * 0.3))),
                        center.add(to.multiple(size * (0.6 - percentPlus * 0.3))),
                        center.add(to.multiple(size * (1.0 - percentPlus * 0.5)))
                )));

                prevAngle = angle;
            }
            if (timer.elapsed()) {
                timer.setStart();
            }
        }
    }

    private IDrawable wait = new Builder<>(new Waiter()).makeBackgrounded(Color.WHITE).get();

    private ValueOrSupplier<IDrawable> main = new ValueOrSupplier<>(wait);

    @Override
    public boolean render() {
        main.get().draw(c);
        return true;
    }

    @Override
    public void setCanvas(ICanvas canvas) {
        c = canvas;
        //SystemSpecific.get().log("");
        main.get().forceDraw(c);
    }

    public TestMain() {
        MouseInfo.get(); // init mouseinfo

        ValueOrSupplier<Boolean> visibleNavi = new ValueOrSupplier<>(false);

        Button visibleButton = new Button(new Button.RectClickable(Builder.text("NAVI", "MENU").get()), b -> {
            visibleNavi.setElem(!visibleNavi.get());
            splitter.getFirst().get().setOnlyForceDraw();
            splitter.setRedraw();
        }, null, null);

        Button menuButton = new Button(new Button.RectClickable(Builder.text("MENU").get()), b -> {
            splitter.getSeparate().setElem(-200 - splitter.getSeparate().get());
            splitter.getFirst().get().setOnlyForceDraw();
        }, null, null);

        Game game = new Game("test2.txt");

        Navigator navi = Builder.navigator(visibleButton, menuButton, null, visibleNavi.getAsSupplier(), 40, Color.WHITE).get();
        navi.getMain().setSupplier(() -> game.getMainDrawable() == null ? wait : game.getMainDrawable());

        splitter.getFirst().setElem(navi);
        splitter.getSecond().setSupplier(() -> game.getMenu() == null ? wait : game.getMenu());
        main.setElem(splitter);
        main.setElem(new Button(new Button.ChangeImgClickable("aal.png", "aax.png", "aax.png", c -> c.getAlpha() != 0), null, null, () -> false));
        SystemSpecific.get().setDrawFunction(this);
    }
}
