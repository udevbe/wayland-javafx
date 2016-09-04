package com.sun.glass.ui.monocle;

import com.google.auto.factory.AutoFactory;
import org.freedesktop.wayland.client.WlSeatProxy;
import org.freedesktop.wayland.client.WlSurfaceProxy;
import org.freedesktop.wayland.client.WlTouchEventsV5;
import org.freedesktop.wayland.client.WlTouchProxy;
import org.freedesktop.wayland.util.Fixed;

import javax.annotation.Nonnull;

@AutoFactory(allowSubclasses = true)
public class WaylandInputDeviceTouch implements InputDevice, WlTouchEventsV5 {

    @Nonnull
    private final WlTouchProxy wlTouchProxy;

    WaylandInputDeviceTouch(@Nonnull final WlSeatProxy wlSeatProxy) {
        this.wlTouchProxy = wlSeatProxy.getTouch(this);
    }

    @Override
    public boolean isTouch() {
        return true;
    }

    @Override
    public boolean isMultiTouch() {
        return true;
    }

    @Override
    public boolean isRelative() {
        return true;
    }

    @Override
    public boolean is5Way() {
        return false;
    }

    @Override
    public boolean isFullKeyboard() {
        return false;
    }

    @Override
    public void down(final WlTouchProxy emitter,
                     final int serial,
                     final int time,
                     @Nonnull final WlSurfaceProxy surface,
                     final int id,
                     @Nonnull final Fixed x,
                     @Nonnull final Fixed y) {

    }

    @Override
    public void up(final WlTouchProxy emitter,
                   final int serial,
                   final int time,
                   final int id) {

    }

    @Override
    public void motion(final WlTouchProxy emitter,
                       final int time,
                       final int id,
                       @Nonnull final Fixed x,
                       @Nonnull final Fixed y) {

    }

    @Override
    public void frame(final WlTouchProxy emitter) {

    }

    @Override
    public void cancel(final WlTouchProxy emitter) {

    }

    @Nonnull
    public WlTouchProxy getWlTouchProxy() {
        return this.wlTouchProxy;
    }
}
