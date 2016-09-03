package com.sun.glass.ui.wayland;

import com.sun.glass.ui.delegate.MenuBarDelegate;
import com.sun.glass.ui.delegate.MenuDelegate;


public class WaylandMenuBarDelegate implements MenuBarDelegate {
    @Override
    public boolean createMenuBar() {
        return true;
    }

    @Override
    public boolean insert(final MenuDelegate menu,
                          final int pos) {
        return true;
    }

    @Override
    public boolean remove(final MenuDelegate menu,
                          final int pos) {
        return true;
    }

    @Override
    public long getNativeMenu() {
        return 0;
    }
}
