/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.ren;

import java.util.Iterator;
import java.util.Set;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dunqiang
 */
public class RENManagerTest {

    public RENManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testLoadEnzymeFile_InputStream_boolean() {
    }

    @Test
    public void testLoadEnzymeFile_File_boolean() {
    }

    @Test
    public void testGetAllEnzymes() {
        Set<REN> rens = RENManager.getAllEnzymes(false, 1);
        System.out.println(rens.size());
        Assert.assertTrue(rens.size() > 0);
        Iterator<REN> renItr = rens.iterator();
        while (renItr.hasNext()) {
            REN ren = renItr.next();
            int[] upstreamCutPos = ren.getUpstreamCutPos();
            int[] downstreamCutPos = ren.getDownstreamCutPos();
            if (downstreamCutPos == null || downstreamCutPos.length < 2) {
                System.out.println("downstreamCutPos.length < 2");
            } else if (downstreamCutPos.length > 2) {
                System.out.println("downstreamCutPos.length > 2");
            }
            if (downstreamCutPos.length != 2) {
                System.out.println("downstreamCutPos.length != 2");
            }
            if (upstreamCutPos != null) {
                System.out.println("upstreamCutPos != null");
                System.out.println(ren.getName());
                System.out.println(ren.getCutType());
            }
            int cutType = ren.getCutType();

        }
    }
}
