package com.sun.glass.ui.monocle;

import com.google.auto.factory.AutoFactory;

@AutoFactory(allowSubclasses = true)
public class WaylandInputDeviceRegistry extends InputDeviceRegistry {


    /*
     *An InputDeviceRegistry handles discovery of input devices and feeding the events from these devices into the
     * JavaFX event queue. The LinuxInputDeviceRegistry included in JavaFX uses the udev Linux system to discover
     * devices and reads its input from device nodes in /dev/input. Other implementations are also possible, such as
     * X11InputDeviceRegistry which takes its input from the X event queue.
     */

    WaylandInputDeviceRegistry() {
        //TODO discover wayland seats & handle their events
    }
}
