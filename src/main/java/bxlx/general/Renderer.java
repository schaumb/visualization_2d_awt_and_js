package bxlx.general;

import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.system.IRenderer;
import bxlx.system.MouseInfo;
import bxlx.system.SystemSpecific;

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
        main.get().setRedraw();
        main.get().forceDraw(c);
    }

    public Renderer(IGame game) {
        MouseInfo.get(); // init mouseinfo

        this.main = game.init().getMain().getAsSupplier();

        SystemSpecific.get().setDrawFunction(this);
    }
}
