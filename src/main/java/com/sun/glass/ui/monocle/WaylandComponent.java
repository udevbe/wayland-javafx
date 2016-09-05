package com.sun.glass.ui.monocle;

import dagger.Component;
import org.freedesktop.wayland.client.WlDisplayProxy;

import javax.inject.Singleton;

@Component(modules = WaylandModule.class)
@Singleton
public interface WaylandComponent {

    WaylandPlatform waylandPlatform();

    WlDisplayProxy wlDisplayProxy();
}
