package com.sun.glass.ui.wayland;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Robot;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.Size;
import com.sun.glass.ui.Timer;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;


public class WaylandApplication extends Application {
    @Override
    protected void runLoop(final Runnable launchable) {

    }

    @Override
    protected void _invokeAndWait(final Runnable runnable) {

    }

    @Override
    protected void _invokeLater(final Runnable runnable) {

    }

    @Override
    protected Object _enterNestedEventLoop() {
        return null;
    }

    @Override
    protected void _leaveNestedEventLoop(final Object retValue) {

    }

    @Override
    public Window createWindow(final Window owner,
                               final Screen screen,
                               final int styleMask) {
        return null;
    }

    @Override
    public Window createWindow(final long parent) {
        return null;
    }

    @Override
    public View createView() {
        return null;
    }

    @Override
    public Cursor createCursor(final int type) {
        return null;
    }

    @Override
    public Cursor createCursor(final int x,
                               final int y,
                               final Pixels pixels) {
        return null;
    }

    @Override
    protected void staticCursor_setVisible(final boolean visible) {

    }

    @Override
    protected Size staticCursor_getBestSize(final int width,
                                            final int height) {
        return null;
    }

    @Override
    public Pixels createPixels(final int width,
                               final int height,
                               final ByteBuffer data) {
        return null;
    }

    @Override
    public Pixels createPixels(final int width,
                               final int height,
                               final IntBuffer data) {
        return null;
    }

    @Override
    public Pixels createPixels(final int width,
                               final int height,
                               final IntBuffer data,
                               final float scale) {
        return null;
    }

    @Override
    protected int staticPixels_getNativeFormat() {
        return 0;
    }

    @Override
    public Robot createRobot() {
        return null;
    }

    @Override
    protected double staticScreen_getVideoRefreshPeriod() {
        return 0;
    }

    @Override
    protected Screen[] staticScreen_getScreens() {
        return new Screen[0];
    }

    @Override
    public Timer createTimer(final Runnable runnable) {
        return null;
    }

    @Override
    protected int staticTimer_getMinPeriod() {
        return 0;
    }

    @Override
    protected int staticTimer_getMaxPeriod() {
        return 0;
    }

    @Override
    protected CommonDialogs.FileChooserResult staticCommonDialogs_showFileChooser(final Window owner,
                                                                                  final String folder,
                                                                                  final String filename,
                                                                                  final String title,
                                                                                  final int type,
                                                                                  final boolean multipleMode,
                                                                                  final CommonDialogs.ExtensionFilter[] extensionFilters,
                                                                                  final int defaultFilterIndex) {
        return null;
    }

    @Override
    protected File staticCommonDialogs_showFolderChooser(final Window owner,
                                                         final String folder,
                                                         final String title) {
        return null;
    }

    @Override
    protected long staticView_getMultiClickTime() {
        return 0;
    }

    @Override
    protected int staticView_getMultiClickMaxX() {
        return 0;
    }

    @Override
    protected int staticView_getMultiClickMaxY() {
        return 0;
    }

    @Override
    protected boolean _supportsTransparentWindows() {
        return false;
    }

    @Override
    protected boolean _supportsUnifiedWindows() {
        return false;
    }

    @Override
    protected int _getKeyCodeForChar(final char c) {
        return 0;
    }
}
