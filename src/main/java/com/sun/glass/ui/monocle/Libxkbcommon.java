package com.sun.glass.ui.monocle;

import org.freedesktop.jaccall.Lib;
import org.freedesktop.jaccall.Ptr;

@Lib("xkbcommon")
public class Libxkbcommon {
    /**
     * Do not apply any flags.
     */
    public static final int XKB_KEYMAP_COMPILE_NO_FLAGS = 0;

    /**
     * Do not apply any context flags.
     */
    public static final int XKB_CONTEXT_NO_FLAGS             = 0;
    /**
     * Create this context with an empty include path.
     */
    public static final int XKB_CONTEXT_NO_DEFAULT_INCLUDES  = (1 << 0);
    /**
     * Don't take RMLVO names from the environment.
     *
     * @since 0.3.0
     */
    public static final int XKB_CONTEXT_NO_ENVIRONMENT_NAMES = (1 << 1);


    @Ptr
    public native long xkb_keymap_new_from_string(@Ptr long context,
                                                  @Ptr(String.class) long string,
                                                  int format,
                                                  int flags);

    @Ptr
    public native long xkb_context_new(int flags);

    @Ptr
    public native long xkb_state_new(@Ptr long keymap);

}
