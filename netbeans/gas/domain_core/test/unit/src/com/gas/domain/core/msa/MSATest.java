/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa;

import com.gas.common.ui.misc.Loc2D;
import java.awt.Point;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class MSATest {

    MSA msa = new MSA();
    String[] entries = {"1234", "ABCD", "EFGH", "IJKL"};

    @Before
    public void setUp() throws Exception {
        msa = new MSA();
        Integer i = 1;
        for (String str : entries) {
            MSA.Entry entry = new MSA.Entry();
            entry.setName(i.toString());
            entry.setData(str);
            msa.getEntries().add(entry);
            if (msa.getConsensus() == null) {
                msa.setConsensus(str);
            }
            i++;
        }
    }

    //@Test
    public void testReplace() {
        System.out.println("testReplace()");
        System.out.println(msa);
        msa.replace(new Loc2D(1, 2, 1, 3), "XY");
        System.out.print("");
        System.out.println(msa);
    }

    @Test
    public void testDelete() {
        System.out.println("testDelete()");
        System.out.println(msa);
        msa.delete(new Loc2D(3, 2, 3, 5));
        System.out.print("");
        System.out.println(msa);
    }

    //@Test
    public void testInsert() {
        System.out.println("testInsert()");
        System.out.println(msa);
        msa.insert(new Point(1, 1), "X");
        System.out.print("");
        System.out.println(msa);
    }
}
