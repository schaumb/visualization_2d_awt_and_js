package bxlx.general;

import bxlx.graphics.Drawable;
import bxlx.graphics.Size;
import bxlx.system.ObservableValue;

/**
 * Created by qqcs on 2017.02.07..
 */
public interface IGame {
    IGame init(ObservableValue<Size> canvasSize);

    ObservableValue<Drawable> getMain();

    default void tick() {
    }
}
