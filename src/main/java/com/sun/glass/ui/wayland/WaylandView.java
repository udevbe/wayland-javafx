package com.sun.glass.ui.wayland;

import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.View;

import java.util.Map;


public class WaylandView extends View {
    @Override
    protected void _enableInputMethodEvents(final long ptr,
                                            final boolean enable) {

    }

    @Override
    protected long _create(final Map capabilities) {
        return 0;
    }

    @Override
    protected long _getNativeView(final long ptr) {
        return 0;
    }

    @Override
    protected int _getX(final long ptr) {
        return 0;
    }

    @Override
    protected int _getY(final long ptr) {
        return 0;
    }

    @Override
    protected void _setParent(final long ptr,
                              final long parentPtr) {

    }

    @Override
    protected boolean _close(final long ptr) {
        return false;
    }

    @Override
    protected void _scheduleRepaint(final long ptr) {

    }

    @Override
    protected void _begin(final long ptr) {

    }

    @Override
    protected void _end(final long ptr) {

    }

    @Override
    protected int _getNativeFrameBuffer(final long ptr) {
        return 0;
    }

    @Override
    protected void _uploadPixels(final long ptr,
                                 final Pixels pixels) {

    }

    @Override
    protected boolean _enterFullscreen(final long ptr,
                                       final boolean animate,
                                       final boolean keepRatio,
                                       final boolean hideCursor) {
        return false;
    }

    @Override
    protected void _exitFullscreen(final long ptr,
                                   final boolean animate) {

    }
}
