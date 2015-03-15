/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.tigr.util;

import com.gas.domain.core.tigr.TigrProject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
public class TigrProjectIO {

    static Logger log = Logger.getLogger(TigrProjectIO.class.getName());
    
    public static void write(TigrProject p, File file) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(p);
            objOut.close();
            fileOut.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static TigrProject read(File file) {
        TigrProject ret = null;
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
            ret = (TigrProject) objIn.readObject();
            objIn.close();
            fileIn.close();
        } catch (IOException ex) {
            log.severe(ex.toString());
        } catch (ClassNotFoundException ex) {
            log.severe(ex.toString());
        } catch (Exception e){
            log.severe(e.toString());
        }
        return ret;
    }
}
