/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor;

import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.core.LocList;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.as.TranslationResult;
import com.gas.domain.core.tigr.TigrProject;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.AbstractSavableEditor;
import com.gas.domain.ui.editor.ISequenceUI;
import com.gas.domain.ui.editor.tigr.api.ITigrPtEditor;
import com.gas.domain.ui.misc.Helper;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import javax.swing.Icon;
import org.openide.windows.TopComponent;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.awt.ActionID;
import org.openide.util.lookup.ServiceProvider;

/**
 * Top component which displays something.
 */
@TopComponent.Description(preferredID = "TigrPtTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "com.gas.tigr.ui.editor.TigrPtTopComponent")
@TopComponent.OpenActionRegistration(displayName = "#CTL_TigrPtAction",
preferredID = "TigrPtTopComponent")
@ServiceProvider(service = ITigrPtEditor.class)
public final class TigrPtEditor extends AbstractSavableEditor implements ITigrPtEditor, ISequenceUI {

    private TigrProject tigrPt;
    private TigrPtPanel tigrPanel;

    public TigrPtEditor() {
        initComponents();        
        hookupListeners();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new TigrPtEditorListeners.PtyListener());
    }

    protected TigrPtPanel getTigrPtPanel() {
        return tigrPanel;
    }

    private void initComponents() {
        setIcon(ImageHelper.createImage(ImageNames.SANGER_16));
        setLayout(new BorderLayout());
        tigrPanel = new TigrPtPanel();
        add(tigrPanel, BorderLayout.CENTER);

        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
    }
    
    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
        UIUtil.removeChild(this);
        String hId = Helper.getHibernateId(tigrPt);
        if(hId != null){
            BannerTC.getInstance().unselectByHibernateId(hId);
        }else{
            BannerTC.getInstance().unselect(tigrPt);
        }
    }

    @Override
    public TigrProject getTigrPt() {
        return this.tigrPt;
    }

    @Override
    public void setTigrPt(TigrProject tigrPt) {
        TigrProject old = this.tigrPt;
        this.tigrPt = tigrPt;
        firePropertyChange("tigrPt", old, this.tigrPt);
    }

    @Override
    public Map<Loc, String> getSelectedSeqs() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LocList getSelections() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSequence() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTranslationResults(Set<TranslationResult> t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTranslationColorProvider(IColorProvider colorProvider) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPrimarySeqColorProvider(IColorProvider colorProvider) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Class<? extends AbstractSavable> getMySavableClass() {
        return MySavable.class;
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public void center(Loc loc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getStatusLineText() {
        return "";
    }    

    @Override
    public IFolderElement getFolderElement() {
        return tigrPt;
    }

    private class MySavable extends AbstractSavable implements Icon {

        private Icon icon;

        @Override
        protected String findDisplayName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        private Icon getIcon() {
            if (icon == null) {
                icon = ImageHelper.createImageIcon(TigrPtEditor.this.getIcon());
            }
            return icon;
        }

        @Override
        protected void handleSave() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean equals(Object obj) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int hashCode() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            getIcon().paintIcon(c, g, x, y);
        }

        @Override
        public int getIconWidth() {
            return getIcon().getIconWidth();
        }

        @Override
        public int getIconHeight() {
            return getIcon().getIconHeight();
        }
    }
}
