package bxlx.jsweet;

import bxlx.CommonError;
import bxlx.Consumer;
import bxlx.IMouseEventListener;
import bxlx.IRenderer;
import bxlx.SystemSpecific;
import bxlx.graphics.Point;
import jsweet.dom.Event;
import jsweet.dom.HTMLAudioElement;
import jsweet.dom.HTMLCanvasElement;
import jsweet.dom.XMLHttpRequest;
import jsweet.lang.Date;
import jsweet.util.StringTypes;

import java.util.HashSet;

import static jsweet.dom.Globals.console;
import static jsweet.dom.Globals.document;
import static jsweet.dom.Globals.window;

/**
 * Created by qqcs on 2016.12.23..
 */
public class JSweetSystemSpecific extends SystemSpecific {

    private HTMLCanvasElement canvasElement;
    private IRenderer renderer;
    private HashSet<Integer> buttonDowns = new HashSet<>();

    private JSweetSystemSpecific() {
    }

    public static SystemSpecific create() {
        if (INSTANCE != null) return get();
        return new JSweetSystemSpecific();
    }

    private void draw(double __var) {
        if (rendering = renderer.render(new HtmlCanvas(canvasElement))) {
            window.requestAnimationFrame(x -> draw(x));
        }
    }

    private Object resized(Event event) {
        if (canvasElement.width != window.innerWidth || canvasElement.height != window.innerHeight) {
            canvasElement.width = window.innerWidth;
            canvasElement.height = window.innerHeight;
        }

        if (!isRendering()) {
            window.requestAnimationFrame(x -> draw(x));
        }
        return null;
    }

    @Override
    public void setDrawFunction(IRenderer renderer) {
        if (canvasElement == null) {
            canvasElement = document.createElement(StringTypes.canvas);
            canvasElement.width = window.innerWidth;
            canvasElement.height = window.innerHeight;
            canvasElement.style.position = "absolute";
            canvasElement.style.top = "0";
            canvasElement.style.left = "0";

            document.body.appendChild(canvasElement);

            window.onresize = this::resized;
        }
        this.renderer = renderer;

        if (!isRendering()) {
            window.requestAnimationFrame(x -> draw(x));
        }
    }

    @Override
    public void setMouseEventListener(IMouseEventListener listener) {
        if (canvasElement != null) {
            canvasElement.addEventListener(StringTypes.mousedown,
                    e -> {
                        buttonDowns.add((int) e.button);
                        listener.down(new Point(e.pageX, e.pageY), e.button == 0);
                        return null;
                    });

            canvasElement.addEventListener(StringTypes.mouseup,
                    e -> {
                        buttonDowns.remove((int) e.button);
                        listener.up(new Point(e.pageX, e.pageY), e.button == 0);
                        return null;
                    });

            canvasElement.addEventListener(StringTypes.mousemove,
                    e -> {
                        listener.move(new Point(e.pageX, e.pageY));
                        return null;
                    });

            canvasElement.addEventListener(StringTypes.mouseout,
                    e -> {
                        for (Integer button : buttonDowns) {
                            listener.up(new Point(e.pageX, e.pageY), button == 0);
                        }
                        buttonDowns.clear();
                        return null;
                    });
        }
    }

    @Override
    public boolean isEqual(double d1, double d2) {
        return Math.abs(d1 - d2) < 0.00000001;
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

    @Override
    public void playMusic(String src) {
        HTMLAudioElement element = document.createElement(StringTypes.audio);
        element.src = src;
        element.style.display = "none";
        element.autoplay = true;
        element.onended = x -> {
            element.play();
            return x;
        };

        document.body.appendChild(element);
    }

    @Override
    public void readTextFileAsync(String fileName, Consumer<String> consumer) {

        XMLHttpRequest request = new XMLHttpRequest();

        request.addEventListener(StringTypes.readystatechange, e -> {
            if (request.readyState == 4 && request.status == 200) {
                consumer.accept(request.responseText);
            } else {
                consumer.accept(null);
            }
            return null;
        });
        request.open("GET", fileName, true);
        request.send();
    }
}
