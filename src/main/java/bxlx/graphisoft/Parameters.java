package bxlx.graphisoft;

import bxlx.graphics.fill.DrawImage;
import bxlx.system.SystemSpecific;

/**
 * Created by ecosim on 4/27/17.
 */
public class Parameters {

    public static String imgDir() {
        if(SystemSpecific.get().getArgs().length < 3) {
            return "";
        }
        return SystemSpecific.get().getArgs()[2];
    }

    public static DrawImage getField(int fieldType) {
        return new DrawImage(imgDir() + fieldType + ".jpg");
    }

    public static DrawImage getMonitor(boolean on) {
        return new DrawImage(imgDir() + "m_" + (on ? "on" : "off") + ".png");
    }

    public static DrawImage getPrincess(int index) {
        return new DrawImage(imgDir() + "q" + (index+1) + ".png");
    }

    public static DrawImage getPrincessButton(int index) {
        return new DrawImage(imgDir() + "b" + (index+1) + ".png");
    }

    public static DrawImage getPrincessMoveField(int index) {
        return new DrawImage(imgDir() + "f" + (index+1) + ".png");
    }

    public static void preloadAllImg() {
        for(int i = 0; i < 10; ++i) {
            SystemSpecific.get().preLoad(imgDir() + "q" + (i+1) + ".png", true);
            SystemSpecific.get().preLoad(imgDir() + "b" + (i+1) + ".png", true);
            SystemSpecific.get().preLoad(imgDir() + "f" + (i+1) + ".png", true);
        }
        for(int i = 1; i < 16; ++i) {
            SystemSpecific.get().preLoad(imgDir() + i + ".jpg", true);
        }
        SystemSpecific.get().preLoad(imgDir() + "m_on.png", true);
        SystemSpecific.get().preLoad(imgDir() + "m_off.png", true);
    }
}
