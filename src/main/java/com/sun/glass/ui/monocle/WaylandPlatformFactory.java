package com.sun.glass.ui.monocle;

import org.freedesktop.wayland.client.WlDisplayProxy;

public class WaylandPlatformFactory extends NativePlatformFactory {

    private static final int MAJOR = 1;
    private static final int MINOR = 0;

    protected boolean matches() {
        //TODO check if we're capable of running wayland (check if xdg_runtime dir is set & check if compositor socket exists)
        return true;
    }

    protected WaylandPlatform createNativePlatform() {
        final WaylandComponent waylandComponent = DaggerWaylandComponent.create();
        final WaylandPlatform  waylandPlatform  = waylandComponent.waylandPlatform();
        final WlDisplayProxy   wlDisplayProxy   = waylandComponent.wlDisplayProxy();

        wlDisplayProxy.getRegistry(waylandPlatform);
        //make sure we receive all globals
        wlDisplayProxy.roundtrip();
        //we should have received the shm proxy by now
        assert waylandPlatform.getWaylandShm() != null;
        wlDisplayProxy.roundtrip();

        return waylandPlatform;
    }

    protected int getMajorVersion() {
        return MAJOR;
    }

    protected int getMinorVersion() {
        return MINOR;
    }
}
