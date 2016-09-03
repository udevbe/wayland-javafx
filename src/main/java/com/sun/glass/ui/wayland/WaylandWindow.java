package com.sun.glass.ui.wayland;

import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;


public class WaylandWindow extends Window {

    protected WaylandWindow(final Window owner,
                            final Screen screen,
                            final int styleMask) {
        super(owner,
              screen,
              styleMask);
    }

    protected WaylandWindow(final long parent) {
        super(parent);
    }

    @Override
    protected long _createWindow(final long ownerPtr,
                                 final long screenPtr,
                                 final int mask) {
        return 0;
    }

    @Override
    protected long _createChildWindow(final long parent) {
        return 0;
    }

    @Override
    protected boolean _close(final long ptr) {
        return false;
    }

    @Override
    protected boolean _setView(final long ptr,
                               final View view) {
        return false;
    }

    @Override
    protected boolean _setMenubar(final long ptr,
                                  final long menubarPtr) {
        return false;
    }

    @Override
    protected boolean _minimize(final long ptr,
                                final boolean minimize) {
        return false;
    }

    @Override
    protected boolean _maximize(final long ptr,
                                final boolean maximize,
                                final boolean wasMaximized) {
        return false;
    }

    @Override
    protected int _getEmbeddedX(final long ptr) {
        return 0;
    }

    @Override
    protected int _getEmbeddedY(final long ptr) {
        return 0;
    }

    @Override
    protected void _setBounds(final long ptr,
                              final int x,
                              final int y,
                              final boolean xSet,
                              final boolean ySet,
                              final int w,
                              final int h,
                              final int cw,
                              final int ch,
                              final float xGravity,
                              final float yGravity) {

    }

    @Override
    protected boolean _setVisible(final long ptr,
                                  final boolean visible) {
        return false;
    }

    @Override
    protected boolean _setResizable(final long ptr,
                                    final boolean resizable) {
        return false;
    }

    @Override
    protected boolean _requestFocus(final long ptr,
                                    final int event) {
        return false;
    }

    @Override
    protected void _setFocusable(final long ptr,
                                 final boolean isFocusable) {

    }

    @Override
    protected boolean _grabFocus(final long ptr) {
        return false;
    }

    @Override
    protected void _ungrabFocus(final long ptr) {

    }

    @Override
    protected boolean _setTitle(final long ptr,
                                final String title) {
        return false;
    }

    @Override
    protected void _setLevel(final long ptr,
                             final int level) {

    }

    @Override
    protected void _setAlpha(final long ptr,
                             final float alpha) {

    }

    @Override
    protected boolean _setBackground(final long ptr,
                                     final float r,
                                     final float g,
                                     final float b) {
        return false;
    }

    @Override
    protected void _setEnabled(final long ptr,
                               final boolean enabled) {

    }

    @Override
    protected boolean _setMinimumSize(final long ptr,
                                      final int width,
                                      final int height) {
        return false;
    }

    @Override
    protected boolean _setMaximumSize(final long ptr,
                                      final int width,
                                      final int height) {
        return false;
    }

    @Override
    protected void _setIcon(final long ptr,
                            final Pixels pixels) {

    }

    @Override
    protected void _setCursor(final long ptr,
                              final Cursor cursor) {

    }

    @Override
    protected void _toFront(final long ptr) {

    }

    @Override
    protected void _toBack(final long ptr) {

    }

    @Override
    protected void _enterModal(final long ptr) {

    }

    @Override
    protected void _enterModalWithWindow(final long dialog,
                                         final long window) {

    }

    @Override
    protected void _exitModal(final long ptr) {

    }

    @Override
    protected void _requestInput(final long ptr,
                                 final String text,
                                 final int type,
                                 final double width,
                                 final double height,
                                 final double Mxx,
                                 final double Mxy,
                                 final double Mxz,
                                 final double Mxt,
                                 final double Myx,
                                 final double Myy,
                                 final double Myz,
                                 final double Myt,
                                 final double Mzx,
                                 final double Mzy,
                                 final double Mzz,
                                 final double Mzt) {

    }

    @Override
    protected void _releaseInput(final long ptr) {

    }
}
