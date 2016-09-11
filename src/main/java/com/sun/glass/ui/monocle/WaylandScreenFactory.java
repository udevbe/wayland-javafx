package com.sun.glass.ui.monocle;


import org.freedesktop.wayland.client.WlCompositorProxy;
import org.freedesktop.wayland.client.WlShellProxy;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class WaylandScreenFactory {

    @Nonnull
    private final PrivateWaylandScreenFactory privateWaylandScreenFactory;

    @Inject
    WaylandScreenFactory(@Nonnull final PrivateWaylandScreenFactory privateWaylandScreenFactory) {
        this.privateWaylandScreenFactory = privateWaylandScreenFactory;
    }

    public WaylandScreen create(final WlCompositorProxy compositorProxy,
                                final WlShellProxy shellProxy) {
        //TODO use (configurable) output global to query screen geometry.
        //TODO create buffer pool
        //TODO create wayland surface
        //TODO use fullscreen_shell instead of wl_shell
        return null;
    }
}
