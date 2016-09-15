package com.sun.glass.ui.monocle;


import org.freedesktop.wayland.client.WlBufferProxy;
import org.freedesktop.wayland.client.WlShmPoolEvents;

import java.util.LinkedList;

public class WaylandBufferPool implements WlShmPoolEvents {

    private LinkedList<WlBufferProxy> bufferQueue = new LinkedList<WlBufferProxy>();
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

        return bufferQueue.pop();
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
