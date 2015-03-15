/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.exportAsImage;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.editor.IPrintEditor;
import java.awt.Frame;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(category = "File",
        id = "com.gas.main.ui.actions.saveAsImage.XImageAction")
@ActionRegistration(displayName = "#CTL_XImageAction")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 2480),
})
@NbBundle.Messages("CTL_XImageAction=Export As Image File...")
public class XImageAction extends AbstractAction {
    
    public XImageAction(){
        putValue(Action.LARGE_ICON_KEY, ImageHelper.createImageIcon(ImageNames.IMAGE_EXPORT_16));
        putValue(Action.LARGE_ICON_KEY, ImageHelper.createImageIcon(ImageNames.IMAGE_EXPORT_24));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent tc = UIUtil.getEditorMode().getSelectedTopComponent();
        if (!(tc instanceof IPrintEditor)) {
            return;
        }
        IPrintEditor editor = (IPrintEditor) tc;
        
        ExportImagePanel exportImagePanel = new ExportImagePanel(editor);
        
        final String TITLE = String.format("Export \"%s\" as an Image", editor.getJobName());
        DialogDescriptor dd = new DialogDescriptor(exportImagePanel, TITLE);
        Object export = DialogDisplayer.getDefault().notify(dd);
        if (export.equals(DialogDescriptor.OK_OPTION)) {
            JFileChooser fileChooser = new JFileChooser();
            File dir = fileChooser.getCurrentDirectory();
            final String format = exportImagePanel.getSelectedFormat();
            String jobName = editor.getJobName();
            jobName = StrUtil.replaceAll(jobName, "/", "-");
            File name = new File(dir, jobName + "." + format);
            
            fileChooser.setSelectedFile(name);
            Frame mainWindow = WindowManager.getDefault().getMainWindow();
            int save = fileChooser.showSaveDialog(mainWindow);
            
            if (save == JFileChooser.APPROVE_OPTION) {
                BufferedImage image = null;
                if (format.equalsIgnoreCase("bmp") || format.equalsIgnoreCase("jpg") || format.equalsIgnoreCase("wbmp")) {
                    image = editor.createImageForExporting(exportImagePanel.isVisibleOnly(), Transparency.OPAQUE);
                } else {
                    image = editor.createImageForExporting(exportImagePanel.isVisibleOnly(), Transparency.TRANSLUCENT);
                }
                
                File selectedFile = fileChooser.getSelectedFile();
                if(selectedFile.exists()){
                    String msg = String.format(CNST.MSG_FORMAT, "\""+selectedFile.getName() + "\" already exists", "Do you want to replace it?");
                    DialogDescriptor.Confirmation c = new DialogDescriptor.Confirmation(msg, TITLE, DialogDescriptor.YES_NO_OPTION);
                    Object answer = DialogDisplayer.getDefault().notify(c);
                    if(answer.equals(DialogDescriptor.OK_OPTION)){
                    
                    }
                }
                UIUtil.toFile(image, format, selectedFile);
            }
        }
    }
}
