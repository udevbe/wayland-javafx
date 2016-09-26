package com.sun.glass.ui.monocle;

import org.freedesktop.wayland.client.WlDisplayProxy;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WaylandPlatformFactory extends NativePlatformFactory {

    public static final ExecutorService WL_LOOP = Executors.newSingleThreadExecutor(r -> new Thread(r,
                                                                                                    "wayland-event-loop"));

    private static final int MAJOR = 1;
    private static final int MINOR = 0;

    protected boolean matches() {
        //TODO check if we're capable of running wayland (check if xdg_runtime dir is set & check if compositor socket exists)
        return true;
    }

    protected WaylandPlatform createNativePlatform() {
        //this ensures that all init happens on the wayland thread
        final Future<WaylandPlatform> waylandPlatformFuture = WL_LOOP.submit(() -> {
            final WlDisplayProxy  wlDisplayProxy  = WlDisplayProxy.connect("wayland-0");
            final WaylandPlatform waylandPlatform = new WaylandPlatform(wlDisplayProxy);
            wlDisplayProxy.getRegistry(waylandPlatform);

            //start looping
            loop(wlDisplayProxy);

            return waylandPlatform;
        });


        try {
            return waylandPlatformFuture.get();
        }
        catch (InterruptedException | ExecutionException e) {
            throw new Error(e);
        }
    }

    private void loop(final WlDisplayProxy wlDisplayProxy) {
        wlDisplayProxy.roundtrip();
        WL_LOOP.submit(() -> loop(wlDisplayProxy));
    }

    protected int getMajorVersion() {
        return MAJOR;
    }

    protected int getMinorVersion() {
        return MINOR;
    }
}
