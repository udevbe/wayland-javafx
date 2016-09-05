package com.sun.glass.ui.monocle;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.sun.glass.events.KeyEvent;
import org.freedesktop.wayland.client.WlKeyboardEventsV5;
import org.freedesktop.wayland.client.WlKeyboardProxy;
import org.freedesktop.wayland.client.WlSeatProxy;
import org.freedesktop.wayland.client.WlSurfaceProxy;
import org.freedesktop.wayland.shared.WlKeyboardKeyState;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

@AutoFactory(allowSubclasses = true)
public class WaylandInputDeviceKeyboard implements InputDevice, WlKeyboardEventsV5 {

    //should be accessed by jfx thread only! ->
    @Nonnull
    private final KeyInput keyInput = KeyInput.getInstance();
    @Nonnull
    private final KeyState keyState = new KeyState();
    //<-

    @Nonnull
    private final WaylandPlatform waylandPlatform;
    @Nonnull
    private final WlKeyboardProxy wlKeyboardProxy;

    WaylandInputDeviceKeyboard(@Provided @Nonnull final WaylandPlatform waylandPlatform,
                               @Nonnull final WlSeatProxy wlSeatProxy) {
        this.waylandPlatform = waylandPlatform;
        this.wlKeyboardProxy = wlSeatProxy.getKeyboard(this);
    }

    @Override
    public boolean isTouch() {
        return false;
    }

    @Override
    public boolean isMultiTouch() {
        return false;
    }

    @Override
    public boolean isRelative() {
        return true;
    }

    @Override
    public boolean is5Way() {
        return true;
    }

    @Override
    public boolean isFullKeyboard() {
        return true;
    }

    @Override
    public void keymap(final WlKeyboardProxy emitter,
                       final int format,
                       final int fd,
                       final int size) {
        //NOOP monocle does it own kind of crude keymap thingy it seems.
    }

    @Override
    public void enter(final WlKeyboardProxy emitter,
                      final int serial,
                      @Nonnull final WlSurfaceProxy surface,
                      @Nonnull final ByteBuffer keys) {
        //NOOP. we only create one surface (window), so it always has the focus by default.
    }

    @Override
    public void leave(final WlKeyboardProxy emitter,
                      final int serial,
                      @Nonnull final WlSurfaceProxy surface) {
        //NOOP. we only create one surface (window), so it always has the focus by default.

    }

    @Override
    public void key(final WlKeyboardProxy emitter,
                    final int serial,
                    final int time,
                    final int key,
                    final int state) {
        this.waylandPlatform.getRunnableProcessor()
                            .invokeLater(() -> keyEvent(serial,
                                                        time,
                                                        key,
                                                        state));
    }

    private void keyEvent(final int serial,
                          final int time,
                          final int key,
                          final int state) {
        this.keyInput.getState(this.keyState);
        final int virtualKeyCode = getVirtualKeyCode(key);
        if (state == WlKeyboardKeyState.PRESSED.value) {
            this.keyState.pressKey(virtualKeyCode);
        }
        else {
            this.keyState.releaseKey(virtualKeyCode);
        }
        this.keyInput.setState(this.keyState);
    }

