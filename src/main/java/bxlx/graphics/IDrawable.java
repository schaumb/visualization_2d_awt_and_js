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

    IDrawable.Redraw needRedraw();

    void setRedraw();

    void forceDraw(ICanvas canvas);

    class Redraw {
        public static final int CHILD_NEED_REDRAW = 0;
        public static final int I_NEED_REDRAW = 1;
        public static final int PARENT_NEED_REDRAW = 2;
        private boolean childRedraw;
        private boolean iRedraw;
        private boolean parentRedraw;

        private void set(int setting) {
            switch (setting) {
                case CHILD_NEED_REDRAW:
                    childRedraw = true;
                    break;
                case I_NEED_REDRAW:
                    iRedraw = true;
                    break;
                case PARENT_NEED_REDRAW:
                    parentRedraw = true;
                    break;
            }
        }

        private boolean get(int getting) {
            switch (getting) {
                case CHILD_NEED_REDRAW:
                    return childRedraw;
                case I_NEED_REDRAW:
                    return iRedraw;
                case PARENT_NEED_REDRAW:
                    return parentRedraw;
            }
            return false;
        }

        private void or(Redraw redraw) {
            for (int i = 0; i < 3; ++i)
                if (redraw.get(i))
                    set(i);
        }

        private int cardinality() {
            return (childRedraw ? 1 : 0) +
                    (iRedraw ? 1 : 0) +
                    (parentRedraw ? 1 : 0);
        }

        public Redraw() {
        }

        public Redraw(int setDef) {
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

        public IDrawable.Redraw setChildNeedRedraw() {
            set(CHILD_NEED_REDRAW);
            return this;
        }

        public IDrawable.Redraw setINeedRedraw() {
            set(I_NEED_REDRAW);
            return this;
        }

        public IDrawable.Redraw setParentNeedRedraw() {
            set(PARENT_NEED_REDRAW);
            return this;
        }

        public IDrawable.Redraw setIf(boolean condition, int which) {
            if (condition) {
                set(which);
            }
            return this;
        }

        public IDrawable.Redraw orIf(boolean condition, IDrawable.Redraw redraw) {
            if (condition) {
                or(redraw);
            }
            return this;
        }
    }
}
