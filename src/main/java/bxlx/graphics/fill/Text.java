package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;

/**
 * Created by qqcs on 2017.01.03..
 */
public class Text extends ChangeableDrawable {
    private final ChangeableValue<String> text;
    private final ChangeableValue<String> referenceText;

    public Text(String text) {
        this(text, null);
    }

    public Text(String text, String referenceText) {
        this.text = new ChangeableValue<>(this, text);
        this.referenceText = new ChangeableValue<>(this, referenceText);
    }

    public ChangeableValue<String> getText() {
        return text;
    }

    public ChangeableValue<String> getReferenceText() {
        return referenceText;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        String nowText = text.get();
        String nowReferenceText = referenceText.get();

        if (nowText.isEmpty())
            return;

        Rectangle rectangle = canvas.getBoundingRectangle();
        int ySize = (int) rectangle.getSize().getHeight();

        int xNeedFitSize = (int) rectangle.getSize().getWidth();
        canvas.setFont("sans-serif", ySize, false, false);
        while (canvas.textWidth(nowReferenceText != null ? nowReferenceText : nowText) > xNeedFitSize) {
            canvas.setFont("sans-serif", --ySize, false, false);
        }
        int xSize = canvas.textWidth(nowText);

        canvas.fillText(nowText, rectangle.getStart().add(new Point(
                (rectangle.getSize().getWidth() - xSize) / 2,
                (rectangle.getSize().getHeight() + ySize) / 2 - ySize / 6.0)));
    }
}
