/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui;

import com.gas.domain.ui.editor.msa.api.IMSAEditor;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.statusline.StatusLineHelper;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.msa.service.api.IMSAService;
import com.gas.domain.core.IExportable;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.misc.api.Newick;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.AbstractSavableEditor;
import com.gas.domain.ui.editor.IExportEditor;
import com.gas.domain.ui.editor.IMySavable;
import com.gas.domain.ui.editor.PrintParam;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.domain.ui.misc.Helper;
import com.gas.msa.ui.alignment.AlignPane;
import com.gas.msa.ui.alignment.widget.ColumnHeaderUI;
import com.gas.msa.ui.alignment.widget.CornerUI;
import com.gas.msa.ui.alignment.widget.RowHeaderUI;
import com.gas.msa.ui.alignment.widget.ViewUI;
import com.gas.msa.ui.tree.TreePane;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.Timer;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;

/**
 * Top component which displays something.
 */
@TopComponent.Description(preferredID = "MSATopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@TopComponent.OpenActionRegistration(displayName = "#CTL_MSAAction",
        preferredID = "MSATopComponent")
@Messages({
    "CTL_MSAAction=MSA"})
public final class MSAEditor extends AbstractSavableEditor implements IMSAEditor, IExportEditor {

    private static Logger logger = Logger.getLogger(MSAEditor.class.getName());
    
    private WeakReference<MSAPane> msaPaneRef;
    private MSA msa;
    private String statusLineText;
    private IMSAService msaService = Lookup.getDefault().lookup(IMSAService.class);

