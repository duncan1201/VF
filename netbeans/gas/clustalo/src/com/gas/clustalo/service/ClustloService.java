/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.clustalo.service;

import com.gas.clustalo.service.api.IClustaloService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.exec.*;

/**
 *
 * @author dq
 */
public class ClustloService implements IClustaloService {
    
    
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
            Logger.getLogger(ClustloService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClustloService.class.getName()).log(Level.SEVERE, null, ex);
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

    private String getExecutablePath() {
        return "";
    }
}
