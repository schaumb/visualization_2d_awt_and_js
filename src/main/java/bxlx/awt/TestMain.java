package bxlx.awt;

import bxlx.general.EntryPoint;

/**
 * Created by qqcs on 2016.12.22..
 */
public class TestMain {
    public static void main(String[] args) {
        AwtSystemSpecific.create().setArgs(args);
        EntryPoint.start();
    }
}