    //copy-pasta from LinuxKeyProcessor
    private int getVirtualKeyCode(final int linuxKeyCode) {
        if (linuxKeyCode >= LinuxInput.KEY_1 && linuxKeyCode <= LinuxInput.KEY_9) {
            return linuxKeyCode - LinuxInput.KEY_1 + KeyEvent.VK_1;
        }
        else if (linuxKeyCode >= LinuxInput.KEY_NUMERIC_0 && linuxKeyCode <= LinuxInput.KEY_NUMERIC_9) {
            return linuxKeyCode - LinuxInput.KEY_NUMERIC_0 + KeyEvent.VK_0;
        }
        else if (linuxKeyCode >= LinuxInput.KEY_F1 && linuxKeyCode <= LinuxInput.KEY_F10) {
            return linuxKeyCode - LinuxInput.KEY_F1 + KeyEvent.VK_F1;
        }
        else if (linuxKeyCode >= LinuxInput.KEY_F11 && linuxKeyCode <= LinuxInput.KEY_F12) {
            return linuxKeyCode - LinuxInput.KEY_F11 + KeyEvent.VK_F11;
        }
        else if (linuxKeyCode >= LinuxInput.KEY_F13 && linuxKeyCode <= LinuxInput.KEY_F24) {
            return linuxKeyCode - LinuxInput.KEY_F13 + KeyEvent.VK_F13;
        }
        else {
            switch (linuxKeyCode) {
                case LinuxInput.KEY_0:
                    return KeyEvent.VK_0;
                case LinuxInput.KEY_A:
                    return KeyEvent.VK_A;
                case LinuxInput.KEY_B:
                    return KeyEvent.VK_B;
                case LinuxInput.KEY_C:
                    return KeyEvent.VK_C;
                case LinuxInput.KEY_D:
                    return KeyEvent.VK_D;
                case LinuxInput.KEY_E:
                    return KeyEvent.VK_E;
                case LinuxInput.KEY_F:
                    return KeyEvent.VK_F;
                case LinuxInput.KEY_G:
                    return KeyEvent.VK_G;
                case LinuxInput.KEY_H:
                    return KeyEvent.VK_H;
                case LinuxInput.KEY_I:
                    return KeyEvent.VK_I;
                case LinuxInput.KEY_J:
                    return KeyEvent.VK_J;
                case LinuxInput.KEY_K:
                    return KeyEvent.VK_K;
                case LinuxInput.KEY_L:
                    return KeyEvent.VK_L;
                case LinuxInput.KEY_M:
                    return KeyEvent.VK_M;
                case LinuxInput.KEY_N:
                    return KeyEvent.VK_N;
                case LinuxInput.KEY_O:
                    return KeyEvent.VK_O;
                case LinuxInput.KEY_P:
                    return KeyEvent.VK_P;
                case LinuxInput.KEY_Q:
                    return KeyEvent.VK_Q;
                case LinuxInput.KEY_R:
                    return KeyEvent.VK_R;
                case LinuxInput.KEY_S:
                    return KeyEvent.VK_S;
                case LinuxInput.KEY_T:
                    return KeyEvent.VK_T;
                case LinuxInput.KEY_U:
                    return KeyEvent.VK_U;
                case LinuxInput.KEY_V:
                    return KeyEvent.VK_V;
                case LinuxInput.KEY_W:
                    return KeyEvent.VK_W;
                case LinuxInput.KEY_X:
                    return KeyEvent.VK_X;
                case LinuxInput.KEY_Y:
                    return KeyEvent.VK_Y;
                case LinuxInput.KEY_Z:
                    return KeyEvent.VK_Z;
                case LinuxInput.KEY_LEFTCTRL:
                case LinuxInput.KEY_RIGHTCTRL:
                    return KeyEvent.VK_CONTROL;
                case LinuxInput.KEY_LEFTSHIFT:
                case LinuxInput.KEY_RIGHTSHIFT:
                    return KeyEvent.VK_SHIFT;
                case LinuxInput.KEY_CAPSLOCK:
                    return KeyEvent.VK_CAPS_LOCK;
                case LinuxInput.KEY_TAB:
                    return KeyEvent.VK_TAB;
                case LinuxInput.KEY_GRAVE:
                    return KeyEvent.VK_BACK_QUOTE;
                case LinuxInput.KEY_MINUS:
                    return KeyEvent.VK_MINUS;
                case LinuxInput.KEY_EQUAL:
                    return KeyEvent.VK_EQUALS;
                case LinuxInput.KEY_BACKSPACE:
                    return KeyEvent.VK_BACKSPACE;
                case LinuxInput.KEY_LEFTBRACE:
                    return KeyEvent.VK_BRACELEFT;
                case LinuxInput.KEY_RIGHTBRACE:
                    return KeyEvent.VK_BRACERIGHT;
                case LinuxInput.KEY_BACKSLASH:
                    return KeyEvent.VK_BACK_SLASH;
                case LinuxInput.KEY_SEMICOLON:
                    return KeyEvent.VK_SEMICOLON;
                case LinuxInput.KEY_APOSTROPHE:
                    return KeyEvent.VK_QUOTE;
                case LinuxInput.KEY_COMMA:
                    return KeyEvent.VK_COMMA;
                case LinuxInput.KEY_DOT:
                    return KeyEvent.VK_PERIOD;
                case LinuxInput.KEY_SLASH:
                    return KeyEvent.VK_SLASH;
                case LinuxInput.KEY_LEFTALT:
                case LinuxInput.KEY_RIGHTALT:
                    return KeyEvent.VK_ALT;
                case LinuxInput.KEY_LEFTMETA:
                case LinuxInput.KEY_RIGHTMETA:
                    return KeyEvent.VK_COMMAND;
                case LinuxInput.KEY_SPACE:
                    return KeyEvent.VK_SPACE;
                case LinuxInput.KEY_MENU:
                    return KeyEvent.VK_CONTEXT_MENU;
                case LinuxInput.KEY_ENTER:
                    return KeyEvent.VK_ENTER;
                case LinuxInput.KEY_LEFT:
                    return KeyEvent.VK_LEFT;
                case LinuxInput.KEY_RIGHT:
                    return KeyEvent.VK_RIGHT;
                case LinuxInput.KEY_UP:
                    return KeyEvent.VK_UP;
                case LinuxInput.KEY_DOWN:
                    return KeyEvent.VK_DOWN;
                case LinuxInput.KEY_HOME:
                    return KeyEvent.VK_HOME;
                case LinuxInput.KEY_DELETE:
                    return KeyEvent.VK_DELETE;
                case LinuxInput.KEY_INSERT:
                    return KeyEvent.VK_INSERT;
                case LinuxInput.KEY_END:
                    return KeyEvent.VK_END;
                case LinuxInput.KEY_PAGEDOWN:
                    return KeyEvent.VK_PAGE_DOWN;
                case LinuxInput.KEY_PAGEUP:
                    return KeyEvent.VK_PAGE_UP;
                case LinuxInput.KEY_NUMLOCK:
                    return KeyEvent.VK_NUM_LOCK;
                case LinuxInput.KEY_ESC:
                    return KeyEvent.VK_ESCAPE;
                case LinuxInput.KEY_NUMERIC_STAR:
                    return KeyEvent.VK_MULTIPLY;
                default:
                    return KeyEvent.VK_UNDEFINED;
            }
        }
    }

    @Override
    public void modifiers(final WlKeyboardProxy emitter,
                          final int serial,
                          final int modsDepressed,
                          final int modsLatched,
                          final int modsLocked,
                          final int group) {
        //NOOP. monocle has a fixed hard coded list of what is considered a modifier and what isn't.
    }

    @Override
    public void repeatInfo(final WlKeyboardProxy emitter,
                           final int rate,
                           final int delay) {
        //NOOP.
    }

    @Nonnull
    public WlKeyboardProxy getWlKeyboardProxy() {
        return this.wlKeyboardProxy;
    }
}
