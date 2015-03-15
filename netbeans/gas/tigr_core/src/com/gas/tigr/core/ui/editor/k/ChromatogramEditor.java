/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.k;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.AbstractSavableEditor;
import com.gas.domain.ui.editor.IMySavable;
import com.gas.domain.ui.editor.IPrintEditor;
import com.gas.domain.ui.editor.PrintParam;
import com.gas.domain.ui.editor.kromatogram.api.IChromatogramEditor;
import com.gas.domain.ui.misc.Helper;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.JComponent;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

@TopComponent.Description(
        preferredID = "ChromatogramTopComponent",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ChromatogramAction",
        preferredID = "ChromatogramTopComponent")
@Messages({
    "CTL_ChromatogramAction=Chromatogram"
})
public final class ChromatogramEditor extends AbstractSavableEditor implements IChromatogramEditor, IPrintEditor {

    private Kromatogram kromatogram;
    ChromatogramPanel chromatogramPanel;
    private String statusLineText = "";

    public ChromatogramEditor() {
        initComp();
        hookupListeners();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new ChromatogramEditorListeners.PtyListener());
    }

    private void initComp() {
        setIcon(ImageHelper.createImage(ImageNames.TRACES_16));
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

        setLayout(new BorderLayout());
        chromatogramPanel = new ChromatogramPanel();
        add(chromatogramPanel, BorderLayout.CENTER);
    }

    @Override
    public void componentClosed() {        
        cancelTimer();
        UIUtil.removeChild(this);
        
        if (kromatogram != null) {
            String hId = Helper.getHibernateId(kromatogram);
            if (hId != null) {
                BannerTC.getInstance().unselectByHibernateId(hId);
            } else {
                BannerTC.getInstance().unselect(kromatogram);
            }
        }
    }

    @Override
    public Kromatogram getKromatogram() {
        return kromatogram;
    }

    @Override
    public void setKromatogram(Kromatogram kromatogram) {
        Kromatogram old = this.kromatogram;
        this.kromatogram = kromatogram;
        firePropertyChange("kromatogram", old, this.kromatogram);
    }

    @Override
    public IFolderElement getFolderElement() {
        return this.kromatogram;
    }

    @Override
    public String getStatusLineText() {
        return statusLineText;
    }

    @Override
    public Class<? extends AbstractSavable> getMySavableClass() {

        return MySavable.class;
    }

    @Override
    public BufferedImage createImageForPrinting(PrintParam printParam) {        
        final double widthImageablePixel = UIUtil.paperLengthToPixel(printParam.getPageFormat().getImageableWidth());
        final int pageNo = printParam.getPageNo();
        RowHeaderUI rowHeaderUI = chromatogramPanel.getRowHeaderUI();
        ChromatogramComp comp = chromatogramPanel.getChromatogramComp();
        final int perPage = MathUtil.round(widthImageablePixel - rowHeaderUI.getWidth());
        int startPixel ;
        if (printParam.getVisibleArea()) {
            startPixel = chromatogramPanel.getScrollPane().getHorizontalScrollBar().getValue();
        } else {
            startPixel = perPage * (pageNo - 1);
        }
        BufferedImage ret = UIUtil.createCompatibleImage(MathUtil.ceil(widthImageablePixel), comp.getHeight());
        
        BufferedImage imageRowHeader = UIUtil.toImage(rowHeaderUI);
        BufferedImage imageComp = UIUtil.toImage(comp, new Rectangle(startPixel, 0, perPage, comp.getHeight()));
        
        ImageHelper.copyImage(imageRowHeader, ret, new Point(0, 0));
        ImageHelper.copyImage(imageComp, ret, new Point(imageRowHeader.getWidth(), 0));
        return ret;
    }

    @Override
    public BufferedImage createImageForExporting(Boolean visibleOnly, int transparency) {
        BufferedImage ret;
        if(visibleOnly){
            ChromatogramComp comp = chromatogramPanel.getChromatogramComp();
            Rectangle visibleRect = comp.getVisibleRect();
            ret = UIUtil.toImage(comp, visibleRect, transparency);
        }else{
            ret = UIUtil.toImage(chromatogramPanel.getChromatogramComp(), transparency);
        }
        return ret;
    }

    @Override
    public Printable createFromPrintableJComponent(PrintParam printParam) {
        return null;
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
        int ret;
        final RowHeaderUI rowHeaderUI = chromatogramPanel.getRowHeaderUI();
        final ChromatogramComp comp = chromatogramPanel.getChromatogramComp();
        final double widthImageable = UIUtil.paperLengthToPixel(pf.getPageFormat().getImageableWidth());
        final double perPage = widthImageable - rowHeaderUI.getWidth();
        
        
        if (pf.getVisibleArea()) {            
            Rectangle visibleRect = comp.getVisibleRect();
            ret = MathUtil.ceil(visibleRect.getWidth() / perPage);
        }else{
            ret = MathUtil.ceil(comp.getWidth() / perPage);
        }
        return ret;
    }

    @Override
    public JComponent getPrintableJComponent() {
        return null;
    }

    @Override
    public String getJobName() {
        return getName();
    }

    private class MySavable extends AbstractSavable implements IMySavable, Icon {

        private Icon icon = null;

        public MySavable() {
            register();
        }

        private Icon getIcon() {
            if (icon == null) {
                icon = ImageHelper.createImageIcon(ChromatogramEditor.this.getIcon());
            }
            return icon;
        }

        @Override
        protected String findDisplayName() {
            return kromatogram.getName();
        }

        @Override
        protected void handleSave() throws IOException {
        }

        ChromatogramEditor tc() {
            return ChromatogramEditor.this;
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
        public void handleCloseWithoutSaving() {
            tc().ic.remove(this);
            unregister();           
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