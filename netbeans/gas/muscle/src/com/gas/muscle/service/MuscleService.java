/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.muscle.service;

import com.gas.common.ui.util.FileHelper;
import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.fasta.FastaParser;
import com.gas.domain.core.msa.MSA;
import com.gas.muscle.service.api.IMuscleService;
import com.gas.domain.core.msa.muscle.MuscleParam;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.apache.commons.exec.*;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Utilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IMuscleService.class)
public class MuscleService implements IMuscleService {

    private final Preferences pref = Preferences.systemNodeForPackage(IMuscleService.class);
    
    @Override
    public MSA align(MuscleParam param) {
        MSA ret = new MSA();
        ret.setMuscleParam(param);
        if (param.getOut() == null) {
            File outFile = FileHelper.getUniqueFile(true);
            param.setOut(outFile);
        }
        FastaParser fastaParser = new FastaParser();
        byte[] outStr = execute(param);

        Fasta outFasta = fastaParser.parse(outStr);
        ret.setEntries(outFasta);
        ret.setType(ret.isDnaByGuess() ? "DNA" : "Protein");
        ret.setLastModifiedDate(new Date());
        return ret;
    }

    @Override
    public boolean validate(MuscleParam params) {
        boolean ret = true;
        if (params.getIn() == null) {
            ret = false;
        }
        return ret;
    }

    private byte[] execute(MuscleParam params) {
        byte[] ret = null;
        String arguments = params.toString();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Executor executor = createExcutor(outputStream);
        CommandLine commandLine = new CommandLine(getDefaultExecutablePath());
        commandLine.addArguments(arguments);
        try {
            executor.execute(commandLine);
            ret = FileHelper.toBytes(params.getOut());
            System.out.print("");
        } catch (ExecuteException ex) {
            Logger.getLogger(MuscleService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MuscleService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    private org.apache.commons.exec.Executor createExcutor(OutputStream outputStream) {

        ByteArrayOutputStream errorOutStream = new ByteArrayOutputStream();
        org.apache.commons.exec.Executor exec = new DefaultExecutor();
        exec.setStreamHandler(new PumpStreamHandler(outputStream, errorOutStream));

        return exec;
    }

    public String getExecutablePath() {
        final String KEY = "executable_path";
        String path = pref.get(KEY, "");
        if (path.isEmpty()) {
            path = getDefaultExecutablePath();
            pref.put(KEY, path);
        }
        
        return path;
        
    }

    private String getDefaultExecutablePath() {
        File file = null;
        if (Utilities.isWindows()) {
            file = InstalledFileLocator.getDefault().locate("modules/ext/muscle3.8.31_i86win32.exe", "com.gas.muscle", false);
        } else if (Utilities.isMac()) {
            file = InstalledFileLocator.getDefault().locate("modules/ext/muscle3.8.31_i86darwin64", "com.gas.muscle", false);
        } else {
            throw new IllegalStateException("Your OS not supported yet");
        }

        file.setExecutable(true);
        return file.getAbsolutePath();
    }
}
