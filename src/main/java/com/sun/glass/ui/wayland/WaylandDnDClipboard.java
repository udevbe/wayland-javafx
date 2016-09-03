package com.sun.glass.ui.wayland;

import com.sun.glass.ui.Clipboard;
import com.sun.glass.ui.SystemClipboard;

import java.util.HashMap;


public class WaylandDnDClipboard extends SystemClipboard {
    protected WaylandDnDClipboard() {
        super(Clipboard.DND);
    }

    @Override
    protected boolean isOwner() {
        return false;
    }

    @Override
    protected void pushToSystem(final HashMap<String, Object> cacheData,
                                final int supportedActions) {

    }

    @Override
    protected void pushTargetActionToSystem(final int actionDone) {

    }

    @Override
    protected Object popFromSystem(final String mimeType) {
        return null;
    }

    @Override
    protected int supportedSourceActionsFromSystem() {
        return 0;
    }

    @Override
    protected String[] mimesFromSystem() {
        return new String[0];
    }
}
