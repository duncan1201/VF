/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.vector.advanced.api;

import com.gas.domain.core.IFileImportService;
import com.gas.domain.core.as.AnnotatedSeq;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author dq
 */
public interface IMolImportService extends IFileImportService {

    List<AnnotatedSeq> receiveXA4(File file);

    //List<AnnotatedSeq> receivePA4(File file);
    List<AnnotatedSeq> receiveProteinsFromDB(File dbDir);

    List<AnnotatedSeq> receiveNucleotidesFromDB(File dbDir);
}
