/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.pref;

import com.gas.common.ui.color.ClustalDNAColorProvider;
import com.gas.common.ui.color.RasMolColorProvider;
import com.gas.common.ui.util.FontUtil;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author dq
 */
public class MSAPref {

    private static MSAPref instance;
    private Preferences pref;
    private PropertyChangeSupport propertyChangeSupport;
    private static final String DEFAULT_DNA_COLOR_PROVIDER = ClustalDNAColorProvider.NAME;
    private static final String DEFAULT_PROTEIN_COLOR_PROVIDER = RasMolColorProvider.NAME;
    private static final String DEFAULT_TRANSLATION_COLOR_PROVIDER = RasMolColorProvider.NAME;
    private static Float defaultFontSize;

    public enum KEY {
        PROTEIN, DNA, TRANSLATION
    };

    public static MSAPref getInstance() {
        if (instance == null) {
            instance = new MSAPref();
        }
        return instance;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    private MSAPref() {
        propertyChangeSupport = new PropertyChangeSupport(this);
        pref = NbPreferences.forModule(MSAPref.class).node(MSAPref.class.getSimpleName());
    }

    public String getColorProviderName(MSAPref.KEY key) {
        String ret = null;
        String def = null;
        if (key == MSAPref.KEY.PROTEIN) {
            def = DEFAULT_PROTEIN_COLOR_PROVIDER;
        } else if (key == MSAPref.KEY.DNA) {
            def = DEFAULT_DNA_COLOR_PROVIDER;
        } else if (key == MSAPref.KEY.TRANSLATION) {
            def = DEFAULT_TRANSLATION_COLOR_PROVIDER;
        }
        ret = pref.get(key.toString(), def);
        return ret;
    }

    public void setColorProviderName(MSAPref.KEY key, String providerName) {
        String old = getColorProviderName(key);
        pref.put(key.toString(), providerName);
        propertyChangeSupport.firePropertyChange("colorProviderName", old, providerName);
    }
    
    public void setFontSize(Float fontSize) {
        Float old = getFontSize();
        pref.putFloat("fontSize", fontSize);
        propertyChangeSupport.firePropertyChange("fontSize", old, fontSize);
    }

    public Float getFontSize() {
        return pref.getFloat("defaultFontSize", getDefaultFontSize());
    }

    private Float getDefaultFontSize() {
        if (defaultFontSize == null) {
            defaultFontSize = FontUtil.getDefaultFontSize();
        }
        return defaultFontSize;
    }

    public String getNodeShape() {
        return pref.get("nodeShape", "None");
    }

    public void setNodeShape(String s) {
        String old = getNodeShape();
        pref.put("nodeShape", s);
        propertyChangeSupport.firePropertyChange("nodeShape", old, s);
    }
}
