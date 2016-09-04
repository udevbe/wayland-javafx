package com.sun.glass.ui.monocle;

import com.google.auto.factory.AutoFactory;
import org.freedesktop.wayland.client.WlPointerEventsV5;
import org.freedesktop.wayland.client.WlPointerProxy;
import org.freedesktop.wayland.client.WlSeatProxy;
import org.freedesktop.wayland.client.WlSurfaceProxy;
import org.freedesktop.wayland.util.Fixed;

import javax.annotation.Nonnull;

@AutoFactory(allowSubclasses = true)
public class WaylandInputDevicePointer implements InputDevice, WlPointerEventsV5 {

    @Nonnull
    private final WlPointerProxy wlPointerProxy;

    WaylandInputDevicePointer(@Nonnull final WlSeatProxy wlSeatProxy) {
        this.wlPointerProxy = wlSeatProxy.getPointer(this);
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
        return false;
    }

    @Override
    public boolean isFullKeyboard() {
        return false;
    }

    @Override
    public void enter(final WlPointerProxy emitter,
                      final int serial,
                      @Nonnull final WlSurfaceProxy surface,
                      @Nonnull final Fixed surfaceX,
                      @Nonnull final Fixed surfaceY) {

    }

    @Override
    public void leave(final WlPointerProxy emitter,
                      final int serial,
                      @Nonnull final WlSurfaceProxy surface) {

    }

    @Override
    public void motion(final WlPointerProxy emitter,
                       final int time,
                       @Nonnull final Fixed surfaceX,
                       @Nonnull final Fixed surfaceY) {

    }

    @Override
    public void button(final WlPointerProxy emitter,
                       final int serial,
                       final int time,
                       final int button,
                       final int state) {

    }

    @Override
    public void axis(final WlPointerProxy emitter,
                     final int time,
                     final int axis,
                     @Nonnull final Fixed value) {

    }

    @Override
    public void frame(final WlPointerProxy emitter) {

    }

    @Override
    public void axisSource(final WlPointerProxy emitter,
                           final int axisSource) {

    }

    @Override
    public void axisStop(final WlPointerProxy emitter,
                         final int time,
                         final int axis) {

    }

    @Override
    public void axisDiscrete(final WlPointerProxy emitter,
                             final int axis,
                             final int discrete) {

    }

    @Nonnull
    public WlPointerProxy getWlPointerProxy() {
        return this.wlPointerProxy;
    }
}
