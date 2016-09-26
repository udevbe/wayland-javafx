package com.sun.glass.ui.monocle;

import org.freedesktop.wayland.client.WlDisplayProxy;
import org.freedesktop.wayland.client.WlOutputEventsV2;
import org.freedesktop.wayland.client.WlOutputProxy;
import org.freedesktop.wayland.client.WlRegistryProxy;

import javax.annotation.Nonnull;

class WaylandOutput implements WlOutputEventsV2 {

    private final WlOutputProxy wlOutputProxy;
    private       int           x;
    private       int           y;
    private       int           physicalWidth;
    private       int           physicalHeight;
    private       int           subpixel;
    private       String        make;
    private       String        model;
    private       int           transform;
    private       int           flags;
    private       int           width;
    private       int           height;
    private       int           refresh;
    private       int           scaleFactor;
    private       boolean       done;

    private boolean geo;
    private boolean mode;

    WaylandOutput(final WlDisplayProxy wlDisplayProxy,
                  final int name,
                  @Nonnull final WlRegistryProxy registryProxy) {
        //binding to the output global will notify the compositor which in turn will send out the output information events
        this.wlOutputProxy = registryProxy.bind(name,
                                                WlOutputProxy.class,
                                                VERSION,
                                                this);

        //keep firing roundrips until we received all output information
        if (this.wlOutputProxy.getVersion() < VERSION) {
            while (!this.geo && !this.mode) {
                //TODO safeguard in case we never receive desired output info
                wlDisplayProxy.roundtrip();
            }
        }
        else {
            while (!this.done) {
                //TODO safeguard in case we never receive desired output info
                wlDisplayProxy.roundtrip();
            }
        }
    }

    @Override
    public void geometry(final WlOutputProxy emitter,
                         final int x,
                         final int y,
                         final int physicalWidth,
                         final int physicalHeight,
                         final int subpixel,
                         @Nonnull final String make,
                         @Nonnull final String model,
                         final int transform) {

        this.x = x;
        this.y = y;
        this.physicalWidth = physicalWidth;
        this.physicalHeight = physicalHeight;
        this.subpixel = subpixel;
        this.make = make;
        this.model = model;
        this.transform = transform;

        this.geo = true;
    }

    @Override
    public void mode(final WlOutputProxy emitter,
                     final int flags,
                     final int width,
                     final int height,
                     final int refresh) {

        this.flags = flags;
        this.width = width;
        this.height = height;
        this.refresh = refresh;

        this.mode = true;
    }

    @Override
    public void done(final WlOutputProxy emitter) {
        this.done = true;
    }

    @Override
    public void scale(final WlOutputProxy emitter,
                      final int factor) {
        this.scaleFactor = factor;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getPhysicalWidth() {
        return this.physicalWidth;
    }

    public int getPhysicalHeight() {
        return this.physicalHeight;
    }

    public int getSubpixel() {
        return this.subpixel;
    }

    public String getMake() {
        return this.make;
    }

    public String getModel() {
        return this.model;
    }

    public int getTransform() {
        return this.transform;
    }

    public int getFlags() {
        return this.flags;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getRefresh() {
        return this.refresh;
    }

    public int getScaleFactor() {
        return this.scaleFactor;
    }

    public WlOutputProxy getWlOutputProxy() {
        return this.wlOutputProxy;
    }
}
