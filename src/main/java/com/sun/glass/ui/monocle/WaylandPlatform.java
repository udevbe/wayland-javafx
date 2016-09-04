package com.sun.glass.ui.monocle;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import org.freedesktop.wayland.client.WlCompositorEventsV4;
import org.freedesktop.wayland.client.WlCompositorProxy;
import org.freedesktop.wayland.client.WlDisplayProxy;
import org.freedesktop.wayland.client.WlRegistryEvents;
import org.freedesktop.wayland.client.WlRegistryProxy;
import org.freedesktop.wayland.client.WlSeatProxy;
import org.freedesktop.wayland.client.WlShellEvents;
import org.freedesktop.wayland.client.WlShellProxy;
import org.freedesktop.wayland.client.WlShmProxy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoFactory(allowSubclasses = true,
             className = "PrivateWaylandPlatformFactory")
public class WaylandPlatform extends NativePlatform implements WlRegistryEvents {

    /*
     * A NativePlatform bundles together a NativeScreen, NativeCursor and InputDeviceRegistry.
     * NativePlatform is a singleton, as are the classes it contains.
     */

    @Nonnull
    private final WaylandCursorFactory waylandCursorFactory;
    @Nonnull
    private final WaylandScreenFactory waylandScreenFactory;

    @Nonnull
    private final WaylandSeatFactory waylandSeatFactory;
    @Nonnull
    private final WaylandShmFactory  waylandShmFactory;

    @Nonnull
    private final WlDisplayProxy    displayProxy;
    @Nullable
    private       WlCompositorProxy compositorProxy;
    @Nullable
    private       WlShellProxy      shellProxy;

    @Nullable
    private WaylandShm waylandShm;

    WaylandPlatform(@Provided @Nonnull final WaylandCursorFactory waylandCursorFactory,
                    @Provided @Nonnull final WaylandScreenFactory waylandScreenFactory,
                    @Provided @Nonnull final WaylandSeatFactory waylandSeatFactory,
                    @Provided @Nonnull final WaylandShmFactory waylandShmFactory,
                    @Nonnull final WlDisplayProxy wlDisplayProxy) {
        this.waylandCursorFactory = waylandCursorFactory;
        this.waylandScreenFactory = waylandScreenFactory;
        this.waylandSeatFactory = waylandSeatFactory;
        this.waylandShmFactory = waylandShmFactory;

        this.displayProxy = wlDisplayProxy;
    }

    protected InputDeviceRegistry createInputDeviceRegistry() {
        return new InputDeviceRegistry();
    }

    protected WaylandCursor createCursor() {
        return this.waylandCursorFactory.create();
    }

    protected WaylandScreen createScreen() {
        //check if we have at least the minimum required globals (any conform wayland compositor is required to provide these)
        assert this.compositorProxy != null;
        assert this.shellProxy != null;

        return this.waylandScreenFactory.create(this.compositorProxy,
                                                this.shellProxy);
    }

    @Override
    public void global(final WlRegistryProxy emitter,
                       final int name,
                       @Nonnull final String interface_,
                       final int version) {
        //compositor, shm and shell are the absolute minimum globals we require to function.
        if (WlCompositorProxy.INTERFACE_NAME.equals(interface_)) {
            this.compositorProxy = emitter.bind(name,
                                                WlCompositorProxy.class,
                                                WlCompositorEventsV4.VERSION,
                                                new WlCompositorEventsV4() {
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

    @Nullable
    public WaylandShm getWaylandShm() {
        return this.waylandShm;
    }
}
