package bxlx.graphics;

import java.util.BitSet;

/**
 * Created by qqcs on 2016.12.24..
 */
public interface IDrawable {
    default boolean draw(ICanvas canvas) {
        if (needRedraw().needRedraw()) {
            forceDraw(canvas);
            return true;
        }
        return false;
    }

    Redraw needRedraw();

    void setRedraw();

    void forceDraw(ICanvas canvas);

    class Redraw extends BitSet {
        private static int COUNTER = 0;
        public static int CHILD_NEED_REDRAW = COUNTER++;
        public static int I_NEED_REDRAW = COUNTER++;
        public static int PARENT_NEED_REDRAW = COUNTER++;

        public Redraw() {
            super(COUNTER);
        }

        public Redraw(int setDef) {
            this();
            set(setDef);
        }

        public Redraw(Redraw redraw) {
            this();
            or(redraw);
        }

        public boolean childNeedRedraw() {
            return get(CHILD_NEED_REDRAW);
        }

        public boolean iNeedRedraw() {
            return get(I_NEED_REDRAW);
        }

        public boolean parentNeedRedraw() {
            return get(PARENT_NEED_REDRAW);
        }

        public boolean needRedrawExcept(int which) {
            return cardinality() > (get(which) ? 1 : 0);
        }

        public boolean needRedraw() {
            return cardinality() > 0;
        }

        public boolean noNeedRedraw() {
            return cardinality() == 0;
        }

        public Redraw setChildNeedRedraw() {
            set(CHILD_NEED_REDRAW);
            return this;
        }

        public Redraw setINeedRedraw() {
            set(I_NEED_REDRAW);
            return this;
        }

        public Redraw setParentNeedRedraw() {
            set(PARENT_NEED_REDRAW);
            return this;
        }

        public Redraw setIf(boolean condition, int which) {
            if (condition) {
                set(which);
            }
            return this;
        }

        public Redraw orIf(boolean condition, Redraw redraw) {
            if (condition) {
                or(redraw);
            }
            return this;
        }
    }
}
