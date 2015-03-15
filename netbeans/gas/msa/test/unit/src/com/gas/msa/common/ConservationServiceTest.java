/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.common;

import com.gas.database.core.msa.service.api.CounterList;
import com.gas.common.ui.core.IntList;
import com.gas.common.ui.matrix.api.IMatrixService;
import com.gas.common.ui.matrix.api.Matrix;
import com.gas.common.ui.matrix.api.MatrixList;
import com.gas.database.core.msa.service.api.ICounterListService;
import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.fasta.FastaParser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class ConservationServiceTest {

    public ConservationServiceTest() {
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
     * Test of calculate method, of class ConservationService.
     */
    //@Test
    public void testCalculate_protein() {
        System.out.println("testCalculate_protein");
        ICounterListService counterListService = Lookup.getDefault().lookup(ICounterListService.class);
        FastaParser parser = new FastaParser();
        Fasta fasta = parser.parse(this.getClass(), "Protein_alignment.fasta");
        CounterList counterList = counterListService.createCounterList(fasta);

        IMatrixService matrixService = Lookup.getDefault().lookup(IMatrixService.class);
        MatrixList matrixList = matrixService.getAllMatrices();
        Matrix matrix = matrixList.getMatrix("Gonnet PAM 120");
        ConservationService instance = new ConservationService();
        instance.calculate(counterList, matrix);

    }

    @Test
    public void testCalculate_dna() {
        System.out.println("testCalculate_dna");
        ICounterListService counterListService = Lookup.getDefault().lookup(ICounterListService.class);
        FastaParser parser = new FastaParser();
        Fasta fasta = parser.parse(this.getClass(), "dna_alignment.fasta");
        CounterList counterList = counterListService.createCounterList(fasta);

        IMatrixService matrixService = Lookup.getDefault().lookup(IMatrixService.class);
        MatrixList matrixList = matrixService.getAllMatrices();
        Matrix matrix = matrixList.getMatrix("iub");
        ConservationService instance = new ConservationService();
        int[] test = instance.calculate(counterList, matrix);
        System.out.println(new IntList(test));
    }
}