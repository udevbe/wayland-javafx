package com.sun.glass.ui.monocle;

import com.google.auto.factory.AutoFactory;
import org.freedesktop.wayland.client.WlKeyboardEventsV5;
import org.freedesktop.wayland.client.WlKeyboardProxy;
import org.freedesktop.wayland.client.WlSeatProxy;
import org.freedesktop.wayland.client.WlSurfaceProxy;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

@AutoFactory(allowSubclasses = true)
public class WaylandInputDeviceKeyboard implements InputDevice, WlKeyboardEventsV5 {

    @Nonnull
    private final WlKeyboardProxy wlKeyboardProxy;

    WaylandInputDeviceKeyboard(@Nonnull final WlSeatProxy wlSeatProxy) {
        this.wlKeyboardProxy = wlSeatProxy.getKeyboard(this);
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
        return false;
    }

    @Override
    public boolean is5Way() {
        return false;
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

    }

    @Override
    public void enter(final WlKeyboardProxy emitter,
                      final int serial,
                      @Nonnull final WlSurfaceProxy surface,
                      @Nonnull final ByteBuffer keys) {

    }

    @Override
    public void leave(final WlKeyboardProxy emitter,
                      final int serial,
                      @Nonnull final WlSurfaceProxy surface) {

    }

    @Override
    public void key(final WlKeyboardProxy emitter,
                    final int serial,
                    final int time,
                    final int key,
                    final int state) {

    }

    @Override
    public void modifiers(final WlKeyboardProxy emitter,
                          final int serial,
                          final int modsDepressed,
                          final int modsLatched,
                          final int modsLocked,
                          final int group) {

    }

    @Override
    public void repeatInfo(final WlKeyboardProxy emitter,
                           final int rate,
                           final int delay) {

    }

    @Nonnull
    public WlKeyboardProxy getWlKeyboardProxy() {
        return this.wlKeyboardProxy;
    }
}
