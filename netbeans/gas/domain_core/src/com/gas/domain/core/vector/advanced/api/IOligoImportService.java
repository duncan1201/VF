/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.vector.advanced.api;

import com.gas.domain.core.IFileImportService;
import com.gas.domain.core.as.AnnotatedSeq;
import java.io.File;
import java.util.List;

/**
 *
 * @author dq
 */
public interface IOligoImportService extends IFileImportService{
    List<AnnotatedSeq> receiveOligosFromDB(File dbDir);
}
