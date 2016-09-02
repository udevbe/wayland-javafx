package com.sun.glass.ui.monocle;

import dagger.Component;

@Component
public interface WaylandComponent {

    PrivateWaylandPlatformFactory privateWaylandPlatformFactory();
}
