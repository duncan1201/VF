/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
public class NewClass {

    public void abc() {
        JarFile jar;
        try {
            jar = new JarFile("/path/to/myarchive.jar");
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                try {
                    jar.getInputStream(entry);
                } catch (SecurityException se) {
                    /* Incorrect signature */
                    throw new Error("Signature verification failed", se);
                } catch (IOException io) {
                }
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }
}
