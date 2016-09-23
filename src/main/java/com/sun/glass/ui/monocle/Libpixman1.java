package com.sun.glass.ui.monocle;

import org.freedesktop.jaccall.Lib;
import org.freedesktop.jaccall.Ptr;
import org.freedesktop.jaccall.Unsigned;

@Lib(value = "pixman-1",
     version = 0)
public class Libpixman1 {

    public static final int PIXMAN_TYPE_ARGB = 2;
    public static final int PIXMAN_TYPE_BGRA = 8;

    public static final int PIXMAN_b8g8r8a8 = PIXMAN_FORMAT(32,
                                                            PIXMAN_TYPE_BGRA,
                                                            8,
                                                            8,
                                                            8,
                                                            8);
    public static final int PIXMAN_a8r8g8b8 = PIXMAN_FORMAT(32,
                                                            PIXMAN_TYPE_ARGB,
                                                            8,
                                                            8,
                                                            8,
                                                            8);

    public static final int PIXMAN_OP_OVER = 0x03;


    static {
        new Libpixman1_Symbols().link();
    }

    public static int PIXMAN_FORMAT(final int bpp,
                                    final int type,
                                    final int a,
                                    final int r,
                                    final int g,
                                    final int b) {
        return (((bpp) << 24) |
                ((type) << 16) |
                ((a) << 12) |
                ((r) << 8) |
                ((g) << 4) |
                ((b)));
    }

    @Ptr
    public static native long pixman_image_create_bits(int format,
                                                       int width,
                                                       int height,
                                                       @Ptr(int.class) long bits,
                                                       int rowstride_bytes);

    public static native void pixman_image_composite(int op,
                                                     @Ptr long src,
                                                     @Ptr long mask,
                                                     @Ptr long dest,
                                                     short src_x,
                                                     short src_y,
                                                     short mask_x,
                                                     short mask_y,
                                                     short dest_x,
                                                     short dest_y,
                                                     @Unsigned short width,
                                                     @Unsigned short height);
}
