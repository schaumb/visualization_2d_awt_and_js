package bxlx.ball18;

import static def.dom.Globals.document;

public class Include {
    private static final String[] includes = new String[]{
            "bxlx/graphics/Color.js",
            "bxlx/graphics/Cursor.js",
            "bxlx/graphics/Font.js",
            "bxlx/system/ColorScheme.js",
            "bxlx/graphics/Point.js",
            "bxlx/graphics/Size.js",
            "bxlx/graphics/Direction.js",
            "bxlx/graphics/ICanvas.js",
            "bxlx/system/IMouseEventListener.js",
            "bxlx/system/IRenderer.js",
            "bxlx/system/functional/MyConsumer.js",
            "bxlx/system/CommonError.js",
            "bxlx/system/SystemSpecific.js",
            "bxlx/system/functional/MySupplier.js",
            "bxlx/system/Observable.js",
            "bxlx/system/Observer.js",
            "bxlx/system/ObservableValue.js",
            "bxlx/graphics/drawable_helper/CanvasChanger.js",
            "bxlx/graphics/drawable_helper/RectangleTranslator.js",
            "bxlx/graphics/drawable_helper/ObservableRectangleTranslator.js",
            "bxlx/graphics/drawable_helper/Clipper.js",
            "bxlx/graphics/drawable_helper/Painter.js",
            "bxlx/graphics/drawable_helper/ShapeClipper.js",
            "bxlx/graphics/drawable_helper/ChangerBuilder.js",
            "bxlx/graphics/Drawable.js",
            "bxlx/graphics/DrawableContainer.js",
            "bxlx/graphics/ImageCaches.js",
            "bxlx/graphics/shapes/Shape.js",
            "bxlx/graphics/shapes/Arc.js",
            "bxlx/graphics/shapes/Polygon.js",
            "bxlx/graphics/shapes/Rectangle.js",
            "bxlx/graphics/container/Wrapper.js",
            "bxlx/graphics/fill/Rect.js",
            "bxlx/graphics/fill/Text.js",
            "bxlx/graphics/container/SplitContainer.js",
            "bxlx/graphics/fill/MultilineText.js",
            "bxlx/graphics/container/Splitter.js",
            "bxlx/graphics/container/Container.js",
            "bxlx/graphics/fill/DrawArc.js",
            "bxlx/graphics/fill/DrawNGon.js",
            "bxlx/graphics/fill/DrawRectangle.js",
            "bxlx/graphics/fill/DrawImage.js",
            "bxlx/jsweet/HtmlCanvas.js",
            "bxlx/jsweet/JSweetSystemSpecific.js",
            "bxlx/system/Timer.js",
            "bxlx/system/MouseInfo.js",
            "bxlx/general/IGame.js",

            "bxlx/ball18/Game.js",
            "bxlx/general/Renderer.js",
            "bxlx/general/EntryPoint.js",
            "bxlx/jsweet/TestMain.js"
    };
    static {
        for(String include : includes) {
            document.writeln("<script language=\"javascript\" src=\"target/ts/" + include + "\"></script>");
        }
        document.writeln("<script language=\"javascript\">" +
                "if(typeof requestedFileName !== 'undefined') {" +
                "   bxlx.system.SystemSpecific.get().setArgs(['/game?', requestedFileName])" +
                "}" +
                "</script>");

    }
}
