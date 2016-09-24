package com.sun.glass.ui.monocle;

import org.freedesktop.wayland.client.WlCompositorEventsV3;
import org.freedesktop.wayland.client.WlCompositorProxy;
import org.freedesktop.wayland.client.WlDisplayProxy;
import org.freedesktop.wayland.client.WlOutputProxy;
import org.freedesktop.wayland.client.WlRegistryEvents;
import org.freedesktop.wayland.client.WlRegistryProxy;
import org.freedesktop.wayland.client.WlSeatProxy;
import org.freedesktop.wayland.client.WlShellEvents;
import org.freedesktop.wayland.client.WlShellProxy;
import org.freedesktop.wayland.client.WlShmProxy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

public class WaylandPlatform extends NativePlatform implements WlRegistryEvents {

    /*
     * A NativePlatform bundles together a NativeScreen, NativeCursor and InputDeviceRegistry.
     * NativePlatform is a singleton, as are the classes it contains.
     */

    @Nonnull
    private final WlDisplayProxy       wlDisplayProxy;
    @Nonnull
    private final WaylandCursorFactory waylandCursorFactory;
    @Nonnull
    private final WaylandScreenFactory waylandScreenFactory;

    @Nonnull
    private final WaylandSeatFactory   waylandSeatFactory;
    @Nonnull
    private final WaylandShmFactory    waylandShmFactory;
    @Nonnull
    private final WaylandOutputFactory waylandOutputFactory;

    @Nullable
    private WlCompositorProxy compositorProxy;
    @Nullable
    private WlShellProxy      shellProxy;

    @Nullable
    private WaylandShm    waylandShm;
    private WaylandOutput waylandOutput;

    @Inject
    WaylandPlatform(@Nonnull final WlDisplayProxy wlDisplayProxy,
                    @Nonnull final WaylandCursorFactory waylandCursorFactory,
                    @Nonnull final WaylandScreenFactory waylandScreenFactory,
                    @Nonnull final WaylandSeatFactory waylandSeatFactory,
                    @Nonnull final WaylandShmFactory waylandShmFactory,
                    @Nonnull final WaylandOutputFactory waylandOutputFactory) {
        this.wlDisplayProxy = wlDisplayProxy;
        this.waylandCursorFactory = waylandCursorFactory;
        this.waylandScreenFactory = waylandScreenFactory;
        this.waylandSeatFactory = waylandSeatFactory;
        this.waylandShmFactory = waylandShmFactory;
        this.waylandOutputFactory = waylandOutputFactory;
    }

    protected InputDeviceRegistry createInputDeviceRegistry() {
        return new InputDeviceRegistry();
    }

    protected WaylandCursor createCursor() {
        return this.waylandCursorFactory.create();
    }

    protected WaylandScreen createScreen() {
        //make sure we receive all globals
        while (this.waylandOutput == null ||
               this.compositorProxy == null ||
               this.shellProxy == null ||
               this.waylandShm == null) {
            //We keep on tripping (pun intended) until we have received all globals.
            //FIXME safe guard so we can bail if required globals are never received
            this.wlDisplayProxy.roundtrip();
        }

        return this.waylandScreenFactory.create(this.waylandOutput,
                                                this.compositorProxy,
                                                this.shellProxy,
                                                this.waylandShm);
    }

    @Override
    public void global(final WlRegistryProxy emitter,
                       final int name,
                       @Nonnull final String interface_,
                       final int version) {
        if (WlCompositorProxy.INTERFACE_NAME.equals(interface_)) {
            this.compositorProxy = emitter.bind(name,
                                                WlCompositorProxy.class,
                                                WlCompositorEventsV3.VERSION,
                                                new WlCompositorEventsV3() {
                                                });
        }
        else if (WlShmProxy.INTERFACE_NAME.equals(interface_)) {
            this.waylandShm = this.waylandShmFactory.create(emitter,
                                                            name,
                                                            interface_,
                                                            version);
        }
        else if (WlShellProxy.INTERFACE_NAME.equals(interface_)) {
            this.shellProxy = emitter.bind(name,
                                           WlShellProxy.class,
                                           WlShellEvents.VERSION,
                                           new WlShellEvents() {
                                           });
        }
        else if (WlOutputProxy.INTERFACE_NAME.equals(interface_)) {
            this.waylandOutput = this.waylandOutputFactory.create(name,
                                                                  emitter);
        }
        else if (WlSeatProxy.INTERFACE_NAME.equals(interface_)) {
            //TODO keep seats stored somewhere? (Is this needed to avoid gc?)
            this.waylandSeatFactory.create(emitter,
                                           name,
                                           interface_,
                                           version,
                                           getInputDeviceRegistry());
        }
    }

    @Override
    public void globalRemove(final WlRegistryProxy emitter,
                             final int name) {
        //TODO listen for eg seat removals
    }
}
