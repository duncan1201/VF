package com.gas.common.ui.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import com.sun.java.swing.plaf.nimbus.TabbedPaneTabbedPaneTabAreaPainter;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dunqiang
 */
public class FontUtilTest {

    public FontUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    //@Test
    public void testGetMSFontFamilies() {
        Iterator<String> itr = FontUtil.getMSFontFamilies().iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            System.out.println("str=" + str);
        }
    }

    //@Test
    public void testGetFontMetrics() {
        FontMetrics m = FontUtil.getFontMetrics(new Font(Font.SERIF, Font.PLAIN, 12));
        Assert.assertTrue(m.getAscent() > 0);
    }

    @Test
    public void testGetFont() {
    }

    @Test
    public void testGetDefaultFontSize() {
        LookAndFeel systemLaf = null;
        Font font = null;
        try {
            String className = NimbusLookAndFeel.class.getName();
            systemLaf = (LookAndFeel) Class.forName(className).newInstance();
            UIDefaults defaults = systemLaf.getDefaults();
            System.out.println(defaults.keySet().size());
            Iterator itr = defaults.keySet().iterator();
            int counter = 0;
            while (itr.hasNext()) {
                Object key = itr.next();
                if (key instanceof String) {
                    if (((String) key).contains("TabbedPane")) {
                        counter++;
                        System.out.println(counter + "\t" + key);
                        System.out.println(defaults.get(key));
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FontUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(FontUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(FontUtil.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    //@Test
    public void testGetDefaultFont() {
        Font font = FontUtil.getDefaultFont();
        System.out.println(font.getSize());
    }

    //@Test
    public void testGetAllFonts() {
        Map<String, Font> fonts = FontUtil.getAllFonts();
        Iterator<String> itr = fonts.keySet().iterator();
        while (itr.hasNext()) {
            final String text = itr.next();
            Font font = fonts.get(text);
            FontMetrics fm = FontUtil.getFontMetrics(font);
            System.out.println(text + " font height=" + fm.getHeight());
        }
    }

    //@Test
    public void testGetDefaultMSFont_float() {
        Iterator<String> itr = FontUtil.getMSFontFamilies().iterator();
        while (itr.hasNext()) {
            String text = itr.next();
            System.out.println(text);
        }
    }
}
