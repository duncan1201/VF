/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core;

import com.gas.common.ui.FileFormat;
import java.io.File;
import java.util.EnumSet;

/**
 *
 * @author dq
 */
public interface IFileImportService {

    String[] getExtensions();
    
    EnumSet<FileFormat> getSupportedFileFormats();

    Object receive(File file);
}
