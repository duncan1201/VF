/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gas.common.ui.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.io.FilenameUtils;
import org.openide.util.Utilities;

/**
 *
 * @author Dunqiang
 */
public class ExecutableFileFilter extends FileFilter{

    @Override
    public boolean accept(File f) {
        boolean canExe = f.canExecute();
        if (Utilities.isWindows()) {
            String ext = FilenameUtils.getExtension(f.getAbsolutePath());
            return canExe && "exe".equalsIgnoreCase(ext);
        } else if (Utilities.isMac()) {
            return canExe;
        }
        
        return canExe;
    }

    @Override
    public String getDescription() {
        return "Executables";
    }
}
