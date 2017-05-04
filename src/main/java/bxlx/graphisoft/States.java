package bxlx.graphisoft;

import bxlx.system.Timer;

/**
 * Created by ecosim on 2017.05.04..
 */
public class States {
    public static final States BEFORE_PUSH = new States(0);
    public static final States PUSH = new States(1000);
    public static final States WAIT_AFTER_PUSH = new States(200);
    public static final States GOTO = new States(1000);
    public static final States WAIT_AFTER_GOTO = new States(200);
    public static final States END_TURN = new States(0);

    private int startLength;
    private Timer timer;


    States(int i) {
        startLength = i;
    }

    public Timer getTimer() {
        if(timer == null) {
            timer = new Timer(startLength);
        }
        return timer;
    }
}

