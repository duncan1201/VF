/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.out;

import com.gas.main.ui.molpane.sitepanel.primer3.out.OutMoreOligosPanel;
import com.gas.main.ui.molpane.sitepanel.primer3.out.OutExplainsPanel;
import com.gas.domain.core.primer3.Oligo;
import com.gas.domain.core.primer3.P3Output;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author dq
 */
public class OutPanel extends JPanel {

    public P3Output p3output;

    public OutPanel() {
        initComponents();
        hookupListeners();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);

        outOligoPanel = new OutOligoPanel();
        outMoreOligoPanel = new OutMoreOligosPanel();
        outReagentsPanel = new OutReagentsPanel();
        outExplainsPanel = new OutExplainsPanel();
        tabbedPane.add("Oligo", outOligoPanel);
        tabbedPane.add("More", outMoreOligoPanel);
        tabbedPane.add("Reagents", outReagentsPanel);
        tabbedPane.add("Stats.", outExplainsPanel);
        add(tabbedPane, BorderLayout.CENTER);

    }

    private void hookupListeners() {
        addPropertyChangeListener(new OutPanelListeners.PtyChangeListener());
    }

    public void setP3output(P3Output p3output) {
        P3Output old = this.p3output;
        this.p3output = p3output;
        firePropertyChange("p3output", old, this.p3output);
    }

    public void refresh() {
        outOligoPanel.setOligo(p3output.getOligoByNo(0));

        outMoreOligoPanel.setOligos(p3output.getAdditionalOligos());
        outReagentsPanel.refresh();
        outExplainsPanel.refresh();        
    }

    void populateUI() {
        if (!p3output.getOligos().isEmpty()) {
            Oligo oligo = p3output.getOligoByNo(0);
            outOligoPanel.setOligo(oligo);

            outMoreOligoPanel.setOligos(p3output.getAdditionalOligos());            
        } else {
            outOligoPanel.setOligo(null);
            outMoreOligoPanel.setOligos(null);
        }
        outReagentsPanel.setP3output(p3output);
        outExplainsPanel.setP3output(p3output);        
    }
    OutOligoPanel outOligoPanel;
    OutMoreOligosPanel outMoreOligoPanel;
    OutReagentsPanel outReagentsPanel;
    OutExplainsPanel outExplainsPanel;
}
