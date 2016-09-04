/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gas.common.ui.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dunqiang
 */
public class LogUtil {
    
    public static void log(Logger logger, Level level, Throwable t){
        log(logger, level, null, t);
    }
    
    public static void log(Logger logger, Level level, String msg){
        log(logger, level, msg, null);
    }
    
    public static void log(Logger logger, Level level, String msg, Throwable t){
        String stackTrace = null;
        if (t != null) {
            StringWriter w = new StringWriter();
            t.printStackTrace(new PrintWriter(w));
            stackTrace = w.toString();
        }
        if (stackTrace != null && msg != null) {
            logger.log(level, "{0}\n{1}", new Object[]{msg, stackTrace});
        } else if (stackTrace != null && msg == null) {
            logger.log(level, stackTrace);
        } else if (stackTrace == null && msg != null) {
            logger.log(level, msg);
        }
    }
}
