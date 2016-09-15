package com.sun.glass.ui.monocle;

import org.freedesktop.jaccall.Lib;
import org.freedesktop.jaccall.Ptr;

@Lib(value = "c",
     version = 6)
public class Libc {

    static {
        new Libc_Symbols().link();
    }

    public static native int mkstemp(@Ptr(String.class) long template);

    public static native int fcntl(int fd,
                                   int cmd,
                                   int arg);

    @Ptr(Void.class)
    public static native long mmap(@Ptr(Void.class) long addr,
                                   int len,
                                   int prot,
                                   int flags,
                                   int fildes,
                                   int off);

    public static native int munmap(@Ptr(Void.class) long addr,
                                    int len);

    public static native int close(int fildes);

    public static native int ftruncate(int fildes,
                                       int length);
}
