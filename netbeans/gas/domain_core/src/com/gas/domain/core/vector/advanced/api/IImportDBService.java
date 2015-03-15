/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.vector.advanced.api;

import com.gas.domain.core.filesystem.Folder;
import java.io.File;

/**
 *
 * @author dq
 */
public interface IImportDBService {

    Folder receive(final File dbDir);
}
