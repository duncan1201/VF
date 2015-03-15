/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.ren;

import com.gas.domain.core.ren.RENManager;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RENComparators.NameComparator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.util.Exceptions;

/**
 *
 * @author dunqiang
 */
public class RENComparatorsTest {

    public RENComparatorsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testLoad() {
        Set enzymes = RENManager.getAllEnzymes();
        List enzymeList = new ArrayList(enzymes);
        System.out.println(enzymeList.size());

        Collections.sort(enzymeList, new NameComparator());
        Iterator all = enzymeList.iterator();
        int blunt = 0;
        int _3 = 0;
        int _5 = 0;
        int supplierNo = 0;
        while (all.hasNext()) {
            REN enzyme = (REN) all.next();
            if (enzyme.getDownstreamEndType() == REN.OVERHANG_5PRIME) {
                _5++;
            }
            if (enzyme.getDownstreamEndType() == REN.OVERHANG_3PRIME) {
                _3++;
            }
            if (enzyme.getDownstreamEndType() == REN.BLUNT) {
                blunt++;
            }
            String supplier = RENManager.getSuppliers(enzyme);
            if (supplier.length() >= 7) {
                supplierNo++;
            }
        }
        System.out.println("5'=" + _5);
        System.out.println("3'=" + _3);
        System.out.println("blunt=" + blunt);
        System.out.println("supplierNo=" + supplierNo);
    }

    @Test
    public void testFilter() {
        RENComparators n = new RENComparators();
        try {
            StringBuilder b = n.filter("commercial.txt");
            //System.out.println(b.toString());
            //FileHelper.toFile(new File("D:/tmp/com.txt"), b);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
