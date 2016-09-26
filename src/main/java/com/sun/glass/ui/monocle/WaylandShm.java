package com.sun.glass.ui.monocle;

import org.freedesktop.wayland.client.WlRegistryProxy;
import org.freedesktop.wayland.client.WlShmEvents;
import org.freedesktop.wayland.client.WlShmProxy;

import javax.annotation.Nonnull;

public class WaylandShm implements WlShmEvents {

    private final WlShmProxy wlShmProxy;

    WaylandShm(@Nonnull final WlRegistryProxy registryProxy,
               final int name,
               @Nonnull final String interfaceName,
               final int version) {
        this.wlShmProxy = registryProxy.bind(name,
                                             WlShmProxy.class,
                                             WlShmEvents.VERSION,
                                             this);
    }

    @Override
    public void format(final WlShmProxy emitter,
                       final int format) {

    }

    public WlShmProxy getWlShmProxy() {
        return this.wlShmProxy;
    }
}
