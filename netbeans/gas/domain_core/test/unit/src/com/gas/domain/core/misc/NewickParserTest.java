/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.misc;

import com.gas.domain.core.misc.api.Newick;
import com.gas.domain.core.misc.data.MiscDataDumb;
import java.io.InputStream;
import org.junit.*;

/**
 *
 * @author dq
 */
public class NewickParserTest {

    public NewickParserTest() {
    }

    /**
     * Test of parse method, of class NewickParser.
     */
    //@Test
    public void testParse() {
        System.out.println("testParse");
        String data = "(Adam:0.030822,(Bob:0.023241,Jane:0.005673):0.011836,(Harry:0.00552,Sally:0.011163):0.007142);";
        NewickParser parser = new NewickParser();
        Newick newich = parser.parse(data);
        String newichStr = newich.toString();
        Assert.assertEquals(data, newichStr);

        String complex = "(NM_20:5.6,(((NM_03:4.3,NM_06:-4.2):1.2,NM_07:-5.5):2.66,NM_04:4.7):1.9,NM_05:-5.6);";
        newich = parser.parse(complex);
        newichStr = newich.toString();
        Assert.assertEquals(complex, newichStr);
    }

    /**
     * Test of parse method, of class NewickParser.
     */
    //@Test
    public void testParse_complex() {
        System.out.println("testParse_complex");

        String complex = "(((NM_03:4.3,NM_06:-4.2):1.2,NM_07:-5.5):2.66,NM_04:4.7);";
        NewickParser parser = new NewickParser();
        Newick newich = parser.parse(complex);
        String newichStr = newich.toString();
        Assert.assertEquals(complex, newichStr);
    }

    @Test
    public void testParse_primates() {
        System.out.println("testParse_primates");
        InputStream stream = MiscDataDumb.class.getResourceAsStream(MiscDataDumb.PRIMATES);

        NewickParser parser = new NewickParser();
        Newick newick = parser.parse(stream);
        System.out.println(newick.toString());
    }

    //@Test
    public void testParse_primates_simple() {
        System.out.println("testParse_primates_simple");
        InputStream stream = MiscDataDumb.class.getResourceAsStream(MiscDataDumb.PRIMATES_SIMPLE);

        NewickParser parser = new NewickParser();
        Newick newich = parser.parse(stream);
        String newickStr = newich.toString();
        System.out.println(newickStr);
    }
}
