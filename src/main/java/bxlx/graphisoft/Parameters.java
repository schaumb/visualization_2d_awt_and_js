package bxlx.graphisoft;

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

}
