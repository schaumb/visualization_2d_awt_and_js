package bxlx.graphics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qqcs on 2016.12.23..
 */
public class Color {
    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    public Color(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    public Color(int red, int green, int blue, int alpha) {
        this.red = red & 0xFF;
        this.green = green & 0xFF;
        this.blue = blue & 0xFF;
        this.alpha = alpha & 0xFF;
    }

    public Color(int rgba) {
        this(rgba, rgba >> 8, rgba >> 16, rgba >> 24);
    }


    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getAlpha() {
        return alpha;
    }

    public Color withAlpha(int alpha) {
        return new Color(red, green, blue, alpha);
    }

    public final static Color WHITE = new Color(255, 255, 255);
    public final static Color LIGHT_GRAY = new Color(192, 192, 192);
    public final static Color GRAY = new Color(128, 128, 128);
    public final static Color DARK_GRAY = new Color(64, 64, 64);
    public final static Color BLACK = new Color(0, 0, 0);

    public final static Color RED = new Color(255, 0, 0);
    public final static Color GREEN = new Color(0, 255, 0);
    public final static Color BLUE = new Color(0, 0, 255);

    public final static Color YELLOW = new Color(255, 255, 0);
    public final static Color MAGENTA = new Color(255, 0, 255);
    public final static Color CYAN = new Color(0, 255, 255);

    public final static Color PINK = new Color(255, 175, 175);
    public final static Color ORANGE = new Color(255, 200, 0);

    public final static Color OPAQUE = new Color(0, 0, 0, 0);

    public Color getScale(Color to, double percent) {
        return new Color(
                (int) ((to.getRed() - getRed()) * percent + getRed()),
                (int) ((to.getGreen() - getGreen()) * percent + getGreen()),
                (int) ((to.getBlue() - getBlue()) * percent + getBlue()),
                (int) ((to.getAlpha() - getAlpha()) * percent + getAlpha())
        );
    }

    public List<Color> getScaleList(Color to, int count) {
        List<Color> result = new ArrayList<>(count);

        for (int i = 0; i < count; ++i) {
            double percent = (double) i / (count - 1);
            result.add(getScale(to, percent));
        }
        return result;
    }

    public static List<Color> getScaleList(Color from, Color middle, Color to, int count) {
        List<Color> result = new ArrayList<>(count);
        if (count % 2 == 1) {
            result.addAll(from.getScaleList(middle, count / 2 + 1));
            result.remove(count / 2);
            result.addAll(middle.getScaleList(to, count / 2 + 1));
        } else {
            List<Color> mul = from.getScaleList(middle, count);
            for (int i = 0; i < count / 2; ++i) {
                result.add(mul.get(i * 2));
            }
            mul = middle.getScaleList(to, count);
            for (int i = 0; i < count / 2; ++i) {
                result.add(mul.get(i * 2 + 1));
            }
        }

        return result;
    }

    public static List<Color> getScaleFromBackToWhite(Color color, int count) {
        return getScaleList(BLACK, color, WHITE, count);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Color color = (Color) o;

        if (red != color.red) return false;
        if (green != color.green) return false;
        if (blue != color.blue) return false;
        return alpha == color.alpha;
    }

    @Override
    public int hashCode() {
        int result = red;
        result = 31 * result + green;
        result = 31 * result + blue;
        result = 31 * result + alpha;
        return result;
    }

    @Override
    public String toString() {
        return "rgba(" + red + ", " + green + ", " + blue + ", " + alpha + ")";
    }
}
