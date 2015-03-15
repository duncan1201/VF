/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.common.ui.util.CommonUtil;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author dq
 */
public class AsPref implements Cloneable {

    private Integer hibernateId;
    private boolean baseNumberShown = true;
    private boolean doubleStranded;
    private boolean minimapShown = true;
    private float zoom = 1;
    private Map<String, Boolean> trackData = new HashMap<String, Boolean>();
    private transient PropertyChangeSupport propertyChangeSupport;

    public AsPref() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        float old = this.zoom;
        this.zoom = zoom;
        propertyChangeSupport.firePropertyChange("zoom", old, this.zoom);
    }
   
    public void touchAll() {
        trackData.size();
    }

    @Override
    public AsPref clone() {
        AsPref ret = CommonUtil.cloneSimple(this);
        ret.setTrackData(CommonUtil.copyOf(trackData));
        return ret;
    }

    public Map<String, Boolean> getTrackData() {
        return trackData;
    }

    public void setTrackData(Map<String, Boolean> trackData) {
        this.trackData = trackData;
    }

    public boolean isDoubleStranded() {
        return doubleStranded;
    }

    public void setDoubleStranded(boolean doubleStranded) {
        boolean old = this.doubleStranded;
        this.doubleStranded = doubleStranded;
        propertyChangeSupport.firePropertyChange("doubleStranded", old, this.doubleStranded);
    }

    public boolean isBaseNumberShown() {
        return baseNumberShown;
    }

    public void setBaseNumberShown(boolean baseNumberShown) {
        boolean old = this.baseNumberShown;
        this.baseNumberShown = baseNumberShown;
        propertyChangeSupport.firePropertyChange("baseNumberShown", old, this.baseNumberShown);
    }

    public boolean isMinimapShown() {
        return minimapShown;
    }

    public void setMinimapShown(boolean minimapShown) {
        boolean old = this.minimapShown;
        this.minimapShown = minimapShown;
        propertyChangeSupport.firePropertyChange("minimapShown", old, this.minimapShown);
    }

    public boolean isTrackVisible(String name) {
        name = name.toUpperCase(Locale.ENGLISH);
        if (!trackData.containsKey(name)) {
            trackData.put(name, true);
        }
        return trackData.get(name);
    }

    public void setTrackVisible(String name, boolean visible) {
        name = name.toUpperCase(Locale.ENGLISH);
        boolean old = isTrackVisible(name);
        trackData.put(name, visible);
        propertyChangeSupport.firePropertyChange(name, old, visible);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    /**
     * for hibernate use only
     */
    protected Integer getHibernateId() {
        return hibernateId;
    }

    /**
     * for hibernate use only
     */
    protected void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }
}
