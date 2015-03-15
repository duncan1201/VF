/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.statusline;

import java.beans.PropertyChangeSupport;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.openide.awt.StatusLineElementProvider;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IMemoryUsageMonitorService.class)
public class MemoryUsageMonitorService implements IMemoryUsageMonitorService {

    private long freeMemory;
    private long totalMemory;
    private long maxMemory;
    private PropertyChangeSupport propertyChangeSupport;
    private StatusLineCompProvider provider = null;
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public MemoryUsageMonitorService() {
        propertyChangeSupport = new PropertyChangeSupport(this);

    }

    @Override
    public void startMonitoring(long initialDelay, long period, TimeUnit timeUnit) {
        if (provider == null) {
            for (StatusLineElementProvider p : Lookup.getDefault().lookupAll(StatusLineElementProvider.class)) {
                if (p instanceof StatusLineCompProvider) {
                    provider = (StatusLineCompProvider) p;
                    break;
                }
            }
        }
        if (provider == null) {
            throw new IllegalStateException("StatusLineCompProvider not found!");
        }

        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                setFreeMemory(Runtime.getRuntime().freeMemory());
                setTotalMemory(Runtime.getRuntime().totalMemory());
                setMaxMemory(Runtime.getRuntime().maxMemory());
                String text = String.format("Using %dMB / %dMB memory", (totalMemory - freeMemory) / (1024 * 1024), totalMemory / (1024 * 1024));

                //slp.getRightMostLabel().setText(text);           
            }
        }, initialDelay, period, timeUnit);

    }

    public long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(long freeMemory) {
        long old = this.freeMemory;
        this.freeMemory = freeMemory;
        propertyChangeSupport.firePropertyChange("freeMemory", old, this.freeMemory);
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(long totalMemory) {
        long old = this.totalMemory;
        this.totalMemory = totalMemory;
        propertyChangeSupport.firePropertyChange("totalMemory", old, this.totalMemory);
    }

    public long getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(long maxMemory) {
        long old = this.maxMemory;
        this.maxMemory = maxMemory;
        propertyChangeSupport.firePropertyChange("maxMemory", old, this.maxMemory);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }
}
