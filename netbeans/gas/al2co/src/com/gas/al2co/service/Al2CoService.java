/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.al2co.service;

import com.gas.al2co.service.api.Al2CoParams;
import com.gas.al2co.service.api.IAl2CoService;
import com.gas.common.ui.core.FloatList;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.exec.*;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Utilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IAl2CoService.class)
public class Al2CoService implements IAl2CoService {

    @Override
    public FloatList calculateConservation(Al2CoParams params) {
        execute(params.toString());
        FloatList ret= Parser.parse(params.getOut());
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
            Logger.getLogger(Al2CoService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Al2CoService.class.getName()).log(Level.SEVERE, null, ex);
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
        File file = null;
        if (Utilities.isWindows()) {
            file = InstalledFileLocator.getDefault().locate("modules/ext/al2co.exe", "com.gas.al2co", false);
        } else if (Utilities.isMac()) {
            throw new IllegalStateException("MAC OS not supported yet");
        } else {
            throw new IllegalStateException("Your OS not supported yet");
        }

        if (file == null) {
            file = new File("D:\\unfuddle_gas_svn\\netbeans\\gas\\al2co\\release\\modules\\ext\\al2co.exe");
        }

        file.setExecutable(true);
        return file.getAbsolutePath();
    }
}
