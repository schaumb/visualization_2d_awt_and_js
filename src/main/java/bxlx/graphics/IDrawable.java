package bxlx.graphics;

/**
 * Created by qqcs on 2016.12.24..
 */
public interface IDrawable {
    default boolean draw(ICanvas canvas) {
        if (needRedraw()) {
            forceDraw(canvas);
            return true;
        }
        return false;
    }

    boolean needRedraw();

    void forceDraw(ICanvas canvas);
}
