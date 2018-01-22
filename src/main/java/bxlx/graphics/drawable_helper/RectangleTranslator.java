package bxlx.graphics.drawable_helper;

import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.Observable;

public interface RectangleTranslator {
    RectangleTranslator IDENTITY = (original, nTh, max) -> original;
    RectangleTranslator NULL_RECTANGLE = (original, nTh, max) -> Rectangle.NULL_RECTANGLE;
    RectangleTranslator SPLIT_X = (original, nTh, max) -> {
        double one = original.getSize().getWidth() / max;
        return new Rectangle(
                original.getStart().add(nTh * one, 0),
                original.getSize().withWidth(one));
    };
    RectangleTranslator SPLIT_Y = (original, nTh, max) -> {
        double one = original.getSize().getHeight() / max;
        return new Rectangle(
                original.getStart().add(0, nTh * one),
                original.getSize().withHeight(one));
    };

    static RectangleTranslator scaled(double scale) {
        return (original, nTh, max) -> original.getScaled(scale);
    }

    static RectangleTranslator translated(Point translate) {
        return (original, nTh, max) -> original.getTranslated(translate);
    }

    static RectangleTranslator aspectRatio(double nowRatio, double alignX, double alignY) {
        double realRatio = nowRatio <= 0 ? 1 : nowRatio;
        double realAlignX = alignX < 0 ? 0 : alignX > 1 ? 1 : alignX;
        double realAlignY = alignY < 0 ? 0 : alignY > 1 ? 1 : alignY;

        return (original, nTh, max) -> {
            double width = Math.min(original.getSize().getWidth(), original.getSize().getHeight() / realRatio);
            Size size = new Size(width, width * realRatio);

            Point start = original.getStart().add(
                    original.getSize().asPoint().add(size.asPoint().negate()).multiple(realAlignX, realAlignY));
            return new Rectangle(start, size);
        };
    }

    static RectangleTranslator margin(double all) {
        return margin(all, all);
    }

    static RectangleTranslator margin(double x, double y) {
        return margin(x, x, y, y);
    }

    static RectangleTranslator margin(double left, double right, double top, double bottom) {
        return (original, nTh, max) -> {
            double leftFrom = left >= 1 ? original.getStart().getX() + left :
                    left >= 0 ? original.getStart().getX() + original.getSize().getWidth() * left :
                            left >= -1 ? original.getStart().getX() + original.getSize().getWidth() * (1 + left) :
                                    original.getStart().getX() + original.getSize().getWidth() + left;

            double rightFrom = right > 1 ? original.getStart().getX() + right :
                    right > 0 ? original.getStart().getX() + original.getSize().getWidth() * right :
                            right > -1 ? original.getStart().getX() + original.getSize().getWidth() * (1 + right) :
                                    original.getStart().getX() + original.getSize().getWidth() + right;

            double topFrom = top >= 1 ? original.getStart().getY() + top :
                    top >= 0 ? original.getStart().getY() + original.getSize().getHeight() * top :
                            top >= -1 ? original.getStart().getY() + original.getSize().getHeight() * (1 + top) :
                                    original.getStart().getY() + original.getSize().getHeight() + top;

            double bottomFrom = bottom > 1 ? original.getStart().getY() + bottom :
                    bottom > 0 ? original.getStart().getY() + original.getSize().getHeight() * bottom :
                            bottom > -1 ? original.getStart().getY() + original.getSize().getHeight() * (1 + bottom) :
                                    original.getStart().getY() + original.getSize().getHeight() + bottom;

            if (leftFrom > rightFrom || topFrom > bottomFrom)
                return Rectangle.NULL_RECTANGLE;

            return new Rectangle(
                    new Point(leftFrom, topFrom),
                    new Point(rightFrom, bottomFrom)
            );
        };
    }

    Rectangle getTranslatedRectangle(Rectangle original, int nTh, int max);
}