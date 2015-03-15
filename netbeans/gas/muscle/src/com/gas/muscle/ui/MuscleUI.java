/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.muscle.ui;

import com.gas.common.ui.misc.RichSeparator;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.muscle.IMuscleUI;
import com.gas.domain.core.msa.muscle.MuscleParam;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import org.jdesktop.swingx.JXHyperlink;

/**
 *
 * @author dq
 */
public class MuscleUI extends JPanel implements IMuscleUI {

    private MuscleParam muscleParam;
    JSpinner maxItrSpinner;
    JSpinner maxTreeSpinner;
    WeakReference<JCheckBox> diagnalOptRef;
    WeakReference<JCheckBox> anchorOptRef;
    WeakReference<JButton> switchBtnRef;
    WeakReference<JLabel> labelRef;
    String profile1;
    String profile2;

    public MuscleUI(String profile1, String profile2) {        
        UIUtil.setDefaultBorder(this);
        this.profile1 = profile1;
        this.profile2 = profile2;
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        createComponents();
        hookupListeners();
    }

    private void createComponents() {
        GridBagConstraints c;

        if (profile1 != null && !profile1.isEmpty() && profile2 != null && !profile2.isEmpty()) {
            JPanel profilePanel = createProfilePanel();
            c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            add(profilePanel, c);
        }

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel alignmentPanel = createAlignmentPanel();
        add(alignmentPanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        JPanel citationPanel = citationPanel();
        add(citationPanel, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        Component comp = Box.createRigidArea(new Dimension(1, 1));
        add(comp, c);
    }
    
    private JPanel citationPanel(){
        JPanel ret = new JPanel();
        final String url = "http://dx.doi.org/10.1093/nar/gkh340";
        ret.add(new JLabel("Please cite "));
        JXHyperlink link = new JXHyperlink(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonUtil.browse(url);
            }
        });
        link.setText("Muscle");
        ret.add(link);
        return ret;
    }

    @Override
    public String getProfile1() {
        return profile1;
    }

    @Override
    public String getProfile2() {
        return profile2;
    }

    private JPanel createProgressivePanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        RichSeparator rs = new RichSeparator("<html><b>Progressive Alignment</b></html>");
        ret.add(rs, c);

        //Max number of iterations
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Max number of iterations:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        maxItrSpinner = new JSpinner();
        maxItrSpinner.setModel(new SpinnerNumberModel(16, 3, 99, 1));
        ret.add(maxItrSpinner, c);

        // Anchor Optimization
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Anchor Optimization:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        JCheckBox anchorOpt = new JCheckBox();
        anchorOpt.setActionCommand("anchorOpt");
        anchorOptRef = new WeakReference<JCheckBox>(anchorOpt);
        ret.add(anchorOpt, c);
        return ret;
    }

    private JPanel createAlignmentPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy++;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        RichSeparator rs = new RichSeparator("<html><b>Alignment Options</b></html>");
        ret.add(rs, c);

        //Max number of trees:
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Max number of trees:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        maxTreeSpinner = new JSpinner();
        maxTreeSpinner.setModel(new SpinnerNumberModel(1, 1, 99, 1));
        ret.add(maxTreeSpinner, c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        add(filler, c);

        //Max number of iterations
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Max number of iterations:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        maxItrSpinner = new JSpinner();
        maxItrSpinner.setModel(new SpinnerNumberModel(16, 3, 99, 1));
        ret.add(maxItrSpinner, c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        filler = Box.createRigidArea(new Dimension(1, 1));
        add(filler, c);

        return ret;
    }

    private JPanel createPairwisePanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy++;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        RichSeparator rs = new RichSeparator("<html><b>Pairwise Alignment</b></html>");
        ret.add(rs, c);

        //Max number of trees:
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Max number of trees:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        maxTreeSpinner = new JSpinner();
        maxTreeSpinner.setModel(new SpinnerNumberModel(1, 1, 99, 1));
        ret.add(maxTreeSpinner, c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        add(filler, c);

        // Diagnal Optimization
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Diagnal Optimization:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        JCheckBox diagnalOpt = new JCheckBox();
        diagnalOpt.setActionCommand("diagnalOpt");
        diagnalOptRef = new WeakReference<JCheckBox>(diagnalOpt);
        ret.add(diagnalOpt, c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        filler = Box.createRigidArea(new Dimension(1, 1));
        add(filler, c);

        return ret;
    }

    private JPanel createProfilePanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        RichSeparator rs = new RichSeparator("<html><b>Profiles</b></html>");
        ret.add(rs, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        JLabel label = new JLabel();
        labelRef = new WeakReference<JLabel>(label);
        ret.add(label, c);
        update();

        c = new GridBagConstraints();
        c.gridy = gridy;
        JButton switchBtn = new JButton("Switch");
        switchBtnRef = new WeakReference<JButton>(switchBtn);
        ret.add(switchBtn, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        ret.add(filler, c);
        return ret;
    }

    void update() {
        labelRef.get().setText(String.format("<html>Aligning <b>%s</b> to <b>%s</b></html>", profile1, profile2));
    }

    private void hookupListeners() {
        if (switchBtnRef != null) {
            switchBtnRef.get().addActionListener(new MuscleUIListeners.SwitchListener());
        }
        maxItrSpinner.addChangeListener(new MuscleUIListeners.SpinnersListener());
        maxTreeSpinner.addChangeListener(new MuscleUIListeners.SpinnersListener());
        if (diagnalOptRef != null) {
            diagnalOptRef.get().addItemListener(new MuscleUIListeners.BoxListener());
        }
        if (anchorOptRef != null) {
            anchorOptRef.get().addItemListener(new MuscleUIListeners.BoxListener());
        }
        addPropertyChangeListener(new MuscleUIListeners.PtyListener());
    }

    JSpinner getMaxItrSpinner() {
        return maxItrSpinner;
    }

    JSpinner getMaxTreeSpinner() {
        return maxTreeSpinner;
    }

    @Override
    public MuscleParam getMuscleParam() {
        return muscleParam;
    }

    @Override
    public void setMuscleParam(MuscleParam muscleParams) {
        MuscleParam old = this.muscleParam;
        this.muscleParam = muscleParams;
        firePropertyChange("muscleParam", old, this.muscleParam);
    }
}
