//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.achartengine;

import android.view.MotionEvent;
import org.achartengine.tools.PanListener;
import org.achartengine.tools.ZoomListener;

public interface ITouchHandler {
    boolean handleTouch(MotionEvent var1);

    void addZoomListener(ZoomListener var1);

    void removeZoomListener(ZoomListener var1);

    void addPanListener(PanListener var1);

    void removePanListener(PanListener var1);
}
