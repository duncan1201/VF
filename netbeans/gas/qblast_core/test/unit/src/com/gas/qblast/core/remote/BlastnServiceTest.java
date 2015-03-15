/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.qblast.core.remote;

import com.gas.common.ui.util.FileHelper;
import com.gas.qblast.core.output.BlastOutput;
import com.gas.qblast.core.output.BlastOutputIterations;
import com.gas.qblast.core.output.Iteration;
import com.gas.qblast.core.parameter.BlastParams;
import com.gas.qblast.core.parameter.BlastParams.PROGRAM;
import com.gas.qblast.core.parameter.BlastParamsFactory;
import com.gas.qblast.core.parameter.GapCost;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
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
public class BlastnServiceTest {
    
    public BlastnServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     * Test of main method, of class BlastnService.
     */
    @Test
    public void testMain() throws Exception {
        String query = "AUGGCCGUCCUGCCGCCCCGAACUCUCCUCCUGCUACUCUCGGGGGCCCUGGCCCUGACCCAGACCUGGGCGGGCUCCCACUCCAUGAGGUAUUUCUUCACCUCCGUGUCCCGGCCCGGCCGCGGGGAGCCCCGCUUCAUCGCCGUGGGCUACGUGGACGACACGCAGUUCGUGCGGUUCGACAGCGACGCCGCGAGCCAGAGGAUGGAGCCGCGGGCGCCGUGGAUAGAGCAGGAGGGUCCGGAGUAUUGGGACCAGGAGACACGGAAUGUGAAGGCCCACUCACAGACUGACCGAGUGAACCUGGGGACCCUGCGCGGCUACUACAACCAGAGCGAGGACGGUUCUCACACCAUCCAGAUAAUGUAUGGCUGCGACGUGGGGUCGGACGGGCGCUUCCUCCGCGGGUACCGGCAGGACGCCUACGACGGCAAGGAUUACAUCGCCCUGAACGAGGACCUGCGCUCUUGGACCGCGGCGGACAUGGCAGCUCAGAUCACCAAGCGCAAGUGGGAGGCGGCCCAUGCGGCGGAGCAGCGGAGAGCCUACCUGGAGGGCCUGUGCGUGGACGGGCUCCGCAGAUACCUGGAGAACGGGAAGGAGACGCUGCAGCGCACGGACCCCCCCAAGACACAUAUGACCCACCACCCCAUCUCUGACCAUGAGGCCACCCUGAGGUGCUGGGCCCUGGGCUUCUACCCUGCGGAGAUCACACUGACCUGGCAGCGGGAUGGGGAGGACCAGACCCAGGACACGGAGCUCGUGGAGACCAGGCCCGCAGGGGAUGGAACCUUCCAGAAGUGGGCGGCUGUGGUGGUACCUUCUGGAGAGGAGCAGAGAUACACCUGCCAUGUGCAGCAUGAGGGUCUUCCCAAGCCCCUCACCCUGAGAUGGGAGCCGUCUUCCCAGCCCACCAUCCCCAUUGUGGGCAUCAUUGCUGGCCUGGUUCUCCUUGGAGCUGUGAUCACUGGAGCUGUGGUCGCUGCCGUGAUGUGGAGGAGGAAGAGCUCAGAUAGAAAAGGAGGGAGCUACACUCAGGCUGCAAGCAGUGACAGUGCCCAGGGCUCUGAUGUGUCUCUCACAGCUUGUAAAGUGUGA";

        final BlastnService service = new BlastnService();
        service.setParams(BlastParamsFactory.createInstance(PROGRAM.blastn));

        service.addListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if (propertyName.equals(AbstractBlastService.PUT_COMMAND_COMPLETED_PROPERTY)) {
                    AbstractBlastService service = (AbstractBlastService) evt.getSource();
                    PutResult putResult = service.getPutResult();
                    System.out.println(putResult);
                    //System.exit(0);
                } else if (propertyName.equals(AbstractBlastService.GET_COMMAND_COMPLETED_PROPERTY)) {
                    AbstractBlastService service = (AbstractBlastService) evt.getSource();
                    String getResult = service.getGetResult();
                    //System.out.println(getResult);

                    FileHelper.toFile(new File("D:\\tmp\\GetResult.txt"), getResult);
                    //System.exit(0);
                } else if (propertyName.equals(AbstractBlastService.PARSING_OUTPUT_COMPLETED_PROPERTY)) {
                    AbstractBlastService service = (AbstractBlastService) evt.getSource();
                    BlastOutput blastOutput = (BlastOutput) evt.getNewValue();
                    BlastOutputIterations itr = blastOutput.getBlastOutputIterations();
                    List<Iteration> iterations = itr.getIteration();
                    System.exit(0);
                }
            }
        });


        BlastParams b = service.getParams();
        service.getGetCommand().setFormatType(GetCommand.FORMAT_TYPE.TEXT);
        b.setQuery(query);
        b.setQueryFrom(1);
        b.setQueryTo(query.length());
        //b.setRepeats(Repeats.Human);
        b.setGapCost(new GapCost(5, 2));

        Thread r = new Thread(new Runnable() {

            @Override
            public void run() {
                service.execute();

            }
        });
        //r.start();     
        
        service.execute();
    }

}
