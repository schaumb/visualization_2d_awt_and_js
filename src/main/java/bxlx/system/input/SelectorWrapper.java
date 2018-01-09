package bxlx.system.input;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Direction;
import bxlx.graphics.container.Splitter;
import bxlx.graphics.drawable.ClippedDrawable;
import bxlx.graphics.drawable.DrawableWrapper;
import bxlx.graphics.drawable.ZoomDrawable;

/**
 * Created by qqcs on 2017.01.27..
 */
public class SelectorWrapper extends DrawableWrapper<Splitter> {
    private final ChangeableDrawable.ChangeableValue<Double> fixSize;

    public SelectorWrapper(Selector selector, double fixSized) {
        super(new Splitter(true, 0, null, null));

        Splitter splitter = getChild().get();
        fixSize = new ChangeableDrawable.ChangeableValue<>(this, fixSized);
        Slider slider = new Slider(false, 0, () -> false);
        splitter.getFirst().setElem(new ZoomDrawable<>(new ClippedDrawable<>(selector, true,
                r -> r.withSize(r.getSize().asPoint().multiple(Direction.RIGHT.getVector()).add(
                        Direction.DOWN.getVector().multiple(fixSize.get() * selector.size())
                ).asSize()))
                , true, r -> 1.0, r -> 0.0, r -> {
            double bowlingSize = fixSize.get() * selector.size() - r.getSize().getHeight();
            if (bowlingSize <= 0) {
                slider.getNow().setElem(0.0);
                getChild().get().getSeparate().setDepFun(rt -> 0.0);
                return 0.0;
            }
            getChild().get().getSeparate().setDepFun(rt -> -40.0);
            return -bowlingSize / r.getSize().getHeight() * slider.getNow().get();
        }));
        splitter.getSecond().setElem(slider);
    }
}
