package com.sun.glass.ui.monocle;

import com.google.auto.factory.AutoFactory;
import com.sun.glass.events.MouseEvent;
import org.freedesktop.wayland.client.WlPointerEventsV5;
import org.freedesktop.wayland.client.WlPointerProxy;
import org.freedesktop.wayland.client.WlSeatProxy;
import org.freedesktop.wayland.client.WlSurfaceProxy;
import org.freedesktop.wayland.shared.WlPointerAxis;
import org.freedesktop.wayland.shared.WlPointerButtonState;
import org.freedesktop.wayland.util.Fixed;

import javax.annotation.Nonnull;

@AutoFactory(allowSubclasses = true)
public class WaylandInputDevicePointer implements InputDevice, WlPointerEventsV5 {

    //should be accessed by jfx thread only! ->
    @Nonnull
    private final MouseInput mouseInput = MouseInput.getInstance();
    @Nonnull
    private final MouseState mouseState = new MouseState();
    //<-

    @Nonnull
    private final WlPointerProxy wlPointerProxy;

    //the protocol version supported by the compositor for this object
    private final int version;

    WaylandInputDevicePointer(@Nonnull final WlSeatProxy wlSeatProxy) {
        this.wlPointerProxy = wlSeatProxy.getPointer(this);
        this.version = this.wlPointerProxy.getVersion();
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
        //NOOP. we only create one surface (window), so it always has the focus by default.
    }

    @Override
    public void leave(final WlPointerProxy emitter,
                      final int serial,
                      @Nonnull final WlSurfaceProxy surface) {
        //NOOP. we only create one surface (window), so it always has the focus by default.
    }

    @Override
    public void motion(final WlPointerProxy emitter,
                       final int time,
                       @Nonnull final Fixed surfaceX,
                       @Nonnull final Fixed surfaceY) {
        RunnableProcessor.runLater(() -> handleMotion(time,
                                                      surfaceX,
                                                      surfaceY));
    }

    private void handleMotion(final int time,
                              @Nonnull final Fixed surfaceX,
                              @Nonnull final Fixed surfaceY) {
        final int x = surfaceX.asInt();
        final int y = surfaceY.asInt();

        this.mouseInput.getState(this.mouseState);
        this.mouseState.setX(x);
        this.mouseState.setY(y);

        //versions prior to V5 do not support the frame event, which signals the end of a stream of events.
        if (this.version < WlPointerEventsV5.VERSION) {
            this.mouseInput.setState(this.mouseState,
                                     false);
        }
    }

    @Override
    public void button(final WlPointerProxy emitter,
                       final int serial,
                       final int time,
                       final int button,
                       final int state) {
        RunnableProcessor.runLater(() -> handleButton(serial,
                                                      time,
                                                      button,
                                                      state));
    }

    private void handleButton(final int serial,
                              final int time,
                              final int button,
                              final int state) {
        this.mouseInput.getState(this.mouseState);
        if (state == WlPointerButtonState.PRESSED.value) {
            this.mouseState.pressButton(mouseButtonForKeyCode(button));
        }
        else {
            this.mouseState.releaseButton(mouseButtonForKeyCode(button));
        }
        //versions prior to V5 do not support the frame event, which signals the end of a stream of events.
        if (this.version < WlPointerEventsV5.VERSION) {
            this.mouseInput.setState(this.mouseState,
                                     false);
        }
    }

    //copy-pasta from LinuxMouseProcessor
    private static int mouseButtonForKeyCode(final int keyCode) {
        switch (keyCode) {
            case LinuxInput.BTN_MOUSE:
                return MouseEvent.BUTTON_LEFT;
            case LinuxInput.BTN_MIDDLE:
                return MouseEvent.BUTTON_OTHER;
            case LinuxInput.BTN_RIGHT:
                return MouseEvent.BUTTON_RIGHT;
            default:
                return -1;
        }
    }

    @Override
    public void axis(final WlPointerProxy emitter,
                     final int time,
                     final int axis,
                     @Nonnull final Fixed value) {
        RunnableProcessor.runLater(() -> handleAxis(time,
                                                    axis,
                                                    value));
    }

    private void handleAxis(final int time,
                            final int axis,
                            @Nonnull final Fixed value) {
        this.mouseInput.getState(this.mouseState);
        if (axis == WlPointerAxis.VERTICAL_SCROLL.value) {
            //monocle does not support a specific scroll value, just down/up/none which corresponds to a value of 1 or 0. :(
            if (value.asInt() < 0) {
                this.mouseState.setWheel(MouseState.WHEEL_DOWN);
            }
            else if (value.asInt() > 0) {
                this.mouseState.setWheel(MouseState.WHEEL_UP);
            }
            else {
                this.mouseState.setWheel(MouseState.WHEEL_NONE);
            }
        }
        //else {
        //monocle does not support horizontal scrolling
        //}


        //versions prior to V5 do not support the frame event, which signals the end of a stream of events.
        if (this.version < WlPointerEventsV5.VERSION) {
            this.mouseInput.setState(this.mouseState,
                                     false);
        }
    }

    @Override
    public void frame(final WlPointerProxy emitter) {
        this.mouseInput.setState(this.mouseState,
                                 false);
    }

    @Override
    public void axisSource(final WlPointerProxy emitter,
                           final int axisSource) {
        //NOOP monocle doesn't care what the event source is
    }

    @Override
    public void axisStop(final WlPointerProxy emitter,
                         final int time,
                         final int axis) {
        //NOOP monocle does it's own kind of kinetic scrolling
    }

    @Override
    public void axisDiscrete(final WlPointerProxy emitter,
                             final int axis,
                             final int discrete) {
        //NOOP monocle does not support a discrete scroll values, just down/up/none which corresponds to a value
        //of 1 or 0. :( This is already handled in the axis() event.
    }

    @Nonnull
    public WlPointerProxy getWlPointerProxy() {
        return this.wlPointerProxy;
    }
}
