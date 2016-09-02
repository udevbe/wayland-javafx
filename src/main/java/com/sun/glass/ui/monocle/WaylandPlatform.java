package com.sun.glass.ui.monocle;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import org.freedesktop.wayland.client.WlCompositorEventsV3;
import org.freedesktop.wayland.client.WlCompositorProxy;
import org.freedesktop.wayland.client.WlDisplayProxy;
import org.freedesktop.wayland.client.WlRegistryProxy;
import org.freedesktop.wayland.client.WlSeatEventsV3;
import org.freedesktop.wayland.client.WlSeatProxy;
import org.freedesktop.wayland.client.WlShellEvents;
import org.freedesktop.wayland.client.WlShellProxy;
import org.freedesktop.wayland.client.WlShmEvents;
import org.freedesktop.wayland.client.WlShmProxy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoFactory(allowSubclasses = true,
             className = "PrivateWaylandPlatformFactory")
public class WaylandPlatform extends NativePlatform {

    /*
     * A NativePlatform bundles together a NativeScreen, NativeCursor and InputDeviceRegistry.
     * NativePlatform is a singleton, as are the classes it contains.
     */

    @Nonnull
    private final WaylandInputDeviceRegistryFactory waylandInputDeviceRegistryFactory;
    @Nonnull
    private final WaylandCursorFactory              waylandCursorFactory;
    @Nonnull
    private final WaylandScreenFactory              waylandScreenFactory;
    @Nonnull
    private final WlDisplayProxy                    displayProxy;
    @Nullable
    private       WaylandInputDeviceRegistry        waylandInputDeviceRegistry;
    @Nullable
    private       WaylandCursor                     waylandCursor;
    @Nullable
    private       WaylandScreen                     waylandScreen;
    @Nullable
    private       WlCompositorProxy                 compositorProxy;
    @Nullable
    private       WlShmProxy                        shmProxy;
    @Nullable
    private       WlShellProxy                      shellProxy;
    @Nullable
    private       WlSeatProxy                       seatProxy;

    private int shmFormats = 0;

    WaylandPlatform(@Provided @Nonnull WaylandInputDeviceRegistryFactory waylandInputDeviceRegistryFactory,
                    @Provided @Nonnull WaylandCursorFactory waylandCursorFactory,
                    @Provided @Nonnull WaylandScreenFactory waylandScreenFactory,
                    @Nonnull WlDisplayProxy wlDisplayProxy) {
        this.waylandInputDeviceRegistryFactory = waylandInputDeviceRegistryFactory;
        this.waylandCursorFactory = waylandCursorFactory;
        this.waylandScreenFactory = waylandScreenFactory;

        this.displayProxy = wlDisplayProxy;
    }

    protected WaylandInputDeviceRegistry createInputDeviceRegistry() {
        if (this.waylandInputDeviceRegistry == null) {
            this.waylandInputDeviceRegistry = this.waylandInputDeviceRegistryFactory.create();
        }
        return this.waylandInputDeviceRegistry;
    }

    protected WaylandCursor createCursor() {
        if (this.waylandCursor == null) {
            //TODO check if we have at least the minimum required globals
            this.waylandCursor = this.waylandCursorFactory.create();
        }
        return this.waylandCursor;
    }

    protected WaylandScreen createScreen() {
        if (this.waylandScreen == null) {
            //TODO check if we have at least the minimum required globals
            this.waylandScreen = this.waylandScreenFactory.create();
        }
        return this.waylandScreen;
    }

    public void onGlobalAdded(@Nonnull final WlRegistryProxy emitter,
                              final int name,
                              @Nonnull final String interfaceName,
                              final int version) {
        global(emitter,
               name,
               interfaceName,
               version);
    }

    private void global(final WlRegistryProxy registryProxy,
                        final int name,
                        final String interfaceName,
                        final int version) {
        //compositor, shm and shell are the absolute minimum globals we require to function.
        if (WlCompositorProxy.INTERFACE_NAME.equals(interfaceName)) {
            this.compositorProxy = registryProxy.bind(name,
                                                      WlCompositorProxy.class,
                                                      WlCompositorEventsV3.VERSION,
                                                      new WlCompositorEventsV3() {
                                                      });
        }
        else if (WlShmProxy.INTERFACE_NAME.equals(interfaceName)) {
            this.shmProxy = registryProxy.bind(name,
                                               WlShmProxy.class,
                                               WlShmEvents.VERSION,
                                               (emitter, format) -> WaylandPlatform.this.shmFormats |= (1 << format));
        }
        else if (WlShellProxy.INTERFACE_NAME.equals(interfaceName)) {
            this.shellProxy = registryProxy.bind(name,
                                                 WlShellProxy.class,
                                                 WlShellEvents.VERSION,
                                                 new WlShellEvents() {
                                                 });
        }
        else if (WlSeatProxy.INTERFACE_NAME.equals(interfaceName)) {
            //FIXME multiple seats can be announced. Handle this.
            this.seatProxy = registryProxy.bind(name,
                                                WlSeatProxy.class,
                                                WlSeatEventsV3.VERSION,
                                                new WlSeatEventsV3() {
                                                    @Override
                                                    public void capabilities(final WlSeatProxy emitter,
                                                                             final int capabilities) {

                                                    }

                                                    @Override
                                                    public void name(final WlSeatProxy emitter,
                                                                     @Nonnull final String name) {
                                                        System.out.println("Got seat with name " + name);
                                                    }
                                                });
        }
    }

    public void onGlobalRemoved(@Nonnull final WlRegistryProxy emitter,
                                final int name) {
        //TODO listen for eg seat removals
    }
}
