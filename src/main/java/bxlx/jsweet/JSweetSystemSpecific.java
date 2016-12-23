package bxlx.jsweet;

import bxlx.CommonError;
import bxlx.SystemSpecific;
import bxlx.graphics.ICanvas;
import jsweet.dom.HTMLCanvasElement;
import jsweet.dom.HTMLDivElement;
import jsweet.lang.Date;
import jsweet.util.StringTypes;

import java.util.function.Consumer;

import static jsweet.dom.Globals.console;
import static jsweet.dom.Globals.document;
import static jsweet.dom.Globals.window;

/**
 * Created by qqcs on 2016.12.23..
 */
public class JSweetSystemSpecific extends SystemSpecific {

    private HTMLCanvasElement canvasElement;
    private Consumer<ICanvas> canvasConsumer;

    private JSweetSystemSpecific() {}

    public static SystemSpecific create() {
        if(INSTANCE != null) return get();
        return new JSweetSystemSpecific();
    }

    private void draw(double var) {
        if(canvasElement.width != window.innerWidth || canvasElement.height != window.innerHeight) {
            canvasElement.width = window.innerWidth;
            canvasElement.height = window.innerHeight;
        }
        canvasConsumer.accept(new HtmlCanvas(canvasElement));

        window.requestAnimationFrame(x -> draw(x));
    }

    @Override
    public void setDrawFunction(Consumer<ICanvas> canvasConsumer) {
        this.canvasConsumer = canvasConsumer;
        if(canvasElement == null) {
            canvasElement = document.createElement(StringTypes.canvas);
            canvasElement.width = window.innerWidth;
            canvasElement.height = window.innerHeight;
            canvasElement.style.position = "absolute";
            canvasElement.style.top = "0";
            canvasElement.style.left = "0";

            document.body.appendChild(canvasElement);
            window.requestAnimationFrame(x -> draw(x));
        }
    }

    @Override
    public boolean isEqual(double d1, double d2) {
        return Math.abs(d1-d2) < 0.00000001;
    }

    @Override
    public long getTime() {
        return (long) new Date().getTime();
    }

    @Override
    public void log(String message) {
        console.debug(message);
    }

    @Override
    public void log(CommonError commonError, String message) {
        console.debug("ERROR: " + commonError.name + " - " + commonError.message + " - " + message);
    }
}
