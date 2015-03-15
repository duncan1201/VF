/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewAnnotationPanel.java
 *
 * Created on Jan 16, 2012, 9:26:43 AM
 */
package com.gas.main.ui.molpane.sitepanel.annotation;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKey;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.core.as.Pozition;
import com.gas.domain.core.as.Qualifier;
import com.gas.domain.core.as.QualifierSet;
import com.gas.domain.core.as.api.IFetureKeyService;
import com.gas.domain.ui.editor.IMolPane;
import com.gas.main.ui.molpane.MolPane;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import org.biojavax.bio.seq.CompoundRichLocation;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;
import org.openide.util.Lookup;

/**

 @author dq
 */
public class AnnPanel extends JPanel {

    private Feture feture;
    private boolean circular;
    private boolean edit;
    private WeakReference<MolPane> molPaneRef;
    private IFetureKeyService fetureKeyService = Lookup.getDefault().lookup(IFetureKeyService.class);
    private String fetureKeyType;
    private String fetureName;
    private JRadioButton undirectedBtn;
    private JRadioButton forwardBtn;
    private JRadioButton reverseBtn;
    private JButton addFetureTypeBtn;
    private JButton delLocBtn;
    private JButton delQualifierBtn;
    private JButton editLocBtn;
    private JButton editQualifierBtn;
    private JComboBox fetureKeyComboBox;
    private JTextField fetureNameField;
    private JList locList;
    private ButtonGroup locOperatorBtnGroup;
    private JButton newLocBtn;
    private JButton locUpBtn;
    private JButton locDownBtn;
    private JButton newQualifierBtn;
    private JRadioButton joinBtn;    
    private JRadioButton orderBtn;
    private JList qualifierList;
    private ButtonGroup strandBtnGroup;
    private DefaultListModel qualifierListModel = new DefaultListModel();
    private DefaultListModel locListModel = new DefaultListModel();
    protected DefaultComboBoxModel comboBoxModel;
    
    private DialogDescriptor dialogDescriptor;
    private NotificationLineSupport notificationLineSupport;            

    /**
     Creates new form NewAnnotationPanel
     */
    public AnnPanel(Feture feture, boolean circular, boolean edit) {
        this.feture = feture;
        this.circular = circular;
        this.edit = edit;
        insertNewFetureKeyIfNeeded();
        initModels(feture);
        initComponents();

        hookupListeners();

        feture2UI(feture);

        AutoCompleteDecorator.decorate(fetureKeyComboBox);
        if (fetureKeyComboBox.getSelectedItem() != null) {
            setFetureKeyType(fetureKeyComboBox.getSelectedItem().toString());
        }
    }
    
    private void insertNewFetureKeyIfNeeded(){
        if(this.feture != null && this.feture.getKey() != null){
            List<String> names = fetureKeyService.getAllNames();
            if(!names.contains(feture.getKey())){
                FetureKey newKey = new FetureKey(feture.getKey());
                fetureKeyService.create(newKey);
            }
        }     
    }
    
    public void setDialogDescriptor(DialogDescriptor dialogDescriptor){
        this.dialogDescriptor = dialogDescriptor;        
        this.notificationLineSupport = this.dialogDescriptor.createNotificationLineSupport();
    }

    private void initModels(Feture feture) {
        if (feture == null) {
            return;
        }
        Lucation luc = feture.getLucation();
        if (luc != null) {
            Iterator<Pozition> itr = luc.getPozitionsItr2();
            while (itr.hasNext()) {
                Pozition poz = itr.next();
                getLocListModel().addElement(poz);
            }
        }

        List<Qualifier> qualifiers = feture.getQualifierSet().getSortedQualifiers();
        for (Qualifier str : qualifiers) {
            getQualifierListModel().addElement(str.toString());
        }
    }

