package bxlx;

import bxlx.graphics.Color;
import bxlx.graphics.Direction;
import bxlx.graphics.Font;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.combined.Navigator;
import bxlx.graphics.fill.Splitter;
import bxlx.graphics.fill.Text;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphisoft.Game;
import bxlx.system.IRenderer;
import bxlx.system.MouseInfo;
import bxlx.system.SystemSpecific;
import bxlx.system.Timer;
import bxlx.system.ValueOrSupplier;
import bxlx.system.input.Button;
import bxlx.system.input.Clickable;
import bxlx.system.input.OnOffClickable;
import bxlx.system.input.RadioButtons;
import bxlx.system.input.Selector;
import bxlx.system.input.SelectorWrapper;

import java.util.Arrays;

/**
 * Created by qqcs on 2016.12.24..
 */
public class TestMain implements IRenderer {
    private ICanvas c;
    private Splitter splitter = new Splitter(true, 0, null, null);

    private Timer timer = new Timer(3000);

    private static class Waiter implements IDrawable {
        private Timer timer = new Timer(3000);

        @Override
        public Redraw needRedraw() {
            return new Redraw(Redraw.PARENT_NEED_REDRAW);
        }

        @Override
        public void setRedraw() {

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
                timer.setPercent(timer.overPercent());
            }
        }
    }

    private IDrawable wait = new Builder<>(new Waiter()).makeBackgrounded(Color.WHITE).get();

    private ValueOrSupplier<IDrawable> main = new ValueOrSupplier<>(wait);

    @Override
    public boolean render() {
        if (main.get().needRedraw().needRedraw()) {
            //SystemSpecific.get().log("");
        }
        main.get().draw(c);
        if (timer.elapsed()) {
            timer.setPercent(timer.overPercent());
        }
        return true;
    }

    @Override
    public void setCanvas(ICanvas canvas) {
        c = canvas;
        c.setFont(new Font("purisa", 10, false, false));
        //SystemSpecific.get().log("");
        main.get().forceDraw(c);
    }

    public TestMain() {
        MouseInfo.get(); // init mouseinfo

        ValueOrSupplier<Boolean> visibleNavi = new ValueOrSupplier<>(false);

        Button<?> visibleButton = new Button<>(new Clickable.RectClickable(Builder.text("NAVI", "MENU").get()), b -> {
            visibleNavi.setElem(!visibleNavi.get());
            splitter.setRedraw();
        }, null, null);

        Button<?> menuButton = new Button<>(new Clickable.RectClickable(Builder.text("MENU").get()), b -> {
            splitter.getSeparate().setElem(r -> -200 - splitter.getSeparate().get().apply(r));
            splitter.setRedraw();
        }, null, null);

        Game game = new Game("test2.txt");

        Navigator navi = Builder.navigator(visibleButton, menuButton, null, visibleNavi.getAsSupplier(), 40, Color.WHITE).get();
        navi.getMain().setSupplier(() -> game.getMainDrawable() == null ? wait : game.getMainDrawable());

        splitter.getFirst().setElem(navi);
        splitter.getSecond().setSupplier(() -> game.getMenu() == null ? wait : game.getMenu());
        main.setElem(splitter);
        main.setElem(new Button<>(new OnOffClickable.ChangeImgClickable("aal.png", "aax.png", "aab.png", c -> c.getAlpha() != 0), null, null, () -> false));

        main.setElem(new Button<>(new OnOffClickable.SameImgClickable("aae.png",
                r -> r.withSize(r.getSize().asPoint().multiple(Direction.DOWN.getVector().multiple(3.0).add(Direction.RIGHT.getVector())).asSize()),
                r -> r.withSize(r.getSize().asPoint().multiple(Direction.DOWN.getVector().multiple(3.0).add(Direction.RIGHT.getVector())).asSize())
                        .withStart(r.getStart().add(r.getSize().asPoint().multiple(Direction.UP.getVector()))),
                r -> r.withSize(r.getSize().asPoint().multiple(Direction.DOWN.getVector().multiple(3.0).add(Direction.RIGHT.getVector())).asSize())
                        .withStart(r.getStart().add(r.getSize().asPoint().multiple(Direction.UP.getVector().multiple(2)))),
                c -> c.getAlpha() != 0), null, null, () -> timer.percent() < 0.5));

        main.setElem(new RadioButtons(true)
                .add(new OnOffClickable.RectCheckBoxWith(Builder.text("Hello", -1).makeColored(Color.BLACK).get()))
                .add(new OnOffClickable.RectCheckBoxWith(Builder.text("Szia", -1).makeColored(Color.BLACK).get()))
                .add(new OnOffClickable.RectCheckBoxWith(Builder.text("Szerbusz", -1).makeColored(Color.BLACK).get()))
        );

        Selector selector = new Selector(false)
                .addText(new Selector.RectClickable(), new Text("Hello", null, -1))
                .addText(new Selector.RectClickable(), new Text("Szia", null, -1))
                .addText(new Selector.RectClickable(), new Text("Szerbusz", null, -1));

        main.setElem(new Builder<>(new SelectorWrapper(selector, 200)).makeBackgrounded(Color.WHITE).get());
        SystemSpecific.get().setDrawFunction(this);

        //main.setElem(new Container().add(new Button<>(new Selector.RectClickable(), null, null, () -> false))
        //    .add(new Text("ASD")));
    }
}
