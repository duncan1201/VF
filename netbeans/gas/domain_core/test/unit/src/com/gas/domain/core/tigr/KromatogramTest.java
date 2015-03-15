/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.tigr;

import com.gas.domain.core.tigr.util.KromatogramParser;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 *
 * @author dq
 */
public class KromatogramTest {

    static Kromatogram ret = null;

    public KromatogramTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {

        File file = null;
        file = new File("D:\\tmp\\abi\\F\\HS354_C02_7360_F_0.b.ab1");
        ret = KromatogramParser.parse(file);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
}
