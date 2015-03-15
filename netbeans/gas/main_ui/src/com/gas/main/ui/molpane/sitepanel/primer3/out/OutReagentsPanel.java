/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.out;

import com.gas.common.ui.misc.api.IJEditorPaneService;
import com.gas.domain.core.primer3.P3Output;
import com.gas.domain.core.primer3.UserInput;
import java.awt.BorderLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class OutReagentsPanel extends JPanel {

    JEditorPane editorPane;
    P3Output p3output;

    OutReagentsPanel() {
        initComponents();
    }
    
    void setP3output(P3Output p3output) {        
        this.p3output = p3output;
        refresh();
    }
    
    void refresh(){
        if(p3output != null){
            editorPane.setText(p3output.getReagentsHtml());
        }else{
            editorPane.setText("");
        }
    }    

    private void initComponents() {
        setLayout(new BorderLayout());
        IJEditorPaneService editorPaneService = Lookup.getDefault().lookup(IJEditorPaneService.class);
        editorPane = editorPaneService.create(false, "text/html");
        JScrollPane scrollPane = new JScrollPane(editorPane);
        add(scrollPane, BorderLayout.CENTER);
    }
}
