/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.primer3.core;

import com.gas.common.ui.util.FileHelper;
import com.gas.domain.core.primer3.IUserInputFactory;
import java.io.File;
import com.gas.domain.core.primer3.UserInput;
import com.gas.primer3.core.api.BoulderIOUtil;
import java.util.Map;
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
public class Primer3ServiceTest {

    public Primer3ServiceTest() {
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

    /**
     * Test of execute method, of class Primer3Executor.
     */
    //@Test
    public void testExecute() {
        boolean updatedOne = false;
        Primer3Service p = new Primer3Service();

        // Input input =
        // InputFactory.getInput(P3Params.class.getResourceAsStream("input_old_04.txt"));
        IUserInputFactory userInputFactory = Lookup.getDefault().lookup(IUserInputFactory.class);
        UserInput userInput = userInputFactory.getP3WEB_V_3_0_0();
        Map<String, String> inputData = BoulderIOUtil.parse(Primer3ServiceTest.class, "dv_conc_vs_dntp_conc_input");
        userInput.updateData(inputData);

        String outputBytes = p.execute(userInput, String.class);

        FileHelper.toFile(new File("D:\\tmp\\p3output.txt"), outputBytes);
        //P3Output output = P3OutputParser.parse(outputBytes);

    }

    @Test
    public void test_checkPrimers() throws Exception {

        String a_10 = "g";
        String a_15 = "gtcctcgaacagctc";
        String a_17 = "gtcctcgaacagctcag";
        String a_20 = "gtcctcgaacagctcagtcc";
        String a_25 = "gtcctcgaacagctcagtccagtcc";
        String a_27 = "gtcctcgaacagctcagtccagtccac";
        String a_36 = "gtcctcgaacagctcagtccgtcctcgaacagctca";
        String a_37 = "gtcctcgaacagctcagtccgtcctcgaacagctcaa";
        String a_40 = "gtcctcgaacagctcagtccgtcctcgaacagctcagtcc";

        Primer3Service p = new Primer3Service();
        UserInput userInput = new UserInput();
        userInput.set("SEQUENCE_ID", "Check primers");
        userInput.set("PRIMER_TASK", "check_primers");
        //userInput.setThermodynamicModels(false);
        userInput.set("SEQUENCE_PRIMER_REVCOMP", a_10);
        userInput.set("PRIMER_PICK_RIGHT_PRIMER", "0");
        userInput.set("PRIMER_PICK_INTERNAL_OLIGO", "0");
        userInput.set("PRIMER_PICK_LEFT_PRIMER", "0");
        userInput.enablePickAnyway();

        //p.setErrorFile(new File("D:\\tmp\\error.txt"));
        p.setEchoSettingsFile(false);
        p.setStrictTags(false);

        String result = p.checkPrimer(userInput, String.class);
        System.out.println(result);
    }
}
