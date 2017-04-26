package bxlx.graphisoft;

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
            "bxlx/graphics/drawable/ZoomDrawable.js",
            "bxlx/graphics/drawable/ColoredDrawable.js",
            "bxlx/graphics/drawable/MarginDrawable.js",
            "bxlx/graphics/drawable/AspectRatioDrawable.js",
            "bxlx/graphics/drawable/VisibleDrawable.js",
            "bxlx/graphics/fill/Rect.js",
            "bxlx/graphics/fill/Text.js",
            "bxlx/graphics/container/SplitContainer.js",
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
            "bxlx/graphisoft/GameViewer.js",
            "bxlx/graphisoft/Game.js",
            "bxlx/general/Renderer.js",
            "bxlx/general/EntryPoint.js",
            "bxlx/jsweet/TestMain.js"
    };
    static {
        // add sprintf function
        document.writeln("<script language=\"javascript\">\n" +
                "function str_repeat(i, m) {\n" +
                "\tfor (var o = []; m > 0; o[--m] = i);\n" +
                "\treturn o.join('');\n" +
                "}\n" +
                "\n" +
                "function sprintf() {\n" +
                "\tvar i = 0, a, f = arguments[i++], o = [], m, p, c, x, s = '';\n" +
                "\twhile (f) {\n" +
                "\t\tif (m = /^[^\\x25]+/.exec(f)) {\n" +
                "\t\t\to.push(m[0]);\n" +
                "\t\t}\n" +
                "\t\telse if (m = /^\\x25{2}/.exec(f)) {\n" +
                "\t\t\to.push('%');\n" +
                "\t\t}\n" +
                "\t\telse if (m = /^\\x25(?:(\\d+)\\$)?(\\+)?(0|'[^$])?(-)?(\\d+)?(?:\\.(\\d+))?([b-fosuxX])/.exec(f)) {\n" +
                "\t\t\tif (((a = arguments[m[1] || i++]) == null) || (a == undefined)) {\n" +
                "\t\t\t\tthrow('Too few arguments.');\n" +
                "\t\t\t}\n" +
                "\t\t\tif (/[^s]/.test(m[7]) && (typeof(a) != 'number')) {\n" +
                "\t\t\t\tthrow('Expecting number but found ' + typeof(a));\n" +
                "\t\t\t}\n" +
                "\t\t\tswitch (m[7]) {\n" +
                "\t\t\t\tcase 'b': a = a.toString(2); break;\n" +
                "\t\t\t\tcase 'c': a = String.fromCharCode(a); break;\n" +
                "\t\t\t\tcase 'd': a = parseInt(a); break;\n" +
                "\t\t\t\tcase 'e': a = m[6] ? a.toExponential(m[6]) : a.toExponential(); break;\n" +
                "\t\t\t\tcase 'f': a = m[6] ? parseFloat(a).toFixed(m[6]) : parseFloat(a); break;\n" +
                "\t\t\t\tcase 'o': a = a.toString(8); break;\n" +
                "\t\t\t\tcase 's': a = ((a = String(a)) && m[6] ? a.substring(0, m[6]) : a); break;\n" +
                "\t\t\t\tcase 'u': a = Math.abs(a); break;\n" +
                "\t\t\t\tcase 'x': a = a.toString(16); break;\n" +
                "\t\t\t\tcase 'X': a = a.toString(16).toUpperCase(); break;\n" +
                "\t\t\t}\n" +
                "\t\t\ta = (/[def]/.test(m[7]) && m[2] && a >= 0 ? '+'+ a : a);\n" +
                "\t\t\tc = m[3] ? m[3] == '0' ? '0' : m[3].charAt(1) : ' ';\n" +
                "\t\t\tx = m[5] - String(a).length - s.length;\n" +
                "\t\t\tp = m[5] ? str_repeat(c, x) : '';\n" +
                "\t\t\to.push(s + (m[4] ? a + p : p + a));\n" +
                "\t\t}\n" +
                "\t\telse {\n" +
                "\t\t\tthrow('Huh ?!');\n" +
                "\t\t}\n" +
                "\t\tf = f.substring(m[0].length);\n" +
                "\t}\n" +
                "\treturn o.join('');\n" +
                "}\n" +
                "</script>");

        for(String include : includes) {
            document.writeln("<script language=\"javascript\" src=\"target/js/" + include + "\"></script>");
        }
    }
}
