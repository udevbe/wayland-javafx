package com.sun.glass.ui.monocle;

import org.freedesktop.wayland.client.WlBufferProxy;
import org.freedesktop.wayland.client.WlShmPoolProxy;
import org.freedesktop.wayland.client.WlShmProxy;
import org.freedesktop.wayland.shared.WlShmFormat;

import java.io.IOException;

public class WaylandBufferPoolFactory {


    private final WlShmProxy wlShmProxy;

    public WaylandBufferPoolFactory(WlShmProxy wlShmProxy) {
        this.wlShmProxy = wlShmProxy;
    }

    public WaylandBufferPool create(int width,
                                    int height,
                                    int size,
                                    WlShmFormat shmFormat) throws IOException {

        final WaylandBufferPool waylandBufferPool = new WaylandBufferPool();
        for (int i = 0; i < size; i++) {
            final int            bufferSize = width * height * 4;
            final WaylandShmPool shmPool    = new WaylandShmPool(bufferSize);

            final WlShmPoolProxy wlShmPoolProxy = this.wlShmProxy.createPool(waylandBufferPool,
                                                                             shmPool.getFileDescriptor(),
                                                                             bufferSize);
            final WlBufferProxy buffer = wlShmPoolProxy.createBuffer(new WaylandBuffer(waylandBufferPool,
                                                                                       shmPool,
                                                                                       width,
                                                                                       height),
                                                                     0,
                                                                     width,
                                                                     height,
                                                                     width * 4,
                                                                     shmFormat.value);
            waylandBufferPool.queueBuffer(buffer);
            wlShmPoolProxy.destroy();
        }
        return waylandBufferPool;
    }
}
