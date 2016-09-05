package com.sun.glass.ui.monocle;

import org.freedesktop.jaccall.Lib;
import org.freedesktop.jaccall.Ptr;

@Lib(value = "C",
     version = 6)
public class Libc {
    @Ptr
    public native long fdopen(int fd,
                              @Ptr(String.class) long mode);

    @Ptr(String.class)
    public native long fgets(@Ptr(String.class) long str,
                             int num,
                             @Ptr long stream);
}
