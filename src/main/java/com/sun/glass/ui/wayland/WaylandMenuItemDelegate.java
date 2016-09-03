package com.sun.glass.ui.wayland;


import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.delegate.MenuItemDelegate;

public class WaylandMenuItemDelegate implements MenuItemDelegate {
    @Override
    public boolean createMenuItem(final String title,
                                  final MenuItem.Callback callback,
                                  final int shortcutKey,
                                  final int shortcutModifiers,
                                  final Pixels pixels,
                                  final boolean enabled,
                                  final boolean checked) {
        return true;
    }

    @Override
    public boolean setTitle(final String title) {
        return true;
    }

    @Override
    public boolean setCallback(final MenuItem.Callback callback) {
        return true;
    }

    @Override
    public boolean setShortcut(final int shortcutKey,
                               final int shortcutModifiers) {
        return true;
    }

    @Override
    public boolean setPixels(final Pixels pixels) {
        return true;
    }

    @Override
    public boolean setEnabled(final boolean enabled) {
        return true;
    }

    @Override
    public boolean setChecked(final boolean checked) {
        return true;
    }
}
