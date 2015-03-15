/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.matrix;

import com.gas.common.ui.matrix.api.Matrix;
import com.gas.common.ui.matrix.api.MatrixList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class MatrixTest {

    public MatrixTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getScore method, of class Matrix.
     */
    @Test
    public void testGetScore_gonnet_pam350() {
        System.out.println("getScore");
        MatrixService service = new MatrixService();
        MatrixList matrixList = service.getAllMatrices();
        Object[][] testData = {
            {'A', 'A', 10},
            {'A', 'B', 0},
            {'A', 'C', 4},
            {'A', 'Y', -14},
            {'D', 'C', -19},
            {'C', 'D', -19},};


        Matrix instance = matrixList.getMatrix("Gonnet PAM 350");

        for (Object[] data : testData) {
            Character r1 = (Character) data[0];
            Character r2 = (Character) data[1];
            Integer expected = (Integer) data[2];
            Short result = instance.getScore(r1, r2);
            assertEquals(expected.shortValue(), result.shortValue());
        }
    }

    /**
     * Test of getScore method, of class Matrix.
     */
    //@Test
    public void testGetScore_iub() {
        System.out.println("getScore");
        MatrixService service = new MatrixService();
        MatrixList matrixList = service.getAllMatrices();
        Object[][] testData = {
            {'A', 'A', 10},
            {'A', 'B', -9},
            {'A', 'C', -9},
            {'A', 'Y', -9},
            {'D', 'C', -9},};


        Matrix instance = matrixList.getMatrix("iub");

        for (Object[] data : testData) {
            Character r1 = (Character) data[0];
            Character r2 = (Character) data[1];
            Integer expected = (Integer) data[2];
            Short result = instance.getScore(r1, r2);
            assertEquals(expected.shortValue(), result.shortValue());
        }
    }
}