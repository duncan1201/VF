package com.gas.common.ui.util;

import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.text.JTextComponent;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;
import org.openide.util.datatransfer.MultiTransferObject;
import org.openide.windows.WindowManager;

public class CommonUtil {

    private final static Logger logger = Logger.getLogger(CommonUtil.class.getName());
    public final static List<Class> pList = new ArrayList<Class>();

    static {
        pList.add(Boolean.TYPE);
        pList.add(Boolean.class);
        pList.add(String.class);
        pList.add(Integer.TYPE);
        pList.add(Integer.class);
        pList.add(Long.TYPE);
        pList.add(Long.class);
    }
    
    public static boolean isNullOrEmpty(Collection c){
        return c == null || c.isEmpty();
    }

    public static void resetWindows() {
        FileUtil.getConfigObject("Actions/Window/org-netbeans-core-windows-actions-ResetWindowsAction.instance",
                Action.class).actionPerformed(null);
    }

    public static void maximizeMainFrame() {
        WindowManager windowManager = WindowManager.getDefault();
        JFrame frame = (JFrame) windowManager.getMainWindow();
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    /**
     * This method guarantees that garbage collection is done unlike
     * <code>{@link System#gc()}</code>
     */
    public static void gc() {
        Object obj = new Object();
        WeakReference ref = new WeakReference<Object>(obj);
        obj = null;
        while (ref.get() != null) {
            System.gc();
        }
    }

    public static boolean is64bit() {
        boolean is64bit;
        if (Utilities.isWindows()) {
            is64bit = (System.getenv("ProgramFiles(x86)") != null);
        } else if (Utilities.isMac()) {
            is64bit = true;
        } else {
            throw new UnsupportedOperationException();
        }
        return is64bit;
    }

    public static void acquireQuitely(Semaphore semaphore) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            System.out.print("");
        }
    }

    /**
     * @return pageExists
     */
    public static boolean printQuietly(Printable printable, Graphics graphics, PageFormat pageFormat, int pageIndex) {
        boolean pageExists = false;
        try {
            pageExists = printable.print(graphics, pageFormat, pageIndex) == Printable.PAGE_EXISTS;
        } catch (PrinterException ex) {
            Exceptions.printStackTrace(ex);
        }
        return pageExists;
    }

    public static MultiTransferObject getTransferData(Transferable t, DataFlavor df) {
        MultiTransferObject ret = null;
        try {
            ret = (MultiTransferObject) t.getTransferData(df);
        } catch (UnsupportedFlavorException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    public static void printQuietly(JTextComponent c, MessageFormat headerFormat,
            MessageFormat footerFormat,
            boolean showPrintDialog,
            PrintService service,
            PrintRequestAttributeSet attributes,
            boolean interactive) {
        try {
            if (headerFormat != null && footerFormat != null && service != null && attributes != null) {
                c.print(headerFormat, footerFormat, showPrintDialog, service, attributes, interactive);
            } else if (headerFormat != null && footerFormat != null) {
                c.print(headerFormat, footerFormat);
            } else {
                c.print();
            }
        } catch (PrinterException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static int getPageCount(Printable printable, PageFormat pageFormat) {
        boolean exist = true;
        int pageIndex = 0;
        int ret = 0;
        BufferedImage image = UIUtil.createCompatibleImage(1, 1);
        Graphics2D g2d = image.createGraphics();
        while (exist) {
            try {
                exist = printable.print(g2d, pageFormat, pageIndex) == Printable.PAGE_EXISTS;
                if (exist) {
                    pageIndex++;
                    ret++;
                }
            } catch (PrinterException ex) {
                exist = false;
            }
        }
        g2d.dispose();
        return ret;
    }

    public static <T> List<T> remove(final List<T> minuend, final List<T> subtrahend) {
        List<T> difference = new ArrayList<T>(minuend);
        for (T o : subtrahend) {
            difference.remove(o);
        }
        return difference;
    }

    public static <T> List<T> remove(final List<T> minuend, T subtrahend) {
        List<T> difference = new ArrayList<T>(minuend);
        difference.remove(subtrahend);
        return difference;
    }

    public static String toString(List<Integer> ints) {
        return toString(ints, 10);
    }

    public static String toString(List<Integer> ints, int radix) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < ints.size(); i++) {
            Integer _int = ints.get(i);
            if (_int == null) {
                ret.append("null");

            } else {
                String str = Integer.toString(ints.get(i), radix);
                ret.append(str);
            }
            if (i + 1 < ints.size()) {
                ret.append(' ');
            }
        }
        return ret.toString();
    }

    public static String toString(int[] ints) {
        return toString(ints, 10);
    }

    public static String toString(int[] ints, int radix) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; ints != null && i < ints.length; i++) {
            String str = Integer.toString(ints[i], radix);
            ret.append(str);
            ret.append(' ');
        }
        return ret.toString();
    }

    public static int[] toIntArray(String str) {
        return toIntArray(str, 10);
    }

    public static List<Integer> toList(int[] data) {
        List<Integer> ret = new ArrayList<Integer>();
        if(data == null){
            return ret;
        }
        for (int i : data) {
            ret.add(i);
        }
        return ret;
    }

    public static String breakUpInts(String intStr, int perLine) {
        int[] ints = toIntArray(intStr);
        return breakUpInts(ints, perLine);
    }

    public static String breakUpInts(int[] ints, int perLine) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < ints.length; i++) {
            ret.append(ints[i]);
            if ((i + 1) % perLine == 0) {
                ret.append('\n');
            } else {
                ret.append(' ');
            }
        }

        return ret.toString();
    }

    public static int[] toIntArray(String str, final int radix) {
        if (str == null) {
            return null;
        }
        //List<Integer> retList = new ArrayList<Integer>();
        int[] ret = null;
        StringTokenizer tokenizer = new StringTokenizer(str, " ");
        ret = new int[tokenizer.countTokens()];
        for (int i = 0; tokenizer.hasMoreTokens(); i++) {
            ret[i] = Integer.parseInt(tokenizer.nextToken(), radix);
        }
        return ret;
    }

    public static List<Integer> toIntList(String str, final int radix) {
        if (str == null) {
            return null;
        }
        List<Integer> ret = new ArrayList<Integer>();
        StringTokenizer tokenizer = new StringTokenizer(str, " ");

        for (int i = 0; tokenizer.hasMoreTokens(); i++) {
            String token = tokenizer.nextToken();
            if (token.equalsIgnoreCase("null")) {
                ret.add(null);
            } else {
                ret.add(Integer.parseInt(token, radix));
            }
        }
        return ret;
    }

    public static <T> Map<String, T> copyOf(Map<String, T> o) {
        Map<String, T> ret = null;

        if (o != null) {
            ret = new HashMap<String, T>();

            Iterator<String> itr = o.keySet().iterator();

            while (itr.hasNext()) {
                String key = itr.next();
                T t = o.get(key);
                if (t instanceof Cloneable) {
                    Object cloned;

                    cloned = ReflectHelper.invoke(t, "clone");
                    ret.put(key, (T) cloned);

                } else if (t.getClass().isAssignableFrom(String.class)
                        || t.getClass().isAssignableFrom(Integer.class)
                        || t.getClass().isAssignableFrom(Float.class)
                        || t.getClass().isAssignableFrom(Double.class)
                        || t.getClass().isAssignableFrom(Boolean.class)
                        || t.getClass().isAssignableFrom(Long.class)) {
                    ret.put(key, t);
                } else {
                    throw new IllegalArgumentException(String.format("Class '%s' must implement Cloneable", t.getClass().toString()));
                }
            }
        }

        return ret;
    }

    public static <T> Set<T> copyOf(Set<T> o) {
        if (o == null) {
            return null;
        }
        Set<T> ret = null;
        if (o != null) {
            ret = new HashSet<T>();
        }
        Iterator<T> itr = o.iterator();
        while (itr.hasNext()) {
            T t = itr.next();
            if (t instanceof Cloneable) {
                Object c = (Object) t;
                Object cloned = ReflectHelper.invoke(t, "clone");
                if (ret == null) {
                    ret = new HashSet<T>();
                }
                ret.add((T) cloned);

            } else if (t.getClass().isAssignableFrom(String.class)
                    || t.getClass().isAssignableFrom(Integer.class)) {
                if (ret == null) {
                    ret = new HashSet<T>();
                }
                ret.add(t);

            } else {
                throw new IllegalArgumentException(String.format("Class '%s' must implement Cloneable interface", t.getClass().toString()));
            }
        }
        return ret;
    }

    public static <T> List<T> copyOf(List<T> o) {
        List<T> ret = null;
        if (o != null) {
            ret = new ArrayList<T>();
        }else{
            return new ArrayList<T>();
        }
        Iterator<T> itr = o.iterator();
        while (itr.hasNext()) {
            T t = itr.next();
            if (t instanceof Cloneable) {
                Object c = (Object) t;
                Object cloned = null;

                cloned = ReflectHelper.invoke(t, "clone");
                if (ret == null) {
                    ret = new ArrayList<T>();
                }
                ret.add((T) cloned);

            } else if (t.getClass().isAssignableFrom(String.class)
                    || t.getClass().isAssignableFrom(Integer.class)
                    || t.getClass().isAssignableFrom(Float.class)
                    || t.getClass().isAssignableFrom(Double.class)
                    || t.getClass().isAssignableFrom(Character.class)) {
                if (ret == null) {
                    ret = new ArrayList<T>();
                }
                ret.add(t);

            } else {
                throw new IllegalArgumentException(String.format("Class '%s' must implement Cloneable interface", t.getClass().toString()));
            }
        }
        return ret;
    }

    /**
     * This method copies only the following data types & its primitives:
     * Boolean, Integer, Float, Double, Date String, Long
     */
    public static <T> T cloneSimple(T from) {

        Class classFrom = from.getClass();
        T ret = (T) ReflectHelper.newInstance(classFrom.getName(), classFrom.getClassLoader());

        List<Field> fields = ReflectHelper.getDeclaredFields(classFrom);
        for (Field f : fields) {
            final String nameField = f.getName();
            final int modifiers = f.getModifiers();
            if (nameField.equalsIgnoreCase("hibernateId") || Modifier.isTransient(modifiers)) {
                continue;
            }
            Class clazz = f.getType();

            if (clazz.isAssignableFrom(String.class)
                    || clazz.isAssignableFrom(Boolean.class)
                    || clazz.isAssignableFrom(boolean.class)
                    || clazz.isAssignableFrom(Float.class)
                    || clazz.isAssignableFrom(float.class)
                    || clazz.isAssignableFrom(Double.class)
                    || clazz.isAssignableFrom(double.class)
                    || clazz.isAssignableFrom(Long.class)
                    || clazz.isAssignableFrom(long.class)
                    || clazz.isAssignableFrom(int.class)
                    || clazz.isAssignableFrom(Integer.class)) {
                Object value = ReflectHelper.getQuietly(f, from);
                if (value != null) {
                    ReflectHelper.setQuietly(f, ret, value);
                }
            } else if (clazz.isAssignableFrom(Date.class)) {
                Date date = (Date) ReflectHelper.getQuietly(f, from);
                if (date != null) {
                    ReflectHelper.setQuietly(f, ret, date.clone());
                }
            }
        }
        return ret;
    }

    public static float[] copyOf(float[] o) {
        float[] ret = null;
        if (o != null) {
            ret = Arrays.copyOf(o, o.length);
        }
        return ret;
    }

    public static int[] copyOf(int[] o) {
        int[] ret = null;
        if (o != null) {
            ret = Arrays.copyOf(o, o.length);
        }
        return ret;
    }

    public static URI getURI(String uri) {
        URI ret = null;
        try {
            ret = new URI(uri);
        } catch (URISyntaxException ex) {
            Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public static boolean browse(String uri) {
        return browse(getURI(uri));
    }

    public static boolean browse(URI uri) {
        boolean ret = true;
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException ex) {
            ret = false;
            Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public static Float parseFloat(String s) {
        Float ret = null;
        try {
            ret = Float.parseFloat(s);
        } catch (Exception e) {
        }
        return ret;
    }

    public static <T> T parse(String a, Class<T> clazz) {
        T ret = null;

        if (clazz.isAssignableFrom(Integer.class)) {
            ret = (T) parseInt(a);
        } else if (clazz.isAssignableFrom(Float.class)) {
            ret = (T) parseFloat(a);
        } else if (clazz.isAssignableFrom(Double.class)) {
            ret = (T) parseDouble(a);
        } else {
            throw new IllegalArgumentException(clazz.getName() + " is not supported!");
        }
        return ret;
    }

    public static Integer parseInt(String s) {
        Integer ret = null;
        try {
            ret = Integer.parseInt(s);
        } catch (Exception e) {
        }
        return ret;
    }

    public static Double parseDouble(String s) {
        Double ret = null;
        try {
            ret = Double.parseDouble(s);
        } catch (Exception e) {
        }
        return ret;
    }

    public static CharSequence randomString(int length) {
        return randomString(length, null);
    }

    public static CharSequence randomString(int length, String postfix) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < length; i++) {
            ret.append(randomChar(true));
        }
        if (postfix != null) {
            ret.append(postfix);
        }
        return ret;
    }

    public static int[] reverse(int[] d) {
        int[] ret = new int[d.length];
        for (int i = 0; i < d.length; i++) {
            ret[i] = d[d.length - 1 - i];
        }
        return ret;
    }

    public static <T> List<T> reverse(List<T> list) {
        List<T> ret = new ArrayList<T>();
        for (int i = list.size() - 1; i > -1; i--) {
            ret.add(list.size() - i - 1, list.get(i));
        }
        return ret;
    }

    public static boolean delete(File f) {
        if (!f.exists()) {
            return true;
        } else if (f.isFile()) {
            boolean result = f.delete();
            return result;
        } else { // is directory
            File[] files = f.listFiles();
            boolean result = true;
            for (File file : files) {
                result = result && delete(file);
                if (!result) {
                    return false;
                }
            }
            return f.delete();
        }
    }

    /*
     * @return a random char A-Z, a-z
     */
    public static char randomChar(boolean bothCase) {
        int ret;
        int delta = (int) (Math.random() * 26);
        ret = 65 + delta;
        if (bothCase) {
            ret = ret + ((Math.random() > 0.5) ? 0 : 32);
        }
        return (char) (ret);
    }
}
