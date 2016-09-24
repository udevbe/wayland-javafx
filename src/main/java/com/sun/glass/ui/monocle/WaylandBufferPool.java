package com.sun.glass.ui.monocle;


import org.freedesktop.wayland.client.WlBufferProxy;
import org.freedesktop.wayland.client.WlShmPoolEvents;

import java.util.concurrent.ArrayBlockingQueue;

public class WaylandBufferPool implements WlShmPoolEvents {

    private ArrayBlockingQueue<WlBufferProxy> bufferQueue = new ArrayBlockingQueue<>(2);
    private boolean destroyed;

    public void queueBuffer(final WlBufferProxy buffer) {
        if (this.destroyed) {
            throw new IllegalStateException("Pool destroyed");
        }

        this.bufferQueue.add(buffer);
    }

    public WlBufferProxy popBuffer() {
        if (this.destroyed) {
            throw new IllegalStateException("Pool destroyed");
        }

        return this.bufferQueue.poll();
    }

    public void destroy() {
        if (this.destroyed) {
            throw new IllegalStateException("Pool destroyed");
        }

        this.bufferQueue.forEach(WlBufferProxy::destroy);
        this.bufferQueue.clear();
        this.destroyed = true;
    }

    public boolean isDestroyed() {
        return this.destroyed;
    }
}
