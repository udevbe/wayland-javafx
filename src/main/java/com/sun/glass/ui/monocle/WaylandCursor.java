package com.sun.glass.ui.monocle;


import com.sun.glass.ui.Size;
import org.freedesktop.wayland.client.WlCompositorProxy;
import org.freedesktop.wayland.client.WlOutputProxy;
import org.freedesktop.wayland.client.WlShmProxy;
import org.freedesktop.wayland.client.WlSurfaceEventsV4;
import org.freedesktop.wayland.client.WlSurfaceProxy;
import org.freedesktop.wayland.shared.WlShmFormat;

import javax.annotation.Nonnull;

public class WaylandCursor extends NativeCursor implements WlSurfaceEventsV4 {

    private final WlShmProxy        wlShmProxy;
    private final Size              size;
    private final WaylandBufferPool waylandBufferPool;
    private final WlSurfaceProxy    wlSurfaceProxy;

    WaylandCursor(final WlShmProxy wlShmProxy,
                  final WlCompositorProxy wlCompositorProxy,
                  final WaylandSeat waylandSeat) {
        this.wlShmProxy = wlShmProxy;
        this.wlSurfaceProxy = wlCompositorProxy.createSurface(this);
        //TODO from config?
        this.size = new Size(32,
                             32);
        this.waylandBufferPool = new WaylandBufferPoolFactory().create(wlShmProxy,
                                                                       this.size.width,
                                                                       this.size.height,
                                                                       2,
                                                                       WlShmFormat.ARGB8888);
    }

    Size getBestSize() {
        return this.size;
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

    @Override
    public void enter(final WlSurfaceProxy emitter,
                      @Nonnull final WlOutputProxy output) {
        //NOOP monocle doesnt really support multi screen setup
    }

    @Override
    public void leave(final WlSurfaceProxy emitter,
                      @Nonnull final WlOutputProxy output) {
        //NOOP monocle doesnt really support multi screen setup
    }
}
