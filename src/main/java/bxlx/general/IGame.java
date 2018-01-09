package bxlx.general;

import bxlx.graphics.Drawable;
import bxlx.system.ObservableValue;

/**
 * Created by qqcs on 2017.02.07..
 */
public interface IGame {
    IGame init();

    ObservableValue<Drawable> getMain();

    default void tick() {
    }
}
