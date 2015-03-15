/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.imports;

import com.gas.common.ui.FileFormat;
import com.gas.common.ui.misc.FileNameExtFilterComparator;
import com.gas.common.ui.util.FileHelper;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.IExportable;
import com.gas.domain.ui.editor.IExportEditor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
@ActionID(category = "File",
        id = "com.gas.main.ui.actions.imports.ExportCurrentEditorAction")
@ActionRegistration(displayName = "#CTL_ExportCurrentEditorAction")
@ActionReferences({
    @ActionReference(path = "Menu/File/Export", position = 0, separatorBefore = -50)
})
@NbBundle.Messages("CTL_ExportCurrentEditorAction=Export the document in the editor...")
public class ExportCurrentEditorAction extends AbstractAction {

    static String TITLE = "Export the selected document";
    
    public ExportCurrentEditorAction(String str, Icon icon){
        super(str, icon);
    }
    
    public ExportCurrentEditorAction(String str){
        this(str, null);
    }
    
    public ExportCurrentEditorAction(){
        super(TITLE + "...");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
                
        TopComponent tc = UIUtil.getEditorMode().getSelectedTopComponent();
        if (tc == null || !(tc instanceof IExportEditor)) {
            String msg;

            if(tc == null){
                msg = "There is no open document";
            }else{
                msg = "The selected document in the editor is not exportable";
            }
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(TITLE);
            DialogDisplayer.getDefault().notify(m);
        } else {
            IExportEditor editor = (IExportEditor) tc;
            IExportable exportable = editor.getExportable();

            final JFileChooser jc = new JFileChooser();
            jc.setDialogType(JFileChooser.SAVE_DIALOG);
            final File dir = jc.getCurrentDirectory();
            String exportName = exportable.getName();
            // replace "/" with "-" because "/" is path separator in Windows OS
            exportName = StrUtil.replaceAll(exportName, "/", "-");
            jc.setSelectedFile(new File(dir, exportName));
            List<FileNameExtensionFilter> filters = getFileFilters(exportable.getSupportedExportFormats());
            for (FileNameExtensionFilter f : filters) {
                jc.addChoosableFileFilter(f);
            }
            Frame frame = WindowManager.getDefault().getMainWindow();

            int answer = UIUtil.showDialog(jc, frame, "Export");
            if (answer == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jc.getSelectedFile();
                FileNameExtensionFilter fileFilter = (FileNameExtensionFilter)jc.getFileFilter();
                File f = new File(selectedFile.getParent(), String.format("%s.%s", selectedFile.getName(), fileFilter.getExtensions()[0]));
                if(f.exists()){
                    DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(String.format("File \"%s\" already exists. Do you want to replace it?", f.getName()), "Export");
                    Object answer2 = DialogDisplayer.getDefault().notify(c);
                    if(answer2.equals(DialogDescriptor.OK_OPTION)){
                        FileHelper.toFile(f, exportable.export(FileFormat.get(fileFilter.getExtensions()[0])));
                    }
                }else{
                    FileHelper.toFile(f, exportable.export(FileFormat.get(fileFilter.getExtensions()[0])));
                }
            }
        }
    }

    private List<FileNameExtensionFilter> getFileFilters(List<FileFormat> formats) {
        List<FileNameExtensionFilter> ret = new ArrayList<FileNameExtensionFilter>();

        //ret.add(new FileNameExtensionFilter("*.*", "*"));
        for (FileFormat format : formats) {
            ret.add(new FileNameExtensionFilter(format.getDesc(), format.getExts()));
        }
        Collections.sort(ret, new FileNameExtFilterComparator());
        return ret;
    }
}
