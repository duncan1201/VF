/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RENView2.java
 *
 * Created on Nov 28, 2011, 9:23:07 AM
 */
package com.gas.enzyme.core.ui;

import com.gas.common.ui.jcomp.CollapsibleTitlePanel;
import com.gas.enzyme.core.ui.actions.DigestAction;
import com.gas.enzyme.core.ui.enzymesused.EnzymesUsedPanel;
import com.gas.enzyme.core.ui.sitesfound.SitesFoundPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 *
 * @author dunqiang
 */
public class RENAnalView extends javax.swing.JPanel implements Scrollable {

    WeakReference<CollapsibleTitlePanel> sitesFoundTitlePanelRef;
    CutSitesPanel cutSitesPanel;
    private EnzymesUsedPanel enzymesUsedPanel;
    private WeakReference<SitesFoundPanel> sitesFoundPanelRef;
    private WeakReference<JButton> digestBtnRef;   
    
    public RENAnalView() {
        initComponents();
        hookupListeners();
    }

    private void initComponents() {
        CardLayout layout = new CardLayout();
        setLayout(layout);

        add(createSearchPanels(), RENAnalPanel.PAGE.SEARCH.toString());
        add(createResultsPanel(), RENAnalPanel.PAGE.RESULT.toString());

        layout.show(this, RENAnalPanel.PAGE.SEARCH.toString());
    }
    
    private void hookupListeners(){
        JTable table = getSitesFoundPanel().getSitesFoundTable();
        table.getSelectionModel().addListSelectionListener(new RENAnalViewListeners.TableSelectListener(this));
    }

    protected void showPanel(RENAnalPanel.PAGE page) {        
        CardLayout layout = (CardLayout) getLayout();
        layout.show(this, page.toString());
    }
    
    public JButton getDigestButton(){
        return digestBtnRef.get();
    }

    private JPanel createSearchPanels() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c ;
        
        CollapsibleTitlePanel cutSitesCollapsibleTitlePanel = new CollapsibleTitlePanel("Cut Sites");
        CollapsibleTitlePanel sitesUsedCollapsibleTitlePanel = new CollapsibleTitlePanel("Enzymes Used");

        cutSitesPanel = new CutSitesPanel();
        cutSitesPanel.setMatchMax(1);
        cutSitesPanel.setMatchMin(1);
        cutSitesPanel.setMustCutFrom(1);
        cutSitesPanel.setMustCutTo(1);
        cutSitesPanel.setMustNotCutFrom(1);
        cutSitesPanel.setMustNotCutTo(1);
        cutSitesPanel.setAnywhereEnabled(true);
        cutSitesCollapsibleTitlePanel.setContentPane(cutSitesPanel);


        enzymesUsedPanel = new EnzymesUsedPanel();
        sitesUsedCollapsibleTitlePanel.setContentPane(enzymesUsedPanel);
                
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        ret.add(cutSitesCollapsibleTitlePanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        ret.add(sitesUsedCollapsibleTitlePanel, c);        
        
        return ret;
    }

    private JPanel createResultsPanel() {
        JPanel ret = new JPanel();
        CollapsibleTitlePanel sitesFoundTitlePanel = new CollapsibleTitlePanel("Sites Found");
        sitesFoundTitlePanelRef = new WeakReference<CollapsibleTitlePanel>(sitesFoundTitlePanel);
        SitesFoundPanel sitesFoundPanel = new SitesFoundPanel();
        sitesFoundPanelRef = new WeakReference<SitesFoundPanel>(sitesFoundPanel);
        sitesFoundTitlePanel.setContentPane(sitesFoundPanel);

        ret.setLayout(new BorderLayout());

        ret.add(sitesFoundTitlePanel, BorderLayout.CENTER);

        JButton digestBtn = new JButton(new DigestAction());
        digestBtn.setHorizontalAlignment(SwingConstants.LEFT);
        digestBtn.setEnabled(false);
        ret.add(digestBtn, BorderLayout.SOUTH);
        digestBtnRef = new WeakReference<JButton>(digestBtn);
        
        return ret;
    }

    public CutSitesPanel getCutSitesPanel() {
        return cutSitesPanel;
    }

    public EnzymesUsedPanel getEnzymesUsedPanel() {
        return enzymesUsedPanel;
    }

    public SitesFoundPanel getSitesFoundPanel() {
        return sitesFoundPanelRef.get();
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.VERTICAL) {
            return visibleRect.height;
        } else if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width;
        } else {
            throw new IllegalArgumentException("invalid orientation"); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return getScrollableBlockIncrement(visibleRect, orientation, direction) / 10;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    public void setSiteCount(int size) {        
        sitesFoundTitlePanelRef.get().setTitle(String.format("Sites Found(%d)", size));
    }
}
