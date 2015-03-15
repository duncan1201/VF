/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.Pref;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author dq
 */
public class CommonPtyPrefs {

    private static CommonPtyPrefs instance;
    private Preferences pref;
    private PropertyChangeSupport propertyChangeSupport;
    private Boolean doubleStranded;
    private Boolean minimapShown;
    private Boolean baseNumberShown;
    private Float rulerFontSize;
    private Float baseFontSize;
    private Float annotationLabelSize;
    private Float zoom;
    private Boolean editable;
    private Integer gcHeight;

    public static CommonPtyPrefs getInstance() {
        if (instance == null) {
            instance = new CommonPtyPrefs();
        }
        return instance;
    }

    public Boolean getEditable() {
        if (editable == null) {
            editable = pref.getBoolean("editable", false);
        }
        return editable;
    }

    public void setEditable(Boolean editable) {
        Boolean old = this.editable;
        this.editable = editable;
        pref.putBoolean("editable", this.editable);
        this.propertyChangeSupport.firePropertyChange("editable", old, this.editable);
    }

    public void setGCHeight(Integer gcHeight) {
        Integer old = this.gcHeight;
        this.gcHeight = gcHeight;
        pref.putInt("gcHeight", this.gcHeight);
        this.propertyChangeSupport.firePropertyChange("gcHeight", old, this.gcHeight);
    }

    public Integer getGCHeight() {
        if (gcHeight == null) {
            gcHeight = pref.getInt("gcHeight", FontUtil.getDefaultFontMetrics().getHeight() * 5);
        }
        return gcHeight;
    }

    public Float getZoom() {
        if (zoom == null) {
            zoom = pref.getFloat("zoom", 1.0f);
        }
        return zoom;
    }

    public void setZoom(Float zoom) {
        Float old = this.zoom;
        this.zoom = zoom;
        pref.putFloat("zoom", this.zoom);
        this.propertyChangeSupport.firePropertyChange("zoom", old, this.zoom);
    }

    public Float getAnnotationLabelSize() {
        if (annotationLabelSize == null) {
            annotationLabelSize = pref.getFloat("annotationLabelSize", FontUtil.getDefaultFontSize());
        }
        return annotationLabelSize;
    }

    public void setAnnotationLabelSize(Float annotationLabelSize) {
        Float old = this.annotationLabelSize;
        this.annotationLabelSize = annotationLabelSize;
        pref.putFloat("annotationLabelSize", this.annotationLabelSize);
        propertyChangeSupport.firePropertyChange("annotationLabelSize", old, this.annotationLabelSize);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public Float getBaseFontSize() {
        if (baseFontSize == null) {
            baseFontSize = pref.getFloat("baseFontSize", FontUtil.getDefaultFontSize());
        }
        return baseFontSize;
    }

    public void setBaseFontSize(Float baseFontSize) {
        Float old = this.baseFontSize;
        this.baseFontSize = baseFontSize;
        pref.putFloat("baseFontSize", this.baseFontSize);
        propertyChangeSupport.firePropertyChange("baseFontSize", old, this.baseFontSize);
    }

    public Float getRulerFontSize() {
        if (rulerFontSize == null) {
            this.rulerFontSize = pref.getFloat("rulerFontSize", Math.round(FontUtil.getDefaultFontSize() * 0.8f));
        }
        return rulerFontSize;
    }

    public void setRulerFontSize(Float rulerFontSize) {
        Float old = this.rulerFontSize;
        this.rulerFontSize = rulerFontSize;
        pref.putFloat("rulerFontSize", this.rulerFontSize);
        propertyChangeSupport.firePropertyChange("rulerFontSize", old, this.rulerFontSize);
    }

    public Boolean isBaseNumberShown() {
        if (baseNumberShown == null) {
            baseNumberShown = pref.getBoolean("baseNumberShown", Boolean.TRUE);
        }
        return baseNumberShown;
    }

    public void setBaseNumberShown(Boolean baseNumberShown) {
        Boolean old = this.baseNumberShown;
        this.baseNumberShown = baseNumberShown;
        pref.putBoolean("baseNumberShown", this.baseNumberShown);
        propertyChangeSupport.firePropertyChange("baseNumberShown", old, this.baseNumberShown);
    }

    public Boolean isMinimapShown() {
        if (minimapShown == null) {
            minimapShown = pref.getBoolean("minimapShown", Boolean.TRUE);
        }
        return minimapShown;
    }

    public void setMinimapShown(Boolean minimapShown) {
        Boolean old = this.minimapShown;
        this.minimapShown = minimapShown;
        pref.putBoolean("minimapShown", this.minimapShown);
        propertyChangeSupport.firePropertyChange("minimapShown", old, this.minimapShown);
    }

    private CommonPtyPrefs() {
        propertyChangeSupport = new PropertyChangeSupport(this);
        pref = NbPreferences.forModule(Pref.class).node(CommonPtyPrefs.class.getSimpleName());
    }

    public boolean isDoubleStranded() {
        if (doubleStranded == null) {
            doubleStranded = pref.getBoolean("doubleStranded", Boolean.FALSE);
        }
        return doubleStranded;
    }

    public void setDoubleStranded(boolean doubleStranded) {
        Boolean old = this.doubleStranded;
        this.doubleStranded = doubleStranded;
        pref.putBoolean("doubleStranded", this.doubleStranded);
        propertyChangeSupport.firePropertyChange("doubleStranded", old, this.doubleStranded);
    }
}
