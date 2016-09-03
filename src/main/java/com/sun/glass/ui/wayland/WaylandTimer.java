package com.sun.glass.ui.wayland;

import com.sun.glass.ui.Timer;


public class WaylandTimer extends Timer {
    /**
     * Constructs a new timer.
     * <p>
     * If the application overrides the Timer.run(), it should call super.run()
     * in order to run the runnable passed to the constructor.
     *
     * @param runnable
     */
    protected WaylandTimer(final Runnable runnable) {
        super(runnable);
    }

    @Override
    protected long _start(final Runnable runnable) {
        return 0;
    }

    @Override
    protected long _start(final Runnable runnable,
                          final int period) {
        return 0;
    }

    @Override
    protected void _stop(final long timer) {

    }
}
