package com.sun.glass.ui.wayland;


import com.sun.glass.ui.Pixels;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class WaylandPixels extends Pixels {
    protected WaylandPixels(final int width,
                            final int height,
                            final ByteBuffer pixels) {
        super(width,
              height,
              pixels);
    }

    protected WaylandPixels(final int width,
                            final int height,
                            final IntBuffer pixels) {
        super(width,
              height,
              pixels);
    }

    protected WaylandPixels(final int width,
                            final int height,
                            final IntBuffer pixels,
                            final float scale) {
        super(width,
              height,
              pixels,
              scale);
    }

    @Override
    protected void _fillDirectByteBuffer(final ByteBuffer bb) {

    }

    @Override
    protected void _attachInt(final long ptr,
                              final int w,
                              final int h,
                              final IntBuffer ints,
                              final int[] array,
                              final int offset) {

    }

    @Override
    protected void _attachByte(final long ptr,
                               final int w,
                               final int h,
                               final ByteBuffer bytes,
                               final byte[] array,
                               final int offset) {

    }
}
