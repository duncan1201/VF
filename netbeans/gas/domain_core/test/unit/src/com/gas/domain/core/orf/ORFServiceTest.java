/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.orf;

import com.gas.common.ui.core.LocList;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.domain.core.geneticCode.api.GeneticCodeTable;
import com.gas.domain.core.geneticCode.api.IGeneticCodeTableService;
import com.gas.domain.core.orf.api.ORFParam;
import data.DataDumb;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import org.junit.*;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class ORFServiceTest {

    private static AnnotatedSeq NM_001114;

    @BeforeClass
    public static void setUpClass() throws Exception {
        NM_001114 = AnnotatedSeqParser.singleParse(DataDumb.class, DataDumb.NM_001114, new FlexGenbankFormat());
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
     * Test of find method, of class ORFService.
     */
    @Test
    public void testFind() {
        System.out.println("find");
        IGeneticCodeTableService service = Lookup.getDefault().lookup(IGeneticCodeTableService.class);
        GeneticCodeTable table = service.getTable("Standard");
        ORFParam params = new ORFParam();
        params.setSequence(NM_001114.getSiquence().getData());
        params.setCircular(false);
        params.setFrames(new int[]{1, 2});
        params.setMinLength(100);
        params.setStartCodons(table.getStartCodons().getBasesAsSet());
        params.setStopCodons(table.getStopCodons().getBasesAsSet());
        params.setStopCodonIncluded(true);

        ORFService instance = new ORFService();
        Map<Integer, LocList> result = instance.find(params);
        Iterator<Integer> itr = result.keySet().iterator();
        while (itr.hasNext()) {
            Integer frame = itr.next();
            LocList locList = result.get(frame);
            Collections.sort(locList);
            System.out.println(String.format("frame %d: %s", frame, locList));
        }
        System.out.print("");
    }
}
