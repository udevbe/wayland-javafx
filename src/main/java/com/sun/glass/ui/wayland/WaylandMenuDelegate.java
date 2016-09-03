package com.sun.glass.ui.wayland;

import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;

/**
 * Created by zubzub on 9/3/16.
 */
public class WaylandMenuDelegate implements MenuDelegate {

    @Override
    public boolean createMenu(final String title,
                              final boolean enabled) {
        return true;
    }

    @Override
    public boolean setTitle(final String title) {
        return true;
    }

    @Override
    public boolean setEnabled(final boolean enabled) {
        return true;
    }

    @Override
    public boolean setPixels(final Pixels pixels) {
        return true;
    }

    @Override
    public boolean insert(final MenuDelegate menu,
                          final int pos) {
        return true;
    }

    @Override
    public boolean insert(final MenuItemDelegate item,
                          final int pos) {
        return true;
    }

    @Override
    public boolean remove(final MenuDelegate menu,
                          final int pos) {
        return true;
    }

    @Override
    public boolean remove(final MenuItemDelegate item,
                          final int pos) {
        return true;
    }
}
