package bxlx.general;

import bxlx.graphics.IDrawable;
import bxlx.system.functional.ValueOrSupplier;

/**
 * Created by qqcs on 2017.02.07..
 */
public interface IGame {
    IGame init();

    ValueOrSupplier<IDrawable> getMain();
}
