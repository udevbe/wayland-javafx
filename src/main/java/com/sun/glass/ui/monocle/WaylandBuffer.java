package com.sun.glass.ui.monocle;

import org.freedesktop.wayland.client.WlBufferEvents;
import org.freedesktop.wayland.client.WlBufferProxy;

import java.io.IOException;
import java.nio.ByteBuffer;


public class WaylandBuffer implements WlBufferEvents {

    private final WaylandBufferPool waylandBufferPool;
    private final WaylandShmPool    shmPool;
    private final int               width;
    private final int               height;

    public WaylandBuffer(final WaylandBufferPool waylandBufferPool,
                         final WaylandShmPool shmPool,
                         final int width,
                         final int height) {
        this.waylandBufferPool = waylandBufferPool;
        this.shmPool = shmPool;
        this.width = width;
        this.height = height;
    }

    @Override
    public void release(final WlBufferProxy emitter) {
        if (this.waylandBufferPool.isDestroyed()) {
            emitter.destroy();
            try {
                this.shmPool.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            this.waylandBufferPool.queueBuffer(emitter);
        }
    }

    public ByteBuffer getByteBuffer() {
        return this.shmPool.asByteBuffer();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
