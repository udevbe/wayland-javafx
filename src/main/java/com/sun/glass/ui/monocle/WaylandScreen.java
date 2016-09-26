package com.sun.glass.ui.monocle;

import com.sun.glass.ui.Pixels;
import org.freedesktop.jaccall.JNI;
import org.freedesktop.jaccall.Pointer;
import org.freedesktop.wayland.client.WlBufferProxy;
import org.freedesktop.wayland.client.WlCallbackProxy;
import org.freedesktop.wayland.client.WlCompositorProxy;
import org.freedesktop.wayland.client.WlDisplayProxy;
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
import java.util.concurrent.CountDownLatch;

import static com.sun.glass.ui.monocle.Libpixman1.PIXMAN_OP_OVER;
import static com.sun.glass.ui.monocle.Libpixman1.PIXMAN_OP_SRC;
import static com.sun.glass.ui.monocle.Libpixman1.PIXMAN_a8r8g8b8;

class WaylandScreen implements NativeScreen,
                               WlSurfaceEventsV4,
                               WlShellSurfaceEvents {

    private final WlSurfaceProxy    wlSurfaceProxy;
    private final WlDisplayProxy    display;
    private final WaylandOutput     waylandOutput;
    private final WaylandBufferPool waylandBufferPool;
    private       int               dpi;
    private       WlBufferProxy     wlBufferProxy;
    private       WlCallbackProxy   wlCallbackProxy;

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

    WaylandScreen(final WaylandBufferPoolFactory waylandBufferPoolFactory,
                  final WlDisplayProxy wlDisplayProxy,
                  final WaylandOutput waylandOutput,
                  final WlCompositorProxy wlCompositorProxy,
                  final WlShellProxy wlShellProxy,
                  final WaylandShm waylandShm) {
        this.display = wlDisplayProxy;
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
        return Pixels.Format.BYTE_BGRA_PRE;
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

        WaylandPlatformFactory.WL_LOOP.submit(() -> {
            //TODO this call should block if no more buffers are available & it should unblock as soon as the compositor release one.
            //-> we don't have to wait here, but instead can initiate a wait as soon as we actually need a buffer (eg uploadPixels).

            if (this.wlBufferProxy == null) {
                this.wlBufferProxy = this.waylandBufferPool.popBuffer();
                this.wlSurfaceProxy.attach(this.wlBufferProxy,
                                           0,
                                           0);
                this.wlSurfaceProxy.damage(x,
                                           y,
                                           width,
                                           height);
            }


            final WaylandBuffer waylandBuffer = (WaylandBuffer) this.wlBufferProxy.getImplementation();

            final long pixels;
            if (b.isDirect()) {
                pixels = JNI.unwrap(b);
            }
            else {
                if (b instanceof ByteBuffer) {
                    final ByteBuffer bb = (ByteBuffer) b;
                    final byte[]     array;
                    if (bb.hasArray()) {
                        array = bb.array();
                    }
                    else {
                        array = new byte[bb.capacity()];
                        bb.rewind();
                        bb.get(array);
                    }
                    pixels = Pointer.nref(array).address;
                }
                else {
                    final IntBuffer ib = ((IntBuffer) b);
                    final int[]     array;
                    if (ib.hasArray()) {
                        array = ib.array();
                    }
                    else {
                        array = new int[ib.capacity()];
                        ib.rewind();
                        ib.get(array);
                    }
                    pixels = Pointer.nref(array).address;
                }
            }

            final long src = Libpixman1.pixman_image_create_bits_no_clear(PIXMAN_a8r8g8b8,
                                                                          width,
                                                                          height,
                                                                          pixels,
                                                                          width * 4);
            final long dst = waylandBuffer.getPixmanImage();

            Libpixman1.pixman_image_composite(alpha == 1.0f ? PIXMAN_OP_SRC : PIXMAN_OP_OVER,
                                              src,
                                              0L,
                                              dst,
                                              (short) 0,
                                              (short) 0,
                                              (short) 0,
                                              (short) 0,
                                              (short) x,
                                              (short) y,
                                              (short) width,
                                              (short) height);
        });
    }

    @Override
    public void swapBuffers() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        WaylandPlatformFactory.WL_LOOP.submit(() -> {
            if (this.wlCallbackProxy != null) {
                this.wlCallbackProxy.destroy();
            }
            this.wlCallbackProxy = this.wlSurfaceProxy.frame((emitter, callbackData) -> {
                countDownLatch.countDown();
            });
            this.wlSurfaceProxy.commit();
            this.wlBufferProxy = null;
        });

        //this blocks until we receive feedback from the compositor that a next redraw should happen
        try {
            countDownLatch.await();
        }
        catch (final InterruptedException e) {
            e.printStackTrace();
        }
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
