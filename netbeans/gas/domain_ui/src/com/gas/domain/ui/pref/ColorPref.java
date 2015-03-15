/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.pref;

import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.ColorGenerator;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.api.IFetureKeyService;
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.prefs.Preferences;
import org.openide.util.Lookup;
import org.openide.util.NbPreferences;

/**
 *
 * @author dq
 */
public class ColorPref {

    private static ColorPref instance;
    private Preferences colorRef;
    private PropertyChangeSupport propertyChangeSupport;
    private final static Map<String, Integer> COLOR_MAP = new HashMap<String, Integer>();

    static {
        IFetureKeyService fetureKeyService = Lookup.getDefault().lookup(IFetureKeyService.class);
        List<String> names = fetureKeyService.getAllNames();
        Collections.sort(names);
        List<Color> colorList = ColorGenerator.generate(names.size());
        for (int i = 0; i < colorList.size(); i++) {
            int c = colorList.get(i).getRGB();
            COLOR_MAP.put(names.get(i).toUpperCase(Locale.ENGLISH), c);
        }
        COLOR_MAP.put(FetureKeyCnst.PRIMER_BINDING_SITE.toUpperCase(Locale.ENGLISH), ColorCnst.DARTMOUTH_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.OVERLAPPING_PRIMER.toUpperCase(Locale.ENGLISH), ColorCnst.DARTMOUTH_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attB1.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attB1r.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attB2.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attB2r.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attB3.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attB3r.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attB4.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attB4r.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attB5.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attB5r.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attP1.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attP1r.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attP2.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attP2r.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attP3.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attP3r.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attP4.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attP4r.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attP5.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attP5r.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attL1.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attL2.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attL3.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attL4.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attL5.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attR1.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attR2.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attR3.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attR4.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
        COLOR_MAP.put(FetureKeyCnst.attR5.toUpperCase(Locale.ENGLISH), ColorCnst.PAKISTAN_GREEN.getRGB());
    }
    private final static int UNDEFINED_COLOR = Color.MAGENTA.getRGB();

    public static ColorPref getInstance() {
        if (instance == null) {
            instance = new ColorPref();
        }
        return instance;
    }

    private ColorPref() {
        propertyChangeSupport = new PropertyChangeSupport(this);
        colorRef = NbPreferences.forModule(ColorPref.class).node(ColorPref.class.getSimpleName());
    }

    private static int getDefaultColor(String name) {
        int ret = Integer.MIN_VALUE;

        name = name.toUpperCase(Locale.ENGLISH);
        if (COLOR_MAP.containsKey(name)) {
            ret = COLOR_MAP.get(name);
        } else {
            ret = UNDEFINED_COLOR;
        }
        return ret;
    }

    public Color getColor(String name) {
        name = name.toUpperCase(Locale.ENGLISH);
        return new Color(colorRef.getInt(name, getDefaultColor(name)));
    }

    public void putColor(String name, Color color) {
        name = name.toUpperCase(Locale.ENGLISH);
        Color old = getColor(name);
        colorRef.putInt(name, color.getRGB());
        propertyChangeSupport.firePropertyChange(name, old, color);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener p){
        propertyChangeSupport.addPropertyChangeListener(p);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener p){
        propertyChangeSupport.removePropertyChangeListener(p);
    }
}
