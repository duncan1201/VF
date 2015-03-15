/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.pdb.core.ui;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.pdb.PDBDoc;
import com.gas.domain.core.pdb.util.PDBWriter;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.AbstractSavableEditor;
import com.gas.domain.ui.editor.pdb.api.IPDBEditor;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.domain.ui.misc.Helper;
import java.awt.BorderLayout;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.util.lookup.ServiceProvider;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//com.gas.pdb.ui//PDB//EN",
autostore = false)
@TopComponent.Description(preferredID = "PDBTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@TopComponent.OpenActionRegistration(displayName = "#CTL_PDBAction",
preferredID = "PDBTopComponent")
@ServiceProvider(service = IPDBEditor.class)
public class PDBEditor extends AbstractSavableEditor implements IPDBEditor {

    private PDBDoc pdbDoc;
    private JMolPnl jmolPnl;

    public PDBEditor() {
        initComponents();

        setIcon(ImageHelper.createImage(ImageNames.STRUCTURE_16));
    }

    @Override
    public PDBDoc getPdbDoc() {
        return pdbDoc;
    }

    @Override
    public void setPdbDoc(PDBDoc pdbDoc) {
        this.pdbDoc = pdbDoc;
        String str = PDBWriter.toString(pdbDoc);
        jmolPnl.openStringInline(str);
        UIUtil.setTopCompName(this, pdbDoc.getPdbId());
        UIUtil.setTopCompToolTip(this, pdbDoc.getName());
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
        jmolPnl = new JMolPnl();
        add(jmolPnl, BorderLayout.CENTER);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void componentClosed() {
        cancelTimer();
        String hId = Helper.getHibernateId(pdbDoc);
        if(hId != null){
            BannerTC.getInstance().unselectByHibernateId(hId);
        }else{
            BannerTC.getInstance().unselect(pdbDoc);
        }
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public IFolderElement getFolderElement() {
        return pdbDoc;
    }

    @Override
    public String getStatusLineText() {
        return "";
    }

    @Override
    public Class<? extends AbstractSavable> getMySavableClass() {
        return null;
    }
}
