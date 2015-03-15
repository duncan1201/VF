/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import java.awt.Color;
import javax.swing.UIManager;

/**
 *
 * @author dq
 */
public class CNST {

    public static final String OS_WINDOWS = "windows";
    public static final String ONE_LINE_ERROR_FORMAT = "<html>%s&nbsp;&nbsp;&nbsp;<b>Hint:</b>%s</html>";
    public static final String ERROR_FORMAT = "<html>%s<br/><br/><b>Hint:</b><br/>%s<html>";
    public static final String MSG_FORMAT = "<html>%s<br/><br/>%s<html>";
    public static final Color BG = Color.WHITE;

    public static enum PAINT {

        STARTED, ENDING
    };

    public static enum LAYOUT {

        STARTED, ENDING
    };

    public static enum CAP {

        HALF_TIP_UP, ONE_TIP, TWO_TIP, ROUNDED, BEZIER
    };
}
