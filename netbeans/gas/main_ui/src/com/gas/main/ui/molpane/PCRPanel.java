/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane;

import com.gas.common.ui.button.WideComboBox;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.primer3.Oligo;
import com.gas.domain.core.primer3.OligoElement;
import com.gas.domain.core.primer3.P3Output;
import java.awt.BorderLayout;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;

/**
 *
 * @author dq
 */
public class PCRPanel extends JPanel {

    P3Output p3output;
    NotificationLineSupport notificationLineSupport;
    DialogDescriptor dialogDescriptor;
    WeakReference<JTextField> nameFieldRef;
    WeakReference<JComboBox> leftComboRef;
    WeakReference<JComboBox> rightComboRef;
    Integer selectedIndex;

    public PCRPanel(P3Output p3output, String nameProposed) {
        super(new GridBagLayout());
        this.p3output = p3output;
        
        Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridy = gridy++;
        JPanel topPanel = createTopPanel(nameProposed);
        add(topPanel, c);
        
        hookupListeners();
    }
    
    public OligoElement getReversePrimer(){
        int rightIndex = leftComboRef.get().getSelectedIndex();
        OligoElement oe = p3output.getOligoByNo(rightIndex).getRight();
        return oe;    
    }

    public void setSelectedIndex(Integer selectedIndex) {
        Integer old = this.selectedIndex;
        this.selectedIndex = selectedIndex;
        firePropertyChange("selectedIndex", old, this.selectedIndex);
    }
    
    public OligoElement getForwardPrimer(){
        int leftIndex = leftComboRef.get().getSelectedIndex();
        OligoElement oe = p3output.getOligoByNo(leftIndex).getLeft();
        return oe;
    }
    
    public String getNewName(){
        return nameFieldRef.get().getText();
    }
    
    private void hookupListeners(){
        nameFieldRef.get().getDocument().addDocumentListener(new PCRPanelListeners.NameFieldListener(this));
        addPropertyChangeListener(new PCRPanelListeners.PtyListener());
    }
    
    public void setDialogDescriptor(DialogDescriptor dialogDescriptor){
        this.dialogDescriptor = dialogDescriptor;
        notificationLineSupport = dialogDescriptor.createNotificationLineSupport();
    }
    
    void validateInput(){
        String name = nameFieldRef.get().getText();
        boolean valid = true;
        if(name.isEmpty()){
            notificationLineSupport.setInformationMessage("Name cannot be empty");            
            valid = false;
        }
        dialogDescriptor.setValid(valid);
    }

    StringList createComboContents(List<Oligo> oligos, boolean forward) {
        NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        
        NumberFormat nfPercent = NumberFormat.getPercentInstance(Locale.ENGLISH);
        nfPercent.setMaximumFractionDigits(2);
        nfPercent.setMinimumFractionDigits(2);
        
        String template = "%s (TM:%s, GC:%s, Self Complementarity:%s, 3' Self Complementarity:%s, Hairpin:%s)";       
        StringList ret = new StringList();
        Iterator<Oligo> itr = oligos.iterator();
        while (itr.hasNext()) {
            Oligo oligo = itr.next();
            OligoElement oe ;
            if(forward){
                oe = oligo.getLeft();
            }else{
                oe = oligo.getRight();
            }
            if(oe == null){
                continue;
            }
            String ordinal = MathUtil.toOrdinal(oligo.getNo() + 1);
            String tmStr = nf.format(oe.getTm());
            String gcStr = nfPercent.format(oe.getGc()/100);
            String selfAny = nf.format(oe.getSelfAny());
            String selfEnd = nf.format(oe.getSelfEnd());
            String hairpinStr = nf.format(oe.getHairpin());
            ret.add(String.format(template, ordinal, tmStr, gcStr, selfAny, selfEnd, hairpinStr));
        }
        Collections.sort(ret);
        return ret;
    }        

    JPanel createTopPanel(String nameProposed) {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridy = gridy;
        ret.add(new JLabel("Name:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JTextField nameField = new JTextField();
        nameFieldRef = new WeakReference<JTextField>(nameField);
        nameField.setText(nameProposed);
        UIUtil.setPreferredWidthByPrototype(nameField, "A very long name name name");
        ret.add(nameField, c);

        c = new GridBagConstraints();
        c.gridwidth = 2;
        c.gridy = gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel tmp = createPrimersPanel();
        ret.add(tmp, c);
        return ret;
    }

    private JPanel createPrimersPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;
        
        c = new GridBagConstraints();
        c.gridy = gridy;
        ret.add(new JLabel("Forward Primer:"), c);
        
        c = new GridBagConstraints();
        c.gridy = gridy++;
        JPanel tmp = new JPanel(new BorderLayout());
        StringList stringList = createComboContents(p3output.getSortedOligos(), true);        
        WideComboBox leftCombo = new WideComboBox(stringList.toArray(new String[stringList.size()]));
        leftComboRef = new WeakReference<JComboBox>(leftCombo);
        tmp.add(leftCombo, BorderLayout.CENTER);    
        final FontMetrics fm = FontUtil.getDefaultFontMetrics();
        final int widthStr = fm.stringWidth("1st Forward Primer Arrow");
        UIUtil.setPreferredWidth(tmp, widthStr);        
        ret.add(tmp, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        ret.add(new JLabel("Reverse Primer:"), c);
        
        c = new GridBagConstraints();
        c.gridy = gridy;
        tmp = new JPanel(new BorderLayout());
        stringList = createComboContents(p3output.getSortedOligos(), false);
        WideComboBox rightCombo = new WideComboBox(stringList.toArray(new String[stringList.size()]));
        rightComboRef = new WeakReference<JComboBox>(rightCombo);
        tmp.add(rightCombo, BorderLayout.CENTER);
        UIUtil.setPreferredWidth(tmp, widthStr);
        ret.add(tmp, c);
        return ret;
    }
}
