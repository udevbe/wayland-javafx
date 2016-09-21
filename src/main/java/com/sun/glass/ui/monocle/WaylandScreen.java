package com.sun.glass.ui.monocle;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.sun.glass.ui.Pixels;
import org.freedesktop.jaccall.JNI;
import org.freedesktop.jaccall.Pointer;
import org.freedesktop.wayland.client.WlBufferProxy;
import org.freedesktop.wayland.client.WlCompositorProxy;
import org.freedesktop.wayland.client.WlOutputProxy;
import org.freedesktop.wayland.client.WlShellProxy;
import org.freedesktop.wayland.client.WlShellSurfaceEvents;
import org.freedesktop.wayland.client.WlShellSurfaceProxy;
import org.freedesktop.wayland.client.WlSurfaceEventsV4;
import org.freedesktop.wayland.client.WlSurfaceProxy;
import org.freedesktop.wayland.shared.WlShellSurfaceFullscreenMethod;
import org.freedesktop.wayland.shared.WlShmFormat;

import javax.annotation.Nonnull;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static com.sun.glass.ui.monocle.Libpixman1.PIXMAN_a8r8g8b8;

@AutoFactory(allowSubclasses = true,
             className = "WaylandScreenFactory")
public class WaylandScreen implements NativeScreen,
                                      WlSurfaceEventsV4,
                                      WlShellSurfaceEvents {

    private final WlSurfaceProxy    wlSurfaceProxy;
    private final WaylandOutput     waylandOutput;
    private final WaylandBufferPool waylandBufferPool;
    private       int               dpi;
    private       WlBufferProxy     wlBufferProxy;

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

    WaylandScreen(@Provided final WaylandBufferPoolFactory waylandBufferPoolFactory,
                  final WaylandOutput waylandOutput,
                  final WlCompositorProxy wlCompositorProxy,
                  final WlShellProxy wlShellProxy,
                  final WaylandShm waylandShm) {
        this.waylandOutput = waylandOutput;

        this.wlSurfaceProxy = wlCompositorProxy.createSurface(this);
        final WlShellSurfaceProxy shellSurface = wlShellProxy.getShellSurface(this,
                                                                              this.wlSurfaceProxy);
        //notify the compositor we want to run fullscreen
        shellSurface.setFullscreen(WlShellSurfaceFullscreenMethod.DRIVER.value,
                                   0,
                                   waylandOutput.getWlOutputProxy());

        this.waylandBufferPool = waylandBufferPoolFactory.create(waylandShm.getWlShmProxy(),
                                                                 getWidth(),
                                                                 getHeight(),
                                                                 2,
                                                                 WlShmFormat.ARGB8888);
        this.wlBufferProxy = this.waylandBufferPool.popBuffer();
        this.wlSurfaceProxy.attach(this.wlBufferProxy,
                                   0,
                                   0);
    }

    public int getWidth() {
        return this.waylandOutput.getWidth();
    }

    public int getHeight() {
        return this.waylandOutput.getHeight();
    }

    public int getDepth() {
        return 32;
    }

    public int getNativeFormat() {
        return Pixels.Format.BYTE_ARGB;
    }

    public int getDPI() {
        if (0 == this.dpi) {
            updateDpi();
        }
        return this.dpi;
    }

    private void updateDpi() {
        if (0 >= this.waylandOutput.getPhysicalWidth() ||
            0 >= this.waylandOutput.getPhysicalHeight() ||
            0 >= this.waylandOutput.getWidth() ||
            0 >= this.waylandOutput.getHeight()) {
            //not all values have a sensible value, return something standard.
            this.dpi = 96;
        }
        else {
            final double diagonalScreenMM = Math.sqrt((this.waylandOutput.getPhysicalWidth() * this.waylandOutput.getPhysicalWidth()) +
                                                      (this.waylandOutput.getPhysicalHeight() * this.waylandOutput.getPhysicalHeight()));
            final double diagonalPixels = Math.sqrt((this.waylandOutput.getWidth() * this.waylandOutput.getWidth()) +
                                                    (this.waylandOutput.getHeight() * this.waylandOutput.getHeight()));
            final double dpMM = diagonalPixels / diagonalScreenMM;
            this.dpi = (int) (dpMM * 25.4);
        }
    }

    @Override
    public long getNativeHandle() {
        return this.wlSurfaceProxy.getPointer();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void uploadPixels(final Buffer b,
                             final int x,
                             final int y,
                             final int width,
                             final int height,
                             final float alpha) {
        final WaylandBuffer waylandBuffer = (WaylandBuffer) this.wlBufferProxy.getImplementation();

        //TODO use pixman native library to update our buffer

        final long pixels;
        if (b.isDirect()) {
            pixels = JNI.unwrap(b);
        }
        else {
            if (b instanceof ByteBuffer) {
                final ByteBuffer bb = (ByteBuffer) b;
                //TODO call bb.hasArray()?
                pixels = Pointer.nref(bb.array()).address;
            }
            else {
                //int buffer
                final IntBuffer ib = ((IntBuffer) b);
                pixels = Pointer.nref(ib.array()).address;
            }
        }

        final long src = Libpixman1.pixman_image_create_bits(PIXMAN_a8r8g8b8,
                                                             width,
                                                             height,
                                                             pixels,
                                                             width * 4);
        final long dst = waylandBuffer.getPixmanImage();

        //Libpixman1.pixman_image_composite();


        this.wlSurfaceProxy.damage(x,
                                   y,
                                   width,
                                   height);
    }

    @Override
    public void swapBuffers() {
        this.wlSurfaceProxy.commit();
        //TODO the current this.wlBufferProxy has been submitted to the compositor and should not be written to.
        //-> mark this buffer as "in use".

        //TODO this call should block if no more buffers are available & it should unblock as soon as the compositor release one.
        //-> we don't have to wait here, but instead can initiate a wait as soon as we actually need a buffer (eg uploadPixels).
        this.wlBufferProxy = this.waylandBufferPool.popBuffer();
        this.wlSurfaceProxy.attach(this.wlBufferProxy,
                                   0,
                                   0);
    }

    public ByteBuffer getScreenCapture() {
        return null;
    }

    public float getScale() {
        return this.waylandOutput.getScaleFactor();
    }

    @Override
    public void enter(final WlSurfaceProxy emitter,
                      @Nonnull final WlOutputProxy output) {
        //NOOP monocle only supports a single output
    }

    @Override
    public void leave(final WlSurfaceProxy emitter,
                      @Nonnull final WlOutputProxy output) {
        //NOOP monocle only supports a single output
    }

    @Override
    public void ping(final WlShellSurfaceProxy emitter,
                     final int serial) {
        emitter.pong(serial);
    }

    @Override
    public void configure(final WlShellSurfaceProxy emitter,
                          final int edges,
                          final int width,
                          final int height) {
        //NOOP monocle doesn't do pointer based resizes
    }

    @Override
    public void popupDone(final WlShellSurfaceProxy emitter) {
        //NOOP monocle assumes we're the only (wayland) client on the system
    }
}
