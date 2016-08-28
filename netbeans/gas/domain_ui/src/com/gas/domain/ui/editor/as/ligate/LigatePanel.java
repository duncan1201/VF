/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.ligate;

import com.gas.common.ui.core.CharSet;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqList;
import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.jdesktop.swingx.JXTitledSeparator;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;

/**
 *
 * @author dq
 */
public class LigatePanel extends JPanel {

    LigateTbl ligateTable;
    protected JButton upBtn;
    protected JButton downBtn;
    protected JButton flipBtn;
    JRadioButton noModBtn;
    JRadioButton klenowFillInBtn;
    JRadioButton exo5to3Btn;
    JRadioButton exo3to5Btn;
    JRadioButton partialFillInBtn;
    protected JCheckBox aBox;
    protected JCheckBox cBox;
    protected JCheckBox gBox;
    protected JCheckBox tBox;
    protected JCheckBox circularizeBox;
    PreviewComp previewComp;
    WeakReference<JTextField> nameFieldRef;
    String initName;
    DialogDescriptor dialogDescriptor;
    NotificationLineSupport notificationLineSupport;
    List<String> existingNames = new ArrayList<String>();
    private ButtonGroup btnGroup = new ButtonGroup();
    
    AnnotatedSeqList asList = null;

    public LigatePanel(AnnotatedSeqList asList) {
        this.asList = asList;
        initComponents();

        hookupListeners();

        circularizeBox.setSelected(true);
        
        getLigateTable().setData(asList);
    }

    public void setExistingNames(List<String> existingNames) {
        this.existingNames = existingNames;
    }

