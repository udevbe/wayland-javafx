package com.sun.glass.ui.monocle;


import org.freedesktop.wayland.client.WlBufferProxy;
import org.freedesktop.wayland.client.WlShmPoolEvents;

import java.util.concurrent.ArrayBlockingQueue;

public class WaylandBufferPool implements WlShmPoolEvents {

    private ArrayBlockingQueue<WlBufferProxy> bufferQueue = new ArrayBlockingQueue<>(2);
    private boolean destroyed;

    public void queueBuffer(WlBufferProxy buffer) {
        if (destroyed) {
            throw new IllegalStateException("Pool destroyed");
        }

        this.bufferQueue.add(buffer);
    }

    public WlBufferProxy popBuffer() {
        if (destroyed) {
            throw new IllegalStateException("Pool destroyed");
        }

        try {
            return bufferQueue.take();
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroy() {
        if (destroyed) {
            throw new IllegalStateException("Pool destroyed");
        }

        for (WlBufferProxy wlBufferProxy : bufferQueue) {
            wlBufferProxy.destroy();
        }
        bufferQueue.clear();
        this.destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
