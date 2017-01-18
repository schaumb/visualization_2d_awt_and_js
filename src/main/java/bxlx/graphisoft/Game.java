package bxlx.graphisoft;

import bxlx.graphics.IDrawable;
import bxlx.system.MyConsumer;
import bxlx.system.SystemSpecific;

/**
 * Created by qqcs on 2017.01.18..
 */
public class Game implements MyConsumer<String> {
    private IDrawable mainDrawable;
    private IDrawable menu;

    public Game(String name) {
        SystemSpecific.get().readTextFileAsync(name, getAsConsumer());
    }

    public IDrawable getMainDrawable() {
        return mainDrawable;
    }

    public IDrawable getMenu() {
        return menu;
    }

    @Override
    public void accept(String data) {

    }
}
