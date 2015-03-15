/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import org.junit.After;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author dq
 */
public class PozitionTest {

    @After
    public void tearDown() {
    }

    /**
     * @see Pozition#deleteSeq(int, int, java.lang.Integer)
     */
    @Test
    public void testDeleteSeq_case() {
        Object[][] dataList = {
            {"case a)", 50, 80, 51, 60, 100, 50, 70},
            {"case a)", 50, 80, 50, 60, 100, 50, 69},
            {"case b)", 1, 100, 95, 9, 100, 1, 85},
            {"case c)", 90, 10, 95, 3, 100, 87, 7},
            {"case d)", 50, 80, 70, 90, 100, 50, 69},
            {"case e)", 50, 80, 39, 69, 100, 39, 49},
            {"case f)", 5, 80, 70, 10, 100, 1, 59},
            {"case g)", 5, 80, 95, 10, 100, 1, 70},
            {"case h)", 5, 95, 92, 2, 100, 3, 89},
            {"case i)", 89, 9, 5, 92, 100, 5, 4},
            {"case j)", 90, 10, 4, 30, 100, 63, 3},
            {"case k)", 90, 10, 78, 95, 100, 78, 10},
            {"case l)", 30, 40, 10, 20, 100, 19, 29},
            {"case m)", 30, 40, 90, 10, 100, 20, 30},
            {"case n)", 90, 10, 20, 30, 100, 79, 10},};
        for (Object[] data : dataList) {
            int i = 0;
            Object desc = data[i++];
            Integer start = (Integer) data[i++];
            Integer end = (Integer) data[i++];
            Integer start2 = (Integer) data[i++];
            Integer end2 = (Integer) data[i++];
            Integer totalLength = (Integer) data[i++];
            Integer startExpected = (Integer) data[i++];
            Integer endExpected = (Integer) data[i++];

            System.out.println(String.format("%s start:%d end:%d start2:%d end2:%d totalLength:%d", desc, start, end, start2, end2, totalLength));
            Pozition poz = new Pozition(start, end);
            poz.deleteSeq(start2, end2, totalLength);
            Assert.assertEquals(poz.getStart(), startExpected);
            Assert.assertEquals(poz.getEnd(), endExpected);
        }
    }
}
