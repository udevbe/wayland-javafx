package com.sun.glass.ui.monocle;

import org.freedesktop.wayland.client.WlBufferEvents;
import org.freedesktop.wayland.client.WlBufferProxy;


public class WaylandBuffer implements WlBufferEvents {

    private final WaylandBufferPool waylandBufferPool;
    private final WaylandShmBuffer  waylandShmBuffer;
    private final long              pixmanImage;

    public WaylandBuffer(final WaylandBufferPool waylandBufferPool,
                         final WaylandShmBuffer waylandShmBuffer) {
        this.waylandBufferPool = waylandBufferPool;
        this.waylandShmBuffer = waylandShmBuffer;

        this.pixmanImage = Libpixman1.pixman_image_create_bits(Libpixman1.PIXMAN_a8r8g8b8,
                                                               waylandShmBuffer.getWidth(),
                                                               waylandShmBuffer.getHeight(),
                                                               waylandShmBuffer.getBuffer(),
                                                               waylandShmBuffer.getWidth() * 4);
    }

    @Override
    public void release(final WlBufferProxy emitter) {
        if (this.waylandBufferPool.isDestroyed()) {
            emitter.destroy();
            this.waylandShmBuffer.close();
        }
        else {
            this.waylandBufferPool.queueBuffer(emitter);
        }
    }

    public WaylandShmBuffer getWaylandShmBuffer() {
        return this.waylandShmBuffer;
    }

    public long getPixmanImage() {
        return this.pixmanImage;
    }
}
