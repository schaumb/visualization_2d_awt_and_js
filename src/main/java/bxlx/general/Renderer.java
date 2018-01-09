package bxlx.general;

import bxlx.graphics.ICanvas;
import bxlx.graphics.Drawable;
import bxlx.system.*;

/**
 * Created by qqcs on 2016.12.24..
 */
public class Renderer implements IRenderer {
    private final IGame game;
    private ObservableValue<ICanvas> canvas = new ObservableValue<>();
    private final ObservableValue<Drawable> main;

    @Override
    public boolean render() {
        Drawable mainDrawable = main.get();
        mainDrawable.draw(canvas.get());

        game.tick();
        return true;
    }

    @Override
    public void setCanvas(ICanvas canvas) {
        this.canvas.setValue(canvas);
    }

    public Renderer(IGame game) {
        MouseInfo.get(); // init mouseinfo

        this.game = game;
        this.main = game.init().getMain();

        canvas.addObserver((observable, from) -> this.main.get()
                .update(null, null));

        SystemSpecific.get().setDrawFunction(this);
    }
}
