/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.print;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.editor.IPrintEditor;
import com.gas.domain.ui.editor.PrintParam;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.print.PrinterJob;
import java.lang.ref.WeakReference;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author dq
 */
public class CtrlPanel extends JPanel {

    private WeakReference<JButton> prevBtnRef;
    private WeakReference<JButton> nextBtnRef;
    private WeakReference<JLabel> pageLabelRef;
    private WeakReference<JRadioButton> portraitBtnRef;
    private WeakReference<JRadioButton> landscapeBtnRef;
    private WeakReference<JCheckBox> visibleAreaBoxRef;
    private WeakReference<JSpinner> zoomSpinnerRef;
    private WeakReference<JCheckBox> printDateBoxRef;
    private WeakReference<JCheckBox> printNameBoxRef;
    private WeakReference<JCheckBox> printPageNoBoxRef;
    private WeakReference<JButton> printDialogBtnRef;
    private WeakReference<IPrintEditor> editorRef;
    private PrintParam printParam;
    private PrinterJob printerJob;

    public CtrlPanel(IPrintEditor editor) {
        editorRef = new WeakReference<IPrintEditor>(editor);
        initComponents();
        hookupListeners();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new CtrlPanelListeners.PtyListener());
        getNextBtn().addActionListener(new CtrlPanelListeners.NextPrevListener());
        getPrevBtn().addActionListener(new CtrlPanelListeners.NextPrevListener());
        getPortraitBtn().addActionListener(new CtrlPanelListeners.OrientationBtnListener());
        getLandscapeBtn().addActionListener(new CtrlPanelListeners.OrientationBtnListener());

        CtrlPanelListeners.ZoomListener zoomListener = new CtrlPanelListeners.ZoomListener();
        getZoomSpinner().addChangeListener(zoomListener);

        getPrintDateBox().addItemListener(new CtrlPanelListeners.CheckBoxListener());
        getPrintPageNoBox().addItemListener(new CtrlPanelListeners.CheckBoxListener());
        getPrintNameBox().addItemListener(new CtrlPanelListeners.CheckBoxListener());

