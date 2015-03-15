/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import java.util.logging.Logger;

/**
 *
 * @author dunqiang
 */
public class TimeTracker {

    private Logger logger = Logger.getLogger(TimeTracker.class.getName());
    private long current;
    private long previous;
    private int counter = 0;
    private String prefix = "";

    public TimeTracker(Class clazz) {
        this(clazz.getName());
    }

    public TimeTracker(String prefix) {
        this.prefix = prefix;
    }

    public void start() {
        counter = 0;
        current = System.currentTimeMillis();
        String outStr = String.format("%s: counting start", prefix);
        logger.info(outStr);
    }

    public void print() {
        print(null);
    }

    public void print(String _prefix) {
        counter++;
        previous = current;
        current = System.currentTimeMillis();
        if (_prefix != null && !_prefix.isEmpty()) {
            String outputStr = null;
            if (counter == 1) {
                outputStr = String.format("%s %d = (%d),%d %d", _prefix, counter, current - previous, previous, current);
            } else {
                outputStr = String.format("%s %d = (%d),%d", _prefix, counter, current - previous, current);
            }
            logger.info(outputStr);
            //System.out.println(outputStr);
        } else {
            String outputStr = null;
            if (counter == 1) {
                outputStr = String.format("%s %d = (%d),%d %d", prefix, counter, current - previous, previous, current);
            } else {
                outputStr = String.format("%s %d = (%d),%d", prefix, counter, current - previous, current);
            }
            logger.info(outputStr);
            //System.out.println(outputStr);
        }
    }
}
