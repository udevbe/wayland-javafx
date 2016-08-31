package com.sun.glass.ui.monocle;


public class WaylandPlatformFactory implements NativePlatformFactory {
    protected boolean matches() {
        return false;
    }

    protected WaylandPlatform createNativePlatform() {
        return null;
    }

    protected int getMajorVersion() {
        return 0;
    }

    protected int getMinorVersion() {
        return 0;
    }
}
