package bxlx.graphisoft.element;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.ICanvas;
import bxlx.graphics.fill.DrawImage;
import bxlx.graphisoft.Parameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ecosim on 4/27/17.
 */
public class Display extends ChangeableDrawable {
    private final List<Princess> princesses = new ArrayList<>();
    private final DrawImage monitor;
    private boolean isActive = true;
    private boolean isOn = false;

    private boolean isDeactivating = false;
    private Player who;

    public Display() {
        this.monitor = new DrawImage(Parameters.imgDir() + "m_off.png");
    }

    public void setPrincess(Princess princess) {
        princesses.add(princess);
        if(!isOn) {
            isOn = true;
            monitor.setFileName(Parameters.imgDir() + "m_on.png");
        }
    }

    public void setActive() {
        isActive = true;
    }

    public void setStartState() {
        isActive = false;
        isDeactivating = false;
    }

    public void setToDeactivating(Player player) {
        isDeactivating = true;
        who = player;
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        if(!isActive)
            return;

        monitor.forceDraw(canvas);
        for(Princess princess : princesses) {
            princess.getDisplayButton().forceDraw(canvas);
        }
    }
}
