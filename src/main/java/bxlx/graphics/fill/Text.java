package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;

/**
 * Created by qqcs on 2017.01.03..
 */
public class Text extends ChangeableDrawable {
    private String text;
    private String referenceText;

    public Text(String text) {
        this(text, null);
    }

    public Text(String text, String referenceText) {
        this.text = text;
        this.referenceText = referenceText;
        setRedraw();
    }

    public String getText() {
        return text;
    }

    public Text setText(String text) {
        this.text = text;
        setRedraw();
        return this;
    }

    public String getReferenceText() {
        return referenceText;
    }

    public Text setReferenceText(String referenceText) {
        this.referenceText = referenceText;
        setRedraw();
        return this;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        if (text.isEmpty())
            return;

        Rectangle rectangle = canvas.getBoundingRectangle();
        int ySize = (int) rectangle.getSize().getHeight();

        int xNeedFitSize = (int) rectangle.getSize().getWidth();
        canvas.setFont("sans-serif", ySize, false, false);
        while (canvas.textWidth(referenceText != null ? referenceText : text) > xNeedFitSize) {
            canvas.setFont("sans-serif", --ySize, false, false);
        }
        int xSize = canvas.textWidth(text);

        canvas.fillText(text, rectangle.getStart().add(new Point(
                (rectangle.getSize().getWidth() - xSize) / 2,
                (rectangle.getSize().getHeight() + ySize) / 2 - ySize / 6.0)));
    }
}
