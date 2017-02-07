package bxlx.jsweet;

import bxlx.general.EntryPoint;

import static jsweet.dom.Globals.window;

/**
 * Created by qqcs on 2016.12.22..
 */
public class TestMain {

    public static void main(String[] args) {
        String[] myArgs = window.location.search.substring(1).split("&");
        JSweetSystemSpecific.create().setArgs(myArgs);
        EntryPoint.start();
    }
}
