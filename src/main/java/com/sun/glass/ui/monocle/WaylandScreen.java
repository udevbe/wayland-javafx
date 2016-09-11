package com.sun.glass.ui.monocle;

import com.google.auto.factory.AutoFactory;
import com.sun.glass.ui.Pixels;
import org.freedesktop.wayland.client.WlBufferEvents;
import org.freedesktop.wayland.client.WlBufferProxy;
import org.freedesktop.wayland.client.WlCompositorProxy;
import org.freedesktop.wayland.client.WlOutputEventsV2;
import org.freedesktop.wayland.client.WlOutputProxy;
import org.freedesktop.wayland.client.WlRegistryProxy;
import org.freedesktop.wayland.client.WlShellProxy;
import org.freedesktop.wayland.client.WlShellSurfaceEvents;
import org.freedesktop.wayland.client.WlShellSurfaceProxy;
import org.freedesktop.wayland.client.WlSurfaceEventsV4;
import org.freedesktop.wayland.client.WlSurfaceProxy;

import javax.annotation.Nonnull;
import java.nio.Buffer;
import java.nio.ByteBuffer;

@AutoFactory(allowSubclasses = true,
             className = "PrivateWaylandScreenFactory")
public class WaylandScreen implements NativeScreen, WlOutputEventsV2, WlBufferEvents, WlSurfaceEventsV4, WlShellSurfaceEvents {

    private final WlSurfaceProxy      wlSurfaceProxy;
    private final WlShellSurfaceProxy wlShellSurfaceProxy;
    private final WlOutputProxy       wlOutputProxy;
    private       int                 width;
    private       int                 height;
    private       int                 dpi;
    private       int                 physicalWidth;
    private       int                 physicalHeight;
    private int scale = 1;

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

    WaylandScreen(final int name,
                  final WlRegistryProxy wlRegistryProxy,
                  final WlCompositorProxy wlCompositorProxy,
                  final WlShellProxy wlShellProxy) {
        this.wlOutputProxy = wlRegistryProxy.bind(name,
                                                  WlOutputProxy.class,
                                                  WlOutputEventsV2.VERSION,
                                                  this);
        this.wlSurfaceProxy = wlCompositorProxy.createSurface(this);
        this.wlShellSurfaceProxy = wlShellProxy.getShellSurface(this,
                                                                this.wlSurfaceProxy);
    }

    public int getDepth() {
        return 32;
    }

    public int getNativeFormat() {
        return Pixels.Format.BYTE_ARGB;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getDPI() {
        return this.dpi;
    }

    public long getNativeHandle() {
        return this.wlSurfaceProxy.getPointer();
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
        return this.scale;
    }

    @Override
    public void geometry(final WlOutputProxy emitter,
                         final int x,
                         final int y,
                         final int physicalWidth,
                         final int physicalHeight,
                         final int subpixel,
                         @Nonnull final String make,
                         @Nonnull final String model,
                         final int transform) {
        this.physicalWidth = physicalWidth;
        this.physicalHeight = physicalHeight;

        if (emitter.getVersion() < WlOutputEventsV2.VERSION) {
            updateDpi();
        }
    }

    private void updateDpi() {
        if (0 <= this.physicalWidth ||
            0 <= this.physicalHeight ||
            0 <= this.width ||
            0 <= this.height) {
            //not all values have a sensible value, return something standard.
            this.dpi = 96;
        }
        else {
            final double diagonalScreenMM = Math.sqrt((this.physicalWidth * this.physicalWidth) + (this.physicalHeight * this.physicalHeight));
            final double diagonalPixels   = Math.sqrt((this.width * this.width) + (this.height * this.height));
            final double dpMM             = diagonalPixels / diagonalScreenMM;
            this.dpi = (int) (dpMM * 25.4);
        }
    }

    @Override
    public void mode(final WlOutputProxy emitter,
                     final int flags,
                     final int width,
                     final int height,
                     final int refresh) {
        this.width = width;
        this.height = height;

        if (emitter.getVersion() < WlOutputEventsV2.VERSION) {
            updateDpi();
        }
    }

    @Override
    public void done(final WlOutputProxy emitter) {
        updateDpi();
    }

    @Override
    public void scale(final WlOutputProxy emitter,
                      final int factor) {
        this.scale = factor;
    }

    @Override
    public void release(final WlBufferProxy emitter) {

    }

    @Override
    public void enter(final WlSurfaceProxy emitter,
                      @Nonnull final WlOutputProxy output) {

    }

    @Override
    public void leave(final WlSurfaceProxy emitter,
                      @Nonnull final WlOutputProxy output) {

    }

    @Override
    public void ping(final WlShellSurfaceProxy emitter,
                     final int serial) {

    }

    @Override
    public void configure(final WlShellSurfaceProxy emitter,
                          final int edges,
                          final int width,
                          final int height) {

    }

    @Override
    public void popupDone(final WlShellSurfaceProxy emitter) {

    }
}
