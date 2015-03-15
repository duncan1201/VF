/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.backup.api;

import java.io.File;

/**
 *
 * @author dq
 */
public interface IRestoreService {
    void restore(File file);
}
