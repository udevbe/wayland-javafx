package com.sun.glass.ui.monocle;


import org.freedesktop.wayland.client.WlDisplayProxy;
import org.freedesktop.wayland.client.WlRegistryEvents;
import org.freedesktop.wayland.client.WlRegistryProxy;

import javax.annotation.Nonnull;

public class WaylandPlatformFactory extends NativePlatformFactory {

    private static final int MAJOR = 1;
    private static final int MINOR = 0;
    private final PrivateWaylandPlatformFactory privateWaylandPlatformFactory;


    public WaylandPlatformFactory() {
        this.privateWaylandPlatformFactory = DaggerWaylandComponent.create()
                                                                   .privateWaylandPlatformFactory();
    }

    protected boolean matches() {
        //TODO check if we're capable of running wayland (check if xdg_runtime dir is set & check if compositor socket exists)
        return true;
    }

    protected WaylandPlatform createNativePlatform() {

        final WlDisplayProxy  wlDisplayProxy  = WlDisplayProxy.connect("wayland-0");
        final WaylandPlatform waylandPlatform = this.privateWaylandPlatformFactory.create(wlDisplayProxy);

        wlDisplayProxy.getRegistry(new WlRegistryEvents() {
            @Override
            public void global(final WlRegistryProxy emitter,
                               final int name,
                               @Nonnull final String interfaceName,
                               final int version) {
                waylandPlatform.onGlobalAdded(emitter,
                                              name,
                                              interfaceName,
                                              version);
            }

            @Override
            public void globalRemove(final WlRegistryProxy emitter,
                                     final int name) {
                waylandPlatform.onGlobalRemoved(emitter,
                                                name);
            }
        });

        return waylandPlatform;
    }

    protected int getMajorVersion() {
        return MAJOR;
    }

    protected int getMinorVersion() {
        return MINOR;
    }
}
