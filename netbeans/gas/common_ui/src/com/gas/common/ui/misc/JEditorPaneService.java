/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import com.gas.common.ui.misc.api.IJEditorPaneService;
import com.gas.common.ui.util.UIUtil;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IJEditorPaneService.class)
public class JEditorPaneService implements IJEditorPaneService {

    @Override
    public JEditorPane create() {
        return create(null);
    }

    @Override
    public JEditorPane create(Boolean editable) {
        return create(editable, null);
    }

    @Override
    /*
     * The creation of JEditorPane must occured in AWT thread
     */
    public JEditorPane create(Boolean editable, String contentType) {
        boolean eventDispatchThread = SwingUtilities.isEventDispatchThread();
        if (eventDispatchThread) {
            JEditorPane ret = new JEditorPane();
            if (editable != null) {
                ret.setEditable(editable);
            }
            if (contentType != null && !contentType.isEmpty()) {
                ret.setContentType(contentType);
            }
            return ret;
        } else {
            JEditorPane ret = null;
            FactoryRunnable test = new FactoryRunnable(editable, contentType);
            UIUtil.invokeAndWait(test);
            ret = test.getEditorPane();
            return ret;
        }
    }

    private class FactoryRunnable implements Runnable {

        private JEditorPane editorPane;
        private Boolean editable;
        private String contentType;

        public FactoryRunnable(Boolean editable, String contentType) {
            this.editable = editable;
            this.contentType = contentType;
        }

        @Override
        public void run() {
            editorPane = new JEditorPane();
            if (editable != null) {
                editorPane.setEditable(editable);
            }
            if (contentType != null && !contentType.isEmpty()) {
                editorPane.setContentType(contentType);
            }
        }

        private JEditorPane getEditorPane() {
            return editorPane;
        }
    }
}
