/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.phylo.actions;

import com.gas.clustalw.core.service.api.IClustalwService;
import com.gas.phylo.clustalw.TreePanel;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.FileHelper;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.misc.api.Newick;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.msa.clustalw.ClustalTreeParam;
import com.gas.domain.ui.editor.msa.api.IMSAEditor;
import com.gas.phylo.clustalw.ClustalwPanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;

/**
 *
 * @author dq
 */
public class TreeAction extends AbstractAction {

    public TreeAction() {
        super("Tree", ImageHelper.createImageIcon(ImageNames.TREE_24));
        putValue(Action.LARGE_ICON_KEY, ImageHelper.createImageIcon(ImageNames.TREE_24));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent tc = UIUtil.getEditorMode().getSelectedTopComponent();
        MSA msa = null;
        IMSAEditor editor;
        if (tc instanceof IMSAEditor) {
            editor = (IMSAEditor) tc;
            msa = editor.getMsa();
        } else {
            DialogDescriptor.Message m = new DialogDescriptor.Message(String.format(CNST.ERROR_FORMAT, "Cannot build phylogenetic tree", "Please open an alignment"), DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle("Phylogenetic tree");
            DialogDisplayer.getDefault().notify(m);
            return;
        }

        TreePanel treePanel = new TreePanel();
        treePanel.setMsa(msa);
        DialogDescriptor dd = new DialogDescriptor(treePanel, String.format("Creating tree for %s", treePanel.getMsa().getName()));
        Object answer = DialogDisplayer.getDefault().notify(dd);
        if (answer.equals(DialogDescriptor.OK_OPTION)) {
            Component comp = treePanel.getSelectedComponent();
            if (comp instanceof ClustalwPanel) {
                IClustalwService clustalwService = Lookup.getDefault().lookup(IClustalwService.class);
                ClustalTreeParam param = treePanel.getMsa().getClustalTreeParam();
                Map<String, String> entries = treePanel.getMsa().getEntriesMapCopy();
                Fasta fasta = new Fasta(entries);
                File file = FileHelper.getUniqueFile(true);
                FileHelper.toFile(file, fasta.toString());
                param.setInfile(file);
                Newick newick = clustalwService.phylogeneticTree(param);
                msa.setNewick(newick);

                editor.setCanSave();
                editor.refreshUI();
            }else{
                
            }
        }
    }
}
