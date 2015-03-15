/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.editor.pubmed;

import com.gas.domain.ui.editor.pubmed.IPubmedEditor;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.BusyPanel;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.pubmed.api.IPubmedDBService;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.pubmed.PubmedArticle;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.AbstractSavableEditor;
import com.gas.domain.ui.editor.IMySavable;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.domain.ui.misc.Helper;
import com.gas.database.core.api.IDomainUtil;
import com.gas.domain.core.IFolderElement;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.windows.TopComponent;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.ServiceProvider;


@TopComponent.Description(preferredID = "PubmedEditorTopComponent",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ServiceProvider(service = IPubmedEditor.class)
public final class PubmedEditor extends AbstractSavableEditor implements IPubmedEditor {

    private CardLayout layout;
    private PubmedArticle pubmedArticle;
    private BusyPanel busyPanel;

    public PubmedEditor() {
        initComponents();
        setIcon(ImageHelper.createImage(ImageNames.PUBLICATION_16));
        associateLookup(new AbstractLookup(ic));
    }

    private void initComponents() {
        layout = new CardLayout();
        setLayout(layout);

        JPanel contentPane = new JPanel(new BorderLayout());
        PubmedArticlePanel pane = new PubmedArticlePanel();

        contentPane.add(pane, BorderLayout.CENTER);

        add(contentPane, "content");

        busyPanel = new BusyPanel();
        busyPanel.setBusy(true);

        add(busyPanel, "busy");

        layout.show(this, "busy");

        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
    }

    public BusyPanel getBusyPanel() {
        return busyPanel;
    }

    @Override
    public void componentClosed() {
        cancelTimer();
        if(pubmedArticle == null){
            return;
        }        
        String hId = Helper.getHibernateId(pubmedArticle);
        if (hId != null) {
            BannerTC.getInstance().unselectByHibernateId(hId);
        } else {
            BannerTC.getInstance().unselect(pubmedArticle);
        }
    }

    @Override
    public void setPubmedArticle(final PubmedArticle pubmedArticle) {
        PubmedArticlePanel panel = UIUtil.getChild(this, PubmedArticlePanel.class);
        panel.setArticle(pubmedArticle);
        this.pubmedArticle = pubmedArticle;
        busyPanel.setBusy(false);
        layout.show(this, "content");

        UIUtil.setTopCompToolTip(this, pubmedArticle.getTitle());

        if (pubmedArticle.getHibernateId() != null) {
            BannerTC.getInstance().setCheckRowByHibernateId(pubmedArticle.getHibernateId(), true);
        } else {
            BannerTC.getInstance().setCheckRow(pubmedArticle, true);
        }
    }

    @Override
    public PubmedArticle getPubmedArticle() {
        return pubmedArticle;
    }

    @Override
    public Class<? extends AbstractSavable> getMySavableClass() {
        return MySavable.class;
    }

    @Override
    public IFolderElement getFolderElement() {
        return pubmedArticle;
    }

    @Override
    public String getStatusLineText() {
        return "";
    }

    public class MySavable extends AbstractSavable implements IMySavable, Icon {

        private Icon icon = null;

        public MySavable() {
            register();
        }

        private Icon getIcon() {
            if (icon == null) {
                icon = ImageHelper.createImageIcon(PubmedEditor.this.getIcon());
            }
            return icon;
        }

        @Override
        protected String findDisplayName() {
            return pubmedArticle.getTitle();
        }

        @Override
        protected void handleSave() throws IOException {
            IPubmedDBService db = Lookup.getDefault().lookup(IPubmedDBService.class);
            Folder folder = ExplorerTC.getInstance().getSelectedFolder();
            pubmedArticle.setFolder(folder);
            db.persist(pubmedArticle);

            tc().ic.remove(this);
            UIUtil.styleTopCompName(tc(), false, false);
            IDomainUtil domainUtil = Lookup.getDefault().lookup(IDomainUtil.class);
            domainUtil.udpateCurrentFolders();
            unregister();
        }

        PubmedEditor tc() {
            return PubmedEditor.this;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PubmedEditor.MySavable) {
                PubmedEditor.MySavable m = (PubmedEditor.MySavable) obj;
                return tc() == m.tc();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return tc().hashCode();
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

        @Override
        public void handleCloseWithoutSaving() {
            tc().ic.remove(this);
            unregister();
        }
    }
}
