/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.clustalw.core.ui.msa;

import com.gas.clustalw.core.service.api.IClustalwService;
import com.gas.common.ui.misc.RichSeparator;
import com.gas.common.ui.util.CommonUtil;
import com.gas.domain.core.msa.clustalw.GeneralParam;
import com.gas.domain.core.msa.clustalw.ClustalwParam;
import com.gas.domain.core.msa.clustalw.IClustalWUI;
import com.gas.common.ui.util.FileHelper;
import com.gas.common.ui.util.UIUtil;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXHyperlink;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class ClustalWUI extends JPanel implements IClustalWUI {

    private PairwiseOptionsUI pairwiseOptionsUI;
    private MultipleOptionsUI multipleOptionsUI;
    ProteinGapsPanel proteinGapsPanel;
    JButton switchProfileBtn;    
    JLabel label;
    private ClustalwParam clustalwParam;
    String profile1;
    String profile2;
    JTextField exeField;
    JButton changeButton;
    IClustalwService clustalwService = Lookup.getDefault().lookup(IClustalwService.class);
    

    public ClustalWUI(boolean vertical, String profile1, String profile2) {
        this.profile1 = profile1;
        this.profile2 = profile2;
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);

        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));

        createComponents(vertical);
        hookupListeners();
    }

    @Override
    public String getProfile1() {
        return profile1;
    }

    @Override
    public String getProfile2() {
        return profile2;
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
        RichSeparator rs = new RichSeparator(new JLabel("<html><b>Profiles</b></html>"));
        ret.add(rs, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        label = new JLabel();
        updateProfilesLabel();
        ret.add(label, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        switchProfileBtn = new JButton("Switch");
        ret.add(switchProfileBtn, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        Component comp = Box.createRigidArea(new Dimension(1, 1));
        ret.add(comp, c);
        return ret;
    }
    
    @Override
    public void setExeField(String path){
        exeField.setText(path);
        exeField.setToolTipText(path);
    }

    void updateProfilesLabel() {
        label.setText(String.format("<html>Aligning <b>%s</b> to <b>%s</b></html>", profile2, profile1));
    }

    private void createComponents(boolean vertical) {
        final Insets insets = UIUtil.getDefaultInsets();
        GridBagConstraints c;

        if (profile2 != null && !profile2.isEmpty() && profile1 != null && !profile1.isEmpty()) {
            c = new GridBagConstraints();
            c.gridx = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            JPanel panel = createProfilePanel();
            add(panel, c);
        }

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(insets.top, 0, insets.bottom, 0);
        pairwiseOptionsUI = new PairwiseOptionsUI(vertical);
        add(pairwiseOptionsUI, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(insets.top, 0, insets.bottom, 0);
        multipleOptionsUI = new MultipleOptionsUI(vertical);
        add(multipleOptionsUI, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        proteinGapsPanel = new ProteinGapsPanel(vertical);
        add(proteinGapsPanel, c);

        if (!vertical) {
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = GridBagConstraints.RELATIVE;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            c.insets = new Insets(insets.top, 0, insets.bottom, 0);
            JPanel executablePanel = executablePanel();
            add(executablePanel, c);
        }
        
        if (!vertical) {
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = GridBagConstraints.RELATIVE;
            JPanel citationPanel = citationPanel();
            add(citationPanel, c);
        }

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        add(filler, c);
    }
    
    private JPanel executablePanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        ret.add(new JLabel("Executable:"), c);
        
        exeField = new JTextField();
        exeField.setEnabled(false);
        exeField.setText(clustalwService.getExecutablePath());
        exeField.setToolTipText(exeField.getText());
        c = new GridBagConstraints();
        c.gridy = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        ret.add(exeField, c);
        
        changeButton = new JButton("Change...");
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        ret.add(changeButton, c);
        
        return ret;
    }

    private JPanel citationPanel() {
        JPanel ret = new JPanel();
        final String url = "http://dx.doi.org/10.1093/bioinformatics/btm404";
        ret.add(new JLabel("Please cite "));
        JXHyperlink link = new JXHyperlink(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonUtil.browse(url);
            }
        });
        link.setText("ClustalW");
        ret.add(link);
        return ret;
    }

    private void hookupListeners() {
        addPropertyChangeListener(new ClustalWUIListeners.PtyListener());
        if (switchProfileBtn != null) {
            switchProfileBtn.addActionListener(new ClustalWUIListeners.SwitchListener());
        }
        if (changeButton != null) {
            changeButton.addActionListener(new ClustalWUIListeners.ChangeListener());
        }
    }

    @Override
    public ClustalwParam getClustalwParam() {
        if (clustalwParam != null && clustalwParam.getGeneralParam() != null) {
            GeneralParam generalParams = clustalwParam.getGeneralParam();
            if (generalParams.getOutfile() == null) {
                File file = FileHelper.getUniqueFile("msa", "msa");
                generalParams.setOutfile(file);
            }
        }
        return clustalwParam;
    }

    @Override
    public void setMsaParam(ClustalwParam clustalwParam) {
        ClustalwParam old = this.clustalwParam;
        this.clustalwParam = clustalwParam;
        firePropertyChange("clustalwParam", old, this.clustalwParam);
    }

    public MultipleOptionsUI getMultipleOptionsUI() {
        return multipleOptionsUI;
    }

    public PairwiseOptionsUI getPairwiseOptionsUI() {
        return pairwiseOptionsUI;
    }
}
