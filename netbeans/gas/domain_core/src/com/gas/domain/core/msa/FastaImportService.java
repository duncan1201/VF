/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa;

import com.gas.common.ui.FileFormat;
import com.gas.domain.core.IFileImportService;
import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.fasta.FastaParser;
import com.gas.domain.core.msa.clustalw.ClustalwParam;
import java.io.File;
import java.util.Date;
import java.util.EnumSet;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IFileImportService.class)
public class FastaImportService implements IFileImportService {

    @Override
    public String[] getExtensions() {
        return FileFormat.FASTA.getExts();
    }

    /**
     * @return MSA if more than one sequence in the file; AnnotatedSeq if only
     * one sequence
     */
    @Override
    public Object receive(File file) {
        Object ret;
        FastaParser parser = new FastaParser();
        Fasta fasta = parser.parse(file);
        if (fasta.getSeqCount() > 1) {
            MSA msa = new MSA();
            msa.setName("Alignment");
            msa.setLastModifiedDate(new Date());
            msa.setEntries(fasta);
            msa.setDesc(String.format("Imported from %s", file.getAbsolutePath()));
            msa.setType(msa.isDnaByGuess() ? "DNA" : "Protein");
            msa.setClustalwParam(new ClustalwParam());
            msa.setLength(fasta.getLength());
            ret = msa;
        } else if (fasta.getSeqCount() == 1) {
            ret = fasta;
        } else {
            ret = null;
        }
        return ret;
    }

    @Override
    public EnumSet<FileFormat> getSupportedFileFormats() {
        EnumSet<FileFormat> ret = EnumSet.of(FileFormat.FASTA);
        return ret;
    }
}