    private void hookupListeners() {
        fetureNameField.getDocument().addDocumentListener(new AnnPanelListeners.FetureNameListener(new WeakReference<AnnPanel>(this)));
        fetureKeyComboBox.addActionListener(new AnnPanelListeners.FetureKeyListener(new WeakReference<AnnPanel>(this)));
        addFetureTypeBtn.addActionListener(new AnnPanelListeners.AddFetureTypeBtnListener(new WeakReference<AnnPanel>(this)));
        forwardBtn.addActionListener(new AnnPanelListeners.StrandBtnListener(this));
        reverseBtn.addActionListener(new AnnPanelListeners.StrandBtnListener(this));
        undirectedBtn.addActionListener(new AnnPanelListeners.StrandBtnListener(this));
        
        qualifierList.addListSelectionListener(new AnnPanelListeners.QualifierListListener(new WeakReference<AnnPanel>(this)));
        delQualifierBtn.addActionListener(new AnnPanelListeners.DelQualifierListener());
        newQualifierBtn.addActionListener(new AnnPanelListeners.NewQualifierListener(new WeakReference<AnnPanel>(this)));
        editQualifierBtn.addActionListener(new AnnPanelListeners.EditQualifierListener(new WeakReference<AnnPanel>(this)));

        locList.addListSelectionListener(new AnnPanelListeners.LocListListener(new WeakReference<AnnPanel>(this)));
        locDownBtn.addActionListener(new AnnPanelListeners.DownBtnListener(new WeakReference<AnnPanel>(this)));
        locUpBtn.addActionListener(new AnnPanelListeners.UpBtnListener(new WeakReference<AnnPanel>(this)));
        newLocBtn.addActionListener(new AnnPanelListeners.NewLocBtnListener(new WeakReference<AnnPanel>(this)));
        editLocBtn.addActionListener(new AnnPanelListeners.EditLocBtnListener(new WeakReference<AnnPanel>(this)));
        delLocBtn.addActionListener(new AnnPanelListeners.DelLocListener(new WeakReference<AnnPanel>(this)));
    }

    public Feture getFeture() {
        return feture;
    }

    public void ui2Feture() {
        if (feture == null) {
            feture = new Feture();
        }

        feture.setKey(fetureKeyType);
        feture.setDisplayName(fetureName);

        ui2qualifiers(feture.getQualifierSet());
        feture.setLucation(ui2Lucation(null));
    }

    private void ui2qualifiers(QualifierSet qualifierSet) {
        qualifierSet.clear();
        qualifierListModel.getSize();
        for (int i = 0; i < qualifierListModel.getSize(); i++) {
            String str = (String) qualifierListModel.get(i);
            qualifierSet.add(str);
        }
    }
    
    protected boolean isCircular(){
        return circular;
    }

    private Lucation ui2Lucation(Lucation ret) {
        if(ret != null){
            ret.clearPozitions();
        }else{
            ret = new Lucation();
        }

        Boolean forward ;
        if(forwardBtn.isSelected()){
            forward = true;
        }else if(reverseBtn.isSelected()){
            forward = false;
        }else{
            forward = null;
        }

        final int size = locListModel.getSize();

        for (int i = 0; i < size; i++) {
            Pozition poz = (Pozition) locListModel.getElementAt(i);
            if(forward == null || forward){
                poz.setRank(i);
            }else{
                poz.setRank(size - i);
            }
            ret.addPozition(poz.clone());
        }
        if (size > 1) {
            if (joinBtn.isSelected()) {
                ret.setTerm(CompoundRichLocation.getJoinTerm().toString());
            } else if (orderBtn.isSelected()) {
                ret.setTerm(CompoundRichLocation.getOrderTerm().toString());
            }
        }

        if (forward != null && forward) {
            ret.setStrand(true);
        } else if (forward != null && !forward) {
            ret.setStrand(false);
        } else if (forward == null) {
            ret.setStrand(null);
        }

        return ret;
    }

    private void feture2UI(Feture feture) {
        if (feture == null) {
            return;
        }
        setFetureKeyType(feture.getKey());
        setFetureName(feture.getDisplayName());
        fetureNameField.setText(getFetureName());
        setSelectedFetureKey(feture.getKey());
        Lucation luc = feture.getLucation();
        Boolean strand = null;
        String term = null;
        if (luc != null) {
            strand = luc.getStrand();
            term = luc.getTerm();
        }
        
        if (strand != null) {
            if (strand) {
                forwardBtn.setSelected(true);
            } else {
                reverseBtn.setSelected(true);
            }
        } else {
            undirectedBtn.setSelected(true);
        }

        if(term != null && term.equals(CompoundRichLocation.getJoinTerm().toString())){
            joinBtn.setSelected(true);
        }else if(term != null && term.equals(CompoundRichLocation.getOrderTerm().toString())){
            orderBtn.setSelected(true);
        }else if(term == null){
            joinBtn.setSelected(true);
        }

    }

    public IMolPane getMolPane() {
        return molPaneRef.get();
    }

