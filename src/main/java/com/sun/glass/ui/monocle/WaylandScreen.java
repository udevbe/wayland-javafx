package com.sun.glass.ui.monocle;

import com.google.auto.factory.AutoFactory;

import java.nio.Buffer;
import java.nio.ByteBuffer;

@AutoFactory(allowSubclasses = true,
             className = "PrivateWaylandScreenFactory")
public class WaylandScreen implements NativeScreen {

    /*
     * A NativeScreen provides a way for JavaFX to get information about the screen it is using and to put pixels on
     * the screen using software rendering. Most implementations in Monocle of NativeScreen are based on Linux frame
     * buffers, but other implementations are possible. For example, the X11Screen class creates a single window using
     * the X API and renders all content into that.
     *
     * Note that there is no class "NativeWindow" in Monocle. This is because Monocle does not make use of any native
     * window system on the devices it uses, and does not require such a window system. In general, Monocle assumes
     * that it owns the entire screen and makes no attempt to cooperate with any window system that may be present.
     *
     * Windows and Stages in JavaFX on embedded Linux devices are concepts internal to JavaFX that do not correspond
     * directly to any native window system. In practice window content is stored in JavaFX and the window stack is
     * composed onto the screen surface whenever JavaFX enders a frame. When using the software rendering pipeline,
     * window content is stored in a Pixels object that wraps a ByteBuffer. When using the accelerated OpenGL ES 2.0
     * pipeline, window content is stored internally in an OpenGL Texture.
     */

    WaylandScreen() {
        //TODO this is our main wayland 'fullscreen' surface
    }

    public int getDepth() {
        return 0;
    }

    public int getNativeFormat() {
        return 0;
    }

    public int getWidth() {
        return 0;
    }

    public int getHeight() {
        return 0;
    }

    public int getDPI() {
        return 0;
    }

    public long getNativeHandle() {
        return 0;
    }

    public void shutdown() {

    }

    public void uploadPixels(final Buffer b,
                             final int x,
                             final int y,
                             final int width,
                             final int height,
                             final float alpha) {

    }

    public void swapBuffers() {

    }

    public ByteBuffer getScreenCapture() {
        return null;
    }

    public float getScale() {
        return 0;
    }
}
