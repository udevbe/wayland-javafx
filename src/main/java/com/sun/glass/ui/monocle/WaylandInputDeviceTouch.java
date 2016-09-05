package com.sun.glass.ui.monocle;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import org.freedesktop.wayland.client.WlSeatProxy;
import org.freedesktop.wayland.client.WlSurfaceProxy;
import org.freedesktop.wayland.client.WlTouchEventsV5;
import org.freedesktop.wayland.client.WlTouchProxy;
import org.freedesktop.wayland.util.Fixed;

import javax.annotation.Nonnull;

@AutoFactory(allowSubclasses = true)
public class WaylandInputDeviceTouch implements InputDevice, WlTouchEventsV5 {

    //should be accessed by jfx thread only! ->
    @Nonnull
    private final TouchInput touchInput = TouchInput.getInstance();
    @Nonnull
    private final TouchState touchState = new TouchState();
    //<-

    @Nonnull
    private final WaylandPlatform waylandPlatform;
    @Nonnull
    private final WlTouchProxy    wlTouchProxy;

    WaylandInputDeviceTouch(@Provided @Nonnull final WaylandPlatform waylandPlatform,
                            @Nonnull final WlSeatProxy wlSeatProxy) {
        this.waylandPlatform = waylandPlatform;
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
        this.waylandPlatform.getRunnableProcessor()
                            .invokeLater(() -> handleDown(serial,
                                                          time,
                                                          surface,
                                                          id,
                                                          x,
                                                          y));
    }

    private void handleDown(final int serial,
                            final int time,
                            @Nonnull final WlSurfaceProxy surface,
                            final int id,
                            @Nonnull final Fixed x,
                            @Nonnull final Fixed y) {
        this.touchInput.getState(this.touchState);
        final TouchState.Point point = new TouchState.Point();
        point.id = id;
        point.x = x.asInt();
        point.y = y.asInt();
        this.touchState.addPoint(point);
    }

    @Override
    public void up(final WlTouchProxy emitter,
                   final int serial,
                   final int time,
                   final int id) {
        this.waylandPlatform.getRunnableProcessor()
                            .invokeLater(() -> handleUp(serial,
                                                        time,
                                                        id));
    }

    private void handleUp(final int serial,
                          final int time,
                          final int id) {
        this.touchInput.getState(this.touchState);
        this.touchState.removePointForID(id);
    }

    @Override
    public void motion(final WlTouchProxy emitter,
                       final int time,
                       final int id,
                       @Nonnull final Fixed x,
                       @Nonnull final Fixed y) {
        this.waylandPlatform.getRunnableProcessor()
                            .invokeLater(() -> handleMotion(time,
                                                            id,
                                                            x,
                                                            y));
    }

    private void handleMotion(final int time,
                              final int id,
                              @Nonnull final Fixed x,
                              @Nonnull final Fixed y) {
        this.touchInput.getState(this.touchState);
        final TouchState.Point pointForID = this.touchState.getPointForID(id);
        pointForID.x = x.asInt();
        pointForID.y = y.asInt();
    }

    @Override
    public void frame(final WlTouchProxy emitter) {
        this.waylandPlatform.getRunnableProcessor()
                            .invokeLater(this::handleFrame);
    }

    private void handleFrame() {
        this.touchInput.setState(this.touchState);
    }

    @Override
    public void cancel(final WlTouchProxy emitter) {
        this.waylandPlatform.getRunnableProcessor()
                            .invokeLater(this::handleCancel);
    }

    private void handleCancel() {
        this.touchState.clear();
        this.touchInput.setState(this.touchState);
    }

    @Nonnull
    public WlTouchProxy getWlTouchProxy() {
        return this.wlTouchProxy;
    }
}
