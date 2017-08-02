package bxlx.ericsson17;

import bxlx.graphics.Color;
import bxlx.graphics.IDrawable;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.fill.DrawNGon;
import bxlx.system.CommonError;
import bxlx.system.SystemSpecific;
import bxlx.system.Timer;
import bxlx.system.functional.ValueOrSupplier;

import java.text.ParseException;

public class GameLogic {
    private final String[] data;
    private final ValueOrSupplier<IDrawable> main;
    private final ValueOrSupplier<String> outerText;

    private IDrawable myMain;

    public GameLogic(String s, ValueOrSupplier<IDrawable> main, ValueOrSupplier<String> text) {
        this.data = s.split("\n");
        this.main = main;
        this.outerText = text;
    }

    public void run() {
        IDrawable waiter = main.get();
        Timer timer = new Timer(10000);


        myMain = Builder.nGon(true, false, 9, 0, true).makeColored(Color.YELLOW).makeBackgrounded(Color.LIGHT_GRAY).get();
        main.setSupplier(() -> timer.elapsed() ? myMain : waiter);

    }
}
