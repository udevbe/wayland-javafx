package com.sun.glass.ui.monocle;


public class WaylandAcceleratedScreen extends AcceleratedScreen {
    /**
     * Perform basic egl intialization - open the display, create the drawing
     * surface, and create a GL context to that drawing surface.
     *
     * @param attributes - attributes to be used for filtering the EGL
     *                   configurations to choose from
     *
     * @throws GLException
     * @throws UnsatisfiedLinkError
     */
    WaylandAcceleratedScreen(final int[] attributes) throws GLException, UnsatisfiedLinkError {
        super(attributes);
    }
}
