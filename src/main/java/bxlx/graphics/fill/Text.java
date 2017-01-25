package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.03..
 */
public class Text extends ChangeableDrawable {
    private final ChangeableValue<String> text;
    private final ChangeableValue<String> referenceText;
    private final ChangeableValue<Integer> align;

    public Text(Supplier<String> text) {
        this.text = new ChangeableValue<>(this, text);
        this.align = new ChangeableValue<>(this, 0);
        this.referenceText = new ChangeableValue<>(this, (String) null);
    }

    public Text(String text) {
        this(text, null, 0);
    }

    public Text(String text, String referenceText, int align) {
        this.text = new ChangeableValue<>(this, text);
        this.referenceText = new ChangeableValue<>(this, referenceText);
        this.align = new ChangeableValue<>(this, (int) Math.signum(align));
    }

    public ChangeableValue<String> getText() {
        return text;
    }

    public ChangeableValue<String> getReferenceText() {
        return referenceText;
    }

    public ChangeableValue<Integer> getAlign() {
        return align;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
        String nowText = text.get();
        String nowReferenceText = referenceText.get();
        int nowAlign = align.get();

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

        double x;
        if(nowAlign < 0) {
            x = 0;
        } else if (nowAlign == 0) {
            x = (rectangle.getSize().getWidth() - xSize) / 2;
        } else {
            x = rectangle.getSize().getWidth() - xSize;
        }

        canvas.fillText(nowText, rectangle.getStart().add(new Point(
                x, (rectangle.getSize().getHeight() + ySize) / 2 - ySize / 6.0)));
    }
}
