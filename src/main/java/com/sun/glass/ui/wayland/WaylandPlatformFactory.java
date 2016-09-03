package com.sun.glass.ui.wayland;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Menu;
import com.sun.glass.ui.MenuBar;
import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.PlatformFactory;
import com.sun.glass.ui.delegate.ClipboardDelegate;
import com.sun.glass.ui.delegate.MenuBarDelegate;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;


public class WaylandPlatformFactory extends PlatformFactory {
    @Override
    public Application createApplication() {
        return new WaylandApplication();
    }

    @Override
    public MenuBarDelegate createMenuBarDelegate(final MenuBar menubar) {
        return new WaylandMenuBarDelegate();
    }

    @Override
    public MenuDelegate createMenuDelegate(final Menu menu) {
        return new WaylandMenuDelegate();
    }

    @Override
    public MenuItemDelegate createMenuItemDelegate(final MenuItem menuItem) {
        return new WaylandMenuItemDelegate();
    }

    @Override
    public ClipboardDelegate createClipboardDelegate() {
        return new WaylandClipboardDelegate();
    }
}
