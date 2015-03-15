/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import com.gas.common.ui.util.MathUtil;
import java.util.List;
import java.util.Locale;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class MathUtilTest {

    public MathUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    //@Test
    public void testRound_double_integer() {
        double result = MathUtil.round(1.23456d, 3);
        System.out.println(result);
    }

    @Test
    public void testToString_double_integer() {
        String ret = MathUtil.toString(123456789.0000d, 5);
        System.out.println(ret);
    }
}
