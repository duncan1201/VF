/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.out;

import com.gas.common.ui.misc.api.IJEditorPaneService;
import com.gas.domain.core.primer3.P3Output;
import java.awt.BorderLayout;
import java.util.Map;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class OutExplainsPanel extends JPanel {

    P3Output p3output;
    JEditorPane editorPane;

    OutExplainsPanel() {
        initComponents();
        hookupListeners();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        IJEditorPaneService editorPaneService = Lookup.getDefault().lookup(IJEditorPaneService.class);
        editorPane = editorPaneService.create(false, "text/html");
        JScrollPane scrollPane = new JScrollPane(editorPane);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void hookupListeners() {
        addPropertyChangeListener(new OutExplainsPanelListeners.PtyListener());
    }

    void setP3output(P3Output p3output) {
        P3Output old = this.p3output;
        this.p3output = p3output;
        firePropertyChange("p3output", old, this.p3output);
    }

    void refresh() {
        if (p3output.containsExplains()) {
            String explainsHtml = p3output.getExplainsInHtml(null, false);
            editorPane.setText(String.format("<html>%s</html>", explainsHtml));
        } else {
            editorPane.setText("");
        }

    }
}
