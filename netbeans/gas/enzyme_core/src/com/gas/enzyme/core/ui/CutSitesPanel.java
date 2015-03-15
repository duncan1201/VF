/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CutSitesPanel.java
 *
 * Created on Nov 26, 2011, 6:25:34 AM
 */
package com.gas.enzyme.core.ui;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.ren.RMap;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dunqiang
 */
public class CutSitesPanel extends javax.swing.JPanel {

    protected Integer matchMax;
    protected Integer matchMin;
    protected Integer mustCutFrom;
    protected Integer mustCutTo;
    protected Integer mustNotCutFrom;
    protected Integer mustNotCutTo;
    protected Boolean mustNotCutEnabled;
    private RMap.InputParams inputParams;
    private Integer totalLength;
    protected JRadioButton anywhereBtn;
    protected ButtonGroup btnGroup;
    protected JSpinner matchMaxSpinner;
    protected JSpinner matchMinSpinner;
    protected JRadioButton mustCutBtn;
    protected JSpinner mustCutFromSpinner;
    protected JSpinner mustCutToSpinner;
    protected JRadioButton mustNotCutBtn;
    protected JSpinner mustNotCutFromSpinner;
    protected JSpinner mustNotCutToSpinner;
    protected FlatBtn mustNotSelectBtn;
    protected FlatBtn mustSelectBtn;
    protected boolean isAdjusting = false;

    /**
     * Creates new form CutSitesPanel
     */
    public CutSitesPanel() {
        initComponents();
        hookupListeners();
    }

    private void hookupListeners() {
        initSpinners();

        anywhereBtn.addActionListener(new CutSitesPanelLtns.AnywhereBtnListener());
        mustCutBtn.addActionListener(new CutSitesPanelLtns.MustCutBtnListener());
        mustNotCutBtn.addActionListener(new CutSitesPanelLtns.MustNotCutBtnListener());

        mustSelectBtn.addActionListener(new CutSitesPanelLtns.SelectBtnListener());
        mustNotSelectBtn.addActionListener(new CutSitesPanelLtns.SelectBtnListener());

        addPropertyChangeListener(new CutSitesPanelLtns.PtyChangeListener(this));
    }

    void populateUI() {
        if (inputParams == null || totalLength == null) {
            return;
        }
        Integer startPos = inputParams.getStartPos();
        Integer endPos = inputParams.getEndPos();

        getMatchMaxSpinner().setValue(inputParams.getMaxOccurence());
        getMatchMinSpinner().setValue(inputParams.getMinOccurence());
        if (Boolean.TRUE.equals(inputParams.getAllow())) {
            getMustCutBtn().setSelected(true);
            setMustCutEnabled(true);
            getMustCutToSpinner().setValue(inputParams.getEndPos());
            getMustCutFromSpinner().setValue(inputParams.getStartPos());
        } else if (Boolean.FALSE.equals(inputParams.getAllow())) {
            getMustNotCutBtn().setSelected(true);
            setMustNotCutEnabled(true);
            getMustNotCutToSpinner().setValue(inputParams.getEndPos());
            getMustNotCutFromSpinner().setValue(inputParams.getStartPos());
        } else if (inputParams.getAllow() == null) {
            getAnywhereBtn().setSelected(true);
            if (inputParams.getEndPos() != null) {
                getMustCutToSpinner().setValue(inputParams.getEndPos());
            }
            if (inputParams.getStartPos() != null) {
                getMustCutFromSpinner().setValue(inputParams.getStartPos());
            }
        }
    }