    public JList getLocList() {
        return locList;
    }

    public DefaultListModel getLocListModel() {
        return locListModel;
    }

    public void setMolPaneRef(WeakReference<MolPane> molPaneRef) {
        this.molPaneRef = molPaneRef;
    }

    public DefaultListModel getQualifierListModel() {
        return qualifierListModel;
    }

    public void setQualifierListModel(DefaultListModel qualifierListModel) {
        this.qualifierListModel = qualifierListModel;
    }

    public String getFetureName() {
        return fetureName;
    }

    public void setFetureName(String fetureName) {
        this.fetureName = fetureName;
    }

    public String getFetureKeyType() {
        return fetureKeyType;
    }

    public void setFetureKeyType(String fetureKeyType) {
        this.fetureKeyType = fetureKeyType;
    }

    //public DefaultComboBoxModel<FetureKey> getComboBoxModel()
    public DefaultComboBoxModel getComboBoxModel() {
        if (comboBoxModel == null) {
            List<FetureKey> fetureKeys = fetureKeyService.getAll();
            FetureKey[] fetureKeysArray = fetureKeys.toArray(new FetureKey[fetureKeys.size()]);
            comboBoxModel = new DefaultComboBoxModel(fetureKeysArray);
        }
        return comboBoxModel;
    }

