/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.service;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.Loc;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.as.util.AnnotatedSeqWriter;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RENManager;
import com.gas.domain.core.ren.RENSet;
import com.gas.domain.core.ren.RMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class DigestServiceTest {

    public static AnnotatedSeq DQ642038;

    public DigestServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        DQ642038 = AnnotatedSeqParser.singleParse(DigestServiceTest.class, "DQ642038.gb", new FlexGenbankFormat());
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of digest method, of class DigestService.
     */
    @Test
    public void testDigest_AnnotatedSeq_List() {
        System.out.println("digest");
        AnnotatedSeq as = DQ642038;
        REN bamHI = RENManager.getEnzyme("BamHI");
        RENSet renSet = new RENSet();
        renSet.add(bamHI);
        RMapService service = new RMapService();
        LocList locList = new LocList();
        locList.add(new Loc(1, as.getLength()));
        RMap rmap = service.findRM(as.getSiquence().getData(), true, "", renSet, 2, 2, locList, Boolean.TRUE);
        StringList entryNames = rmap.getEntryNames();
        Set<RMap.Entry> entries = rmap.getSortedEntries(entryNames);
        DigestService instance = new DigestService();
        AnnotatedSeqWriter writer = new AnnotatedSeqWriter();
        List<AnnotatedSeq> result = instance.digest(as, new ArrayList<RMap.Entry>(entries));        
        //AsHelper.print(result.get(0));
        System.out.println(writer.toString(result.get(0)));
        System.out.print("");
        //AsHelper.print(result.get(1));
        System.out.print("");
    }
}
