package com.sun.glass.ui.monocle;

import dagger.Module;
import dagger.Provides;
import org.freedesktop.wayland.client.WlDisplayProxy;

import javax.inject.Singleton;

@Module
public class WaylandModule {

    @Provides
    @Singleton
    WlDisplayProxy providesWlDisplayProxy() {
        //TODO from config
        return WlDisplayProxy.connect("wayland-0");
    }

    @Provides
    @Singleton
    Libxkbcommon providesLibxkbcommon() {
        new Libxkbcommon_Symbols().link();
        return new Libxkbcommon();
    }
}
