package com.sun.glass.ui.monocle;

import org.freedesktop.wayland.client.WlBufferEvents;
import org.freedesktop.wayland.client.WlBufferProxy;

import java.io.IOException;


public class WaylandBuffer implements WlBufferEvents {

    private final WaylandBufferPool waylandBufferPool;
    private final WaylandShmBuffer  waylandShmBuffer;

    public WaylandBuffer(final WaylandBufferPool waylandBufferPool,
                         final WaylandShmBuffer shmPool) {
        this.waylandBufferPool = waylandBufferPool;
        this.waylandShmBuffer = shmPool;
    }

    @Override
    public void release(final WlBufferProxy emitter) {
        if (this.waylandBufferPool.isDestroyed()) {
            emitter.destroy();
            try {
                this.waylandShmBuffer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            this.waylandBufferPool.queueBuffer(emitter);
        }
    }

    public WaylandShmBuffer getWaylandShmBuffer() {
        return this.waylandShmBuffer;
    }
}
