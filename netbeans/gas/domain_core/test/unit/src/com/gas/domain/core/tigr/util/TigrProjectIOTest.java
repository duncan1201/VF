/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.tigr.util;

import com.gas.common.ui.core.IntList;
import com.gas.common.ui.util.MathUtil;
import com.gas.domain.core.tasm.Condig;
import com.gas.domain.core.tasm.Rid;
import com.gas.domain.core.tigr.TigrProject;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class TigrProjectIOTest {

    public TigrProjectIOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testWrite() {
    }

    @Test
    public void testRead() {
        System.out.println("read");
        String filePath = "D:\\sequenceanalysis_vf\\trunk\\netbeans\\gas\\database_core\\release\\modules\\ext\\exampleData\\Shortgun assemblies\\tigr_project.ser";
        //String filePath = "D:\\tmp\\tigr\\tigr_project.ser";
        File file = new File(filePath);
        TigrProject expResult = null;
        TigrProject result = (TigrProject) TigrProjectIO.read(file);
        Iterator<Condig> itr = result.getUnmodifiableCondigs().iterator();
        while (itr.hasNext()) {
            Condig condig = itr.next();
            String seq = condig.getLsequence();
            IntList qvs = new IntList(condig.getQualities());
            System.out.println("asmbId="+condig.getAsmblId());
            System.out.println("max qv="+qvs.getMax());
            System.out.println("min qv="+qvs.getMin());
            System.out.println("avg qv="+MathUtil.avg(qvs.toArray(new Integer[qvs.size()]), Integer.class));
            StringBuilder ret = new StringBuilder();
            for(int i = 0; i < seq.length(); i++){
                char c = seq.charAt(i);
                Integer qv = qvs.get(i);              
                ret.append(String.format("[%d %s %d]", i + 1, c, qv));
            }
            System.out.println(ret);
            System.out.println();
            Iterator<Rid> rItr = condig.getRids().iterator();
            while (rItr.hasNext()) {
                Rid rid = rItr.next();
                Assert.assertEquals(rid.getKromatogram().getOffsets().size(), rid.getKromatogram().getBases().size());
                System.out.print("");
            }
        }
        System.out.print("");
    }
}
