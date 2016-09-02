package com.sun.glass.ui.monocle;


import com.google.auto.factory.AutoFactory;
import com.sun.glass.ui.Size;

@AutoFactory(allowSubclasses = true)
public class WaylandCursor extends NativeCursor {

    WaylandCursor() {
        //TODO create wayland surface with cursor role
    }

    Size getBestSize() {
        return null;
    }

    void setVisibility(final boolean visibility) {

    }

    void setImage(final byte[] cursorImage) {

    }

    void setLocation(final int x,
                     final int y) {

    }

    void setHotSpot(final int hotspotX,
                    final int hotspotY) {

    }

    void shutdown() {

    }
}
