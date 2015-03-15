/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.domain.core.as.util.FetureKeyParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class FetureKeyTest {

    public FetureKeyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() throws IOException {
        InputStream inputStream = FetureKeyTest.class.getResourceAsStream("feature_keys.txt");

        List<FetureKey> keys = FetureKeyParser.parse(inputStream);
        Assert.assertTrue(keys.size() > 0);
    }
}