        printDialogBtnRef.get().addActionListener(new CtrlPanelListeners.PrintDialogBtnListener());
        if (editorRef.get().supportVisibleAreaOnly()) {
            visibleAreaBoxRef.get().addItemListener(new CtrlPanelListeners.CheckBoxListener());
        }
    }

    public void setPrinterJob(PrinterJob printerJob) {
        this.printerJob = printerJob;
    }

    protected PrinterJob getPrinterJob() {
        return this.printerJob;
    }

    public void setPrintParam(PrintParam printParam) {
        PrintParam old = this.printParam;
        this.printParam = printParam;
        firePropertyChange("printParam", old, this.printParam);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel printOptionsPanel = createPrintOptionsPanel();
        add(printOptionsPanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.CENTER;
        JPanel navigationPanel = createNavigationPanel();
        add(navigationPanel, c);

    }

    private JPanel createPrintOptionsPanel() {
        JPanel ret = new JPanel();
        GridBagConstraints c;

        c = new GridBagConstraints();
        ret.add(new JLabel("Orientation:"), c);

        ButtonGroup btnGroup = new ButtonGroup();

        c = new GridBagConstraints();
        JRadioButton portraitBtn = new JRadioButton("Portrait");
        portraitBtn.setActionCommand("portrait");
        btnGroup.add(portraitBtn);
        portraitBtnRef = new WeakReference<JRadioButton>(portraitBtn);
        ret.add(portraitBtn, c);

        c = new GridBagConstraints();
        JRadioButton landscapeBtn = new JRadioButton("Landscape");
        landscapeBtn.setActionCommand("landscape");
        btnGroup.add(landscapeBtn);
        landscapeBtnRef = new WeakReference<JRadioButton>(landscapeBtn);
        ret.add(landscapeBtn, c);

        if (editorRef.get().supportVisibleAreaOnly()) {
            c = new GridBagConstraints();
            JCheckBox visibleAreaBox = new JCheckBox("Visbile Area Only");
            visibleAreaBox.setActionCommand("visibleArea");
            visibleAreaBoxRef = new WeakReference<JCheckBox>(visibleAreaBox);
            ret.add(visibleAreaBox, c);
        }

        c = new GridBagConstraints();
        JCheckBox printNameBox = new JCheckBox("Name");
        printNameBox.setActionCommand("name");
        printNameBoxRef = new WeakReference<JCheckBox>(printNameBox);
        ret.add(printNameBox, c);

        c = new GridBagConstraints();
        JCheckBox printDateBox = new JCheckBox("Date");
        printDateBox.setActionCommand("date");
        printDateBoxRef = new WeakReference<JCheckBox>(printDateBox);
        ret.add(printDateBox, c);
        printDateBox.setEnabled(!getPrintEditor().isPrintableJComponent());

        c = new GridBagConstraints();
        JCheckBox printPageNoBox = new JCheckBox("Page No");
        printPageNoBox.setActionCommand("pageNo");
        printPageNoBoxRef = new WeakReference<JCheckBox>(printPageNoBox);
        ret.add(printPageNoBox, c);

        c = new GridBagConstraints();
        JButton printDialogBtn = new JButton("Printer...", ImageHelper.createImageIcon(ImageNames.PRINTER_16));
        printDialogBtnRef = new WeakReference<JButton>(printDialogBtn);
        ret.add(printDialogBtn, c);
        return ret;
    }

    private JPanel createNavigationPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        JButton prevBtn = new JButton("Prev");
        prevBtn.setActionCommand("prev");
        prevBtnRef = new WeakReference<JButton>(prevBtn);
        ret.add(prevBtn, c);

        c = new GridBagConstraints();
        JLabel label = new JLabel();
        pageLabelRef = new WeakReference<JLabel>(label);
        ret.add(label, c);

        c = new GridBagConstraints();
        JButton nextBtn = new JButton("Next");
        nextBtn.setActionCommand("next");
        nextBtnRef = new WeakReference<JButton>(nextBtn);
        ret.add(nextBtn, c);

        c = new GridBagConstraints();
        ret.add(new JLabel("Zoom:"), c);

        c = new GridBagConstraints();
        JSpinner zoomSpinner = new JSpinner();
        zoomSpinnerRef = new WeakReference<JSpinner>(zoomSpinner);
        zoomSpinner.setModel(new SpinnerNumberModel(0.4, 0.4, 1, 0.1));
        zoomSpinner.setEditor(new JSpinner.NumberEditor(zoomSpinner, "#%"));
        Dimension spinnerSize = UIUtil.getSize(10000, JSpinner.class);
        UIUtil.setPreferredWidth(zoomSpinner, spinnerSize.width);
        ret.add(zoomSpinner, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        ret.add(filler, c);

        return ret;
    }

    protected PrintParam getPrintParam() {
        return this.printParam;
    }

    protected PrintPanel getPrintPanel() {
        PrintPanel ret = UIUtil.getParent(this, PrintPanel.class);
        return ret;
    }

    protected JCheckBox getPrintDateBox() {
        return printDateBoxRef.get();
    }

    protected JCheckBox getPrintPageNoBox() {
        return printPageNoBoxRef.get();
    }

    protected JCheckBox getPrintNameBox() {
        return printNameBoxRef.get();
    }

    protected JButton getPrevBtn() {
        return prevBtnRef.get();
    }

    protected JButton getNextBtn() {
        return nextBtnRef.get();
    }

    protected JLabel getPageLabel() {
        return pageLabelRef.get();
    }

    protected JRadioButton getPortraitBtn() {
        return portraitBtnRef.get();
    }

    protected JRadioButton getLandscapeBtn() {
        return landscapeBtnRef.get();
    }

    protected JCheckBox getVisibleAreaBox() {
        if(visibleAreaBoxRef != null){
            return visibleAreaBoxRef.get();
        }else{
            return null;
        }
    }

    protected JSpinner getZoomSpinner() {
        return zoomSpinnerRef.get();
    }

    protected IPrintEditor getPrintEditor() {
        return editorRef.get();
    }
}
