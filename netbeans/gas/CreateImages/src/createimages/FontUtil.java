/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package createimages;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author dunqiang
 */
public class FontUtil {

    private static final String testData = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String[] MS_CANDIDATES = {/*"DejaVu Sans Mono","Consolas",*/"Monospaced"};
    private static Set<String> monospaceFontFamilies = new TreeSet<String>();
    // keys are in UPPERCASE
    private static Map<String, Font> allFonts = new HashMap<String, Font>();
    private static Graphics graphics;
    private static Map<Font, Integer> MSCharWidthMap = new HashMap<Font, Integer>();
    private static Map<String, Integer> charWidths = new HashMap<String, Integer>();
    private static Map<Font, FontMetrics> fontMetricsMap = new HashMap<Font, FontMetrics>();
    public static final String MONOSPACED = "Monospaced";
    public static final String SERIF = "Serif";
    public static final String SANS_SERIF = "SansSerif";
    public static final String DIALOG = "Dialog";
    public static final String DIALOG_INPUT = "DialogInput";

    public static Set<String> getMSFontFamilies() {
        if (monospaceFontFamilies.isEmpty()) {
            initMSFonts();
        }
        return monospaceFontFamilies;
    }

    public static FontMetrics getFontMetrics(Component comp) {
        Font font = comp.getFont();
        return getFontMetrics(font);
    }

    public static Integer charWidth(Font font, Character c) {
        FontMetrics fm = getFontMetrics(font);
        String fmStr = fm.toString();
        if (!charWidths.containsKey(fmStr + c)) {
            charWidths.put(fmStr + c, fm.charWidth(c));
        }
        return charWidths.get(fmStr + c);
    }

    public static FontMetrics getDefaultFontMetrics() {
        float fontSize = getDefaultFontSize();
        Font font = getDefaultFont().deriveFont(fontSize);
        return getFontMetrics(font);
    }

    /*
     The code must be executed in EventDispatchThread
     */
    public static FontMetrics getFontMetrics(final Font font) {
        if (font == null) {
            throw new IllegalArgumentException("font cannot be null");
        }
        FontMetrics ret = null;
        if (!fontMetricsMap.containsKey(font)) {
            if (graphics == null) {
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                graphics = ge.createGraphics(new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR));
            }
            FontMetrics fontMetrics = graphics.getFontMetrics(font);
            fontMetricsMap.put(font, fontMetrics);
        }
        ret = fontMetricsMap.get(font);
        if (ret == null) {
            int a = 1;
        }
        return ret;
    }

    public static Integer getMSFontCharWidth(Font font) {
        if (!MSCharWidthMap.containsKey(font)) {
            Set<String> families = getMSFontFamilies();
            String family = font.getFamily();
            if (!families.contains(family)) {
                throw new IllegalArgumentException(String.format("Font '%s' is not a monospace font", family));
            }
            int width = getFontMetrics(font).charWidth('a');
            MSCharWidthMap.put(font, width);
        }
        return MSCharWidthMap.get(font);
    }

    public static void initAllFonts() {

        if (allFonts.isEmpty()) {

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

            Font[] fonts = ge.getAllFonts();
            //System.out.println(fonts.length);

            for (Font f : fonts) {
                String fontName = f.getName();
                String fontFamily = f.getFamily();

                if (fontName.endsWith("Italic") || fontName.endsWith("Bold")) {
                    continue;
                }
                allFonts.put(fontFamily.toUpperCase(Locale.ENGLISH), f);
            }
        }
    }

    public static void initMSFonts() {

        if (monospaceFontFamilies.isEmpty()) {
            JLabel label = new JLabel();


            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

            Font[] fonts = ge.getAllFonts();
            //System.out.println(fonts.length);

            for (Font f : fonts) {
                String fontName = f.getName();

                if (fontName.endsWith("Italic") || fontName.endsWith("Bold")) {
                    continue;
                }
                boolean e = true;

                FontMetrics fontMetrics = label.getFontMetrics(f);

                Integer width = null;
                for (int i = 0; i < testData.length(); i++) {
                    if (width == null) {
                        width = fontMetrics.charWidth(testData.charAt(i));
                    } else {
                        int tmp = fontMetrics.charWidth(testData.charAt(i));
                        if (tmp != width) {
                            e = false;
                            break;
                        }
                    }
                }
                if (e) {
                    monospaceFontFamilies.add(f.getFamily());
                }
            }
        }
    }

    public static Font getFont(String fontFamily) {
        if (allFonts.isEmpty()) {
            initAllFonts();
        }

        return allFonts.get(fontFamily.toUpperCase(Locale.ENGLISH));
    }

    public static Map<String, Font> getAllFonts() {
        if (allFonts.isEmpty()) {
            initAllFonts();
        }
        return allFonts;
    }

    public static Font getDefaultMSFont() {
        return getDefaultMSFont(getDefaultFontSize());
    }
    
    public static FontMetrics getDefaultMSFontMetrics(){
        Font font = getDefaultMSFont();
        return getFontMetrics(font);
    }

    public static Float getDefaultFontSize() {
        UIDefaults uiDefaults = UIManager.getDefaults();
        Float ret = uiDefaults.getFont("Button.font").getSize2D();
        return ret;
    }
    
    public static float getSmallFontSize(float ratio) {
        return Math.round(getDefaultFontSize() * ratio);
    }

    public static float getSmallFontSize() {
        return Math.round(getDefaultFontSize() * 0.75f);
    }

    public static float getDefaultMenuFontSize() {
        UIDefaults uiDefaults = UIManager.getDefaults();
        float ret = uiDefaults.getFont("Menu.font").getSize2D();
        return ret;
    }

    public static Font getCustomMSFont(float size) {
        String[] fontFamilies = {"CONSOLAS", "Courier New", "Courier"};
        Font ret = null;
        for (String f : fontFamilies) {
            ret = getFont(f);
            if (ret != null) {
                break;
            }
        }
        ret = ret.deriveFont(size);
        return ret;
    }

    public static Font getDefaultMSFont(float size) {

        Font ret = null;

        for (String candidate : MS_CANDIDATES) {
            ret = getFont(candidate);
            if (ret != null) {
                break;
            }
        }

        ret = ret.deriveFont(size);
        return ret;
    }

    public static Font getDefaultSansSerifFont() {
        return getFont(SANS_SERIF).deriveFont(getDefaultFontSize());
    }

    public static Font getDefaultSerifFont() {
        return getFont(SERIF).deriveFont(getDefaultFontSize());
    }

    public static Font getDefaultFont() {
        String className = UIManager.getSystemLookAndFeelClassName();
        Font font = null;
        try {
            LookAndFeel systemLaf = (LookAndFeel) Class.forName(className).newInstance();
            UIDefaults defaults = systemLaf.getDefaults();
            font = defaults.getFont("Label.font");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FontUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(FontUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(FontUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return font;
    }
}
