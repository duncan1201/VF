/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.mafft.service;

import com.gas.mafft.service.api.IMafftService;
import com.gas.mafft.service.api.MafftParams;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.exec.*;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IMafftService.class)
public class MafftService implements IMafftService {

    @Override
    public void align(MafftParams params) {
        String a = getBatchFilePath();
        try {
            //execute(" /c  D:\\tmp\\test.bat");
            execute(" /c  "+ a +" --auto --out D:\\tmp\\mafft_02.txt D:\\tmp\\example1.txt");            
            System.out.println("Done");
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        } 
    }
    
    public boolean validate(MafftParams params){
        boolean ret = false;
        return ret;
    }

    private String execute(String arguments) {
        String ret = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Executor executor = createExcutor(outputStream);
        CommandLine commandLine = new CommandLine("cmd");
        commandLine.addArguments(arguments);
        try {
            executor.execute(commandLine);
            ret = outputStream.toString();
            System.out.println(ret);
        } catch (ExecuteException ex) {
            Logger.getLogger(MafftService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MafftService.class.getName()).log(Level.SEVERE, null, ex);
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

    private String getBatchFilePath() {
        File file = null;
        if (Utilities.isWindows()) {
            file = InstalledFileLocator.getDefault().locate("modules/ext/mafft-win/mafft.bat", "com.gas.mafft", false);
        } else if (Utilities.isMac()) {
            throw new IllegalStateException("MAC OS not supported yet");
        } else {
            throw new IllegalStateException("Your OS not supported yet");
        }

        if (file == null) {
            file = new File("D:\\unfuddle_gas_svn\\netbeans\\gas\\mafft\\release\\modules\\ext\\mafft-win\\mafft.bat");
        }
        file.setExecutable(true);
        return file.getAbsolutePath();
    }
}
