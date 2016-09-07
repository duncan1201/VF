/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gas.main.ui.exceptionhandler;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 *
 * @author Dunqiang
 */
public class SevereExceptionHandler extends Handler {

    @Override
    public void publish(LogRecord record) {
        if (record.getLevel() == Level.SEVERE) {
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
    
}
