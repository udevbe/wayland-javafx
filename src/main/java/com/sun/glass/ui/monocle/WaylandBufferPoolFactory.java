package com.sun.glass.ui.monocle;

import org.freedesktop.wayland.client.WlBufferProxy;
import org.freedesktop.wayland.client.WlShmPoolProxy;
import org.freedesktop.wayland.client.WlShmProxy;
import org.freedesktop.wayland.shared.WlShmFormat;

import javax.inject.Inject;

public class WaylandBufferPoolFactory {

    @Inject
    WaylandBufferPoolFactory() {
    }

    public WaylandBufferPool create(final WlShmProxy wlShmProxy,
                                    final int width,
                                    final int height,
                                    final int size,
                                    final WlShmFormat shmFormat) {

        final WaylandBufferPool waylandBufferPool = new WaylandBufferPool();
        for (int i = 0; i < size; i++) {
            final int              bufferSize = width * height * 4;
            final WaylandShmBuffer shmPool    = new WaylandShmBuffer(bufferSize);

            final WlShmPoolProxy wlShmPoolProxy = wlShmProxy.createPool(waylandBufferPool,
                                                                        shmPool.getFileDescriptor(),
                                                                        bufferSize);
            final WlBufferProxy buffer = wlShmPoolProxy.createBuffer(new WaylandBuffer(waylandBufferPool,
                                                                                       shmPool),
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
