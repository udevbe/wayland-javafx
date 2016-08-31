package com.sun.glass.ui.monocle;


import com.sun.glass.ui.monocle.NativeScreen;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class WaylandScreen implements NativeScreen {
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
