package bxlx.graphics.fill;

import bxlx.graphics.Drawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.ObservableValue;

/**
 * Created by qqcs on 2017.01.03..
 */
public class Text extends Drawable {
    private final ObservableValue<String> text;
    private final ObservableValue<String> referenceText;
    private final ObservableValue<Integer> align;

    public Text(String str, String ref, int align) {
        this(new ObservableValue<>(str), new ObservableValue<>(ref), new ObservableValue<>(align));
    }

    public Text(ObservableValue<String> text, ObservableValue<String> referenceText, ObservableValue<Integer> align) {
        this.text = text;
        this.referenceText = referenceText;
        this.align = align;

        text.addObserver((x, y) -> setRedraw());
        referenceText.addObserver((x, y) -> setRedraw());
        align.addObserver((x, y) -> setRedraw());
    }

    public ObservableValue<String> getText() {
        return text;
    }

    public ObservableValue<String> getReferenceText() {
        return referenceText;
    }

    public ObservableValue<Integer> getAlign() {
        return align;
    }

    @Override
    public void forceDraw(ICanvas canvas) {
        String nowText = text.get();
        String nowReferenceText = referenceText.get();
        int nowAlign = align.get();

        if (nowText == null ||  nowText.isEmpty())
            return;

        Rectangle rectangle = canvas.getBoundingRectangle();
        int ySize = (int) rectangle.getSize().getHeight();

        int xNeedFitSize = (int) rectangle.getSize().getWidth();
        canvas.setFont(canvas.getFont().withSize(ySize));
        while (ySize > 2 && canvas.textWidth(nowReferenceText != null ? nowReferenceText : nowText) > xNeedFitSize) {
            canvas.setFont(canvas.getFont().withSize(--ySize));
        }
        int xSize = canvas.textWidth(nowText);

        double x;
        if (nowAlign < 0) {
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
