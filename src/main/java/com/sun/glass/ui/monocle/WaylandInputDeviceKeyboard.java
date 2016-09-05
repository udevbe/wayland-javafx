package com.sun.glass.ui.monocle;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import org.freedesktop.jaccall.Pointer;
import org.freedesktop.wayland.client.WlKeyboardEventsV5;
import org.freedesktop.wayland.client.WlKeyboardProxy;
import org.freedesktop.wayland.client.WlSeatProxy;
import org.freedesktop.wayland.client.WlSurfaceProxy;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

@AutoFactory(allowSubclasses = true)
public class WaylandInputDeviceKeyboard implements InputDevice, WlKeyboardEventsV5 {

    //should be accessed by jfx thread only!
    @Nonnull
    private final KeyState keyState = new KeyState();

    @Nonnull
    private final WaylandPlatform waylandPlatform;
    @Nonnull
    private final Libc            libc;
    @Nonnull
    private final Libxkbcommon    libxkbcommon;
    @Nonnull
    private final WlKeyboardProxy wlKeyboardProxy;

    private final long xkbContext;
    private       long state;

    WaylandInputDeviceKeyboard(@Provided @Nonnull final WaylandPlatform waylandPlatform,
                               @Provided @Nonnull final Libc libc,
                               @Provided @Nonnull final Libxkbcommon libxkbcommon,
                               @Nonnull final WlSeatProxy wlSeatProxy) {
        this.waylandPlatform = waylandPlatform;
        this.libc = libc;
        this.libxkbcommon = libxkbcommon;
        this.wlKeyboardProxy = wlSeatProxy.getKeyboard(this);

        //init xkbcommon. it will keep track of keys on a per keyboard base.
        this.xkbContext = this.libxkbcommon.xkb_context_new(Libxkbcommon.XKB_CONTEXT_NO_FLAGS);
    }

    @Override
    public boolean isTouch() {
        return false;
    }

    @Override
    public boolean isMultiTouch() {
        return false;
    }

    @Override
    public boolean isRelative() {
        return true;
    }

    @Override
    public boolean is5Way() {
        return true;
    }

    @Override
    public boolean isFullKeyboard() {
        return true;
    }

    @Override
    public void keymap(final WlKeyboardProxy emitter,
                       final int format,
                       final int fd,
                       final int size) {
        //we offload keymap events to the jfx thread as it's interpretation influence subsequent key events
        //which are also handled/converted on the jfx thread
        this.waylandPlatform.getRunnableProcessor()
                            .invokeLater(() -> keymapEvent(format,
                                                           fd,
                                                           size));
    }

    private void keymapEvent(final int format,
                             final int fd,
                             final int size) {
        if (this.state != 0L) {
            //TODO what to do with previous state?
        }

        //update keymap, used by key and modifiers events. http://xkbcommon.org/doc/current/md_doc_quick-guide.html
        final long file = this.libc.fdopen(fd,
                                           Pointer.nref("r").address);
        if (file == 0L) {
            //TODO handle failure
        }

        //get a pointer to gc enabled memory.
        final Pointer<String> contents = Pointer.nref(new String(new char[size]));
        this.libc.fgets(contents.address,
                        size,
                        file);

        final long keymap = this.libxkbcommon.xkb_keymap_new_from_string(this.xkbContext,
                                                                         contents.address,
                                                                         format,
                                                                         Libxkbcommon.XKB_CONTEXT_NO_FLAGS);
        if (keymap == 0L) {
            //TODO handle failure
        }

        this.state = this.libxkbcommon.xkb_state_new(keymap);
    }

    @Override
    public void enter(final WlKeyboardProxy emitter,
                      final int serial,
                      @Nonnull final WlSurfaceProxy surface,
                      @Nonnull final ByteBuffer keys) {
        //NOOP. we only create one surface (window), so it always has the focus by default.
    }

    @Override
    public void leave(final WlKeyboardProxy emitter,
                      final int serial,
                      @Nonnull final WlSurfaceProxy surface) {
        //NOOP. we only create one surface (window), so it always has the focus by default.

    }

    @Override
    public void key(final WlKeyboardProxy emitter,
                    final int serial,
                    final int time,
                    final int key,
                    final int state) {
        this.waylandPlatform.getRunnableProcessor()
                            .invokeLater(() -> keyEvent(serial,
                                                        time,
                                                        key,
                                                        state));
    }

    private void keyEvent(final int serial,
                          final int time,
                          final int key,
                          final int state) {
        KeyInput.getInstance()
                .getState(this.keyState);

        //TODO convert wayland key event to jfx key event

    }

    @Override
    public void modifiers(final WlKeyboardProxy emitter,
                          final int serial,
                          final int modsDepressed,
                          final int modsLatched,
                          final int modsLocked,
                          final int group) {
        this.waylandPlatform.getRunnableProcessor()
                            .invokeLater(() -> modifiersEvent(serial,
                                                              modsDepressed,
                                                              modsLatched,
                                                              modsLocked,
                                                              group));
    }

    private void modifiersEvent(final int serial,
                                final int modsDepressed,
                                final int modsLatched,
                                final int modsLocked,
                                final int group) {
        KeyInput.getInstance()
                .getState(this.keyState);

        //TODO convert wayland modifiers event to jfx key event

    }

    @Override
    public void repeatInfo(final WlKeyboardProxy emitter,
                           final int rate,
                           final int delay) {
        //TODO? does jfx care?
    }

    @Nonnull
    public WlKeyboardProxy getWlKeyboardProxy() {
        return this.wlKeyboardProxy;
    }
}
