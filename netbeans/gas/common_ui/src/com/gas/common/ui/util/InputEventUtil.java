/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import java.awt.event.InputEvent;
import org.openide.util.Utilities;

/**
 *
 * @author liyuanli
 */
public class InputEventUtil {

    public static int getAllMasks() {
        int ret = 0;
        ret = Integer.MAX_VALUE;
        return ret;
    }

    public static int getCtrlDownMask() {
        int ret = 0;
        if (Utilities.isWindows()) {
            ret = InputEvent.CTRL_DOWN_MASK;
        } else if (Utilities.isMac()) {
            ret = InputEvent.META_DOWN_MASK;
        } else {
            throw new IllegalStateException("Your OS not supported");
        }
        return ret;
    }

    public static int getCtrlLeftBtnDownMask() {
        int ret = 0;
        ret = getCtrlDownMask() | InputEvent.BUTTON1_DOWN_MASK;
        return ret;
    }

    public static int getCtrlLeftBtnMask() {
        int ret = 0;
        ret = getCtrlDownMask() | InputEvent.BUTTON1_MASK;
        return ret;
    }

    public static int getShiftLeftClickMask() {
        int ret = 0;
        ret = InputEvent.BUTTON1_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK;
        return ret;
    }
}