    public MSAEditor() {
        initComponents();
        associateLookup(new AbstractLookup(ic));
        hookupListeners();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new MSAEditorListeners.PtyListener());
    }

    @Override
    public IExportable getExportable() {
        return msa;
    }

    private void initComponents() {
        UIUtil.setTopCompIcon(this, ImageHelper.createImage(ImageNames.ATG_16));
        LayoutManager layout = new BorderLayout();
        this.setLayout(layout);
        MSAPane msaPane = new MSAPane();
        add(msaPane, BorderLayout.CENTER);
        msaPaneRef = new WeakReference<MSAPane>(msaPane);

        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
    }

    protected MSAPane getMSAPane() {
        return msaPaneRef.get();
    }

    @Override
    public void componentDeactivated() {
        statusLineText = StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().getText();
        StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().clear();
    }

    @Override
    public void componentClosed() {
        cancelTimer();
        UIUtil.removeChild(this);
        String hId = Helper.getHibernateId(msa);
        if (hId != null) {
            BannerTC.getInstance().unselectByHibernateId(hId);
            if (msa.isNewickDirty()) {
                msaService.merge(msa);
            } else {
                msaService.mergeSetting(msa);
            }
        } else {
            BannerTC.getInstance().unselect(msa);
        }
        msa = null;
    }

    @Override
    public void setCanSave() {
        super.setCanSave();
    }

    @Override
    public MSA getMsa() {
        return msa;
    }

    @Override
    public void setMsa(MSA msa) {
        MSA old = this.msa;
        this.msa = msa;
        firePropertyChange("msa", old, this.msa);
    }
    
    @Override
    public void refreshUI() {
        refreshUI(null);
    }

    @Override
    public void refreshUI(String selectedPane) {
        Newick newick = msa.getNewick();
        if (newick != null) {
            if (msaPaneRef.get().getTreePane() == null) {
                msaPaneRef.get().initTreePane();
            }
            msaPaneRef.get().getTreePane().refreshUI(msa);
        }
        msaPaneRef.get().getAlignPane().getMsaScroll().refreshUI();
        if (selectedPane != null) {
            msaPaneRef.get().setSelectedTab(selectedPane);
        }
    }

    @Override
    public Class<? extends AbstractSavable> getMySavableClass() {
        return MySavable.class;
    }

    @Override
    public BufferedImage createImageForPrinting(PrintParam printParam) {
        BufferedImage ret = null;
        final double widthImageablePixel = UIUtil.paperLengthToPixel(printParam.getPageFormat().getImageableWidth());
        JComponent comp = getCurrentComp();
        if (comp instanceof AlignPane) {
            AlignPane pane = (AlignPane) comp;
            CornerUI cornerUI = pane.getMsaScroll().getCornerUI();
            ColumnHeaderUI columnHeaderUI = pane.getMsaScroll().getColumnHeaderUI();
            columnHeaderUI.setPaintVisibleOnly(printParam.getVisibleArea());
            ViewUI viewUI = pane.getMsaScroll().getViewUI();
            viewUI.setPaintVisibleOnly(printParam.getVisibleArea());
            RowHeaderUI rowHeaderView = pane.getMsaScroll().getRowHeaderUI();
            final int perPage = MathUtil.round(widthImageablePixel - rowHeaderView.getWidth());
            int startPixel;
            BufferedImage imageViewUI;
            BufferedImage imageColumnHeaderUI;
            if (printParam.getVisibleArea()) {
                startPixel = pane.getMsaScroll().getHorizontalScrollBar().getValue();
                imageViewUI = UIUtil.toImage(viewUI, new Rectangle(startPixel, 0, viewUI.getVisibleRect().width, viewUI.getHeight()));
                imageColumnHeaderUI = UIUtil.toImage(columnHeaderUI, new Rectangle(startPixel, 0, columnHeaderUI.getVisibleRect().width, columnHeaderUI.getHeight()));
            } else {
                startPixel = perPage * (printParam.getPageNo() - 1);
                imageViewUI = UIUtil.toImage(viewUI, new Rectangle(startPixel, 0, perPage, viewUI.getHeight()));
                imageColumnHeaderUI = UIUtil.toImage(columnHeaderUI, new Rectangle(startPixel, 0, perPage, columnHeaderUI.getHeight()));
            }
            BufferedImage imageCornerUI = UIUtil.toImage(cornerUI);
            BufferedImage imageRowHeader = UIUtil.toImage(rowHeaderView);

            ret = UIUtil.createCompatibleImage(MathUtil.ceil(imageRowHeader.getWidth() + imageViewUI.getWidth()), viewUI.getHeight() + columnHeaderUI.getHeight());

            ImageHelper.copyImage(imageCornerUI, ret, new Point(0, 0));
            ImageHelper.copyImage(imageColumnHeaderUI, ret, new Point(imageRowHeader.getWidth(), 0));
            ImageHelper.copyImage(imageRowHeader, ret, new Point(0, imageColumnHeaderUI.getHeight()));
            ImageHelper.copyImage(imageViewUI, ret, new Point(imageRowHeader.getWidth(), imageColumnHeaderUI.getHeight()));

            viewUI.setPaintVisibleOnly(true);
            columnHeaderUI.setPaintVisibleOnly(true);
        } else if (comp instanceof TreePane) {
            TreePane pane = (TreePane) comp;
            JComponent compTree = (JComponent) pane.getVisibleTree();
            ret = UIUtil.toImage(compTree);
        } else {
            throw new UnsupportedOperationException();
        }
        return ret;
    }

    @Override
    public BufferedImage createImageForExporting(java.lang.Boolean visibleOnly, int transparency) {
        BufferedImage ret = null;
        JComponent comp = getCurrentComp();
        if (comp instanceof AlignPane) {
            AlignPane pane = (AlignPane) comp;
            CornerUI cornerUI = pane.getMsaScroll().getCornerUI();
            ColumnHeaderUI columnHeaderUI = pane.getMsaScroll().getColumnHeaderUI();
            columnHeaderUI.setPaintVisibleOnly(visibleOnly);
            ViewUI viewUI = pane.getMsaScroll().getViewUI();
            viewUI.setPaintVisibleOnly(visibleOnly);
            RowHeaderUI rowHeaderView = pane.getMsaScroll().getRowHeaderUI();
            int startPixel;
            BufferedImage imageViewUI;
            BufferedImage imageColumnHeaderUI;
            if (visibleOnly) {
                startPixel = pane.getMsaScroll().getHorizontalScrollBar().getValue();
                imageViewUI = UIUtil.toImage(viewUI, new Rectangle(startPixel, 0, viewUI.getVisibleRect().width, viewUI.getHeight()));
                imageColumnHeaderUI = UIUtil.toImage(columnHeaderUI, new Rectangle(startPixel, 0, columnHeaderUI.getVisibleRect().width, columnHeaderUI.getHeight()));
            } else {
                startPixel = 0;
                imageViewUI = UIUtil.toImage(viewUI, new Rectangle(startPixel, 0, viewUI.getWidth(), viewUI.getHeight()));
                imageColumnHeaderUI = UIUtil.toImage(columnHeaderUI, new Rectangle(startPixel, 0, columnHeaderUI.getWidth(), columnHeaderUI.getHeight()));
            }
            BufferedImage imageCornerUI = UIUtil.toImage(cornerUI, transparency);
            BufferedImage imageRowHeader = UIUtil.toImage(rowHeaderView, transparency);

            ret = UIUtil.createCompatibleImage(MathUtil.ceil(imageRowHeader.getWidth() + imageViewUI.getWidth()), viewUI.getHeight() + columnHeaderUI.getHeight(), transparency);

            ImageHelper.copyImage(imageCornerUI, ret, new Point(0, 0));
            ImageHelper.copyImage(imageColumnHeaderUI, ret, new Point(imageRowHeader.getWidth(), 0));
            ImageHelper.copyImage(imageRowHeader, ret, new Point(0, imageColumnHeaderUI.getHeight()));
            ImageHelper.copyImage(imageViewUI, ret, new Point(imageRowHeader.getWidth(), imageColumnHeaderUI.getHeight()));

            viewUI.setPaintVisibleOnly(true);
            columnHeaderUI.setPaintVisibleOnly(true);
        } else if (comp instanceof TreePane) {
            TreePane pane = (TreePane) comp;
            JComponent compTree = (JComponent) pane.getVisibleTree();
            ret = UIUtil.toImage(compTree, transparency);
        } else {
            throw new UnsupportedOperationException();
        }
        return ret;
    }

    @Override
    public Printable createFromPrintableJComponent(PrintParam printParam) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isPrintableJComponent() {
        return false;
    }

    @Override
    public boolean supportVisibleAreaOnly() {
        return true;
    }

    @Override
    public int getTotalPages(PrintParam pf) {
        int ret = 0;
        JComponent comp = getCurrentComp();
        final double widthImageable = UIUtil.paperLengthToPixel(pf.getPageFormat().getImageableWidth());
        if (comp instanceof AlignPane) {
            final AlignPane alignPane = (AlignPane) comp;
            final RowHeaderUI rowHeaderUI = alignPane.getMsaScroll().getRowHeaderUI();
            final ViewUI viewUI = alignPane.getMsaScroll().getViewUI();
            final double perPage = widthImageable - rowHeaderUI.getWidth();
            if (pf.getVisibleArea()) {
                Rectangle visibleRect = viewUI.getVisibleRect();
                ret = MathUtil.ceil(visibleRect.getWidth() / perPage);
            } else {
                ret = MathUtil.ceil(viewUI.getWidth() / perPage);
            }

        } else if (comp instanceof TreePane) {
            ret = 1;
        }
        return ret;
    }

    /**
     * @return valid values: AlignPane, TreePane
     */
    private JComponent getCurrentComp() {
        JComponent comp = null;
        if (msaPaneRef.get().getAlignPane() != null && msaPaneRef.get().getAlignPane().isVisible()) {
            comp = msaPaneRef.get().getAlignPane();
        } else if (msaPaneRef.get().getTreePane() != null && msaPaneRef.get().getTreePane().isVisible()) {
            comp = msaPaneRef.get().getTreePane();
        }
        return comp;
    }

    @Override
    public JComponent getPrintableJComponent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getJobName() {
        return msa.getName();
    }

    @Override
    public IFolderElement getFolderElement() {
        return msa;
    }

    @Override
    public String getStatusLineText() {
        return statusLineText;
    }

    public class MySavable extends AbstractSavable implements IMySavable, Icon {

        private Icon icon;

        public MySavable() {
            register();
        }

        private Icon getIcon() {
            if (icon == null) {
                icon = ImageHelper.createImageIcon(ImageNames.SMILE_16);
            }
            return icon;
        }

        @Override
        protected String findDisplayName() {
            return getMsa().getName();
        }

        @Override
        protected void handleSave() throws IOException {
            MSA msa = getMsa();
            msa.setLastModifiedDate(new Date());
            msaService.merge(msa);
            tc().ic.remove(this);
            BannerTC.getInstance().updataRowByHibernateId(msa);
            UIUtil.styleTopCompName(tc(), false, false);
            unregister();
        }

        MSAEditor tc() {
            return MSAEditor.this;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof MySavable) {
                MySavable m = (MySavable) obj;
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
