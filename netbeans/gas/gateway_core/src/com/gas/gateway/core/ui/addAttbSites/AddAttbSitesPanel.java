/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.addAttbSites;

import com.gas.common.ui.util.UIUtil;
import com.gas.gateway.core.service.api.PrimerAdapter;
import com.gas.gateway.core.service.api.RecomType;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;

/**
 *
 * @author dq
 */
public class AddAttbSitesPanel extends JPanel {

    private DialogDescriptor dialogDescriptor;
    private NotificationLineSupport notificationLineSupport;
    private SensePanel sensePanel;
    private AntiSensePanel antisensePanel;
    protected RecomTypePanel recomTypePanel;
    private AttbSitesPanel attbSitesPanel;
    private PreviewPanel previewPanel;
    private JCheckBox adapterPrimers;
    private JCheckBox templateSpecificPrimers;

    public AddAttbSitesPanel() {
        initComponents();

        hookupListeners();

        recomTypePanel.setRecombType(RecomType.GW);
    }

    private void initComponents() {
        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        recomTypePanel = new RecomTypePanel();
        add(recomTypePanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        attbSitesPanel = new AttbSitesPanel();
        add(attbSitesPanel, c);

        JPanel additionsPanel = createAdditionsPanel();
        c = new GridBagConstraints();
        c.gridx = 0;       
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(additionsPanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;       
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        previewPanel = new PreviewPanel();
        add(previewPanel, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel createPrimersPanel = createPrimersPanel();
        add(createPrimersPanel, c);
        
        sensePanel.updateUI(getLeftPrimerAdapter());
        antisensePanel.updateUI(getRightPrimerAdapter());
    }
    
    private JPanel createAdditionsPanel(){
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
                
        sensePanel = new SensePanel();
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;
        c.weighty = 1;
        ret.add(sensePanel, c);
        
        antisensePanel = new AntiSensePanel();
        c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;        
        c.weighty = 1;
        ret.add(antisensePanel, c);
        
        return ret;
    }
    
    public boolean isAdapterPrimers(){
        return adapterPrimers.isSelected();
    }
    
    public boolean isTemplateSpecificPrimers(){
        return templateSpecificPrimers.isSelected();
    }
    
    private JPanel createPrimersPanel(){
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        
        c = new GridBagConstraints();
        adapterPrimers = new JCheckBox("Generate adapter primers", true);
        ret.add(adapterPrimers, c);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        templateSpecificPrimers = new JCheckBox("Generate template-specific primers", true);
        ret.add(templateSpecificPrimers, c);
        
        return ret;
    }

    SensePanel getSensePanel() {
        return sensePanel;
    }

    AntiSensePanel getAntisensePanel() {
        return antisensePanel;
    }
    
    protected void updatePreview() {
        previewPanel.updatePreview();
    }

    public void showRecombinationType(RecomType recombType) {
        attbSitesPanel.showRecombinationType(recombType);
    }

    public PrimerAdapter getLeftPrimerAdapter() {
        return attbSitesPanel.getLeftPrimerAdapter();
    }

    public PrimerAdapter getRightPrimerAdapter() {
        return attbSitesPanel.getRightPrimerAdapter();
    }

    public Boolean isStartCodonIncluded() {
        return sensePanel.isStartCodonIncluded();
    }

    public Boolean isSDIncluded() {
        return sensePanel.isSdIncluded();
    }

    public Boolean isStopCodonIncluded() {
        return antisensePanel.isStopCodonIncluded();
    }
    
    public boolean isFuseForwardPrimer(){
        return sensePanel.isFuse();
    }
    
    public boolean isFuseReversePrimer(){
        return antisensePanel.isFuse();
    }

    public Boolean isKozakIncluded() {
        return sensePanel.isKozakIncluded();
    }

    private void hookupListeners() {
    }

    public void setRecombinationType(RecomType recombinationType) {
        if (recombinationType == null) {
            throw new IllegalArgumentException();
        }
        recomTypePanel.setRecombType(recombinationType);
        showRecombinationType(recombinationType);
        updatePreview();
        sensePanel.updateUI(getLeftPrimerAdapter());
        antisensePanel.updateUI(getRightPrimerAdapter());
    }

    public void validateInput() {
    }

    public void setDialogDescriptor(DialogDescriptor dialogDescriptor) {
        this.dialogDescriptor = dialogDescriptor;
        notificationLineSupport = this.dialogDescriptor.createNotificationLineSupport();
    }
}
