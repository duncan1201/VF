/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.actions;

import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.ui.editor.as.IASEditor;
import com.gas.gateway.core.service.api.AttSite;
import com.gas.gateway.core.service.api.AttSiteList;
import com.gas.gateway.core.service.api.IAttSiteService;
import com.gas.gateway.core.ui.annotateAttSites.AnnotateSitesPanel;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 *
 * @author dq
 */
@ActionID(category = "Tools",
        id = "com.gas.gateway.ui.actions.AnnotateAttSitesAction")
@ActionRegistration(displayName = "#CTL_AnnotateAttSitesAction")
@ActionReferences({
    @ActionReference(path = "Menu/Tools/Gateway", position = 2445)
})
@NbBundle.Messages("CTL_AnnotateAttSitesAction=Annotate att Sites")
public class AnnotateAttSitesAction extends AbstractAction {

    private IAttSiteService service = Lookup.getDefault().lookup(IAttSiteService.class);
    private static String TITLE = "Annotate att Sites";

    public AnnotateAttSitesAction() {
        super(TITLE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent tc = UIUtil.getEditorMode().getSelectedTopComponent();
        if (tc == null || !(tc instanceof IASEditor)) {
            String msg = String.format(CNST.ERROR_FORMAT, "No nucleotide editor open", "Please open a nucleotide editor");
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(TITLE);
            DialogDisplayer.getDefault().notify(m);
            return;
        }
        IASEditor editor = (IASEditor) tc;
        AnnotatedSeq as = editor.getAnnotatedSeq();
        if(as.isProtein()){
            String msg = String.format(CNST.ERROR_FORMAT, "The editor is an amino acid editor", "Please open a nucleotide editor");
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(TITLE);
            DialogDisplayer.getDefault().notify(m);                       
            return;
        }        

        AnnotateSitesPanel panel = new AnnotateSitesPanel();
        DialogDescriptor dd = new DialogDescriptor(panel, TITLE);
        Integer answer = (Integer) DialogDisplayer.getDefault().notify(dd);
        if (answer.equals(DialogDescriptor.OK_OPTION)) {
            AnnotateSitesPanel.SEARCH_TYPE type = panel.getSearchType();
            AttSiteList sites = null;
            if (type == AnnotateSitesPanel.SEARCH_TYPE.attBSites) {
                sites = service.getAttBSites(as, true);
            } else if (type == AnnotateSitesPanel.SEARCH_TYPE.attPSites) {
                sites = service.getAttPSites(as);
            } else if (type == AnnotateSitesPanel.SEARCH_TYPE.attLSites) {
                sites = service.getAttLSites(as);
            } else if (type == AnnotateSitesPanel.SEARCH_TYPE.attRSites) {
                sites = service.getAttRSites(as);
            }
            final String msg = String.format("%s %s found!", sites.size() > 0 ? sites.size() + "" : "No", type.toString());
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(TITLE);
            DialogDisplayer.getDefault().notify(m);

            if (sites.size() > 0) {
                as.setLastModifiedDate(new Date());
                as.getFetureSet().addAll(toFetures(sites));
                editor.refresh();
                editor.setCanSave();
            }
        }
    }

    private List<Feture> toFetures(AttSiteList attSiteList) {
        List<Feture> ret = new ArrayList<Feture>();
        for (AttSite attSite : attSiteList) {
            Feture feture = toFeture(attSite);
            ret.add(feture);
        }
        return ret;
    }

    private Feture toFeture(AttSite attSite) {
        Feture ret = new Feture();
        Character clazz = attSite.getClazz();
        String name = attSite.getName();
        String baseName = attSite.getBaseName();
        ret.setKey(name);
        Lucation luc = new Lucation(attSite.getLoc().getStart(), attSite.getLoc().getEnd(), attSite.getLoc().isStrand());
        ret.setLucation(luc);
        ret.getQualifierSet().add(String.format("label=%s", name));
        return ret;
    }
}
