package bxlx.general;

import bxlx.graphics.IDrawable;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.container.SplitContainer;
import bxlx.graphics.container.Splitter;
import bxlx.graphics.drawable.AspectRatioDrawable;
import bxlx.graphics.drawable.MarginDrawable;
import bxlx.graphics.fill.DrawImage;
import bxlx.system.SystemSpecific;

import java.util.ArrayList;

/**
 * Created by qqcs on 2017.02.07..
 */
public class MenuCreator {
    private String logo;
    private String url;
    private final ArrayList<IDrawable> buttons = new ArrayList<>();
    private double buttonsSize = 0;

    public String getLogo() {
        return logo;
    }

    public MenuCreator setLogo(String logo) {
        this.logo = logo;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public MenuCreator setUrl(String url) {
        this.url = url;
        return this;
    }

    public MenuCreator addButton(IDrawable button) {
        buttons.add(button);
        return this;
    }

    public ArrayList<IDrawable> getButtons() {
        return buttons;
    }

    public double getButtonsSize() {
        return buttonsSize;
    }

    public MenuCreator setButtonsSize(double buttonsSize) {
        this.buttonsSize = buttonsSize;
        return this;
    }

    public IDrawable getMenu() {

        IDrawable result = new Builder.ContainerBuilder<>(new SplitContainer<>(true))
                .transform(b -> new MarginDrawable<>(b, 0.1, 0.1))
                .addAndTransformAll(buttons)
                .get();
        if (getButtonsSize() != 0) {
            result = Builder.splitter(true, getButtonsSize(), null, result).get();
        }

        if (getLogo() != null) {

            AspectRatioDrawable<DrawImage> aspectRatioDrawable = Builder.imageKeepAspectRatio(getLogo(), -1, -1).get();
            Splitter s = new Splitter(true, 0, aspectRatioDrawable, result);
            s.getSeparate().setElem(r -> aspectRatioDrawable.getClip().get().apply(r).getSize().getWidth());

            if (getUrl() != null) {
                s.getFirst().setElem(Builder.getCursorChangeClickable()
                        .makeButton(r -> SystemSpecific.get().open(getUrl()))
                        .with(0, aspectRatioDrawable)
                        .get());
            }
            result = s;
        }
        return result;
    }
}
