package bxlx.conticup17;

import static jsweet.dom.Globals.document;

/**
 * Created by ecosim on 4/26/17.
 */
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
            "bxlx/graphics/IDrawable.js",
            "bxlx/system/IMouseEventListener.js",
            "bxlx/system/IRenderer.js",
            "bxlx/system/functional/MyConsumer.js",
            "bxlx/system/CommonError.js",
            "bxlx/system/SystemSpecific.js",
            "bxlx/system/functional/ValueOrSupplier.js",
            "bxlx/graphics/ChangeableDrawable.js",
            "bxlx/graphics/ImageCaches.js",
            "bxlx/graphics/shapes/Shape.js",
            "bxlx/graphics/shapes/Arc.js",
            "bxlx/graphics/shapes/Polygon.js",
            "bxlx/graphics/shapes/Rectangle.js",
            "bxlx/graphics/container/DrawableContainer.js",
            "bxlx/graphics/container/SizeChangeableContainer.js",
            "bxlx/graphics/drawable/DrawableWrapper.js",
            "bxlx/graphics/container/TransformerContainer.js",
            "bxlx/graphics/drawable/ClippedDrawable.js",
            "bxlx/graphics/drawable/ShapeDrawable.js",
            "bxlx/graphics/drawable/ShapeClippedDrawable.js",
            "bxlx/graphics/drawable/ZoomDrawable.js",
            "bxlx/graphics/drawable/ColoredDrawable.js",
            "bxlx/graphics/drawable/MarginDrawable.js",
            "bxlx/graphics/drawable/AspectRatioDrawable.js",
            "bxlx/graphics/drawable/VisibleDrawable.js",
            "bxlx/graphics/fill/Rect.js",
            "bxlx/graphics/fill/Text.js",
            "bxlx/graphics/container/SplitContainer.js",
            "bxlx/graphics/fill/MultilineText.js",
            "bxlx/graphics/container/Splitter.js",
            "bxlx/graphics/fill/DrawNumber.js",
            "bxlx/graphics/container/Container.js",
            "bxlx/graphics/fill/DrawArc.js",
            "bxlx/graphics/fill/DrawNGon.js",
            "bxlx/graphics/fill/DrawRectangle.js",
            "bxlx/graphics/fill/DrawImage.js",
            "bxlx/graphics/combined/Stick.js",
            "bxlx/graphics/combined/Magnifying.js",
            "bxlx/jsweet/HtmlCanvas.js",
            "bxlx/jsweet/JSweetSystemSpecific.js",
            "bxlx/system/Timer.js",
            "bxlx/system/FPS.js",
            "bxlx/system/MouseInfo.js",
            "bxlx/system/input/Button.js",
            "bxlx/system/input/ButtonWithData.js",
            "bxlx/system/input/clickable/OnOffClickable.js",
            "bxlx/system/input/clickable/ChangeImageClickable.js",
            "bxlx/system/input/clickable/CheckboxClickable.js",
            "bxlx/system/input/clickable/ColorSchemeClickable.js",
            "bxlx/system/input/clickable/RectClickable.js",
            "bxlx/system/input/clickable/SameImageClickable.js",
            "bxlx/system/input/clickable/CursorChangeClickable.js",
            "bxlx/system/input/RadioButtons.js",
            "bxlx/system/input/Selector.js",
            "bxlx/system/input/Slider.js",
            "bxlx/system/input/DiscreteSlider.js",
            "bxlx/system/input/SelectorWrapper.js",

            "bxlx/graphics/combined/Navigator.js",
            "bxlx/graphics/combined/Builder.js",
            "bxlx/general/IGame.js",
            "bxlx/conticup17/RobotStates.js",
            "bxlx/conticup17/RobotStateTimer.js",
            "bxlx/conticup17/PlayerPointDrawable.js",
            "bxlx/conticup17/PacketDrawable.js",
            "bxlx/conticup17/RobotDrawable.js",
            "bxlx/conticup17/OneStateDrawable.js",
            "bxlx/conticup17/StationDrawable.js",
            "bxlx/conticup17/StateDrawable.js",
            "bxlx/conticup17/Game.js",
            "bxlx/general/Renderer.js",
            "bxlx/general/EntryPoint.js",
            "bxlx/jsweet/TestMain.js"
    };
    static {
        for(String include : includes) {
            document.writeln("<script language=\"javascript\" src=\"target/js/" + include + "\"></script>");
        }
        document.writeln("<script language=\"javascript\">" +
                "if(typeof requestedFileName !== 'undefined') {" +
                "   bxlx.system.SystemSpecific.get().setArgs(['/game?', requestedFileName])" +
                "}" +
                "</script>");

    }
}
