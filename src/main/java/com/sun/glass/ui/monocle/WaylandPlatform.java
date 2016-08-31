package com.sun.glass.ui.monocle;


public class WaylandPlatform extends NativePlatform {

    protected InputDeviceRegistry createInputDeviceRegistry() {
        return null;
    }

    protected NativeCursor createCursor() {
        return null;
    }

    protected WaylandScreen createScreen() {
        return null;
    }
}
