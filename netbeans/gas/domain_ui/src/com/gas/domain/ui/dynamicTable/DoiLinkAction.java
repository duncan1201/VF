/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.dynamicTable;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URISyntaxException;
import org.jdesktop.swingx.hyperlink.AbstractHyperlinkAction;
import org.jdesktop.swingx.hyperlink.HyperlinkAction;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
public class DoiLinkAction extends AbstractHyperlinkAction<String> {

    HyperlinkAction browse = HyperlinkAction.createHyperlinkAction(null,
            java.awt.Desktop.Action.BROWSE);
    public final static String URL_FORMAT = "http://dx.doi.org/%s";

    @Override
    protected void installTarget() {
        setName(target);
        setVisited(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            URI uri = null;
            if (target.startsWith("http")) {
                uri = new URI(target);
            } else {
                uri = new URI(String.format(URL_FORMAT, target));
            }
            browse.setTarget(uri);
            browse.actionPerformed(null);
        } catch (URISyntaxException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