    private void setSelectedFetureKey(String str) {
        fetureKeyComboBox.getItemCount();
        for (int i = 0; i < fetureKeyComboBox.getItemCount(); i++) {
            FetureKey fetureKey = (FetureKey) fetureKeyComboBox.getItemAt(i);
            if (fetureKey.getName().equalsIgnoreCase(str)) {
                fetureKeyComboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    //DefaultComboBoxModel<FetureKey> comboBoxModel
    public void setComboBoxModel(DefaultComboBoxModel comboBoxModel) {
        this.comboBoxModel = comboBoxModel;
    }

    private void initComponents() {

        final Insets insets = new Insets(3, 7, 3, 7);              

        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = insets;
        JPanel fetureKeyPanel = createFetureKeyPanel();
        add(fetureKeyPanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = insets;
        JPanel qualifiersPanel = createQualifiersPanel();        
        add(qualifiersPanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = insets;
        JPanel locationPanel = createLocationPanel();
        add(locationPanel, c);
    }

    private JPanel createFetureKeyPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy++;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        ret.add(new JXTitledSeparator("Feature Key"), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        ret.add(new JLabel("Name:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        fetureNameField = new JTextField();
        ret.add(fetureNameField, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        final Component filler = Box.createRigidArea(new Dimension(1, 1));
        ret.add(filler, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        ret.add(new JLabel("Type:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        fetureKeyComboBox = new JComboBox();
        fetureKeyComboBox.setModel(getComboBoxModel());
        ret.add(fetureKeyComboBox, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        addFetureTypeBtn = new FlatBtn(new ImageIcon(ImageHelper.createImage(ImageNames.PLUS_16)), "", true, 8);
        addFetureTypeBtn.setFocusPainted(false);
        addFetureTypeBtn.setIconTextGap(0);
        addFetureTypeBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        addFetureTypeBtn.setOpaque(false);
        ret.add(addFetureTypeBtn, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        ret.add(new JLabel("Strand:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        JPanel strandPanel = createStrandPanel();
        ret.add(strandPanel, c);


        return ret;
    }

    private JPanel createStrandPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        strandBtnGroup = new ButtonGroup();

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        forwardBtn = new JRadioButton();
        forwardBtn.setActionCommand("forward");
        strandBtnGroup.add(forwardBtn);
        forwardBtn.setText("Forward");
        ret.add(forwardBtn, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        reverseBtn = new JRadioButton();
        reverseBtn.setActionCommand("reverse");
        strandBtnGroup.add(reverseBtn);
        reverseBtn.setText("Reverse");
        ret.add(reverseBtn, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        undirectedBtn = new JRadioButton();
        undirectedBtn.setActionCommand("undirected");
        strandBtnGroup.add(undirectedBtn);
        undirectedBtn.setText("Undirected");
        ret.add(undirectedBtn, c);

        return ret;
    }

    private JPanel createQualifiersPanel() {
        JPanel ret = new JPanel(new GridBagLayout());

        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        ret.add(new JXTitledSeparator("Qualifiers"), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.gridheight = 3;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        qualifierList = new JList();
        if(edit){
            UIUtil.setPreferredWidthByPrototype(qualifierList, "note=camR(Confers resisteance to chloramphenicol) ");
        }
        qualifierList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        qualifierList.setModel(getQualifierListModel());
        final int visibleRowCount = Math.max(getQualifierListModel().getSize() + 1, 3);
        qualifierList.setVisibleRowCount(visibleRowCount);        
        JScrollPane scrollPane = new JScrollPane(qualifierList);
        ret.add(scrollPane, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        newQualifierBtn = new JButton();
        newQualifierBtn.setText("New...");
        ret.add(newQualifierBtn, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        editQualifierBtn = new JButton();
        editQualifierBtn.setText("Edit...");
        editQualifierBtn.setEnabled(false);
        ret.add(editQualifierBtn, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        delQualifierBtn = new JButton("Delete");
        ret.add(delQualifierBtn, c);

        return ret;
    }

    private JPanel createLocationPanel() {
        JPanel locationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        locationPanel.add(new JXTitledSeparator("Locations"), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel locListPanel = createLocListPanel();
        locationPanel.add(locListPanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel OperatorPanel = createLocOperatorPanel();
        locationPanel.add(OperatorPanel, c);

        return locationPanel;
    }

    private JPanel createLocListPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 5;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        locList = new JList();
        locList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        locList.setModel(getLocListModel());
        final int visibleRowCount = Math.max(getLocListModel().size() + 1, 3);
        locList.setVisibleRowCount(visibleRowCount);        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(locList);
        ret.add(scrollPane, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;        
        locUpBtn = new JButton("Up");
        locUpBtn.setEnabled(false);
        ret.add(locUpBtn, c);
        
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        locDownBtn = new JButton("Down");
        locDownBtn.setEnabled(false);
        ret.add(locDownBtn, c);        
        
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        newLocBtn = new JButton();
        newLocBtn.setText("New...");
        ret.add(newLocBtn, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        editLocBtn = new JButton();
        editLocBtn.setText("Edit...");
        editLocBtn.setEnabled(false);
        ret.add(editLocBtn, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        delLocBtn = new JButton();
        delLocBtn.setText("Delete");
        delLocBtn.setEnabled(false);
        ret.add(delLocBtn, c);
        return ret;
    }

    private JPanel createLocOperatorPanel() {
        locOperatorBtnGroup = new ButtonGroup();
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = 2;
        ret.add(new JXTitledSeparator("Location Operator"), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        joinBtn = new JRadioButton();
        locOperatorBtnGroup.add(joinBtn);
        joinBtn.setText("Join"); // NOI18N
        joinBtn.setEnabled(false);
        ret.add(joinBtn, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        orderBtn = new JRadioButton();
        locOperatorBtnGroup.add(orderBtn);
        orderBtn.setText("Order"); // NOI18N
        orderBtn.setEnabled(false);
        ret.add(orderBtn, c);
        return ret;
    }
    
    protected JButton getLocUpBtn(){
        return locUpBtn;
    }
    
    protected JButton getLocDownBtn(){
        return locDownBtn;
    }

    public JComboBox getFetureKeyComboBox() {
        return fetureKeyComboBox;
    }

    protected JList getQualifierList() {
        return qualifierList;
    }
    
    protected JButton getDelQualifierBtn(){
        return delQualifierBtn;
    }
    
    protected JButton getEditQualifierBtn(){
        return editQualifierBtn;
    }
    
    protected JButton getDelLocBtn(){
        return delLocBtn;
    }
    
    protected JButton getEditLocBtn(){
        return editLocBtn;
    }
    
    protected JRadioButton getJoinBtn(){
        return joinBtn;
    }
    
    protected JRadioButton getOrderBtn(){
        return orderBtn;
    }

    public void validateInput() {
        if(notificationLineSupport == null || dialogDescriptor == null){
            return ;
        }
        boolean valid = true;
        if (fetureName == null || fetureName.isEmpty()) {            
            notificationLineSupport.setInformationMessage("Feature name cannot be empty!");
            valid = false;
        }
        if (locListModel.isEmpty()) {            
            notificationLineSupport.setInformationMessage("Location cannot be empty!");
            valid = false;
        }

        if(valid){
            notificationLineSupport.clearMessages();
        }
        dialogDescriptor.setValid(valid);
    }

    public ButtonGroup getStrandBtnGroup() {
        return strandBtnGroup;
    }
}
