package com.sun.glass.ui.wayland;

import com.sun.glass.ui.Clipboard;
import com.sun.glass.ui.delegate.ClipboardDelegate;


public class WaylandClipboardDelegate implements ClipboardDelegate {
    @Override
    public Clipboard createClipboard(final String clipboardName) {
        if (Clipboard.SYSTEM.equals(clipboardName)) {
            return new WaylandSystemClipboard();
        }
        if (Clipboard.DND.equals(clipboardName)) {
            return new WaylandDnDClipboard();
        }
        return null;
    }
}
