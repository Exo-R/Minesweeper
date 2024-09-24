package ru.game.view.timer;

import java.awt.event.ActionListener;
import javax.swing.Timer;

public class GameTimer {

    private static final int DELAY_SEC = 1000;
    private static final int INITIAL_DELAY_SEC = 0;

    private final Timer timer;
    private int seconds;

    public GameTimer(ActionListener taskPerformer) {
        seconds = 0;
        timer = new Timer(DELAY_SEC, taskPerformer);
        timer.setInitialDelay(INITIAL_DELAY_SEC);
    }

    public int getSeconds() {
        return seconds;
    }

    public void addSecond() {
        ++seconds;
    }

    public void stop() {
        timer.stop();
    }

    public void start() {
        timer.start();
    }

    public void reset() {
        timer.stop();
        seconds = 0;
    }

    public boolean isRunning() {
        return timer.isRunning();
    }
}
