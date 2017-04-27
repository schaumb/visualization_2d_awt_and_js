package bxlx.jsweet;

import bxlx.graphics.Color;
import bxlx.graphics.Cursor;
import bxlx.graphics.Font;
import bxlx.graphics.ImageCaches;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.system.CommonError;
import bxlx.system.IMouseEventListener;
import bxlx.system.IRenderer;
import bxlx.system.SystemSpecific;
import jsweet.dom.CanvasRenderingContext2D;
import jsweet.dom.Event;
import jsweet.dom.HTMLAudioElement;
import jsweet.dom.HTMLCanvasElement;
import jsweet.dom.HTMLImageElement;
import jsweet.dom.ImageData;
import jsweet.dom.XMLHttpRequest;
import jsweet.lang.Date;
import jsweet.util.StringTypes;

import java.util.HashSet;
import java.util.function.Consumer;

import static jsweet.dom.Globals.console;
import static jsweet.dom.Globals.document;
import static jsweet.dom.Globals.setTimeout;
import static jsweet.dom.Globals.window;
import static jsweet.lang.Globals.isNaN;
import static jsweet.lang.Globals.parseFloat;
import static jsweet.util.Globals.equalsStrict;
import static jsweet.util.Globals.equalsLoose;
import static jsweet.util.Globals.typeof;

/**
 * Created by qqcs on 2016.12.23..
 */
public class JSweetSystemSpecific extends SystemSpecific {

    private HTMLCanvasElement canvasElement;
    private IRenderer renderer;
    private HashSet<Integer> buttonDowns = new HashSet<>();
    private ImageCaches<HTMLAudioElement> musicCache = new ImageCaches<>(src -> {
        HTMLAudioElement element = document.createElement(StringTypes.audio);
        element.src = src;
        element.style.display = "none";
        element.autoplay = false;

        document.body.appendChild(element);
        return element;
    });

    private ImageCaches<ImageData> imageTransparencyCache = new ImageCaches<>(src -> {
        HTMLImageElement img = HtmlCanvas.imageCaches.get(src);
        HTMLCanvasElement canvasElement = document.createElement(StringTypes.canvas);
        canvasElement.width = img.naturalWidth;
        canvasElement.height = img.naturalHeight;
        CanvasRenderingContext2D rc = canvasElement.getContext(StringTypes._2d);
        rc.drawImage(img, 0, 0);
        return rc.getImageData(0, 0, img.naturalWidth, img.naturalHeight);
    });

    private HtmlCanvas htmlCanvas;

    private JSweetSystemSpecific() {
    }

    public static SystemSpecific create() {
        if (INSTANCE != null) return get();
        return new JSweetSystemSpecific();
    }

    private void draw(double __var) {
        if (canvasElement == null || renderer == null) {
            return;
        }

        if (rendering = renderer.render()) {
            window.requestAnimationFrame(x -> draw(x));
        }
    }

    private Object resized(Event event) {
        if (canvasElement == null || window == null || renderer == null) {
            return null;
        }

        /*if (canvasElement.width != Math.max(window.innerWidth, minimumSize.getWidth()) ||
                canvasElement.height != Math.max(window.innerHeight, minimumSize.getHeight())) {

        }*/
        canvasElement.width = Math.max(window.innerWidth, minimumSize.getWidth());
        canvasElement.height = Math.max(window.innerHeight, minimumSize.getHeight());
        renderer.setCanvas(htmlCanvas = new HtmlCanvas(canvasElement));

        if (!isRendering()) {
            window.requestAnimationFrame(x -> draw(x));
        }
        return null;
    }

    @Override
    public void setDrawFunction(IRenderer renderer) {
        if (canvasElement == null) {
            canvasElement = document.createElement(StringTypes.canvas);
            canvasElement.width = Math.max(window.innerWidth, minimumSize.getWidth());
            canvasElement.height = Math.max(window.innerHeight, minimumSize.getHeight());
            canvasElement.style.position = "absolute";
            canvasElement.style.top = "0";
            canvasElement.style.left = "0";

            setMouseEventListeners();
            document.body.appendChild(canvasElement);

            renderer.setCanvas(htmlCanvas = new HtmlCanvas(canvasElement));
            window.onresize = this::resized;
        }
        this.renderer = renderer;

        if (!isRendering()) {
            window.requestAnimationFrame(x -> draw(x));
        }
    }

    @Override
    public void setMouseEventListeners() {
        if (canvasElement != null) {
            for (IMouseEventListener listener : listeners) {
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
        HTMLAudioElement elem = musicCache.get(src);

        if (elem.readyState > 0) {
            elem.play();
        }
    }

    @Override
    public void readTextFileAsync(String from, String fileName, Consumer<String> consumer) {

        XMLHttpRequest request = new XMLHttpRequest();

        request.addEventListener(StringTypes.readystatechange, e -> {
            if (request.readyState == 4) {
                if (request.status == 200 || request.status == 0) {
                    consumer.accept(request.responseText);
                } else {
                    consumer.accept(null);
                }
            }
            return null;
        });
        /*
        request.open("POST", from, true);
        request.send(fileName);
        */

        request.open("GET", fileName, true);
        request.send();
    }

    @Override
    public Size preLoad(String src, boolean img) {
        if (img) {
            HTMLImageElement htmlImageElement = HtmlCanvas.imageCaches.get(src);
            return new Size(htmlImageElement.naturalWidth, htmlImageElement.naturalHeight);
        } else {
            musicCache.get(src);
            return null;
        }
    }

    @Override
    public Color getColor(String pic, double x, double y) {
        HTMLImageElement htmlImageElement = HtmlCanvas.imageCaches.get(pic);
        if (htmlImageElement.complete) {
            ImageData data = imageTransparencyCache.get(pic);
            int intX = (int) Math.min(htmlImageElement.naturalWidth - 1, Math.round(x * htmlImageElement.naturalWidth));
            int intY = (int) Math.min(htmlImageElement.naturalHeight - 1, Math.round(y * htmlImageElement.naturalHeight));
            int getPixel = intX + intY * (int) htmlImageElement.naturalWidth;
            return new Color(data.data.$get(getPixel * 4).intValue(),
                    data.data.$get(getPixel * 4 + 1).intValue(),
                    data.data.$get(getPixel * 4 + 2).intValue(),
                    data.data.$get(getPixel * 4 + 3).intValue());
        }
        return Color.OPAQUE;
    }

    @Override
    public <T> boolean equals(T first, T second) {
        return equalsStrict(first, second) ||
                (equalsLoose(typeof(first), "number") &&
                        equalsLoose(typeof(second), "number") &&
                        isNaN(parseFloat(first.toString())) &&
                        isNaN(parseFloat(second.toString())));
    }

    @Override
    public int stringLength(Font font, String string) {
        if (htmlCanvas != null) {
            Font tmp = htmlCanvas.getFont();
            if (font != null) {
                htmlCanvas.setFont(font);
            }
            int res = htmlCanvas.textWidth(string);
            htmlCanvas.setFont(tmp);
            return res;
        }
        return -1;
    }

    @Override
    public void open(String thing) {
        window.open(thing, "_blank");
    }

    @Override
    public void logout() {
        window.location.href = "/logout";
    }

    @Override
    public void setCursor(Cursor cursor) {
        String cursorName = "default";
        switch (cursor) {
            case DEFAULT:
                break;
            case HAND:
                cursorName = "pointer";
                break;
        }
        canvasElement.style.cursor = cursorName;
    }

    @Override
    public void runAfter(Runnable run, int millisec) {
        setTimeout(run, millisec);
    }
}
