/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.color;

import com.gas.common.ui.combo.ImgComboRenderer.IComboItem;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import org.biojava.BioJavaHelper;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IColorProvider.class)
public class RasMolColorProvider implements IColorProvider, IComboItem {

    public static final String NAME = "RasMol";

    /*
     From Ramol V 2.6  http://www.chemie.fu-berlin.de/chemnet/use/rasmol.html#chcolours
     ASP,GLU   bright red [230,10,10]     CYS,MET     yellow [230,230,0]
     LYS,ARG   blue       [20,90,255]     SER,THR     orange [250,150,0]
     PHE,TYR   mid blue   [50,50,170]     ASN,GLN     cyan   [0,220,220]
     GLY       light grey [235,235,235]   LEU,VAL,ILE green  [15,130,15]
     ALA       dark grey  [200,200,200]   TRP         pink   [180,90,180]
     HIS       pale blue  [130,130,210]   PRO         flesh  [220,150,130]     
     */
    private final static Map<String, Color> colors = new HashMap<String, Color>();

    static {
        colors.put("ASP", new Color(230, 10, 10));
        colors.put("GLU", new Color(230, 10, 10));
        colors.put("LYS", new Color(20, 90, 255));
        colors.put("ARG", new Color(20, 90, 255));
        colors.put("PHE", new Color(50, 50, 170));
        colors.put("TYR", new Color(50, 50, 170));
        colors.put("GLY", new Color(235, 235, 235));//GLY       light grey [235,235,235]
        colors.put("ALA", new Color(200, 200, 200));//ALA       dark grey  [200,200,200]
        colors.put("HIS", new Color(130, 130, 210));//HIS       pale blue  [130,130,210]
        colors.put("CYS", new Color(230, 230, 0));//CYS     yellow [230,230,0]
        colors.put("MET", new Color(230, 230, 0));//MET     yellow [230,230,0]
        colors.put("SER", new Color(250, 150, 0));//SER     orange [250,150,0]
        colors.put("THR", new Color(250, 150, 0));//THR     orange [250,150,0]
        colors.put("ASN", new Color(0, 220, 220));//ASN     cyan   [0,220,220]
        colors.put("GLN", new Color(0, 220, 220));//GLN     cyan   [0,220,220]
        colors.put("LEU", new Color(15, 130, 15));//LEU green  [15,130,15]
        colors.put("VAL", new Color(15, 130, 15));//VAL green  [15,130,15]
        colors.put("ILE", new Color(15, 130, 15));//ILE green  [15,130,15]
        colors.put("TRP", new Color(180, 90, 180));//TRP         pink   [180,90,180]
        colors.put("PRO", new Color(220, 150, 130));//PRO         flesh  [220,150,130]  
        colors.put("TER", Color.BLACK);
    }

    @Override
    public Color getColor(String s) {
        if (s.length() != 1 && s.length() != 3) {
            throw new IllegalArgumentException(String.format("The lenght of amino acid name must be 1 or 3: %d", s.length()));
        }
        Color ret = null;
        String aa3Code = null;
        if (s.length() == 1) {
            aa3Code = BioJavaHelper.toAA3LetterCode(s.charAt(0));
        }
        boolean present = colors.containsKey(aa3Code);
        if (present) {
            ret = colors.get(aa3Code);
        }
        return ret;
    }

    @Override
    public Color getColor(Character c) {
        return getColor(c.toString());
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }

    @Override
    public boolean isForProtein() {
        return true;
    }

    @Override
    public boolean isForNucleotide() {
        return false;
    }

    @Override
    public boolean isBackgroundColor() {
        return true;
    }

    @Override
    public ImageIcon getImageIcon() {
        return ImageIconCreator.createImageIcon(this);
    }
}