    private void initComponents() {
        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();        
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        JPanel namePanel = createNamePanel();
        add(namePanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.9;
        JPanel mainPanel = createMainPanel();        
        add(mainPanel, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.1;
        c.weighty = 1.0;
        JPanel panel = createPreviewPanel();
        add(panel, c);
    }

    private JPanel createMainPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        ligateTable = new LigateTbl(new LigateTblMdl());
        JScrollPane scrollPane = new JScrollPane(ligateTable);
        ligateTable.setFillsViewportHeight(true);

        ret.add(scrollPane, c);
        
        c = new GridBagConstraints();        
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.1;
        c.weighty = 1.0;
        JPanel panel = createCtrlPanel();
        ret.add(panel, c);
        return ret;
    }
    
    private JPanel createPreviewPanel(){
        JPanel ret = new JPanel(new GridBagLayout());
        ret.setBorder(BorderFactory.createTitledBorder( //
                new LineBorder(Color.GRAY), //
                "Preview", //
                TitledBorder.LEFT, //
                TitledBorder.TOP, //
                UIManager.getFont("TitledBorder.font")));
        GridBagConstraints c;
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        previewComp = new PreviewComp(this.asList);
        ret.add(previewComp, c);
        return ret;
    }

    public void setDialogDescriptor(DialogDescriptor dialogDescriptor) {
        this.dialogDescriptor = dialogDescriptor;
        notificationLineSupport = dialogDescriptor.createNotificationLineSupport();
    }

    public void validateInput() {
        if (notificationLineSupport == null) {
            return;
        }
        String name = nameFieldRef.get().getText().trim();
        CharSet intersect = AnnotatedSeq.forbiddenChars.intersect(name.toCharArray());
        boolean valid;
        if (name.isEmpty()) {
            notificationLineSupport.setInformationMessage("Name cannot be empty");
            valid = false;
        } else if (existingNames.contains(name)) {
            notificationLineSupport.setInformationMessage("Duplicate name");
            valid = false;
        } else if (!getLigateTable().compatible()) {
            notificationLineSupport.setInformationMessage("Incompatible Ends");
            valid = false;
        } else if (!intersect.isEmpty()) {
            notificationLineSupport.setInformationMessage(String.format("\"%s\" %s not allowed", intersect.toString(), intersect.size() > 1 ? "are" : "is"));
            valid = false;
        } else {
            notificationLineSupport.clearMessages();
            valid = true;
        }
        if (dialogDescriptor != null) {
            dialogDescriptor.setValid(valid);
        }
    }

    private JPanel createNamePanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;
        c = new GridBagConstraints();
        ret.add(new JLabel("Name"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        JTextField nameField = new JTextField();
        nameFieldRef = new WeakReference<JTextField>(nameField);
        UIUtil.setPreferredWidthByPrototype(nameField, "Ligated Sequence(1000)");
        ret.add(nameField, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        circularizeBox = new JCheckBox("Circularize");
        ret.add(circularizeBox, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        ret.add(filler, c);
        return ret;
    }

    private void hookupListeners() {
        noModBtn.addActionListener(new LigatePanelListeners.ModBtnListener(this));
        klenowFillInBtn.addActionListener(new LigatePanelListeners.ModBtnListener(this));
        exo5to3Btn.addActionListener(new LigatePanelListeners.ModBtnListener(this));
        exo3to5Btn.addActionListener(new LigatePanelListeners.ModBtnListener(this));
        partialFillInBtn.addItemListener(new LigatePanelListeners.PartialFillInListener(this));

        aBox.addItemListener(new LigatePanelListeners.DNTPListener(this));
        cBox.addItemListener(new LigatePanelListeners.DNTPListener(this));
        gBox.addItemListener(new LigatePanelListeners.DNTPListener(this));
        tBox.addItemListener(new LigatePanelListeners.DNTPListener(this));
        circularizeBox.addItemListener(new LigatePanelListeners.CircularizeBoxListener(this));
        flipBtn.addActionListener(new LigatePanelListeners.FlipBtnListener(this));
        upBtn.addActionListener(new LigatePanelListeners.MoveUpListener(this));
        downBtn.addActionListener(new LigatePanelListeners.MoveDownListener(new WeakReference<LigatePanel>(this)));

        addPropertyChangeListener(new LigatePanelListeners.PtyChangeListener(this));
        getLigateTable().getSelectionModel().addListSelectionListener(new LigatePanelListeners.TblSelectListener(this));
        nameFieldRef.get().getDocument().addDocumentListener(new LigatePanelListeners.NameListener(this));
    }

    private JPanel createCtrlPanel() {
        JPanel panel = new JPanel();
        LayoutManager layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        upBtn = new JButton("Move Up");
        upBtn.setEnabled(false);
        panel.add(upBtn, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        downBtn = new JButton("Move Down");
        downBtn.setEnabled(false);
        panel.add(downBtn, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        panel.add(separator, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        flipBtn = new JButton("Flip");
        flipBtn.setEnabled(false);

        panel.add(flipBtn, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        JXTitledSeparator titledSeparator = new JXTitledSeparator("Modify Ends");
        panel.add(titledSeparator, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        noModBtn = new JRadioButton("None");
        noModBtn.setActionCommand("none");
        noModBtn.setEnabled(false);
        noModBtn.setSelected(true);
        panel.add(noModBtn, c);
        btnGroup.add(noModBtn);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        klenowFillInBtn = new JRadioButton("Klenow Fill-in");
        klenowFillInBtn.setActionCommand("klenow");
        klenowFillInBtn.setEnabled(false);

        panel.add(klenowFillInBtn, c);
        btnGroup.add(klenowFillInBtn);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        exo5to3Btn = new JRadioButton("5'-3' exo");
        exo5to3Btn.setActionCommand("5to3");
        exo5to3Btn.setEnabled(false);

        panel.add(exo5to3Btn, c);
        btnGroup.add(exo5to3Btn);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        exo3to5Btn = new JRadioButton("3'-5' exo");
        exo3to5Btn.setActionCommand("3to5");
        exo3to5Btn.setEnabled(false);

        panel.add(exo3to5Btn, c);
        btnGroup.add(exo3to5Btn);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        partialFillInBtn = new JRadioButton("Partial Fill-in");
        partialFillInBtn.setActionCommand("partial");
        partialFillInBtn.setEnabled(false);

        panel.add(partialFillInBtn, c);
        btnGroup.add(partialFillInBtn);

        JPanel basesPanel = createBasesPanel();
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 4, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        panel.add(basesPanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        panel.add(filler, c);

        return panel;
    }

    public Boolean isCircularize() {
        return circularizeBox.isSelected();
    }

    private JPanel createBasesPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        aBox = new JCheckBox("A");
        aBox.setEnabled(false);
        aBox.setActionCommand("dATP");
        ret.add(aBox, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        cBox = new JCheckBox("C");
        cBox.setEnabled(false);
        cBox.setActionCommand("dCTP");

        ret.add(cBox, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        gBox = new JCheckBox("G");
        gBox.setEnabled(false);
        gBox.setActionCommand("dGTP");

        ret.add(gBox, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        tBox = new JCheckBox("T");
        tBox.setEnabled(false);
        tBox.setActionCommand("dTTP");
        ret.add(tBox, c);

        return ret;
    }
    
    public AnnotatedSeqList getModifiedData() {
        return getLigateTable().getModifiedData();
    }

    /**
     * @return the name of the ligated molecule
     */
    public String getNewName() {
        return nameFieldRef.get().getText();
    }

    public void setInitName(String initName) {
        String old = this.initName;
        this.initName = initName;
        firePropertyChange("initName", old, this.initName);
    }

    LigateTbl getLigateTable() {
        if (ligateTable == null) {
            ligateTable = new LigateTbl(new LigateTblMdl());
        }
        return ligateTable;                
    }

}
