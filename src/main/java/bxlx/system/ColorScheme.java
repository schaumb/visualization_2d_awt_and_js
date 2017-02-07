package bxlx.system;

import bxlx.graphics.Color;
import bxlx.graphics.Font;

/**
 * Created by qqcs on 2017.02.07..
 */
public class ColorScheme {
    public final Color buttonColor;
    public final Color insideColor;
    public final Color clickedColor;
    public final Color disabledColor;
    public final Color buttonBorderColor;
    public final Color buttonTextColor;

    public final Color separatorColor;

    public final Font font;
    public final Color textColor;
    public final Color backgroundColor;


    public ColorScheme(Color buttonColor, Color insideColor, Color clickedColor, Color disabledColor, Color buttonBorderColor, Color buttonTextColor, Color separatorColor, Font font, Color textColor, Color backgroundColor) {
        this.buttonColor = buttonColor;
        this.insideColor = insideColor;
        this.clickedColor = clickedColor;
        this.disabledColor = disabledColor;
        this.buttonBorderColor = buttonBorderColor;
        this.buttonTextColor = buttonTextColor;

        this.separatorColor = separatorColor;

        this.font = font;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    public static final ColorScheme STANDARD_COLORS =
            new ColorScheme(Color.GRAY, Color.DARK_GRAY, Color.DARK_GRAY.getScale(Color.BLACK, 0.5), Color.LIGHT_GRAY, Color.BLACK, Color.BLUE, Color.BLACK,
                    new Font("sans-serif", 10, false, false), Color.BLACK, Color.WHITE);

    private static ColorScheme CURRENT_COLOR_SCHEME = STANDARD_COLORS;

    public static ColorScheme getCurrentColorScheme() {
        return CURRENT_COLOR_SCHEME;
    }

    public static ColorScheme setCurrentColorScheme(ColorScheme currentColorScheme) {
        return CURRENT_COLOR_SCHEME = currentColorScheme;
    }
}
