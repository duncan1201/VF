/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.task;

import com.gas.common.ui.accordian2.IOutlookPanel;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.main.ui.molpane.MolPane;
import com.gas.domain.core.primer3.UserInput;
import com.gas.primer3.core.mispriminglib.MisprimingLib;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import org.jdesktop.swingx.JXHyperlink;

/**
 *
 * @author dq
 */
public class TaskPanel extends JPanel implements IOutlookPanel {

    public PickPanel leftPickPanel;
    public PickPanel rightPickPanel;
    public PickPanel internalPickPanel;
    private JSpinner PRIMER_NUM_RETURN;
    private JComboBox PRIMER_MISPRIMING_LIBRARY;
    //private JComboBox PRIMER_TASK;    
    
    public TaskPanel() {
        Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, 0));
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        leftPickPanel = new PickPanel(PickPanel.TYPE.LEFT);
        add(leftPickPanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        internalPickPanel = new PickPanel(PickPanel.TYPE.INTERNAL);
        add(internalPickPanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        rightPickPanel = new PickPanel(PickPanel.TYPE.RIGHT);
        add(rightPickPanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        add(createMisprimingPanel(), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        //c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        add(createNumToReturnPanel(), c);
        
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        add(filler, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        JPanel citePanel = createCitePanel();
        add(citePanel, c);

        hookupListeners();
    }

    private JPanel createCitePanel() {
        JPanel ret = new JPanel();
        JLabel label = new JLabel();
        final String url = "http://primer3.wi.mit.edu/primer3web_help.htm#citationRequest";
        label.setForeground(Color.GRAY);
        label.setText("Please cite ");
        JXHyperlink link = new JXHyperlink(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonUtil.browse(url);
            }
        });
        link.setText("Primer3");
        link.setFocusable(false);
        
        ret.add(label);
        ret.add(link);
        return ret;
    }

    private void hookupListeners() {
    }


    /*
     * PRIMER_NUM_RETURN
     */
    int getPrimerNumReturn() {
        Integer ret = (Integer) PRIMER_NUM_RETURN.getValue();
        return ret;
    }

    private JPanel createNumToReturnPanel() {
        JPanel ret = new JPanel();
        //ret.setBorder(BorderFactory.createLineBorder(Color.yellow));
        LayoutManager layout = new GridBagLayout();
        ret.setLayout(layout);
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Number to return:"), c);

        c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        PRIMER_NUM_RETURN = new JSpinner();
        Dimension size = UIUtil.getSize(100, JSpinner.class);
        PRIMER_NUM_RETURN.setModel(new SpinnerNumberModel(1, 1, 5, 1));
        UIUtil.setPreferredWidth(PRIMER_NUM_RETURN, size.width);
        ret.add(PRIMER_NUM_RETURN, c);

        c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        ret.add(Box.createRigidArea(new Dimension(1, 1)), c);
        return ret;
    }

    private JPanel createMisprimingPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        ret.add(new JLabel("Mispriming library:"), c);

        c = new GridBagConstraints();
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        StringList misprimingLib = getMisprimingLibDefaultComboBoxModel();
        final String longest = misprimingLib.longest() + "A";
        Dimension size = UIUtil.getSize(longest, JComboBox.class);
        PRIMER_MISPRIMING_LIBRARY = new JComboBox(misprimingLib.toArray(new String[misprimingLib.size()]));
        UIUtil.setPreferredWidthByPrototype(PRIMER_MISPRIMING_LIBRARY, longest);
        ret.add(PRIMER_MISPRIMING_LIBRARY, c);

        c = new GridBagConstraints();
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        ret.add(Box.createRigidArea(new Dimension(1, 1)), c);

        return ret;
    }

    private StringList getMisprimingLibDefaultComboBoxModel() {
        StringList ret = new StringList(MisprimingLib.getAllNames());
        return ret;
    }

    String getSEQUENCE_PRIMER() {
        return null;
    }

    private StringList createPrimer3Tasks() {
        StringList ret = new StringList(Primer3Task.getAllNames());
        return ret;
    }

    public List<String> validateInput(AnnotatedSeq as) {
        List<String> ret = new ArrayList<String>();

        ret.addAll(leftPickPanel.validateInput(as));
        if (!ret.isEmpty()) {
            return ret;
        }
        ret.addAll(internalPickPanel.validateInput(as));
        if (!ret.isEmpty()) {
            return ret;
        }
        ret.addAll(rightPickPanel.validateInput(as));
        if (!ret.isEmpty()) {
            return ret;
        }

        if (!leftPickPanel.isPicking() 
                && !leftPickPanel.isUseExisting()
                && !internalPickPanel.isPicking()
                && !internalPickPanel.isPicking()
                && !rightPickPanel.isPicking()
                && !rightPickPanel.isUseExisting()) {
            ret.add(String.format(CNST.MSG_FORMAT, "No task selected", "Please select at least one task(e.g., \"Forward Primer\")"));
        }
        return ret;
    }

    public void updateUserInputFromUI(UserInput userInput, AnnotatedSeq as) {

        userInput.getData().put("PRIMER_TASK", "generic");
        userInput.enablePickAnyway();
        userInput.getData().put("PRIMER_NUM_RETURN", PRIMER_NUM_RETURN.getValue().toString());

        leftPickPanel.updateUserInputFromUI(userInput, as);
        rightPickPanel.updateUserInputFromUI(userInput, as);
        internalPickPanel.updateUserInputFromUI(userInput, as);

        String name = (String) PRIMER_MISPRIMING_LIBRARY.getSelectedItem();
        userInput.getData().put("PRIMER_MISPRIMING_LIBRARY", name);
    }

    public void populateUI(UserInput userInput) {
        if (userInput == null) {
            return;
        }
        leftPickPanel.populateUI(userInput);
        rightPickPanel.populateUI(userInput);
        internalPickPanel.populateUI(userInput);

        String primerNumReturn = userInput.get("PRIMER_NUM_RETURN");
        PRIMER_NUM_RETURN.setValue(Integer.parseInt(primerNumReturn));

        String primerMisprimingLib = userInput.get("PRIMER_MISPRIMING_LIBRARY");
        MisprimingLib type = MisprimingLib.getByName(primerMisprimingLib);
        if (type != null) {
            PRIMER_MISPRIMING_LIBRARY.setSelectedItem(type.getName());
        }
    }

    @Override
    public void expanded() {
        MolPane molPane = getMolPane();
        if (molPane == null) {
            return;
        }
        AnnotatedSeq as = molPane.getAs();
        if (!as.isCircular()) {
        }
    }

    MolPane getMolPane() {
        MolPane molPane = UIUtil.getParent(this, MolPane.class);
        return molPane;
    }
}
