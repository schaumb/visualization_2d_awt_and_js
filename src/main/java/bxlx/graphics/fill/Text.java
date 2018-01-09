package bxlx.graphics.fill;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.shapes.Rectangle;

import java.util.function.Supplier;

/**
 * Created by qqcs on 2017.01.03..
 */
public class Text extends ChangeableDrawable {
    private final ChangeableDrawable.ChangeableValue<String> text;
    private final ChangeableDrawable.ChangeableValue<String> referenceText;
    private final ChangeableDrawable.ChangeableValue<Integer> align;

    public Text(Supplier<String> text) {
        this.text = new ChangeableDrawable.ChangeableValue<>(this, text);
        this.align = new ChangeableDrawable.ChangeableValue<>(this, 0);
        this.referenceText = new ChangeableDrawable.ChangeableValue<>(this, (String) null);
    }

    public Text(String text) {
        this(text, (String) null, 0);
    }

    public Text(String text, String referenceText, int align) {
        this.text = new ChangeableDrawable.ChangeableValue<>(this, text);
        this.referenceText = new ChangeableDrawable.ChangeableValue<>(this, referenceText);
        this.align = new ChangeableDrawable.ChangeableValue<>(this, (int) Math.signum(align));
    }

    public Text(Supplier<String> text, String referenceText, int align) {
        this.text = new ChangeableDrawable.ChangeableValue<>(this, text);
        this.referenceText = new ChangeableDrawable.ChangeableValue<>(this, referenceText);
        this.align = new ChangeableDrawable.ChangeableValue<>(this, (int) Math.signum(align));
    }

    public Text(String text, Supplier<String> referenceText, int align) {
        this.text = new ChangeableDrawable.ChangeableValue<>(this, text);
        this.referenceText = new ChangeableDrawable.ChangeableValue<>(this, referenceText);
        this.align = new ChangeableDrawable.ChangeableValue<>(this, (int) Math.signum(align));
    }

    public Text(Supplier<String> text, Supplier<String> referenceText, int align) {
        this.text = new ChangeableDrawable.ChangeableValue<>(this, text);
        this.referenceText = new ChangeableDrawable.ChangeableValue<>(this, referenceText);
        this.align = new ChangeableDrawable.ChangeableValue<>(this, (int) Math.signum(align));
    }

    @Override
    public IDrawable.Redraw needRedraw() {
        IDrawable.Redraw result = super.needRedraw();
        return result.setIf(result.iNeedRedraw(), IDrawable.Redraw.PARENT_NEED_REDRAW);
    }

    public ChangeableDrawable.ChangeableValue<String> getText() {
        return text;
    }

    public ChangeableDrawable.ChangeableValue<String> getReferenceText() {
        return referenceText;
    }

    public ChangeableDrawable.ChangeableValue<Integer> getAlign() {
        return align;
    }

    @Override
    public void forceRedraw(ICanvas canvas) {
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
