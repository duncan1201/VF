/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import com.gas.common.ui.IReclaimable;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.tree.TreePath;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import sun.swing.SwingUtilities2;

/**
 *
 * @author dunqiang
 */
public class UIUtil {

    private static Logger log = Logger.getLogger(UIUtil.class.getName());
    private static Object SandboxClipboardKey = new Object();

    /**
     * @param level 1-based
     */
    public static void expandTree(JTree tree, int level) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            TreePath treePath = tree.getPathForRow(i);
            if (!tree.isExpanded(treePath)) {
                int pathCount = treePath.getPathCount();
                if (pathCount <= level) {
                    tree.expandPath(treePath);
                }
            }
        }
    }
    
    public static int showDialog(JFileChooser fileChooser, Component parent){
        return showDialog(fileChooser, parent, null);
    }
    
    public static int showDialog(JFileChooser fileChooser, Component parent, String approveButtonText){
        UIUtil.setSystemLookAndFeel();
        fileChooser.updateUI();
        Dimension pSize = fileChooser.getPreferredSize();
        UIUtil.setPreferredWidth(fileChooser, Math.round(pSize.width * 1.51f));
        
        int ret = 0;
        if(approveButtonText != null && !approveButtonText.isEmpty()){
            ret = fileChooser.showDialog(parent, approveButtonText);
        }else{
            final int dialogType = fileChooser.getDialogType();
            if(dialogType == JFileChooser.OPEN_DIALOG){
                ret = fileChooser.showOpenDialog(parent);
            }else{
                ret = fileChooser.showSaveDialog(parent);
            }
            
        }
        
        UIUtil.setNimbusLookAndFeel();
        return ret;
    }

    public static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            log.severe(ex.toString());
        }
    }
    
    public static void setSizeToSmall(JComponent comp){
        comp.putClientProperty("JComponent.sizeVariant", "small");
    }
    
    public static void setSizeToMini(JComponent comp){
        comp.putClientProperty("JComponent.sizeVariant", "mini");
    }    

    public static void setNimbusLookAndFeel() {
        try {
            if (Utilities.isWindows()) {
                UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
                //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } else if (Utilities.isMac()) {
                UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
            }
            UIManager.put("ViewTabDisplayerUI", "com.gas.main.ui.NoTabsTabDisplayerUI");

        } catch (Exception e) {
            log.severe(e.toString());
        }
    }

    public static void simulateMousePress(final Point loc) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    final Point oldLoc = MouseInfo.getPointerInfo().getLocation();
                    Robot a = new Robot();
                    a.mouseMove(loc.x, loc.y);
                    a.mousePress(InputEvent.BUTTON1_MASK);
                    a.mouseMove(oldLoc.x, oldLoc.y);
                } catch (AWTException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
    }

    public static void attachAction(JComponent comp, KeyStroke keyStroke, Action action) {
        InputMap inputMap = comp.getInputMap();
        inputMap.put(keyStroke, action.getClass().getName());

        ActionMap actionMap = comp.getActionMap();
        actionMap.put(action.getClass().getName(), action);

    }

    public static int[] convertRowIndexToModel(JTable table, int[] rows) {
        int[] ret = new int[rows.length];
        for (int i = 0; i < rows.length; i++) {
            int indexModel = table.convertRowIndexToModel(rows[i]);
            ret[i] = indexModel;
        }
        return ret;
    }

    public static boolean areShowing(Component[] comps) {
        for (Component comp : comps) {
            if (!comp.isShowing()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param paperLength in 1/72nds of an inch
     */
    public static double paperLengthToPixel(double paperLength) {
        double ret = paperLength / 72 * getScreenResolution();
        return ret;
    }
    private static Map<Object, Dimension> preferredWidthsSpinner = new HashMap<Object, Dimension>();
    private static Map<Object, Dimension> preferredWidthsCombo = new HashMap<Object, Dimension>();
    private static Map<Object, Dimension> preferredWidthsTextField = new HashMap<Object, Dimension>();
    private static Map<Object, Dimension> preferredWidthsLabel = new HashMap<Object, Dimension>();
    private static Map<Object, Dimension> preferredWidthsList = new HashMap<Object, Dimension>();

    private static Dimension getPreferredWidth(Object prototype, Class clazz) {
        Dimension ret = null;
        if (JSpinner.class.isAssignableFrom(clazz)) {
            if (!preferredWidthsSpinner.containsKey(prototype)) {
                JSpinner tmp = new JSpinner();
                tmp.setValue(prototype);
                Dimension size = tmp.getPreferredSize();
                preferredWidthsSpinner.put(prototype, size);
            }
            ret = preferredWidthsSpinner.get(prototype);
        } else if (JComboBox.class.isAssignableFrom(clazz)) {
            if (!preferredWidthsCombo.containsKey(prototype)) {
                JComboBox tmp = new JComboBox();
                tmp.addItem(prototype);
                Dimension size = tmp.getPreferredSize();
                preferredWidthsCombo.put(prototype, size);
            }
            ret = preferredWidthsCombo.get(prototype);
        } else if (JTextField.class.isAssignableFrom(clazz)) {
            if (!preferredWidthsTextField.containsKey(prototype)) {
                JTextField tmp = new JTextField();
                tmp.setText(prototype.toString());
                Dimension size = tmp.getPreferredSize();
                preferredWidthsTextField.put(prototype, size);
            }
            ret = preferredWidthsTextField.get(prototype);
        } else if (JLabel.class.isAssignableFrom(clazz)) {
            if (!preferredWidthsLabel.containsKey(prototype)) {
                JLabel tmp = new JLabel();
                tmp.setText(prototype.toString());
                Dimension size = tmp.getPreferredSize();
                preferredWidthsLabel.put(prototype, size);
            }
            ret = preferredWidthsLabel.get(prototype);
        } else if (JList.class.isAssignableFrom(clazz)) {
            if (!preferredWidthsList.containsKey(prototype)) {
                Object[] a = {prototype};
                JList tmp = new JList(a);
                Dimension size = tmp.getPreferredSize();
                preferredWidthsList.put(prototype, size);
            }
            ret = preferredWidthsList.get(prototype);
        } else {
            throw new UnsupportedOperationException();
        }
        return ret;
    }

    public static void enabledRecursively(Component c, boolean enabled) {
        c.setEnabled(enabled);
        if (c instanceof Container) {
            Container container = (Container) c;
            for (int i = 0; i < container.getComponentCount(); i++) {
                Component comp = container.getComponent(i);
                enabledRecursively(comp, enabled);
            }
        }
    }

    public static void setPreferredWidthByPrototype(JComponent comp, Object prototype) {
        if (comp instanceof JSpinner || comp instanceof JComboBox || comp instanceof JTextField || comp instanceof JLabel || comp instanceof JList) {
            Dimension size = getPreferredWidth(prototype, comp.getClass());
            setPreferredWidth(comp, size.width);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static PrintRequestAttributeSet getDefaultPrintRequestAttributeSet(PrintService printService) {
        PrintRequestAttributeSet ret = new HashPrintRequestAttributeSet();
        Class[] categories = printService.getSupportedAttributeCategories();
        for (Class c : categories) {
            if (PrintRequestAttribute.class.isAssignableFrom(c)) {
                PrintRequestAttribute pra = (PrintRequestAttribute) printService.getDefaultAttributeValue(c);
                if (pra != null) {
                    System.out.println(c + "\t" + pra);
                    ret.add(pra);
                }
            }
        }
        return ret;
    }

    public static int getScreenResolution() {
        return Toolkit.getDefaultToolkit().getScreenResolution();
    }

    public static List<Arc2D> createArcsByCenter(double x, double y, double radius,
            double angSt, double angExt, int closure) {
        List<Arc2D> ret = new ArrayList<Arc2D>();
        final int increment = angExt > 0 ? 1 : 1;
        for (double angle = angSt; containsArithmetically(angSt, angSt + angExt, angle); angle -= 2 * increment) {
            Arc2D.Float a = new Arc2D.Float();
            a.setArcByCenter(x, y, radius, angle, increment, closure);
            ret.add(a);
        }
        return ret;
    }

    public static Insets getDefaultVerticalInsets() {
        Insets insets = getDefaultInsets();
        insets.left = 0;
        insets.right = 0;
        return insets;
    }

    public static Insets getDefaultHorizontalInsets() {
        Insets insets = getDefaultInsets();
        insets.top = insets.bottom = 0;
        return insets;
    }

    public static Border createDefaultBorder() {
        Insets insets = getDefaultInsets();
        Border ret = BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right);
        return ret;
    }

    public static void setDefaultBorder(JComponent c) {
        c.setBorder(createDefaultBorder());
    }    
    
    public static Insets getDefaultInsets() {
        final FontMetrics fm = FontUtil.getDefaultFontMetrics();
        int fontHeight = fm.getHeight();
        Insets ret = new Insets(fontHeight / 4, fontHeight / 2, fontHeight / 4, fontHeight / 2);
        return ret;
    }

    public static Double getCenterAngle(double startAngle, double endAngle) {
        Double ret = null;
        startAngle = MathUtil.normalizeDegree(startAngle);
        endAngle = MathUtil.normalizeDegree(endAngle);

        double width = LocUtil.angleWidth(startAngle, endAngle);
        ret = startAngle + width * 0.5;
        ret = MathUtil.normalizeDegree(ret);
        return ret;
    }

    /**
     * @param startAngle in degrees
     * @param endAngle in degrees
     * @param angle in degrees
     * @return returns whether the angle is within the range indicated by the
     * parameters
     */
    private static boolean containsArithmetically(double startAngle, double endAngle, double angle) {
        boolean ret;
        if (startAngle <= endAngle) {
            ret = angle >= startAngle && angle <= endAngle;
        } else {
            ret = angle >= endAngle && angle <= startAngle;
        }
        return ret;
    }

    public static void showPopupMenu(JPopupMenu popup, Component invoker, int x, int y) {
        boolean isShowing = invoker.isShowing();
        if (isShowing) {
            popup.show(invoker, x, y);
        }
    }

    public static BufferedImage toImage(JComponent comp) {
        return toImage(comp, Transparency.TRANSLUCENT);
    }

    public static BufferedImage toImage(JComponent comp, int transparency) {
        return toImage(comp, new Dimension(comp.getWidth(), comp.getHeight()), transparency);
    }

    public static BufferedImage toImage(JComponent comp, Rectangle rect) {
        return toImage(comp, rect, Transparency.TRANSLUCENT);
    }

    public static BufferedImage toImage(JComponent comp, Rectangle rect, int transparency) {
        BufferedImage image = null;
        if (rect != null) {
            image = createCompatibleImage(rect.width, rect.height, transparency);
        } else {
            image = createCompatibleImage(comp.getWidth(), comp.getHeight(), transparency);
        }
        Graphics2D g = image.createGraphics();
        if (rect != null) {
            g.translate(-rect.x, -rect.y);
        }
        comp.paint(g);
        g.dispose();
        return image;
    }

    public static BufferedImage toImage(JComponent comp, Dimension sizeNew) {
        return toImage(comp, sizeNew, Transparency.TRANSLUCENT);
    }

    /**
     * create a scaled image
     */
    public static BufferedImage toImage(JComponent component, Dimension sizeNew, int transparency) {
        double scaleX = sizeNew.getWidth() / component.getWidth();
        double scaleY = sizeNew.getHeight() / component.getHeight();
        int width = MathUtil.round(component.getWidth() * scaleX);
        int height = MathUtil.round(component.getHeight() * scaleY);
        BufferedImage image = createCompatibleImage(width, height, transparency);
        AffineTransform at = AffineTransform.getScaleInstance(scaleX, scaleY);
        Graphics2D gImage = image.createGraphics();

        gImage.setTransform(at);

        component.paint(gImage);

        gImage.dispose();
        return image;
    }

    public static BufferedImage createCompatibleImage(int width, int height) {
        return createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    public static BufferedImage createCompatibleImage(int width, int height, int transparency) {
        GraphicsConfiguration configuration = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
        return configuration.createCompatibleImage(width, height, transparency);
    }

    public static Point2D getEastMiddle(GeneralPath path) {
        Rectangle2D rect = path.getBounds2D();
        double centerY = rect.getCenterY();
        double maxX = rect.getMaxX();
        Point2D ret = new Point2D.Double(maxX, centerY);
        return ret;
    }

    public static Point2D getWestMiddle(GeneralPath path) {
        Rectangle2D rect = path.getBounds2D();
        double centerY = rect.getCenterY();
        double minX = rect.getMinX();
        Point2D ret = new Point2D.Double(minX, centerY);
        return ret;
    }

    public static String toString(GeneralPath path) {
        StringBuilder ret = new StringBuilder();
        PathIterator itr = path.getPathIterator(null);
        ret.append(path.getBounds2D());
        ret.append('\n');
        while (!itr.isDone()) {
            double[] coords = new double[6];
            int type = itr.currentSegment(coords);
            if (type == PathIterator.SEG_CLOSE) {
                ret.append("CLOSE");
            } else if (type == PathIterator.SEG_CUBICTO) {
                ret.append("SEG_CUBICTO");
                ret.append('(');
                ret.append(coords[0]);
                ret.append(',');
                ret.append(coords[1]);
                ret.append(')');
                ret.append('(');
                ret.append(coords[2]);
                ret.append(',');
                ret.append(coords[3]);
                ret.append(')');
                ret.append('(');
                ret.append(coords[4]);
                ret.append(',');
                ret.append(coords[5]);
                ret.append(')');
            } else if (type == PathIterator.SEG_LINETO) {
                ret.append("LINETO");
                ret.append('(');
                ret.append(coords[0]);
                ret.append(',');
                ret.append(coords[1]);
                ret.append(')');
            } else if (type == PathIterator.SEG_MOVETO) {
                ret.append("MOVETO");
                ret.append('(');
                ret.append(coords[0]);
                ret.append(',');
                ret.append(coords[1]);
                ret.append(')');
            } else if (type == PathIterator.SEG_QUADTO) {
                ret.append("QUADTO");
                ret.append('(');
                ret.append(coords[0]);
                ret.append(',');
                ret.append(coords[1]);
                ret.append(')');
                ret.append('(');
                ret.append(coords[2]);
                ret.append(',');
                ret.append(coords[3]);
                ret.append(')');
            }
            ret.append('\n');
            itr.next();
        }
        return ret.toString();
    }

    public static void disableScrolling(JScrollPane s) {
        Action empty = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };
        s.getActionMap().put("unitScrollRight", empty);
        s.getActionMap().put("unitScrollLeft", empty);
        s.getActionMap().put("unitScrollUp", empty);
        s.getActionMap().put("unitScrollDown", empty);
    }

    public static void showPopupCenter(Component owner, Component contents, int delay) {
        int x, y;
        if (WindowManager.getDefault() != null && WindowManager.getDefault().getMainWindow() != null) {
            Frame frame = WindowManager.getDefault().getMainWindow();
            Point locOnScreen = frame.getLocationOnScreen();
            Dimension frameSize = frame.getSize();
            Dimension contentsSize = contents.getPreferredSize();
            x = Math.round(locOnScreen.x + frameSize.width * 0.5f - contentsSize.width * 0.5f);
            y = Math.round(locOnScreen.y + frameSize.height * 0.5f - contentsSize.height * 0.5f);
        } else {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension pSize = contents.getPreferredSize();
            x = Math.round(screenSize.width * 0.5f - pSize.width * 0.5f);
            y = Math.round(screenSize.height * 0.5f - pSize.height * 0.5f);
        }

        showPopup(owner, contents, x, y, delay);
    }

    public static void showPopup(Component owner, Component contents, int x, int y, int delay) {
        PopupFactory factory = PopupFactory.getSharedInstance();
        final Popup popup = factory.getPopup(owner, contents, x, y);
        popup.show();
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popup.hide();
            }
        });
        timer.start();
    }

    public static Polygon rect2Polygon(Rectangle2D rect) {
        Polygon ret = new Polygon();

        ret.addPoint(MathUtil.round(rect.getMinX()), MathUtil.round(rect.getMinY()));
        ret.addPoint(MathUtil.round(rect.getMinX()), MathUtil.round(rect.getMaxY()));
        ret.addPoint(MathUtil.round(rect.getMaxX()), MathUtil.round(rect.getMaxY()));
        ret.addPoint(MathUtil.round(rect.getMaxX()), MathUtil.round(rect.getMinY()));


        return ret;
    }

    public static Rectangle transform(AffineTransform at, Rectangle rect, boolean sameSize) {
        Rectangle ret = (Rectangle) rect.clone();
        if (sameSize) {
            Point loc = rect.getBounds().getLocation();
            loc = transform(at, loc);
            ret.setRect(loc.x, loc.y, ret.getWidth(), ret.getHeight());
        } else {
            Point2D newP1 = transform(at, new Point2D.Double(ret.getX(), ret.getY()));
            Point2D newP2 = transform(at, new Point2D.Double(ret.getMaxX(), ret.getMaxY()));
            ret.setFrameFromDiagonal(newP1, newP2);
        }
        return ret;
    }

    public static Rectangle2D transform(AffineTransform at, Rectangle2D rect, boolean sameSize) {
        Rectangle2D ret = (Rectangle2D) rect.clone();
        if (sameSize) {
            Point loc = rect.getBounds().getLocation();
            loc = transform(at, loc);
            ret.setRect(loc.x, loc.y, ret.getWidth(), ret.getHeight());
        } else {
            Point2D newP1 = transform(at, new Point2D.Double(ret.getX(), ret.getY()));
            Point2D newP2 = transform(at, new Point2D.Double(ret.getMaxX(), ret.getMaxY()));
            ret.setFrameFromDiagonal(newP1, newP2);
        }
        return ret;
    }

    public static Polygon transform(AffineTransform at, Polygon polygon) {
        Polygon ret = new Polygon();
        float[] srcPts = new float[polygon.npoints * 2];
        float[] dstPts = new float[polygon.npoints * 2];
        for (int i = 0; i < polygon.npoints; i++) {
            int x = polygon.xpoints[i];
            int y = polygon.ypoints[i];
            srcPts[i * 2] = x;
            srcPts[i * 2 + 1] = y;
        }
        at.transform(srcPts, 0, dstPts, 0, polygon.npoints);
        for (int i = 0; i < polygon.npoints; i++) {
            ret.addPoint(Math.round(dstPts[i * 2]), Math.round(dstPts[i * 2 + 1]));
        }
        return ret;
    }

    public static Point transform(AffineTransform at, Point ptSrc) {
        Point ret = new Point();
        at.transform(ptSrc, ret);
        return ret;
    }

    public static Line2D.Double translate(Line2D line, double dx, double dy) {
        Line2D.Double ret = new Line2D.Double();
        final Point2D p1 = line.getP1();
        final Point2D p2 = line.getP2();
        ret.setLine(p1.getX() + dx, p1.getY() + dy, p2.getX() + dx, p2.getY() + dy);
        return ret;
    }

    public static Point2D transform(AffineTransform at, Point2D p) {
        Point2D.Float ret = new Point2D.Float();
        at.transform(p, ret);
        return ret;
    }

    public static Point2D.Double transform(AffineTransform at, Point2D.Double p) {
        Point2D.Double ret = new Point2D.Double();
        at.transform(p, ret);
        return ret;
    }

    public static void toFile(RenderedImage image, String format, File file) {
        try {
            ImageIO.write(image, format, file);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static Dimension getSize(Object v, Class clazz) {
        Dimension ret = null;
        if (JSpinner.class.isAssignableFrom(clazz)) {
            JSpinner spinner = new JSpinner();
            spinner.setValue(v);
            ret = spinner.getPreferredSize();
        } else if (JComboBox.class.isAssignableFrom(clazz)) {
            JComboBox comboBox = new JComboBox(new Object[]{v});
            ret = comboBox.getPreferredSize();
        } else if (JTextField.class.isAssignableFrom(clazz)) {
            JTextField textField = new JTextField();
            textField.setText(v.toString());
            ret = textField.getPreferredSize();
        } else {
            throw new UnsupportedOperationException();
        }
        return ret;
    }

    public static void calibrateTableColumnWidth(Object[] prototypes, JTable table) {

        TableColumn column;

        final FontMetrics fm = FontUtil.getFontMetrics(table);

        for (int col = 0; col < table.getColumnModel().getColumnCount(); col++) {
            column = table.getColumnModel().getColumn(col);

            int cellWidth = fm.stringWidth(prototypes[col].toString());
            column.setPreferredWidth(cellWidth);
        }
    }

    public static void makeWidthSame(JComponent... comps) {
        int maxWidth = Integer.MIN_VALUE;
        for (JComponent comp : comps) {
            int width = comp.getPreferredSize().width;
            if (maxWidth < width) {
                maxWidth = width;
            }
        }
        for (JComponent comp : comps) {
            UIUtil.setPreferredWidth(comp, maxWidth);
        }
    }

    public static void initColumnSizes(JTable table) {
        TableModel model = table.getModel();
        TableColumn column = null;
        Component comp = null;
        int headerWidth = 0;
        int cellWidth = 0;
        int maxCellWidth = 0;
        TableCellRenderer headerRenderer =
                table.getTableHeader().getDefaultRenderer();

        for (int col = 0; col < table.getColumnModel().getColumnCount(); col++) {
            column = table.getColumnModel().getColumn(col);

            comp = headerRenderer.getTableCellRendererComponent(
                    null, column.getHeaderValue(),
                    false, false, 0, 0);
            headerWidth = comp.getPreferredSize().width;

            maxCellWidth = 0;
            for (int row = 0; row < model.getRowCount(); row++) {
                comp = table.getDefaultRenderer(model.getColumnClass(col)).
                        getTableCellRendererComponent(
                        table, model.getValueAt(0, col),
                        false, false, 0, col);
                cellWidth = comp.getPreferredSize().width;
                maxCellWidth = Math.max(maxCellWidth, cellWidth);
            }


            column.setPreferredWidth(Math.max(headerWidth, maxCellWidth));
        }
    }

    public static void removeChild(Container container) {

        if (container == null) {
            return;
        }

        if (container instanceof IReclaimable) {
            ((IReclaimable) container).cleanup();
        }

        removeListeners(container);

        while (container.getComponentCount() > 0) {
            Component childComp = container.getComponent(container.getComponentCount() - 1);

            if (childComp instanceof Container) {
                Container _c = (Container) childComp;
                removeChild(_c);
                removeListeners(_c);
            }
            if (childComp instanceof AbstractButton) {
                removeListeners((AbstractButton) childComp);
            }
            if (childComp instanceof IReclaimable) {
                IReclaimable r = (IReclaimable) childComp;
                r.cleanup();
            }
            if (childComp instanceof AWTEventListener) {
                Toolkit.getDefaultToolkit().removeAWTEventListener((AWTEventListener) childComp);
            }
            if (childComp instanceof KeyEventDispatcher) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher((KeyEventDispatcher) childComp);
            }
            container.remove(container.getComponentCount() - 1);
        }
    }

    public static void removeListeners(Container container) {

        for (int i = 0; i < container.getComponentListeners().length; i++) {
            container.removeComponentListener(container.getComponentListeners()[i]);
        }

        MouseListener[] mls = container.getMouseListeners();
        for (int i = 0; i < mls.length; i++) {
            container.removeMouseListener(mls[i]);
        }

        MouseMotionListener[] mmls = container.getMouseMotionListeners();
        for (int i = 0; i < mmls.length; i++) {
            container.removeMouseMotionListener(mmls[i]);
        }

        MouseWheelListener[] mwls = container.getMouseWheelListeners();
        for (int i = 0; i < mwls.length; i++) {
            container.removeMouseWheelListener(mwls[i]);
        }

        for (int i = 0; i < container.getPropertyChangeListeners().length; i++) {
            container.removePropertyChangeListener(container.getPropertyChangeListeners()[i]);
        }
    }

    public static void removeActionListeners(AbstractButton btn) {
        ActionListener[] als = btn.getActionListeners();
        for (int i = 0; i < als.length; i++) {
            btn.removeActionListener(als[i]);
        }
    }

    private static int removeListeners(AbstractButton btn) {
        int ret = 0;
        removeActionListeners(btn);
        ChangeListener[] cls = btn.getChangeListeners();
        for (int i = 0; i < cls.length; i++) {
            btn.removeChangeListener(cls[i]);
        }
        if (btn.getChangeListeners().length > 0) {
            throw new IllegalStateException("Cannot remove ChangeListeners");
        }
        MouseListener[] mls = btn.getMouseListeners();
        for (int i = 0; i < mls.length; i++) {
            btn.removeMouseListener(mls[i]);
        }
        if (btn.getMouseListeners().length > 0) {
            throw new IllegalStateException("Cannot remove MouseListeners");
        }
        MouseMotionListener[] mmls = btn.getMouseMotionListeners();
        for (int i = 0; i < mmls.length; i++) {
            btn.removeMouseMotionListener(mmls[i]);
        }
        if (btn.getMouseMotionListeners().length > 0) {
            throw new IllegalStateException("Cannot remove MouseMotionListener");
        }
        MouseWheelListener[] mwls = btn.getMouseWheelListeners();
        for (int i = 0; i < mwls.length; i++) {
            btn.removeMouseWheelListener(mwls[i]);
        }
        if (btn.getMouseWheelListeners().length > 0) {
            throw new IllegalStateException("Cannot remove MouseWheelListener");
        }
        ItemListener[] ils = btn.getItemListeners();
        for (int i = 0; i < ils.length; i++) {
            btn.removeItemListener(ils[i]);
        }
        if (btn.getItemListeners().length > 0) {
            throw new IllegalStateException("Cannot remove ItemListeners");
        }
        return ret;
    }

    public static void invokeAndWait(Runnable runnable) {
        try {
            SwingUtilities.invokeAndWait(runnable);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static void doLayout(Container container) {
        LayoutManager layoutManager = container.getLayout();
        if (layoutManager != null) {
            layoutManager.layoutContainer(container);
        }
    }

    public static ComponentUI getUI(JComponent comp, Class<? extends BasicLookAndFeel> clazz) {
        ComponentUI ret = null;
        try {
            BasicLookAndFeel laf = (BasicLookAndFeel) clazz.newInstance();
            ret = laf.getDefaults().getUI(comp);
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    public static KeyEvent toKeyEvent(Component src, KeyStroke keyStroke) {
        KeyEvent ret = new KeyEvent(src, KeyEvent.KEY_PRESSED, Calendar.getInstance().getTimeInMillis(), keyStroke.getModifiers(), keyStroke.getKeyCode(), keyStroke.getKeyChar());
        return ret;
    }

    public static Point getLocOnScreen(Component comp) {
        Point ret = null;
        if (comp.isShowing()) {
            ret = comp.getLocationOnScreen();
        }
        return ret;
    }

    public static Point getLoc(Component comp) {
        Point ret = null;
        if (comp.isShowing()) {
            ret = comp.getLocation();
        }
        return ret;
    }

    public static void removeComponentsByClass(Container container, Class clazz) {
        Component[] comps = container.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (clazz.isAssignableFrom(comps[i].getClass())) {
                container.remove(comps[i]);
            }
        }
    }

    public static Integer toScreenWidth(int totalScreenLength, int totalPos, int pos) {
        return toScreenWidth(totalScreenLength, totalPos, pos, Integer.class);
    }

    public static Dimension wider(Dimension d1, Dimension d2) {
        Dimension ret = null;
        ret = d1.getWidth() > d2.getWidth() ? d1 : d2;
        return ret;
    }

    public static Dimension widest(JComponent... cs) {
        List<Dimension> ds = new ArrayList<Dimension>();
        for (JComponent c : cs) {
            ds.add(c.getPreferredSize());
        }
        return widest(ds);
    }

    public static Dimension widest(List<Dimension> sizes) {
        Dimension ret = null;
        for (Dimension s : sizes) {
            if (ret == null || ret.width < s.width) {
                ret = s;
            }
        }
        return ret;
    }

    public static Integer toScreenWidth(Rectangle displayRect, int totalPos, int pos) {
        return toScreenWidth(displayRect.width, totalPos, pos, Integer.class) + displayRect.x;
    }

    public static Frame getFrame(Component c) {
        Component w = c;

        while (!(w instanceof Frame) && (w != null)) {
            w = w.getParent();
        }
        return (Frame) w;
    }

    public static Rectangle getBoundsOnScreen(Component comp) {
        Rectangle ret = null;
        if (comp != null && comp.isVisible() && comp.isShowing()) {
            Point p = comp.getLocationOnScreen();
            Dimension size = comp.getSize();
            ret = new Rectangle(p, size);
        }
        return ret;
    }

    public static boolean contains(double xDouble, double yDouble, double width, double height, Point p) {
        int w = (int) Math.round(width);
        int h = (int) Math.round(height);
        if ((w | h) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if either dimension is zero, tests below must return false...
        int x = (int) Math.round(xDouble);
        int y = (int) Math.round(yDouble);
        if (p.x < x || p.y < y) {
            return false;
        }
        w += x;
        h += y;
        //    overflow || intersect
        return ((w < x || w > p.x)
                && (h < y || h > p.y));
    }

    public static boolean contains(Rectangle rect, Point p) {
        boolean ret = false;
        if (rect != null && p != null && rect.contains(p)) {
            ret = true;
        }
        return ret;
    }

    public static <T> T findTopComponent(final String tcID, final Class<T> clazz) {
        boolean isEDT = SwingUtilities.isEventDispatchThread();
        TopComponent ret = null;
        if (!isEDT) {

            FutureTask<TopComponent> future = new FutureTask<TopComponent>(
                    new Callable<TopComponent>() {
                @Override
                public TopComponent call() {
                    return (TopComponent) findTopComponent(tcID, clazz);
                }
            });
            UIUtil.invokeAndWait(future);
            try {
                ret = future.get();
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            } catch (ExecutionException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            ret = WindowManager.getDefault().findTopComponent(tcID);
        }
        return (T) ret;
    }

    public static <T> T toScreen(int totalPos, int pos, int totalWidth, int leftInset, int alignment, Class<T> clazz) {
        T ret = null;
        Float retFloat = toScreen(totalPos, pos, totalWidth, leftInset, alignment);
        if (Integer.class.isAssignableFrom(clazz)) {
            ret = (T) MathUtil.round(retFloat);
        } else if (Float.class.isAssignableFrom(clazz)) {
            ret = (T) retFloat;
        } else {
        }
        return ret;
    }

    public static Mode getEditorMode() {
        return WindowManager.getDefault().findMode("editor");
    }

    /*
     * @ret 1-based postion
     */
    public static int toPos(double x, double screenWidth, double totalPos) {
        int ret = 0;
        double retFloat = x * totalPos * 1.0f / screenWidth;
        ret = (int) Math.round(retFloat + 1);
        return ret;
    }

    /*
     * @param alignment (SwingConstants.RIGHT/LEFT/CENTER)
     */
    public static float toScreen(int totalPos, int pos, float totalLength, int leftInset, int alignment) {
        Float[] ret = new Float[2];
        ret[0] = UIUtil.toScreenWidthFloat(totalLength, totalPos, pos - 1) + leftInset + 1;
        ret[1] = UIUtil.toScreenWidthFloat(totalLength, totalPos, pos) + leftInset;

        if (alignment == SwingConstants.CENTER) {
            return MathUtil.avg(ret, Float.class);
        } else if (alignment == SwingConstants.LEFT) {
            return ret[0];
        } else if (alignment == SwingConstants.RIGHT) {
            return ret[1];
        } else {
            throw new IllegalArgumentException("SwingConstants.CENTER/LEFT/RIGHT only");
        }
    }

    /*
     * @param relativePos: 1-based
     */
    public static float toScreen(int totalPos, int pos, Rectangle displayRect, int alignment) {
        return toScreen(totalPos, pos, displayRect.width, displayRect.x, alignment);
    }

    public static Boolean isDescendantOfClass(Component src, Class clazz) {
        Boolean ret = false;
        if (clazz.isAssignableFrom(src.getClass())) {
            return true;
        }
        Container parent = src.getParent();
        while (parent != null) {
            if (clazz.isAssignableFrom(parent.getClass())) {
                ret = true;
                break;
            }
            parent = parent.getParent();
        }
        return ret;
    }

    public static void removeComponentsInLayer(JLayeredPane pane, int... layers) {
        for (int layer : layers) {
            Component[] comp = pane.getComponentsInLayer(layer);
            for (Component c : comp) {
                pane.remove(c);
            }
        }
    }

    public static void removeComponentsInLayer(JLayeredPane pane, int layer, Class clazz) {
        Component[] comp = pane.getComponentsInLayer(layer);
        for (Component c : comp) {
            if (c.getClass().isAssignableFrom(clazz)) {
                pane.remove(c);
            }
        }
    }

    public static List<Component> getComponentsInLayer(JLayeredPane pane, int layer) {
        Component[] comp = pane.getComponentsInLayer(layer);
        List<Component> ret = Arrays.asList(comp);
        return ret;
    }

    public static <T> List<T> getComponentsInLayer(JLayeredPane pane, int layer, Class<T> clazz) {
        List<T> ret = new ArrayList<T>();
        Component[] comp = pane.getComponentsInLayer(layer);
        for (Component c : comp) {
            if (clazz != null) {
                if (c.getClass().isAssignableFrom(clazz)) {
                    ret.add((T) c);
                }
            }
        }
        return ret;
    }

    public static Boolean isWithin(JComponent c, Point ptOnScreen) {
        Boolean ret = null;
        Point locOnScreen = getLocOnScreen(c);
        if (locOnScreen != null) {
            Rectangle bounds = c.getBounds();
            bounds.setLocation(locOnScreen);
            ret = bounds.contains(ptOnScreen);
        }
        return ret;

    }

    public static boolean isOccupied(Container c, Rectangle rect) {
        boolean ret = false;
        Point loc = rect.getLocation();
        Point upperRight = new Point(loc);
        upperRight.translate(rect.width - 1, 0);

        Point lowerLeft = new Point(loc);
        lowerLeft.translate(0, rect.height - 1);

        Point lowerRight = new Point(loc);
        lowerRight.translate(rect.width - 1, rect.height - 1);

        if (c.getComponentAt(loc) != c) {
            ret = true;
        } else if (c.getComponentAt(upperRight) != c) {
            ret = true;
        } else if (c.getComponentAt(lowerLeft) != c) {
            ret = true;
        } else if (c.getComponentAt(lowerRight) != c) {
            ret = true;
        }
        return ret;
    }

    public static <T> T toScreenWidth(int totalScreenLength, int totalPos, int pos, Class<T> clazz) {

        T ret = null;
        if (totalScreenLength >= 0) {
            Float retFloat = totalScreenLength * pos * 1.0f / totalPos;

            if (clazz.isAssignableFrom(Integer.class)) {
                retFloat = Math.min(retFloat, totalScreenLength - 1); // cannot be off bigger than the total screen length
                retFloat = Math.max(0, retFloat); // cannot be less than 0                
                ret = MathUtil.round(retFloat, clazz);

            } else if (clazz.isAssignableFrom(Float.class)) {
                retFloat = Math.max(0, retFloat); // cannot be less than 0                
                ret = (T) retFloat;
            } else {
                throw new IllegalArgumentException(String.format("Class '%s' not supported!", clazz.getName()));
            }
        }
        return ret;
    }

    public static boolean containsY(Rectangle rect, int y) {
        return y >= rect.y && y <= rect.y + rect.height - 1;
    }

    public static Rectangle shrink(Rectangle rect, double shrinkX, double shrinkY) {
        Rectangle ret = new Rectangle(rect);
        ret.x += shrinkX;
        ret.width -= shrinkX * 2;
        ret.y += shrinkY;
        ret.height -= shrinkY * 2;
        return ret;
    }

    public static Rectangle2D shrink(Rectangle2D rect, double shrinkX, double shrinkY) {
        Rectangle2D ret = (Rectangle2D) rect.clone();
        ret.setRect(rect.getMinX() + shrinkX,
                rect.getMinY() + shrinkY,
                rect.getWidth() - shrinkX * 2,
                rect.getHeight() - shrinkY * 2);

        return ret;
    }

    public static float inch2Pixels(float inch) {
        int resolution = getScreenResolution();
        return inch * resolution;
    }

    public static float toCentimeter(int pixels) {
        float ret = 0;
        int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        float dotPerCM = dpi / 2.54f;
        ret = pixels / dotPerCM;
        return ret;
    }

    public static Float toScreenWidthFloat(float totalScreenLength, int totalPos, Integer pos) {
        Float ret = null;
        if (pos != null) {
            ret = totalScreenLength * pos * 1.0f / totalPos;

            ret = Math.min(ret, totalScreenLength); // cannot be bigger than the total screen length
            ret = Math.max(0, ret); // cannot be less than 0
        }
        return ret;
    }

    public static Double toDegree(int pos, int totalPos, int alignment) {
        return toDegree(pos, totalPos, alignment, Double.class);
    }

    public static <T> T toDegree(int pos, int totalPos, int alignment, Class<T> retType) {
        T ret = null;
        double prev = 0;
        double cur = 0;
        Double retDouble = null;
        prev = 360.0 * (pos - 1) / totalPos;
        cur = 360.0 * pos / totalPos;
        if (alignment == SwingConstants.CENTER) {
            retDouble = (cur + prev + 1) * .5;
        } else if (alignment == SwingConstants.RIGHT) {
            retDouble = cur;
        } else if (alignment == SwingConstants.LEFT) {
            retDouble = prev;
        } else {
            throw new IllegalArgumentException(String.format("Alignment '%d' not supported", alignment));
        }
        if (retType.isAssignableFrom(Double.class)) {
            ret = (T) retDouble;
        } else if (retType.isAssignableFrom(Float.class)) {
            Float tmp = retDouble.floatValue();
            ret = (T) tmp;
        } else if (retType.isAssignableFrom(Integer.class)) {
            Integer tmp = MathUtil.round(retDouble);
            ret = (T) tmp;
        } else {
            throw new IllegalArgumentException(String.format("class '%s' not supported", retType.getName()));
        }
        return ret;
    }

    public static boolean containsDataFlavor(DataFlavor dataFlavor) {
        DataFlavor[] dfs = getClipboard().getAvailableDataFlavors();
        for (DataFlavor df : dfs) {
            if (df == dataFlavor) {
                return true;
            }
        }
        return false;
    }

    public static void clearClipboard() {
        try {
            getClipboard().setContents(new Transferable() {
                @Override
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[0];
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return false;
                }

                @Override
                public Object getTransferData(DataFlavor flavor) {
                    return null;
                }
            }, null);
        } catch (IllegalStateException e) {
        }
    }

    public static Clipboard getClipboard() {
        if (SwingUtilities2.canAccessSystemClipboard()) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            return toolkit.getSystemClipboard();
        }
        Clipboard clipboard = (Clipboard) sun.awt.AppContext.getAppContext().
                get(SandboxClipboardKey);
        if (clipboard == null) {
            clipboard = new Clipboard("Sandboxed Component Clipboard");
            sun.awt.AppContext.getAppContext().put(SandboxClipboardKey,
                    clipboard);
        }
        return clipboard;
    }

    public static void setTopCompHtmlDisplayName(final TopComponent tc, final String htmlDisplayName) {
        if (SwingUtilities.isEventDispatchThread()) {
            tc.setHtmlDisplayName(htmlDisplayName);
        } else {
            UIUtil.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    setTopCompHtmlDisplayName(tc, htmlDisplayName);
                }
            });
        }
    }

    public static void styleTopCompName(final TopComponent tc, final boolean bold, final boolean italic) {
        String PLAIN = "<html>%s</html>";
        String BOLD = "<html><b>%s</b></html>";
        String ITALIC = "<html><i>%s</i></html>";
        String BOLD_ITALIC = "<html><i><b>%s</b></i></html>";
        if (SwingUtilities.isEventDispatchThread()) {
            String name = tc.getName();
            if (name == null || name.isEmpty()) {
                return;
            }
            String format;
            if (bold && italic) {
                format = BOLD_ITALIC;
            } else if (bold) {
                format = BOLD;
            } else if (italic) {
                format = ITALIC;
            } else {
                format = PLAIN;
            }
            tc.setHtmlDisplayName(String.format(format, name));
        } else {
            UIUtil.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    styleTopCompName(tc, bold, italic);
                }
            });
        }
    }

    public static void setTopCompName(final TopComponent tc, final String name) {
        if (SwingUtilities.isEventDispatchThread()) {
            tc.setName(name);
        } else {
            UIUtil.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    setTopCompName(tc, name);
                }
            });
        }
    }

    public static void setTopCompIcon(final TopComponent tc, final Image icon) {
        if (SwingUtilities.isEventDispatchThread()) {
            tc.setIcon(icon);
        } else {
            UIUtil.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    setTopCompIcon(tc, icon);
                }
            });
        }
    }

    public static TopComponent getSelectedEditor() {
        TopComponent ret = null;
        Mode mode = getEditorMode();
        if (mode != null) {
            ret = mode.getSelectedTopComponent();
        }
        return ret;
    }

    public static TopComponent[] getOpenEditors() {
        Mode mode = getEditorMode();
        return mode.getTopComponents();
    }

    public static void setTopCompToolTip(final TopComponent tc, final String toolTip) {
        if (SwingUtilities.isEventDispatchThread()) {
            tc.setToolTipText(toolTip);
        } else {
            UIUtil.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    setTopCompToolTip(tc, toolTip);
                }
            });

        }
    }

    public static <C> C getParent(Component child, Class<C> clazz) {
        C ret = null;
        if (clazz.isAssignableFrom(child.getClass())) {
            ret = (C) child;
            return ret;
        }
        Container current = null;
        while (ret == null) {
            if (current == null) {
                current = child.getParent();
            } else {
                current = current.getParent();
            }
            if (current == null) {
                break;
            } else if (clazz.isAssignableFrom(current.getClass())) {
                ret = (C) current;
            }
        }
        return ret;
    }

    public static <C> C getChild(Container c, Class<C> clazz) {
        C ret = null;
        int count = c.getComponentCount();
        for (int i = 0; i < count; i++) {
            Component comp = c.getComponent(i);
            if (clazz.isAssignableFrom(comp.getClass())) {
                ret = (C) comp;
                break;
            } else if (comp instanceof Container) {
                ret = getChild((Container) comp, clazz);
                if (ret != null) {
                    break;
                }
            }
        }
        return ret;
    }

    public static JMenuItem getMenuItem(JPopupMenu p, Class clazz) {
        JMenuItem ret = null;
        List<JMenuItem> menuItems = UIUtil.getMenuItems(p);
        for (JMenuItem menuItem : menuItems) {
            Action action = menuItem.getAction();
            if (action != null && clazz.isAssignableFrom(action.getClass())) {
                ret = menuItem;
                break;
            }
        }
        return ret;
    }

    public static <T> T getAction(JPopupMenu p, Class<T> clazz) {
        T ret = null;
        List<JMenuItem> menuItems = UIUtil.getMenuItems(p);
        for (JMenuItem menuItem : menuItems) {
            Action action = menuItem.getAction();
            if (action != null && clazz.isAssignableFrom(action.getClass())) {
                ret = (T) action;
                break;
            }
        }
        return ret;
    }

    public static List<JMenuItem> getMenuItems(JPopupMenu p) {
        List<JMenuItem> ret = new ArrayList<JMenuItem>();
        MenuElement[] elts = p.getSubElements();
        for (MenuElement ele : elts) {
            if (ele instanceof JMenu) {
                JMenu menu = (JMenu) ele;
                ret.addAll(getMenuItems(menu.getPopupMenu()));
            } else if (ele instanceof JMenuItem) {
                ret.add((JMenuItem) ele);
            }
        }
        return ret;
    }

    public static <C extends JComponent> List<C> getChildren(Container c, Class<C> clazz) {
        List<C> ret = new ArrayList<C>();
        int count = c.getComponentCount();
        for (int i = 0; i < count; i++) {
            Component comp = c.getComponent(i);
            if (clazz.isAssignableFrom(comp.getClass())) {
                ret.add((C) comp);
                if (comp instanceof Container) {
                    ret.addAll(getChildren((Container) comp, clazz));
                }
            } else if (comp instanceof Container) {
                ret.addAll(getChildren((Container) comp, clazz));
            }
        }
        return ret;
    }

    public static Rectangle getBoundsNoBorder(Container container) {
        Rectangle ret = new Rectangle();
        Rectangle bounds = container.getBounds();
        Insets insets = container.getInsets();
        ret.x = insets.left;
        ret.y = insets.top;
        ret.width = bounds.width - insets.left - insets.right;
        ret.height = bounds.height - insets.top - insets.bottom;
        return ret;
    }

    public static Point getUpperRight(Rectangle rect) {
        Point ret = new Point();
        ret.x = rect.x + rect.width + 1;
        ret.y = rect.y;
        return ret;
    }

    public static Point convertPointFromScreen(Point pt, Component c) {
        Point ret = new Point(pt);
        SwingUtilities.convertPointFromScreen(ret, c);
        return ret;
    }

    public static Rectangle convertRect(Component source, Rectangle rect, Component destination) {
        Rectangle2D rec2D = convertRect(source, new Rectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()), destination);
        Rectangle ret = new Rectangle();
        ret.setRect(rec2D.getX(), rec2D.getY(), rec2D.getWidth(), rec2D.getHeight());
        return ret;
    }

    public static Rectangle2D convertRect(Component source, Rectangle2D rect, Component destination) {
        double x = rect.getX();
        double y = rect.getY();
        Point newPoint = SwingUtilities.convertPoint(source, MathUtil.round(x), MathUtil.round(y), destination);
        Rectangle2D ret = (Rectangle2D) rect.clone();
        ret.setFrame(newPoint.x, newPoint.y, ret.getWidth(), ret.getHeight());
        return ret;
    }

    public static Rectangle convertRectToScreen(Rectangle rect, Component comp) {
        Point loc = rect.getLocation();
        SwingUtilities.convertPointToScreen(loc, comp);
        return new Rectangle(loc, rect.getSize());
    }

    public static Point convertScreenLocationToParent(Container parent, int x, int y) {
        for (Container p = parent; p != null; p = p.getParent()) {
            if (p instanceof Window) {
                Point point = new Point(x, y);

                SwingUtilities.convertPointFromScreen(point, parent);
                return point;
            }
        }
        throw new Error("convertScreenLocationToParent: no window ancestor");
    }

    public static void setTopInsets(JComponent c, int top) {
        Insets insets = c.getInsets();
        insets.top = top;
        c.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
    }

    public static void setLeftInsets(JComponent c, int left) {
        Insets insets = c.getInsets();
        insets.left = left;
        c.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
    }

    public static void setRightInsets(JComponent c, int right) {
        Insets insets = c.getInsets();
        insets.right = right;
        c.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
    }

    public static void setHorizontalInsets(JComponent c, int width) {
        Insets insets = c.getInsets();
        insets.right = width;
        insets.left = width;
        c.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
    }

    public static void setPreferredWidth(Component c, int width) {
        if (c == null) {
            return;
        }
        Dimension size = c.getPreferredSize();
        if (size == null || size.width != width) {
            size.width = width;
            c.setPreferredSize(size);
        }
    }

    public static void setWidth(Component c, int width) {
        Dimension size = c.getSize();
        size.width = width;
        c.setSize(size);
    }

    public static void setX(Component c, int x) {
        int oldX = c.getX();
        if (x != oldX) {
            c.setLocation(x, c.getY());
        }
    }

    public static void setY(Component c, int y) {
        int oldY = c.getY();
        if (oldY != y) {
            c.setLocation(c.getX(), y);
        }
    }

    public static void setBackground(Container container, final Color color, Class clazz) {
        int count = container.getComponentCount();
        if (clazz.isAssignableFrom(container.getClass())) {
            container.setBackground(color);
        }
        for (int i = 0; i < count; i++) {
            Component child = container.getComponent(i);
            if (child instanceof Container) {
                setBackground((Container) child, color, clazz);
            }
        }
    }

    public static void setHeight(Component c, int height) {
        Dimension size = c.getSize();
        size.height = height;
        c.setSize(size);
    }

    public static void setMaximumWidth(JComponent c, int width) {
        Dimension size = c.getMaximumSize();
        size.width = width;
        c.setMaximumSize(size);
    }

    public static void setMaximumHeight(JComponent c, int height) {
        Dimension size = c.getMaximumSize();
        size.height = height;
        c.setMaximumSize(size);
    }

    public static void setPreferredHeight(Component c, int height) {
        Dimension size = c.getPreferredSize();
        if (size == null || size.height != height) {
            Dimension newSize = new Dimension(size);
            newSize.height = height;
            c.setPreferredSize(newSize);
        }
    }
}
