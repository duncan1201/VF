/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import com.gas.common.ui.color.ClustalDNAColorProvider;
import com.gas.common.ui.color.RasMolColorProvider;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author dq
 */
public class Pref {

    public static class LastOpenFolderPref {

        private final String propertyName = "lastOpenFolder";
        private static LastOpenFolderPref instance;
        private Preferences pref;
        private PropertyChangeSupport propertyChangeSupport;

        public static LastOpenFolderPref getInstance() {
            if (instance == null) {
                instance = new LastOpenFolderPref();
            }
            return instance;
        }

        public String getPropertyName() {
            return propertyName;
        }

        private LastOpenFolderPref() {
            propertyChangeSupport = new PropertyChangeSupport(this);
            pref = NbPreferences.forModule(Pref.class).node(LastOpenFolderPref.class.getSimpleName());
        }

        public void setLastOpenFolder(String value) {
            String old = getLastOpenFolder();
            pref.put(propertyName, value);
            propertyChangeSupport.firePropertyChange(propertyName, old, value);
        }

        public String getLastOpenFolder() {
            String value = pref.get(propertyName, "");
            return value;
        }

        private PropertyChangeSupport getPropertyChangeSupport() {
            return propertyChangeSupport;
        }
    }

    public static class TrackVisiblePref {

        private static TrackVisiblePref instance;
        private Preferences pref;
        private PropertyChangeSupport propertyChangeSupport;

        public static TrackVisiblePref getInstance() {
            if (instance == null) {
                instance = new TrackVisiblePref();
            }
            return instance;
        }

        private TrackVisiblePref() {
            propertyChangeSupport = new PropertyChangeSupport(this);
            pref = NbPreferences.forModule(Pref.class).node(TrackVisiblePref.class.getSimpleName());
        }

        public boolean isVisible(String name) {
            boolean ret = pref.getBoolean(name.toUpperCase(Locale.ENGLISH), true);
            return ret;
        }

        public void setVisible(String name, boolean visible) {
            name = name.toUpperCase(Locale.ENGLISH);
            boolean old = isVisible(name);
            pref.putBoolean(name, visible);

            propertyChangeSupport.firePropertyChange(name, old, visible);
        }

        public void addPropertyChangeListener(PropertyChangeListener l) {
            propertyChangeSupport.addPropertyChangeListener(l);
        }
    }

    public static class LabelVisiblePref {

        private static LabelVisiblePref instance;
        private Preferences pref;
        private PropertyChangeSupport propertyChangeSupport;

        public static LabelVisiblePref getInstance() {
            if (instance == null) {
                instance = new LabelVisiblePref();
            }
            return instance;
        }

        private LabelVisiblePref() {
            propertyChangeSupport = new PropertyChangeSupport(this);
            pref = NbPreferences.forModule(Pref.class).node(LabelVisiblePref.class.getSimpleName());
        }

        public boolean isVisible(String featureType) {
            boolean ret = pref.getBoolean(featureType.toUpperCase(Locale.ENGLISH), true);
            return ret;
        }

        public void setVisible(String featureType, boolean visible) {
            boolean old = isVisible(featureType.toUpperCase(Locale.ENGLISH));
            pref.putBoolean(featureType.toUpperCase(Locale.ENGLISH), visible);
            propertyChangeSupport.firePropertyChange(featureType.toUpperCase(Locale.ENGLISH), old, visible);
        }

        public PropertyChangeSupport getPropertyChangeSupport() {
            return propertyChangeSupport;
        }
    }

    public static class CommonPtyPrefs {

        private static CommonPtyPrefs instance;
        private Preferences pref;
        private PropertyChangeSupport propertyChangeSupport;
        private Float rulerFontSize;
        private Float baseFontSize;
        private Float annotationLabelSize;
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

        private CommonPtyPrefs() {
            propertyChangeSupport = new PropertyChangeSupport(this);
            pref = NbPreferences.forModule(Pref.class).node(CommonPtyPrefs.class.getSimpleName());
        }
    }

    public static class ColorProviderPrefs {

        private static ColorProviderPrefs instance;
        private Preferences pref;
        private PropertyChangeSupport propertyChangeSupport;
        private static final String DEFAULT_DNA_COLOR_PROVIDER = ClustalDNAColorProvider.NAME;
        private static final String DEFAULT_PROTEIN_COLOR_PROVIDER = RasMolColorProvider.NAME;
        private static final String DEFAULT_TRANSLATION_COLOR_PROVIDER = RasMolColorProvider.NAME;

        public enum KEY {

            PROTEIN, DNA, TRANSLATION
        };

        public static ColorProviderPrefs getInstance() {
            if (instance == null) {
                instance = new ColorProviderPrefs();
            }
            return instance;
        }

        private ColorProviderPrefs() {
            propertyChangeSupport = new PropertyChangeSupport(this);
            pref = NbPreferences.forModule(Pref.class).node(ColorProviderPrefs.class.getSimpleName());
        }

        public String getColorProviderName(KEY key) {
            String ret = null;
            String def = null;
            if (key == KEY.PROTEIN) {
                def = DEFAULT_PROTEIN_COLOR_PROVIDER;
            } else if (key == KEY.DNA) {
                def = DEFAULT_DNA_COLOR_PROVIDER;
            } else if (key == KEY.TRANSLATION) {
                def = DEFAULT_TRANSLATION_COLOR_PROVIDER;
            }
            ret = pref.get(key.toString(), def);
            return ret;
        }

        public void setColorProviderName(KEY key, String providerName) {
            String old = getColorProviderName(key);
            pref.put(key.toString(), providerName);
            propertyChangeSupport.firePropertyChange("colorProviderName", old, providerName);
        }
    }
}
