package com.sun.glass.ui.monocle;

import org.freedesktop.wayland.client.WlRegistryProxy;
import org.freedesktop.wayland.client.WlSeatEventsV3;
import org.freedesktop.wayland.client.WlSeatProxy;
import org.freedesktop.wayland.shared.WlSeatCapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WaylandSeat implements WlSeatEventsV3 {

    @Nonnull
    private final WlSeatProxy         wlSeatProxy;
    @Nonnull
    private final InputDeviceRegistry inputDeviceRegistry;

    @Nullable
    private WaylandInputDeviceKeyboard waylandInputDeviceKeyboard;
    @Nullable
    private WaylandInputDeviceTouch    waylandInputDeviceTouch;
    @Nullable
    private WaylandInputDevicePointer  waylandInputDevicePointer;

    WaylandSeat(@Nonnull final WlRegistryProxy registryProxy,
                final int name,
                @Nonnull final String interfaceName,
                final int version,
                @Nonnull final InputDeviceRegistry inputDeviceRegistry) {
        this.inputDeviceRegistry = inputDeviceRegistry;
        this.wlSeatProxy = registryProxy.bind(name,
                                              WlSeatProxy.class,
                                              WlSeatEventsV3.VERSION,
                                              this);
    }

    @Override
    public void capabilities(final WlSeatProxy emitter,
                             final int capabilities) {
        //TODO delete/add wayland input device

        if ((WlSeatCapability.KEYBOARD.value & capabilities) != 0 && this.waylandInputDeviceKeyboard == null) {
            //keyboard was added
            this.waylandInputDeviceKeyboard = new WaylandInputDeviceKeyboard(this.wlSeatProxy);
            this.inputDeviceRegistry.getInputDevices()
                                    .add(this.waylandInputDeviceKeyboard);
        }
        else if ((WlSeatCapability.KEYBOARD.value & capabilities) == 0 && this.waylandInputDeviceKeyboard != null) {
            //keyboard was removed
            this.inputDeviceRegistry.getInputDevices()
                                    .remove(this.waylandInputDeviceKeyboard);
            this.waylandInputDeviceKeyboard.getWlKeyboardProxy()
                                           .release();
            this.waylandInputDeviceKeyboard = null;
        }

        if ((WlSeatCapability.POINTER.value & capabilities) != 0 && this.waylandInputDevicePointer == null) {
            //pointer was added
            this.waylandInputDevicePointer = new WaylandInputDevicePointer(this.wlSeatProxy);
            this.inputDeviceRegistry.getInputDevices()
                                    .add(this.waylandInputDevicePointer);
        }
        else if ((WlSeatCapability.POINTER.value & capabilities) == 0 && this.waylandInputDevicePointer != null) {
            //pointer was removed
            this.inputDeviceRegistry.getInputDevices()
                                    .remove(this.waylandInputDevicePointer);
            this.waylandInputDevicePointer.getWlPointerProxy()
                                          .release();
            this.waylandInputDevicePointer = null;
        }

        if ((WlSeatCapability.TOUCH.value & capabilities) != 0 && this.waylandInputDeviceTouch == null) {
            //touch was added
            this.waylandInputDeviceTouch = new WaylandInputDeviceTouch(this.wlSeatProxy);
            ;
            this.inputDeviceRegistry.getInputDevices()
                                    .add(this.waylandInputDeviceTouch);
        }
        else if ((WlSeatCapability.TOUCH.value & capabilities) == 0 && this.waylandInputDeviceTouch != null) {
            //touch was removed
            this.inputDeviceRegistry.getInputDevices()
                                    .remove(this.waylandInputDeviceTouch);
            this.waylandInputDeviceTouch.getWlTouchProxy()
                                        .release();
            this.waylandInputDeviceTouch = null;
        }

        //TODO release seat if no devices are announced anymore
    }

    @Override
    public void name(final WlSeatProxy emitter,
                     @Nonnull final String name) {

    }

    @Nullable
    public WaylandInputDevicePointer getWaylandInputDevicePointer() {
        return waylandInputDevicePointer;
    }
}
