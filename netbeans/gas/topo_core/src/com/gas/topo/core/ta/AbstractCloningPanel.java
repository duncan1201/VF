/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.ta;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.common.ui.util.Unicodes;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.topo.core.openmol.IChooseDataPanelValidator;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.jdesktop.swingx.JXHyperlink;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;

/**
 *
 * @author dq
 */
public abstract class AbstractCloningPanel extends JPanel {

    private DialogDescriptor dialogDescriptor;
    private NotificationLineSupport notificationLineSupport;
    private SeqEndViewer insertViewer;
    private SeqEndViewer vectorViewer;
    private JPanel insertPanel;
    private JPanel vectorPanel;
    private JPanel insertContentPanel;
    private JPanel vectorContentPanel;
    private static final String INSERT_INSTRUCTION = "INSERT_INSTRUCTION";
    private static final String INSERT_SNAPSHOTS = "INSERT_SNAPSHOTS";
    private static final String VECTOR_INSTRUCTION = "VECTOR_INSTRUCTION";
    private static final String VECTOR_SNAPSHOTS = "VECTOR_SNAPSHOTS";

    public AbstractCloningPanel() {
        setOpaque(true);
        setBackground(Color.WHITE);
        LayoutManager layout = null;
        layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        insertPanel = createInsertPanel();
        add(insertPanel, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        add(new JSeparator(), c);

        c = new GridBagConstraints();
        c.gridx = 0;       
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        vectorPanel = createVectorPanel();
        add(vectorPanel, c);
    }

    public void setDialogDescriptor(DialogDescriptor dialogDescriptor) {
        this.dialogDescriptor = dialogDescriptor;
        this.notificationLineSupport = this.dialogDescriptor.createNotificationLineSupport();
    }
    
    public void validateInput(){
        if(notificationLineSupport == null){
            return;
        }
        AnnotatedSeq insert = getInsert();
        AnnotatedSeq vector = getVector();
        if(insert == null){
            this.notificationLineSupport.setInformationMessage("Please select an insert");
            this.dialogDescriptor.setValid(false);
        }else if(vector == null){
            this.notificationLineSupport.setInformationMessage("Please select a vector");
            this.dialogDescriptor.setValid(false);
        }else{
            this.notificationLineSupport.clearMessages();
            this.dialogDescriptor.setValid(true);
        }
    }

    private JPanel createVectorPanel() {

        vectorViewer = new SeqEndViewer(true);
        vectorViewer.setFont(FontUtil.getDefaultMSFont());
        final FontMetrics fm = FontUtil.getFontMetrics(vectorViewer);
        final int charWidth = fm.charWidth('A');
        final Insets insets = new Insets(0, 2 * charWidth, 0, 2 * charWidth);
        final Dimension dSize = vectorViewer.getDesiredSize();
        vectorViewer.setPreferredSize(new Dimension(dSize.width + insets.left + insets.right, dSize.height + insets.top + insets.bottom));

        CardLayout cardLayout = new CardLayout();
        vectorContentPanel = new JPanel(cardLayout);
        vectorContentPanel.setOpaque(false);
        vectorContentPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.LIGHT_GRAY), getVectorContentPanelTitle(), TitledBorder.LEADING, TitledBorder.TOP));
        vectorContentPanel.add(createVectorInstructions(), VECTOR_INSTRUCTION);
        vectorContentPanel.add(vectorViewer, VECTOR_SNAPSHOTS);
        cardLayout.show(vectorContentPanel, VECTOR_INSTRUCTION);
                
        JPanel ctrlPanel = createVectorCtrlPanel();                       

        JPanel ret = new JPanel(new BorderLayout());
        UIUtil.setDefaultBorder(ret);
        ret.setOpaque(false);     
        ret.add(vectorContentPanel, BorderLayout.CENTER);

        ret.add(ctrlPanel, BorderLayout.EAST);
        return ret;
    }
    
    protected abstract String getVectorContentPanelTitle();
    protected abstract String getVectorContentPanelTitle(AnnotatedSeq vector);
  
    
    private JPanel createInsertCtrlPanel(){
        JPanel ret = new JPanel(new GridBagLayout());
        ret.setOpaque(false);
        GridBagConstraints c = null;
        
        final FontMetrics fm = FontUtil.getFontMetrics(this);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        Component comp = Box.createRigidArea(new Dimension(1, Math.round(fm.getHeight() * 1.5f)));
        ret.add(comp, c);        
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        JButton addBtn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.FOLDER_OPEN_24));
        addBtn.setAction(new AbstractCloningPanelActions.OpenInsertAction(this, "", ImageHelper.createImageIcon(ImageNames.FOLDER_OPEN_24)));
        ret.add(addBtn, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1.0;
        comp = Box.createRigidArea(new Dimension(1,1));
        ret.add(comp, c);
                        
        return ret;
    }
    
    private JPanel createVectorCtrlPanel(){
        JPanel ret = new JPanel(new GridBagLayout());
        ret.setOpaque(false);
        GridBagConstraints c = null;
        
        final FontMetrics fm = FontUtil.getFontMetrics(this);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        Component comp = Box.createRigidArea(new Dimension(1, Math.round(fm.getHeight() * 1.5f)));
        ret.add(comp, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        JButton addBtn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.FOLDER_OPEN_24));
        addBtn.setAction(new AbstractCloningPanelActions.OpenVectorAction(this, "", ImageHelper.createImageIcon(ImageNames.FOLDER_OPEN_24)));      
        ret.add(addBtn, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1.0;
        comp = Box.createRigidArea(new Dimension(1,1));
        ret.add(comp, c);        
        
        return ret;
    }

    private JPanel createVectorInstructions() {
        JPanel ret = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ret.setOpaque(false);
        ret.add(new JLabel("Please click "));
        AbstractCloningPanelActions.OpenVectorAction openAction = new AbstractCloningPanelActions.OpenVectorAction(this, "here", null);
        JXHyperlink link = new JXHyperlink(openAction);
        link.setFocusable(false);
        ret.add(link);        
        ret.add(new JLabel(String.format(" to choose a TOPO%s vector", Unicodes.TRADEMARK)));
        return ret;
    }

    private JPanel createInsertPanel() {
        insertViewer = new SeqEndViewer(false);
        insertViewer.setFont(FontUtil.getDefaultMSFont());
        final FontMetrics fm = FontUtil.getFontMetrics(insertViewer);
        final int charWidth = fm.charWidth('A');
        final Insets insets = new Insets(0, 6 * charWidth, 0, 6 * charWidth);    
        final Dimension dSize = insertViewer.getDesiredSize();
        insertViewer.setPreferredSize(new Dimension(dSize.width + insets.left + insets.right, dSize.height + insets.top + insets.bottom));
        
        CardLayout cardLayout = new CardLayout(); 
        insertContentPanel = new JPanel(cardLayout);
        insertContentPanel.setOpaque(false);        
        insertContentPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.LIGHT_GRAY), getInsertContentPanelTitle(),TitledBorder.LEADING, TitledBorder.TOP));
        insertContentPanel.add(createInsertInstrutions(), INSERT_INSTRUCTION);
        insertContentPanel.add(insertViewer, INSERT_SNAPSHOTS);

        cardLayout.show(insertContentPanel, INSERT_INSTRUCTION);
        
        JPanel ctrlPanel = createInsertCtrlPanel();
        
        JPanel ret = new JPanel(new BorderLayout());
        ret.setOpaque(false);
        UIUtil.setDefaultBorder(ret);
        ret.add(insertContentPanel, BorderLayout.CENTER);
        ret.add(ctrlPanel, BorderLayout.EAST);
        
        return ret;
    }
    
    protected abstract String getInsertContentPanelTitle();     
    protected abstract String getInsertContentPanelTitle(AnnotatedSeq insert);

    /**
     * @param insert validated insert
     */
    public void setInsert(AnnotatedSeq insert) {
        insertViewer.setAs(insert);  
        insertContentPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.LIGHT_GRAY), getInsertContentPanelTitle(insert), TitledBorder.LEADING, TitledBorder.TOP));
        showInsertSnapshot();
    }
    
    public AnnotatedSeq getInsert(){
        return insertViewer.getAs();
    }
    
    public AnnotatedSeq getVector(){
        return vectorViewer.getAs();        
    }

    /**
     * @param vector validated vector
     */
    public void setVector(AnnotatedSeq vector) {
        vectorViewer.setAs(vector);        
        vectorContentPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.LIGHT_GRAY), getVectorContentPanelTitle(vector), TitledBorder.LEADING, TitledBorder.TOP));
        showVectorSnapshot();
    }

    private void showInsertSnapshot() {
        CardLayout cardLayout = (CardLayout) insertContentPanel.getLayout();
        cardLayout.show(insertContentPanel, INSERT_SNAPSHOTS);
    }

    private void showVectorSnapshot() {
        CardLayout cardLayout = (CardLayout) vectorContentPanel.getLayout();
        cardLayout.show(vectorContentPanel, VECTOR_SNAPSHOTS);
    }

    private JPanel createInsertInstrutions() {
        JPanel ret = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ret.setOpaque(false);
        ret.add(new JLabel("Please click "));
        AbstractCloningPanelActions.OpenInsertAction openAction = new AbstractCloningPanelActions.OpenInsertAction(this, "here", null);
        JXHyperlink link = new JXHyperlink(openAction);
        link.setFocusable(false);
        ret.add(link);
        ret.add(new JLabel(" to choose an insert"));

        return ret;
    }
    
    public abstract IChooseDataPanelValidator[] getInsertValidators();
    
    public abstract IChooseDataPanelValidator[] getVectorValidators();
}
