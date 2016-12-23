package bxlx.jsweet;


import bxlx.graphics.Size;

import static jsweet.dom.Globals.console;

/**
 * Created by qqcs on 2016.12.22..
 */
public class TestMain {

    public TestMain() {
    }

    public static void main(String[] args) {
        Size s = new Size(3, 4);
        console.debug(s.asPoint().asSize().toString());
    }
}
