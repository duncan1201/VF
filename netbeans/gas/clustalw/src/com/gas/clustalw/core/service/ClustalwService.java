/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.clustalw.core.service;

import com.gas.clustalw.core.service.api.IClustalwService;
import com.gas.domain.core.aln.Aln;
import com.gas.domain.core.aln.IAlnIOService;
import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.fasta.FastaParser;
import com.gas.domain.core.misc.api.INewickIOService;
import com.gas.domain.core.misc.api.Newick;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.msa.clustalw.ClustalTreeParam;
import com.gas.domain.core.msa.clustalw.ClustalwParam;
import com.gas.domain.core.msa.clustalw.DataParams;
import com.gas.domain.core.msa.clustalw.GeneralParam;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
 import org.openide.modules.InstalledFileLocator;
 import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IClustalwService.class)
public class ClustalwService implements IClustalwService {

    private final static Logger logger = Logger.getLogger(ClustalwService.class.getName());

    private final Preferences pref = Preferences.systemNodeForPackage(IClustalwService.class);
    
    @Override
    public MSA msa(ClustalwParam clustalwParam) {
        STATE state = validate(clustalwParam);
        if (state != STATE.VALID) {
            throw new IllegalArgumentException();
        }
        GeneralParam generalParams = clustalwParam.getGeneralParam();
        File outFile = generalParams.getOutfile();
        if (outFile == null) {
            throw new IllegalArgumentException();
        }

        MSA ret = new MSA();
        ret.setClustalwParam(clustalwParam);
        String retStr = execute(clustalwParam.toString());

        GeneralParam.OUTPUT output = generalParams.getOutput();

        if (output == GeneralParam.OUTPUT.FASTA) {
            FastaParser parser = new FastaParser();
            Fasta fasta = parser.parse(outFile);
            ret.setEntries(fasta);
        } else if (output == GeneralParam.OUTPUT.CLUSTAL) {
            IAlnIOService alnService = Lookup.getDefault().lookup(IAlnIOService.class);
            Aln aln = alnService.parse(outFile);
            ret.setEntries(aln);
        } else {
            throw new UnsupportedOperationException();
        }
        ret.setType(ret.isDnaByGuess() ? "DNA" : "Protein");
        ret.setLastModifiedDate(new Date());
        return ret;
    }

    private String execute(String arguments) {
        String ret = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Executor executor = createExcutor(outputStream);
        CommandLine commandLine = new CommandLine(getExecutablePath());
        commandLine.addArguments(arguments);
        try {
            executor.execute(commandLine);
            ret = outputStream.toString();
            System.out.println(ret);
        } catch (ExecuteException ex) {
            Logger.getLogger(ClustalwService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClustalwService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public Newick phylogeneticTree(ClustalTreeParam params) {
        validate(params);
        String out = execute(params.toString());
        String filePath = out.substring(out.indexOf('[') + 1, out.indexOf(']'));
        INewickIOService service = Lookup.getDefault().lookup(INewickIOService.class);
        Newick ret = service.parse(new File(filePath));

        return ret;

    }

    @Override
    public STATE validate(ClustalTreeParam params) {
        STATE ret = STATE.VALID;
        if (params.getInfile() == null) {
            ret = STATE.INFILE_EMPTY;
        }
        return ret;
    }

    @Override
    public STATE validate(ClustalwParam params) {
        STATE ret = STATE.VALID;
        DataParams dataParams = params.getDataParams();
        GeneralParam generalParams = params.getGeneralParam();
        if (dataParams.getInfile() == null && dataParams.getProfile1() == null && dataParams.getProfile2() == null) {
            ret = STATE.INFILE_EMPTY;
        }
        return ret;
    }

    private Executor createExcutor(OutputStream outputStream) {

        //ByteArrayInputStream inputStream = new ByteArrayInputStream("aaa".getBytes());
        ByteArrayOutputStream errorOutStream = new ByteArrayOutputStream();
        Executor exec = new DefaultExecutor();
        exec.setStreamHandler(new PumpStreamHandler(outputStream));

        return exec;
    }

    @Override
    public File getDefaultExecutableDirectory() {
        final File dir = InstalledFileLocator.getDefault().locate("modules/ext", "com.gas.clustalw.core", false);
        return dir;
    }
    
    @Override
    public void setExecutable(File file) {
        pref.put("executablePath", file.getAbsolutePath());
    }

    @Override
    public String getExecutablePath() {
        String ret = pref.get("executablePath", getDefaultExecutablePath());
        File file = new File(ret);
        file.setExecutable(true);
        return file.getAbsolutePath();
    }
    
    private String getDefaultExecutablePath() {
        File file = null;
        if (Utilities.isWindows()) {
            file = InstalledFileLocator.getDefault().locate("modules/ext/clustalw2.exe", "com.gas.clustalw.core", false);
        } else if (Utilities.isMac()) {
            file = InstalledFileLocator.getDefault().locate("modules/ext/clustalw2", "com.gas.clustalw.core", false);
        } else {
            throw new UnsupportedOperationException(Utilities.getOperatingSystem() + " is not supported");
        }

        return file.getAbsolutePath();
    }
}
