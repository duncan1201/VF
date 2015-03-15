/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core;

import com.gas.common.ui.FileFormat;
import java.util.List;

/**
 *
 * @author dq
 */
public interface IExportable {

    List<FileFormat> getSupportedExportFormats();

    String export(FileFormat format);

    String getName();
}
