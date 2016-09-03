package com.sun.glass.ui.wayland;

import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;


public class WaylandCursor extends Cursor {
    protected WaylandCursor(final int type) {
        super(type);
    }

    protected WaylandCursor(final int x,
                            final int y,
                            final Pixels pixels) {
        super(x,
              y,
              pixels);
    }

    @Override
    protected long _createCursor(final int x,
                                 final int y,
                                 final Pixels pixels) {
        return 0;
    }
}