    private void initSpinners() {
        // match
        matchMinSpinner.getModel().addChangeListener(new CutSitesPanelLtns.MatchMinSpinnerLtn(this));

        SpinnerNumberModel model = (SpinnerNumberModel) matchMinSpinner.getModel();
        model.setMinimum(1);

        matchMaxSpinner.getModel().addChangeListener(new CutSitesPanelLtns.MatchMaxSpinnerLtn(this));

        // must cut
        model = (SpinnerNumberModel) mustCutFromSpinner.getModel();
        model.setMinimum(1);

        mustCutFromSpinner.getModel().addChangeListener(new CutSitesPanelLtns.MustCutFromSpinnerLtn(this));

        mustCutToSpinner.getModel().addChangeListener(new CutSitesPanelLtns.MustCutToSpinnerLtn(this));

        // must not cut
        model = (SpinnerNumberModel) mustNotCutFromSpinner.getModel();
        model.setMinimum(1);

        mustNotCutFromSpinner.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Integer value = (Integer) mustNotCutFromSpinner.getValue();
                SpinnerNumberModel model = (SpinnerNumberModel) mustNotCutToSpinner.getModel();
                model.setMinimum(value);
                isAdjusting = true;
                setMustNotCutFrom(value);
                isAdjusting = false;
            }
        });

        mustNotCutToSpinner.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Integer value = (Integer) mustNotCutToSpinner.getValue();
                SpinnerNumberModel model = (SpinnerNumberModel) mustNotCutFromSpinner.getModel();
                model.setMaximum(value);
                isAdjusting = true;
                setMustNotCutTo(value);
                isAdjusting = false;
            }
        });

        matchMinSpinner.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Integer value = (Integer) matchMinSpinner.getValue();
                isAdjusting = true;
                setMatchMin(value);
                isAdjusting = false;
            }
        });

        matchMaxSpinner.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Integer value = (Integer) matchMaxSpinner.getValue();
                isAdjusting = true;
                setMatchMax(value);
                isAdjusting = false;
            }
        });
    }

    protected void setTotalLength(Integer totalLength) {
        Integer old = this.totalLength;
        this.totalLength = totalLength;
        firePropertyChange("totalLength", old, this.totalLength);
    }

    public void setInputParams(RMap.InputParams inputParams) {
        RMap.InputParams old = this.inputParams;
        this.inputParams = inputParams;
        firePropertyChange("inputParams", old, this.inputParams);
    }

    public RMap.InputParams getInputParams() {
        return this.inputParams;
    }

    public Integer getTotalLength() {
        return totalLength;
    }

    public boolean isAnywhereEnabled() {
        return anywhereBtn.isSelected();
    }

    public Integer getMatchMax() {
        return matchMax;
    }

    public Integer getMatchMin() {
        return matchMin;
    }

    public Integer getMustCutFrom() {
        return mustCutFrom;
    }

    public Integer getMustCutTo() {
        return mustCutTo;
    }

    public void setAnywhereEnabled(Boolean anywhereEnabled) {
        anywhereBtn.setSelected(anywhereEnabled);
        if (anywhereEnabled) {
            setMustCutEnabled(false);
            setMustNotCutEnabled(false);
        }
    }

    public boolean isMustCutEnabled() {
        return mustCutBtn.isSelected();
    }

    public void setMustCutEnabled(Boolean mustCutEnabled) {
        this.mustCutBtn.setSelected(mustCutEnabled);
        mustCutFromSpinner.setEnabled(mustCutEnabled);
        mustCutToSpinner.setEnabled(mustCutEnabled);
        mustSelectBtn.setEnabled(mustCutEnabled);
        if (mustCutEnabled) {
            setAnywhereEnabled(false);
            setMustNotCutEnabled(false);
        }
    }

    public boolean isMustNotCutEnabled() {
        return mustNotCutEnabled;
    }

    public void setMustNotCutEnabled(Boolean mustNotCutEnabled) {
        this.mustNotCutBtn.setSelected(mustNotCutEnabled);
        Boolean old = this.mustNotCutEnabled;
        this.mustNotCutEnabled = mustNotCutEnabled;
        firePropertyChange("mustNotCutEnabled", old, this.mustNotCutEnabled);
        if (mustNotCutEnabled) {
            setAnywhereEnabled(false);
            setMustCutEnabled(false);
        }
    }

    public Integer getMustNotCutFrom() {
        return mustNotCutFrom;
    }

    public void setMustNotCutFrom(Integer mustNotCutFrom) {
        Integer old = this.mustNotCutFrom;
        this.mustNotCutFrom = mustNotCutFrom;
        firePropertyChange("mustNotCutFrom", old, this.mustNotCutFrom);
    }

    public Integer getMustNotCutTo() {
        return mustNotCutTo;
    }

    public void setMustNotCutTo(Integer mustNotCutTo) {
        Integer old = this.mustNotCutTo;
        this.mustNotCutTo = mustNotCutTo;
        firePropertyChange("mustNotCutTo", old, this.mustNotCutTo);
    }

    public void setMustCutTo(Integer mustCutTo) {
        Integer old = this.mustCutTo;
        this.mustCutTo = mustCutTo;
        firePropertyChange("mustCutTo", old, this.mustCutTo);
    }

    public void setMustCutFrom(Integer mustCutFrom) {
        Integer old = this.mustCutFrom;
        this.mustCutFrom = mustCutFrom;
        firePropertyChange("mustCutFrom", old, this.mustCutFrom);
    }

    public void setMatchMin(Integer matchMin) {
        Integer old = this.matchMin;
        this.matchMin = matchMin;
        firePropertyChange("matchMin", old, this.matchMin);
    }

    public void setMatchMax(Integer matchMax) {
        Integer old = this.matchMax;
        this.matchMax = matchMax;
        firePropertyChange("matchMax", old, this.matchMax);
    }

    public JSpinner getMatchMaxSpinner() {
        return matchMaxSpinner;
    }

    public JSpinner getMatchMinSpinner() {
        return matchMinSpinner;
    }

    public JRadioButton getAnywhereBtn() {
        return anywhereBtn;
    }

    public JRadioButton getMustCutBtn() {
        return mustCutBtn;
    }

    public JSpinner getMustCutFromSpinner() {
        return mustCutFromSpinner;
    }

    public JSpinner getMustCutToSpinner() {
        return mustCutToSpinner;
    }

    public JRadioButton getMustNotCutBtn() {
        return mustNotCutBtn;
    }

    public JSpinner getMustNotCutFromSpinner() {
        return mustNotCutFromSpinner;
    }

    public JSpinner getMustNotCutToSpinner() {
        return mustNotCutToSpinner;
    }

    private void initComponents() {
        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(0, insets.left, 0, 0));

        LayoutManager layout = null;
        layout = new GridBagLayout();
        this.setLayout(layout);
        GridBagConstraints c = null;

        JPanel matchPanel = createMatchPanel();
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(matchPanel, c);

        JPanel cutPosPanel = createCutPosPanel();
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(cutPosPanel, c);

        btnGroup = new ButtonGroup();
        btnGroup.add(anywhereBtn);
        btnGroup.add(mustCutBtn);
        btnGroup.add(mustNotCutBtn);
    }

    private JPanel createMatchPanel() {
        JPanel ret = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Match"), c);

        matchMinSpinner = new javax.swing.JSpinner();
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        ret.add(matchMinSpinner, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        ret.add(new JLabel("to"), c);

        matchMaxSpinner = new javax.swing.JSpinner();
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        ret.add(matchMaxSpinner, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        ret.add(new JLabel("times"), c);
        return ret;
    }

    private JPanel createCutPosPanel() {

        JPanel ret = new JPanel(new GridBagLayout());
        int gridy = 0;

        anywhereBtn = new JRadioButton();
        anywhereBtn.setText("Cut anywhere");
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy++;
        c.gridwidth = 5;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        ret.add(anywhereBtn, c);

        // must cut within    
        mustCutBtn = new JRadioButton();
        mustCutBtn.setText("Cut within");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(mustCutBtn, c);

        mustCutFromSpinner = new javax.swing.JSpinner();
        c = new GridBagConstraints();
        c.gridy = gridy;
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        ret.add(mustCutFromSpinner, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.CENTER;
        ret.add(new JLabel("to"), c);

        mustCutToSpinner = new javax.swing.JSpinner();
        c = new GridBagConstraints();
        c.gridy = gridy;
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        ret.add(mustCutToSpinner, c);

        mustSelectBtn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.SELECT_16));
        mustSelectBtn.setActionCommand("mustSelect");
        c = new GridBagConstraints();
        c.gridy = gridy++;
        ret.add(mustSelectBtn, c);

        // must not cut within
        mustNotCutBtn = new JRadioButton();
        mustNotCutBtn.setText("Not cut within");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(mustNotCutBtn, c);

        mustNotCutFromSpinner = new javax.swing.JSpinner();
        c = new GridBagConstraints();
        c.gridy = gridy;
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        ret.add(mustNotCutFromSpinner, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.CENTER;
        ret.add(new JLabel("to"), c);

        mustNotCutToSpinner = new JSpinner();
        c = new GridBagConstraints();
        c.gridy = gridy;
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        ret.add(mustNotCutToSpinner, c);

        mustNotSelectBtn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.SELECT_16));
        mustNotSelectBtn.setActionCommand("mustNotSelect");
        c = new GridBagConstraints();
        c.gridy = gridy;
        ret.add(mustNotSelectBtn, c);

        return ret;
    }
}
