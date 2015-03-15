/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.edit;

import com.gas.common.ui.combo.ComboBoxToolTipRenderer;
import com.gas.common.ui.misc.DNADocumentFilter;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.ColorUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.geneticCode.api.Codon;
import com.gas.domain.core.geneticCode.api.CodonList;
import com.gas.domain.core.geneticCode.api.GeneticCodeTable;
import com.gas.domain.core.geneticCode.api.GeneticCodeTableList;
import com.gas.domain.core.geneticCode.api.IGeneticCodeTableService;
import com.gas.domain.core.primer3.OligoElement;
import com.gas.domain.core.ren.IRENListService;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RENList;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import static java.awt.image.ImageObserver.WIDTH;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class EditPrimerPanel extends JPanel implements IEditPrimerUI{
    
    private DialogDescriptor dialogDescriptor;
    private NotificationLineSupport notificationLineSupport;
    private IRENListService renListSvc = Lookup.getDefault().lookup(IRENListService.class);
    private IGeneticCodeTableService codeTableSvc = Lookup.getDefault().lookup(IGeneticCodeTableService.class);
    
    private OligoElement oligoElement;
    
    private JTextPane extPane;
    private JTextPane primerPane;
    
    private JRadioButton codonBtn;
    private JRadioButton enzymesBtn;
    private JRadioButton customBtn;        
    
    private JComboBox geneticCodeCombo;
    private JComboBox aminoAcidsCombo;
    private JComboBox codonCombo;        
    
    private JComboBox renListCombo;
    private JComboBox renCombo;
    private JComboBox renBasesCombo;
    private JTextField customBasesField;
    
    private JButton attachBtn;
    private JButton replaceBtn;
    
    private static Insets INSETS = new Insets(0, UIUtil.getDefaultInsets().left * 3, 0, 0);
    
    static final String CMD_ENZYMES_OPTION = "cmdEmzymesOption";
    static final String CMD_CUSTOM_OPTION = "cmdCustomOption";
    static final String CMD_GENETIC_CODE = "cmdGeneticCode";
    static final String CMD_AMINO_ACID = "cmdAminoAcid";
    static final String CMD_CODON = "cmdCodon";
    static final String CMD_RENLIST = "cmdRENList";
    static final String CMD_REN = "cmdREN";
    static final String CMD_REN_BASES = "cmdRENBases";
    static final String CMD_ATTACH = "cmdAttach";
    static final String CMD_REPLACE = "cmdReplace";
    
    public EditPrimerPanel(Feture feture){
        oligoElement = (OligoElement)feture.getData();
        createComponents();
        initComponents();
        hookupListeners();
    }
    
    private void hookupListeners(){
        EditPrimerPanelListeners.ComboAndBtnListeners listener = new EditPrimerPanelListeners.ComboAndBtnListeners(this);
        
        geneticCodeCombo.addActionListener(listener);
        aminoAcidsCombo.addActionListener(listener); 
        codonCombo.addActionListener(listener);
        
        renListCombo.addActionListener(listener);
        renCombo.addActionListener(listener);
        
        attachBtn.addActionListener(listener);
        replaceBtn.addActionListener(listener);
        
        EditPrimerPanelListeners.CustomBaseFieldListener docListener = new EditPrimerPanelListeners.CustomBaseFieldListener(this);
        customBasesField.getDocument().addDocumentListener(docListener);
    }
    
    private void initComponents(){
        ButtonGroup bGroup = new ButtonGroup();
        bGroup.add(codonBtn);
        bGroup.add(enzymesBtn);
        bGroup.add(customBtn);
        
        codonBtn.setSelected(true);
        
        GeneticCodeTableList codeTableList = codeTableSvc.getTables();        
        ComboBoxModel model = new DefaultComboBoxModel(codeTableList.toArray());
        geneticCodeCombo.setModel(model);
        
        updateAminoAcidCombo();
        updateCodonCombo();
        
        List<RENList> renLists = renListSvc.getAll();
        renListCombo.setModel(new DefaultComboBoxModel(RENListComboItem.create(renLists)));
        
        updateRenCombo();
        
        RENComboItem renComboItem = (RENComboItem)renCombo.getSelectedItem();
        REN ren = renComboItem.getRen();
        boolean isAmbiguous = ren.isAmbiguous();
        List<String> bases = BioUtil.getNonAmbiguousDNAs(ren.getRecognitionSite());
        if(isAmbiguous){
            bases.add(0, ren.getRecognitionSite());
        }
        UIUtil.setPreferredWidthByPrototype(renBasesCombo, ren.getRecognitionSite() + "A");
        renBasesCombo.setModel(new DefaultComboBoxModel(bases.toArray(new String[bases.size()])));
    }
    
    @Override
    public void setDialogDescriptor(DialogDescriptor dialogDescriptor){
        this.dialogDescriptor = dialogDescriptor;
        this.notificationLineSupport = this.dialogDescriptor.createNotificationLineSupport();
    }
        
    
    private void createComponents(){
        setLayout(new GridBagLayout());
        GridBagConstraints c;                
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel primerSeqPanel = createPrimerSeqPanel();
        add(primerSeqPanel, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel editPanel = createEditPanel();
        add(editPanel, c);

    }
    
    private JPanel createPrimerSeqPanel(){
        JPanel ret = new JPanel();
        ret.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Primer Sequence", TitledBorder.LEFT, TitledBorder.TOP));
        ret.setLayout(new GridBagLayout());
        GridBagConstraints c;
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        JLabel five = new JLabel("5'");
        five.setHorizontalAlignment(SwingConstants.CENTER);

        UIUtil.setPreferredWidthByPrototype(five, "AB");
        five.setBackground(ColorCnst.MELLOW_APRICOT);
        five.setOpaque(true);
        ret.add(five, c);
        
        c = new GridBagConstraints();        
        c.weighty = 1;
        extPane = new JTextPane();
        extPane.setText(oligoElement.getTail());        
        extPane.setEditable(true);        
        ((AbstractDocument)extPane.getDocument()).setDocumentFilter(new DNADocumentFilter());
        FontMetrics fm = FontUtil.getFontMetrics(extPane);
        UIUtil.setPreferredWidth(extPane, fm.stringWidth("ATCGATCDATCD"));
        ret.add(extPane, c);
        
        c = new GridBagConstraints();        
        c.weighty = 1;
        ret.add(new JLabel("-"), c);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        primerPane = new JTextPane();
        primerPane.setCaret(new HighlightCaret());
        primerPane.setEditable(false);
        primerPane.setContentType("text/html");
        primerPane.setFont(FontUtil.getDefaultMSFont(primerPane.getFont().getSize2D()));
        primerPane.setText(oligoElement.getSeq());
        ret.add(primerPane, c);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        JLabel three = new JLabel("3'");
        three.setHorizontalAlignment(SwingConstants.CENTER);
        UIUtil.setPreferredWidthByPrototype(three, "AB");
        three.setBackground(ColorCnst.MELLOW_APRICOT);
        three.setOpaque(true);
        ret.add(three, c);
        
        return ret;
    }
    
    private JPanel createEditPanel(){
        JPanel ret = new JPanel();
        ret.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Edit", TitledBorder.LEFT, TitledBorder.TOP));
        ret.setLayout(new GridBagLayout());
        GridBagConstraints c;
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel condonsPanel = createCodonsPanel();
        ret.add(condonsPanel, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel enzymesPanel = createEnzymesPanel();
        ret.add(enzymesPanel, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel customPanel = createCustomPanel();
        ret.add(customPanel, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel btnPanel = createBtnsPanel();
        ret.add(btnPanel, c);
        
        return ret;
    }
    
    private JPanel createBtnsPanel(){
        JPanel ret = new JPanel(new GridLayout(1, 2));
        
        attachBtn = new JButton("Attach to 5' terminus");
        attachBtn.setActionCommand(CMD_ATTACH);
        attachBtn.setFocusable(false);
        ret.add(attachBtn);
        
        replaceBtn = new JButton("Replace selected primer bases");
        replaceBtn.setActionCommand(CMD_REPLACE);
        replaceBtn.setFocusable(false);
        ret.add(replaceBtn);
        return ret;
    }
    
    private JPanel createEnzymesPanel(){
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        
        c = new GridBagConstraints();
        enzymesBtn = new JRadioButton("Site:");   
        enzymesBtn.setFocusable(false);
        ret.add(enzymesBtn, c);
        
        c = new GridBagConstraints();
        renListCombo = new JComboBox();
        renListCombo.setActionCommand(CMD_RENLIST);
        renListCombo.setFocusable(false);
        ret.add(renListCombo, c);
        
        c = new GridBagConstraints();
        renCombo = new JComboBox();
        renCombo.setActionCommand(CMD_REN);
        renCombo.setFocusable(false);
        ret.add(renCombo, c);
        
        c = new GridBagConstraints();
        renBasesCombo = new JComboBox();
        Font font = renBasesCombo.getFont();
        renBasesCombo.setFont(FontUtil.getDefaultMSFont(font.getSize2D()));
        renBasesCombo.setFocusable(false);
        ret.add(renBasesCombo, c);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        ret.add(filler, c);
        
        return ret;
    }
    
    private JPanel createCustomPanel(){
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        
        c = new GridBagConstraints();
        customBtn = new JRadioButton("Custom bases");
        customBtn.setFocusable(false);
        ret.add(customBtn, c);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;       
        ret.add(Box.createRigidArea(new Dimension(1, 1)), c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.insets = INSETS;
        customBasesField = new JTextField();
        ((AbstractDocument)customBasesField.getDocument()).setDocumentFilter(new DNADocumentFilter());
        UIUtil.setPreferredWidthByPrototype(customBasesField, "AAAAAAAAAAAAAA");
        ret.add(customBasesField, c);
        
        
        return ret;
    }
    
    private JPanel createCodonsPanel(){
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridx = 0, gridy = 0;
        
        c = new GridBagConstraints();
        c.gridx = gridx++;
        c.gridy = gridy;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;        
        codonBtn = new JRadioButton("Choose codon");
        codonBtn.setFocusable(false);
        ret.add(codonBtn, c);
        
        
        c = new GridBagConstraints();
        c.gridx = gridx;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        ret.add(filler, c);        
        
        
        gridx = 0;
        c = new GridBagConstraints();
        c.gridx = gridx++;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        c.insets = INSETS;
        ret.add(new JLabel("Genetic code:"), c);
        
        c = new GridBagConstraints();
        c.gridx = gridx++;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        geneticCodeCombo = new JComboBox();
        geneticCodeCombo.setActionCommand(CMD_GENETIC_CODE);
        geneticCodeCombo.setRenderer(new ComboBoxToolTipRenderer());
        geneticCodeCombo.setFocusable(false);
        UIUtil.setPreferredWidthByPrototype(geneticCodeCombo, "1. Invertebrate Mitochondrial ABC");
        ret.add(geneticCodeCombo, c);
        
        c = new GridBagConstraints();
        c.gridx = gridx++;
        c.gridy = gridy;
        ret.add(new JLabel("Amino Acid:"), c);
        
        c = new GridBagConstraints();
        c.gridx = gridx++;
        c.gridy = gridy++;
        aminoAcidsCombo = new JComboBox();
        aminoAcidsCombo.setActionCommand(CMD_AMINO_ACID);
        aminoAcidsCombo.setFocusable(false);
        ret.add(aminoAcidsCombo, c);

        // row 2
        gridx = 0;
        c = new GridBagConstraints();
        c.gridx = gridx++;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        ret.add(new JLabel("Codon:"), c);        
        
        c = new GridBagConstraints();
        c.gridx = gridx++;
        c.gridy = gridy;
        c.gridwidth = 2;
        codonCombo = new JComboBox();
        codonCombo.setActionCommand(CMD_CODON);
        codonCombo.setFocusable(false);
        c.anchor = GridBagConstraints.WEST;
        UIUtil.setPreferredWidthByPrototype(codonCombo, "ATCG");
        ret.add(codonCombo, c);
        
        return ret;
    }

    @Override
    public void updateCodonCombo() {
        
        GeneticCodeTable codeTable = (GeneticCodeTable)geneticCodeCombo.getSelectedItem();
        
        AminoAcidComboItem selectedItem = (AminoAcidComboItem)aminoAcidsCombo.getSelectedItem();
        Character selectedAA = selectedItem.getAminoAcid();
        CodonList codonList = codeTable.getCodons(selectedAA);
        codonCombo.setModel(new DefaultComboBoxModel(codonList.toArray()));   
                
    }

    @Override
    public void updateAminoAcidCombo() {        
        GeneticCodeTable codeTable = (GeneticCodeTable)geneticCodeCombo.getSelectedItem();  
        Set<Character> aminoAcids = codeTable.getAminoAcids();
        AminoAcidComboItem[] items = AminoAcidComboItem.create(aminoAcids, AminoAcidComboItem.DISPLAY.THREE_LETTER_ONLY);        
        aminoAcidsCombo.setModel(new DefaultComboBoxModel(items));       
    }

    @Override
    public void updateRenCombo() {
        RENListComboItem item = (RENListComboItem)renListCombo.getSelectedItem();
        RENList renList = item.getRenList();
        RENComboItem[] items = RENComboItem.create(renList);
        renCombo.setModel(new DefaultComboBoxModel(items));
        enzymesBtn.setSelected(true);
        
    }

    @Override
    public void updateRenBasesCombo() {
        RENComboItem item = (RENComboItem)renCombo.getSelectedItem();
        REN ren = item.getRen();
        renBasesCombo.setModel(new DefaultComboBoxModel(new String[]{ren.getRecognitionSite()}));
    }
    
    private String getChosenSeq(){
        if(codonBtn.isSelected()){
            Codon item = (Codon)codonCombo.getSelectedItem();
            return item.getNucleotides();
        }else if(enzymesBtn.isSelected()){
            String item = (String)renBasesCombo.getSelectedItem();
            return item;
        }else if(customBtn.isSelected()){
            return customBasesField.getText();
        }else{
            return null;
        }
    }

    @Override
    public void attachClicked() {          
        final String newTail = getChosenSeq() + extPane.getText();
        extPane.setText(newTail);
        
        //oligoElement.setTail(newTail);
    }
    
    @Override
    public void replaceClicked(){
        boolean valid = true;
        String msg = "";
        String chosenSeq = getChosenSeq();
        int selectionStart = primerPane.getSelectionStart();
        int selectionEnd = primerPane.getSelectionEnd();
        if(selectionStart == selectionEnd){
            valid = false;
            msg = "Please select bases to replace";            
        }else {
            String selectedText = primerPane.getSelectedText();            
            if(selectedText.length() != chosenSeq.length()){
                valid = false;
                msg = "The length of the selected bases does not match that of the replacing bases";
            }
        }        
        
        if(!valid){            
            notificationLineSupport.setInformationMessage(msg);
        }else{
            notificationLineSupport.clearMessages();            
            final int length = primerPane.getDocument().getLength();
            try {
                String text = primerPane.getDocument().getText(0, length);
                StringBuilder newText = new StringBuilder();
                newText.append("<html>");
                newText.append(text.substring(0, selectionStart));
                newText.append(String.format("<font color=#%s>", ColorUtil.toHex(ColorCnst.CRIMSON)));
                newText.append(chosenSeq);
                newText.append("</font>");
                if(selectionEnd < text.length() - 1){
                    newText.append(text.substring(selectionEnd));
                }
                newText.append("</html>");
                primerPane.setText(newText.toString());
                //oligoElement.setSeq(primerPane.getDocument().getText(0, length).trim());
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
            
        }
        //dialogDescriptor.setValid(valid);

    }


    @Override
    public String getExtension() {
        final int length = extPane.getDocument().getLength();
        String ret = null;
        try {
            ret = extPane.getDocument().getText(0, length).trim();
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    @Override
    public void selectCustomPanel() {
        customBtn.setSelected(true); 
    }

    @Override
    public String getPrimerSeq() {
        final int length = primerPane.getDocument().getLength();
        String ret = null;
        try {
            ret = primerPane.getDocument().getText(0, length).trim();            
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    @Override
    public void selectEnzymesPanel() {
        enzymesBtn.setSelected(true);
    }

    @Override
    public void selectCodonsPanel() {
        codonBtn.setSelected(true);
    }
}
