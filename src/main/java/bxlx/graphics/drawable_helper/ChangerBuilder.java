package bxlx.graphics.drawable_helper;

import bxlx.graphics.Color;
import bxlx.graphics.shapes.Shape;
import bxlx.system.ObservableValue;

public abstract class ChangerBuilder {
    private final ChangerBuilder before;

    private ChangerBuilder(ChangerBuilder before) {
        this.before = before;
    }

    public static class ClipperBuilder extends ChangerBuilder {
        private boolean isFake;
        private RectangleTranslator rectangleTranslator;

        ClipperBuilder(RectangleTranslator rectangleTranslator, ChangerBuilder changerBuilder) {
            super(changerBuilder);
            this.rectangleTranslator = rectangleTranslator;
        }

        public ClipperBuilder setFake(boolean fake) {
            isFake = fake;
            return this;
        }

        protected Clipper getAndThen(CanvasChanger then) {
            return new Clipper(isFake, rectangleTranslator, then);
        }
    }

    public static class PainterBuilder extends ChangerBuilder {
        private ObservableValue<Color> color = new ObservableValue<>();

        private PainterBuilder(ChangerBuilder before) {
            super(before);
        }

        public PainterBuilder setColor(Color color) {
            this.color = new ObservableValue<>(color);
            return this;
        }

        public PainterBuilder setColorFromObservable(ObservableValue<Color> color) {
            this.color = color;
            return this;
        }

        @Override
        protected Painter getAndThen(CanvasChanger then) {
            return new Painter(color, then);
        }
    }

    public static class ShapeClipperBuilder extends ChangerBuilder {
        private ObservableValue<Shape> shape;

        private ShapeClipperBuilder(ChangerBuilder before) {
            super(before);
        }

        public ShapeClipperBuilder setShape(Shape shape) {
            this.shape = new ObservableValue<>(shape);
            return this;
        }

        public ShapeClipperBuilder setShapeFromObservable(ObservableValue<Shape> shape) {
            this.shape = shape;
            return this;
        }

        @Override
        protected CanvasChanger getAndThen(CanvasChanger then) {
            return new ShapeClipper(then, shape);
        }
    }

    public static ClipperBuilder clip(RectangleTranslator rectangleTranslator) {
        return new ClipperBuilder(rectangleTranslator, null);
    }

    public static ShapeClipperBuilder clipShape() {
        return new ShapeClipperBuilder(null);
    }

    public static PainterBuilder paint() {
        return new PainterBuilder(null);
    }

    public ClipperBuilder thenClip(RectangleTranslator rectangleTranslator) {
        return new ClipperBuilder(rectangleTranslator, this);
    }

    public PainterBuilder thenPaint() {
        return new PainterBuilder(this);
    }

    public ShapeClipperBuilder thenClipShape() {
        return new ShapeClipperBuilder(this);
    }

    public final CanvasChanger get() {
        CanvasChanger andThen = getAndThen(null);
        return before == null ? andThen : before.getAndThen(andThen);
    }

    protected abstract CanvasChanger getAndThen(CanvasChanger then);
}
