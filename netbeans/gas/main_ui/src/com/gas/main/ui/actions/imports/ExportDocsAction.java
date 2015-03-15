/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.imports;

import com.gas.common.ui.misc.FileNameExtFilterComparator;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqWriter;
import com.gas.domain.ui.banner.BannerTC;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

@ActionID(category = "File",
id = "com.gas.main.ui.actions.ExportDocsAction")
@ActionRegistration(displayName = "#CTL_ExportDocsAction")
@ActionReferences({
    @ActionReference(path = "Menu/File/Export", position = 3333)
})
@Messages("CTL_ExportDocsAction=Selection into archive...")
public final class ExportDocsAction extends AbstractAction {

    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    
    public ExportDocsAction(){
        super("Selection into archive...");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        List list = aaa();
        if (list.isEmpty()) {
            return;
        }
        AnnotatedSeq as = (AnnotatedSeq)list.get(0);
        final Frame frame = WindowManager.getDefault().getMainWindow();
        final JFileChooser jc = new JFileChooser();
        jc.setDialogType(JFileChooser.SAVE_DIALOG);
        final File dir = jc.getCurrentDirectory();
        jc.setSelectedFile(new File(dir, as.getName())); 
        List<FileNameExtensionFilter> filters = getFileFilters(list);
        for (FileNameExtensionFilter f : filters) {
            jc.addChoosableFileFilter(f);
        }

        int answer = UIUtil.showDialog(jc, frame, "Export");
        if (answer == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = jc.getSelectedFile();            
            final FileNameExtensionFilter filter = (FileNameExtensionFilter)jc.getFileFilter();
            final String[] exts = filter.getExtensions();
            File f = new File(selectedFile.getParent(), String.format("%s.%s", selectedFile.getName(), exts[0]));
            
            AnnotatedSeq full = asService.getFullByHibernateId(as.getHibernateId());
            AnnotatedSeqWriter.toFile(full, f);
        }
    }

    private List<FileNameExtensionFilter> getFileFilters(List list) {
        List<FileNameExtensionFilter> ret = new ArrayList<FileNameExtensionFilter>();
        
        ret.add(new FileNameExtensionFilter("Genbank Files(*.gb)", "gb"));
        //ret.add(new FileNameExtensionFilter("Genbank Files(*.gp)", "gp"));
        Collections.sort(ret, new FileNameExtFilterComparator());
        return ret;
    }

    private List aaa() {
        List list = BannerTC.getInstance().getCheckedObjects();
        return list;
    }
}
