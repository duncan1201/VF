/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.editor.as;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.domain.ui.editor.TopBracket;
import com.gas.common.ui.color.ColorProviderFetcher;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.misc.Filler;
import com.gas.common.ui.ruler.Ruler;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.editor.as.IASEditor;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.AbstractSavableEditor;
import com.gas.domain.ui.editor.IMolPane;
import com.gas.domain.ui.editor.IMySavable;
import com.gas.domain.ui.misc.Helper;
import com.gas.common.ui.util.Pref;
import com.gas.domain.ui.editor.IPrintEditor;
import com.gas.domain.ui.editor.PrintParam;
import com.gas.main.ui.molpane.IMolPaneFactory;
import com.gas.main.ui.molpane.MolPane;
import com.gas.main.ui.molpane.graphpane.GraphPane;
import com.gas.main.ui.molpane.graphpane.GraphPanel;
import com.gas.main.ui.molpane.graphpane.RowHeaderView;
import com.gas.main.ui.ringpane.RingGraphPanel;
import com.gas.main.ui.ringpane.RingPane;
import com.gas.common.ui.statusline.StatusLineHelper;
import com.gas.domain.core.IExportable;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.editor.IExportEditor;
import com.gas.domain.ui.editor.as.lineage.AncestorsComp;
import com.gas.domain.ui.explorer.ExplorerTC;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import org.openide.windows.TopComponent;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.awt.ActionID;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@TopComponent.Description(preferredID = "EditorTCTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "com.gas.main.ui.test.EditorTCTopComponent")
@TopComponent.OpenActionRegistration(displayName = "#CTL_EditorTCAction",
        preferredID = "EditorTCTopComponent")
@ServiceProvider(service = IASEditor.class)
public final class ASEditor extends AbstractSavableEditor implements IASEditor, IPrintEditor, IExportEditor {

    private AnnotatedSeq as;
    private WeakReference<MolPane> molPaneRef;
    private String statusLineText;
    private IMolPaneFactory molPaneFactory = Lookup.getDefault().lookup(IMolPaneFactory.class);

    public ASEditor() {
        setOpaque(true);
        initComponents();

        associateLookup(new AbstractLookup(ic));
        hookupListener();
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
    }

    public List<TopBracket> getSelectedTopBrackets() {
        return molPaneRef.get().getGraphPane().getGraphPanel().getResPanel().getSelectedTopBrackets();
    }

    @Override
    public void setCanSave() {
        Folder selected = ExplorerTC.getInstance().getSelectedFolder();
        if (selected.isNCBIFolder() || selected.isRecycleBin()) {
            return;
        }
        super.setCanSave();
    }

    private void hookupListener() {
    }

    @Override
    public void refresh() {
        getMolPane().refresh();
    }

    @Override
    public IMolPane getMolPane() {
        if (molPaneRef == null || molPaneRef.get() == null) {
            MolPane molPane = molPaneFactory.getMolPane();
            add(molPane, BorderLayout.CENTER);
            molPaneRef = new WeakReference<MolPane>(molPane);
        }
        return molPaneRef.get();
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE); 
    }

    @Override
    public void displayRENAnalysisPanel() {
        MolPane molPane = (MolPane) getMolPane();
        molPane.displayRENAnalysisPanel();
    }

    @Override
    public void componentDeactivated() {     
        statusLineText = StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().getText();
        StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().clear();
    }

    private void setValues() {

        setMinimapShown(as.getAsPref().isMinimapShown());

        Float rulerFontSize = Pref.CommonPtyPrefs.getInstance().getRulerFontSize();
        setRulerFontSize(rulerFontSize);

        Float baseFontSize = Pref.CommonPtyPrefs.getInstance().getBaseFontSize();
        setBaseFontSize(baseFontSize);

        Float annotationFontSize = Pref.CommonPtyPrefs.getInstance().getAnnotationLabelSize();
        setAnnotationLabelSize(annotationFontSize);

        AnnotatedSeq as = molPaneRef.get().getAs();
        boolean isAA = AsHelper.isAminoAcid(as);
        Pref.ColorProviderPrefs.KEY key = isAA ? Pref.ColorProviderPrefs.KEY.PROTEIN : Pref.ColorProviderPrefs.KEY.DNA;
        String selectedName = Pref.ColorProviderPrefs.getInstance().getColorProviderName(key);

        IColorProvider colorProvider = ColorProviderFetcher.getColorProvider(selectedName);
        if (colorProvider != null) {
            getMolPane().setPrimarySeqColorProvider(colorProvider);
        }

        if (!isAA) {
            String colorSchemeName = Pref.ColorProviderPrefs.getInstance().getColorProviderName(Pref.ColorProviderPrefs.KEY.TRANSLATION);
            colorProvider = ColorProviderFetcher.getColorProvider(colorSchemeName);
            if (colorProvider != null) {
                getMolPane().setTranslationColorProvider(colorProvider);
            }
            
            ((MolPane)getMolPane()).getSidePanel().getAnalysisPanel().getGcPlotPanel().getHeightSpinner().setValue(Pref.CommonPtyPrefs.getInstance().getGCHeight());
        }

    }

    @Override
    public void componentClosed() {
        cancelTimer();
        StatusLineHelper.getDefaultStatusLineCompProvider().getStatusLineElement().clear();
        UIUtil.removeChild(this);

        if (as != null) {
            String hId = Helper.getHibernateId(as);
            if (hId != null) {
                BannerTC.getInstance().unselectByHibernateId(hId);
                IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
                asService.mergeAsPref(as);
            } else {
                BannerTC.getInstance().unselect(as);
            }
        }
    }

    @Override
    public void setAnnotatedSeq(AnnotatedSeq as) {
        this.as = as;
        ((MolPane) getMolPane()).setAs(as);
        setValues();

        if (as.getHibernateId() != null) {
            BannerTC.getInstance().setCheckRowByHibernateId(as.getHibernateId(), true);
        } else {
            BannerTC.getInstance().setCheckRow(as, true);
        }
    }

    @Override
    public AnnotatedSeq getAnnotatedSeq() {
        return this.as;
    }

    @Override
    public void setSelection(Loc loc) {
        LocList locList = new LocList();
        locList.add(loc);
        molPaneRef.get().setSelections(locList);
    }

    @Override
    public void setMinimapShown(Boolean shown) {
        molPaneRef.get().setMinimapShown(shown);
    }

    @Override
    public void setRulerFontSize(Float size) {
        molPaneRef.get().setRulerFontSize(size);
    }

    @Override
    public void setBaseFontSize(Float size) {
        molPaneRef.get().setBaseFontSize(size);
    }

    public void setAnnotationLabelSize(Float size) {
        molPaneRef.get().setAnnotationLabelSize(size);
    }

    @Override
    public Class<? extends AbstractSavable> getMySavableClass() {
        return MySavable.class;
    }

    @Override
    public LocList getSelections() {
        return getMolPane().getSelections();
    }

    @Override
    public IExportable getExportable() {
        return as;
    }

    @Override
    public String getStatusLineText() {
        return statusLineText;
    }

    @Override
    public IFolderElement getFolderElement() {
        return as;
    }

    public class MySavable extends AbstractSavable implements IMySavable, Icon {

        private Icon icon = null;

        public MySavable() {
            register();
        }

        private Icon getIcon() {
            if (icon == null) {
                icon = ImageHelper.createImageIcon(ASEditor.this.getIcon());
            }
            return icon;
        }

        @Override
        protected String findDisplayName() {
            return as.getName();
        }

        @Override
        protected void handleSave() throws IOException {
            IAnnotatedSeqService service = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
            AnnotatedSeq as = molPaneRef.get().getAs();
            as.setLastModifiedDate(new Date());
            service.merge(as);
            BannerTC.getInstance().updataRowByHibernateId(as);
            tc().ic.remove(this);
            UIUtil.styleTopCompName(tc(), false, false);
            unregister();
        }

        ASEditor tc() {
            return ASEditor.this;
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

    @Override
    public BufferedImage createImageForPrinting(PrintParam printParam) {
        BufferedImage ret = null;
        final double widthImageablePixel = UIUtil.paperLengthToPixel(printParam.getPageFormat().getImageableWidth());
        JComponent comp = getCurrentComp();
        final int pageNo = printParam.getPageNo();
        if (comp instanceof GraphPane) {
            GraphPane pane = (GraphPane) comp;
            Ruler ruler = pane.getColumnHeaderView();
            GraphPanel graphPanel = pane.getGraphPanel();
            graphPanel.setPaintVisibleOnly(false);
            RowHeaderView rowHeaderView = pane.getRowHeaderView();
            final int perPage = MathUtil.round(widthImageablePixel - rowHeaderView.getWidth());
            int startPixel;
            if (printParam.getVisibleArea()) {
                startPixel = pane.getScrollPane().getHorizontalScrollBar().getValue();
            } else {
                startPixel = perPage * (pageNo - 1);
            }
            ret = UIUtil.createCompatibleImage(MathUtil.ceil(widthImageablePixel), graphPanel.getHeight() + (ruler == null? 0: ruler.getHeight()));
            BufferedImage imageRowHeader = UIUtil.toImage(rowHeaderView);
            BufferedImage imageRuler = null;
            if(ruler != null){
                imageRuler = UIUtil.toImage(ruler, new Rectangle(startPixel, 0, perPage, ruler.getHeight()));
            }
            BufferedImage imageGraphPanel = UIUtil.toImage(graphPanel, new Rectangle(startPixel, 0, perPage, graphPanel.getHeight()));
            if(imageRuler != null){
                ImageHelper.copyImage(imageRuler, ret, new Point(imageRowHeader.getWidth(), 0));
            }
            ImageHelper.copyImage(imageRowHeader, ret, new Point(0, imageRuler == null? 0: imageRuler.getHeight()));
            ImageHelper.copyImage(imageGraphPanel, ret, new Point(imageRowHeader.getWidth(), imageRuler == null? 0: imageRuler.getHeight()));
            graphPanel.setPaintVisibleOnly(true);
        } else if (comp instanceof RingPane) {
            RingPane pane = (RingPane) comp;
            RingGraphPanel ringGraphPanel = pane.getRingGraphPanel();
            Rectangle visibleRect = ringGraphPanel.getVisibleRect();
            ret = UIUtil.toImage(ringGraphPanel, visibleRect);
        } else if (comp instanceof JEditorPane) {
            JEditorPane pane = (JEditorPane) comp;
            ret = UIUtil.toImage(pane);
        } else if (comp instanceof AncestorsComp) {
            AncestorsComp ac = (AncestorsComp) comp;
            if (printParam.getVisibleArea()) {

                Rectangle visibleRect = ac.getVisibleRect();
                ret = UIUtil.toImage(comp, visibleRect);
            } else {
                Rectangle rect = new Rectangle();
                rect.x = MathUtil.round((pageNo - 1) * widthImageablePixel);
                rect.y = 0;
                rect.width = MathUtil.ceil(widthImageablePixel);
                rect.height = comp.getHeight();

                ret = UIUtil.toImage(comp, rect);
            }

        } else {
            throw new UnsupportedOperationException();
        }
        return ret;
    }

    @Override
    public int getTotalPages(PrintParam pf) {
        int ret = 0;
        JComponent comp = getCurrentComp();
        final double widthImageable = UIUtil.paperLengthToPixel(pf.getPageFormat().getImageableWidth());
        if (comp instanceof RingPane) {
            ret = 1;
        } else if (comp instanceof GraphPane) {
            GraphPane graphPane = (GraphPane) comp;
            RowHeaderView rowHeaderView = graphPane.getRowHeaderView();
            final GraphPanel graphPanel = graphPane.getGraphPanel();
            final double perPage = widthImageable - rowHeaderView.getWidth();
            if (pf.getVisibleArea()) {
                Rectangle visibleRect = graphPanel.getVisibleRect();
                ret = MathUtil.ceil(visibleRect.getWidth() / perPage);
            } else {
                ret = MathUtil.ceil(graphPanel.getWidth() / perPage);
            }
        } else if (comp instanceof JEditorPane) {
            JEditorPane pane = (JEditorPane) comp;
            Printable printable = pane.getPrintable(null, null);
            ret = CommonUtil.getPageCount(printable, pf.getPageFormat());
        } else if (comp instanceof AncestorsComp) {
            AncestorsComp ac = (AncestorsComp) comp;
            if (pf.getVisibleArea()) {
                Rectangle visibleRect = ac.getVisibleRect();
                ret = MathUtil.ceil(visibleRect.getWidth() / widthImageable);
            } else {
                ret = MathUtil.ceil(ac.getWidth() / widthImageable);
            }
        }
        return ret;
    }

    @Override
    public String getJobName() {
        String ret = null;
        JComponent comp = getCurrentComp();
        if (comp instanceof GraphPane) {
            ret = String.format("%s", getMolPane().getAs().getName());
        } else if (comp instanceof RingPane) {
            ret = String.format("%s", getMolPane().getAs().getName());
        } else if (comp instanceof JEditorPane) {
            ret = String.format("%s-text-view", getMolPane().getAs().getName());
        } else if (comp instanceof AncestorsComp) {
            ret = String.format("%s-ancestor-view", getMolPane().getAs().getName());
        } else {
            throw new UnsupportedOperationException();
        }
        return ret;
    }

    /**
     * @see MolPane#getCurrentComponent()
     */
    private JComponent getCurrentComp() {
        final MolPane molPane = (MolPane) getMolPane();
        return molPane.getCurrentComponent();
    }

    @Override
    public boolean supportVisibleAreaOnly() {
        boolean ret;
        JComponent comp = getCurrentComp();
        ret = (comp instanceof GraphPane) || (comp instanceof AncestorsComp);
        return ret;
    }

    @Override
    public boolean isPrintableJComponent() {
        JComponent comp = getCurrentComp();
        return comp instanceof JEditorPane;
    }

    @Override
    public JComponent getPrintableJComponent() {
        JComponent ret = null;
        JComponent comp = getCurrentComp();
        if (comp instanceof JEditorPane) {
            ret = comp;
        }
        return ret;
    }

    @Override
    public BufferedImage createImageForExporting(Boolean visibleOnly, int transparency) {
        BufferedImage ret = null;
        JComponent comp = getCurrentComp();
        if (comp instanceof GraphPane) {
            GraphPane pane = (GraphPane) comp;
            final RowHeaderView rowHeaderView = pane.getRowHeaderView();
            final GraphPanel graphPanel = pane.getGraphPanel();
            final Ruler ruler = pane.getColumnHeaderView();
            final Filler corner = (Filler) pane.getScrollPane().getCorner(ScrollPaneConstants.UPPER_LEFT_CORNER);

            BufferedImage imageRowHeader = UIUtil.toImage(rowHeaderView, transparency);
            BufferedImage imageRuler = null;
            BufferedImage imageGraphPanel;
            BufferedImage imageCorner = null;
            if(corner.getHeight() > 0){
                imageCorner = UIUtil.toImage(corner, transparency);
            }
            
            if (visibleOnly) {
                Rectangle visibleRect = graphPanel.getVisibleRect();
                imageGraphPanel = UIUtil.toImage(graphPanel, visibleRect);
                if(ruler != null){
                    imageRuler = UIUtil.toImage(ruler, new Rectangle(0, 0, visibleRect.width, ruler.getHeight()), transparency);
                }
            } else {
                if(ruler != null){
                    imageRuler = UIUtil.toImage(ruler);
                }
                graphPanel.setPaintVisibleOnly(false);
                imageGraphPanel = UIUtil.toImage(graphPanel, transparency);
                graphPanel.setPaintVisibleOnly(true);
            }

            ret = UIUtil.createCompatibleImage(imageRowHeader.getWidth() + graphPanel.getWidth(), imageGraphPanel.getHeight() + (imageRuler == null? 0: imageRuler.getHeight()), transparency);

            if(imageCorner != null){
                ImageHelper.copyImage(imageCorner, ret, new Point(0, 0));
            }
            if(imageRuler != null){
                ImageHelper.copyImage(imageRuler, ret, new Point(imageRowHeader.getWidth(), 0));
            }
            ImageHelper.copyImage(imageRowHeader, ret, new Point(0, imageRuler == null ? 0: imageRuler.getHeight()));
            ImageHelper.copyImage(imageGraphPanel, ret, new Point(imageRowHeader.getWidth(), imageRuler == null? 0: imageRuler.getHeight()));
        } else if (comp instanceof RingPane) {
            RingPane pane = (RingPane) comp;
            RingGraphPanel ringGraphPanel = pane.getRingGraphPanel();
            Rectangle visibleRect = ringGraphPanel.getVisibleRect();
            ret = UIUtil.toImage(ringGraphPanel, visibleRect, transparency);

        } else if (comp instanceof JEditorPane) {
            ret = UIUtil.toImage(comp, transparency);
        } else if (comp instanceof AncestorsComp) {
            AncestorsComp ac = (AncestorsComp) comp;
            if (visibleOnly) {
                Rectangle visibleRect = ac.getVisibleRect();
                ret = UIUtil.toImage(comp, visibleRect, transparency);
            } else {
                ret = UIUtil.toImage(comp, transparency);
            }
        }
        return ret;
    }

    @Override
    public Printable createFromPrintableJComponent(PrintParam printParam) {
        Printable ret = null;
        JComponent comp = getCurrentComp();
        if (comp instanceof JEditorPane) {
            JEditorPane pane = (JEditorPane) comp;
            MessageFormat headerFormat = new MessageFormat(printParam.getJobName().getValue());
            MessageFormat footerFormat = new MessageFormat("Page {0}");
            ret = pane.getPrintable(headerFormat, footerFormat);
        } else if (comp instanceof JTable) {
            JTable table = (JTable) comp;
            ret = table.getPrintable(JTable.PrintMode.NORMAL, null, null);
        } else {
            throw new UnsupportedOperationException();
        }
        return ret;
    }
}
