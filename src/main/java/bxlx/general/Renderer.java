package bxlx.general;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.combined.Builder;
import bxlx.system.ColorScheme;
import bxlx.system.IRenderer;
import bxlx.system.MouseInfo;
import bxlx.system.SystemSpecific;
import bxlx.system.functional.ValueOrSupplier;

import java.util.function.Supplier;

/**
 * Created by qqcs on 2016.12.24..
 */
public class Renderer implements IRenderer {
    private ICanvas c;
    private final Supplier<IDrawable> main;

    @Override
    public boolean render() {
        IDrawable mainDrawable = main.get();
        if (mainDrawable.needRedraw().needRedraw()) {
            //SystemSpecific.get().log("");
        }
        mainDrawable.draw(c);

        return true;
    }

    @Override
    public void setCanvas(ICanvas canvas) {
        c = canvas;
        //SystemSpecific.get().log("");
        main.get().forceDraw(c);
    }

    public Renderer(IGame game) {
        MouseInfo.get(); // init mouseinfo

        this.main = new ValueOrSupplier.Transform<IDrawable, IDrawable>().transform(game.init().getMain(),
                i -> new Builder<>(i).makeBackgrounded(ColorScheme.getCurrentColorScheme().backgroundColor).get());

        SystemSpecific.get().setDrawFunction(this);
    }
}
