/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.resources;

import java.io.File;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class ExporterTest {

    @Test
    public void testExport() {
        Exporter exporter = new Exporter(new File("D:\\sequenceanalysis_vf\\trunk\\netbeans\\gas\\database_core\\release\\modules\\ext\\resources"), new File("D:\\resources-output"));
        exporter.export();
        System.out.println();
    }

    //@Test
    public void testCreateNameList() {
        Exporter exporter = new Exporter(new File("D:\\sequenceanalysis_vf\\trunk\\netbeans\\gas\\database_core\\release\\modules\\ext\\resources"), new File("D:\\resources-output"));
        String nameList = exporter.createNameList();
        System.out.println(nameList);
    }
}
