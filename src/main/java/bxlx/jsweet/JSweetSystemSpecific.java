package bxlx.jsweet;

import bxlx.CommonError;
import bxlx.SystemSpecific;
import jsweet.lang.Date;

import static jsweet.dom.Globals.console;

/**
 * Created by qqcs on 2016.12.23..
 */
public class JSweetSystemSpecific extends SystemSpecific {

    static {
        new JSweetSystemSpecific();
    }
    
    @Override
    public boolean isEqual(double d1, double d2) {
        return Math.abs(d1-d2) < 0.00000001;
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
}
