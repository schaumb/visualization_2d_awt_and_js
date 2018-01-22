package bxlx.general;

import bxlx.graphics.ICanvas;
import bxlx.graphics.Drawable;
import bxlx.graphics.Size;
import bxlx.system.IRenderer;
import bxlx.system.MouseInfo;
import bxlx.system.ObservableValue;
import bxlx.system.SystemSpecific;

/**
 * Created by qqcs on 2016.12.24..
 */
public class Renderer implements IRenderer {
    private final IGame game;
    private final ObservableValue<Drawable> main;
    private ObservableValue<ICanvas> canvas = new ObservableValue<>();
    private ObservableValue<Size> canvasSize = new ObservableValue<>(Size.ZERO);

    public Renderer(IGame game) {
        MouseInfo.get(); // init mouseinfo

        this.game = game;
        this.main = game.init(canvasSize).getMain();

        canvas.addObserver((observable, from) -> this.main.get()
                .update(null, null));

        SystemSpecific.get().setDrawFunction(this);
    }

    @Override
    public boolean render() {
        ICanvas canvas = this.canvas.get();
        this.canvasSize.setValue(canvas.getBoundingRectangle().getSize());
        game.tick();

        Drawable mainDrawable = main.get();
        mainDrawable.draw(canvas);

        return true;
    }

    @Override
    public void setCanvas(ICanvas canvas) {
        this.canvas.setValue(canvas);
        this.canvasSize.setValue(canvas.getBoundingRectangle().getSize());
    }
}
