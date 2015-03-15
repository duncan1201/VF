/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.statusline;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author dq
 */
public interface IMemoryUsageMonitorService {

    void startMonitoring(long initialDelay, long period, TimeUnit timeUnit);
}
