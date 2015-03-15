/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.service;


import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RENManager;
import com.gas.domain.core.ren.RENSet;
import com.gas.domain.core.ren.RMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dunqiang
 */
public class RestrictionMapServiceTest {
    
    static AnnotatedSeq NM_008238_gb ;
    static RMapService service ;
    static RENSet EcoRI_rens = new RENSet();
    static RENSet AvaI_rens = new RENSet();
    
    
    public RestrictionMapServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        NM_008238_gb = AnnotatedSeqParser.singleParse(RestrictionMapServiceTest.class, "NM_008238.gb", new FlexGenbankFormat());
        service = new RMapService();
        
        EcoRI_rens.add(RENManager.getEnzyme("EcoRI"));
        
        AvaI_rens.add(RENManager.getEnzyme("AvaI"));
        
        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testFindRM() {
        Assert.assertTrue(!EcoRI_rens.isEmpty());
        RMap rm = service.findRM(NM_008238_gb.getSiquence().getData(), false, "", EcoRI_rens, 1, 1,  null, true);
        Assert.assertTrue(rm.getSize() == 1);
        Iterator<RMap.Entry> entryItr = rm.getEntriesIterator();
        while(entryItr.hasNext()){
            RMap.Entry entry = entryItr.next();
            Assert.assertEquals(entry.getName(), "EcoRI");
            int from = entry.getStart();
            int to = entry.getEnd();
            String test = NM_008238_gb.getSiquence().getData(from, to);
            Assert.assertEquals("gaattc", test);
        }
        
        rm = service.findRM(NM_008238_gb.getSiquence().getData(), false, "", AvaI_rens, 1, 1, null, true);
        Assert.assertEquals(rm.getSize(), 3);
        //System.out.println(rm);
        rm.filter(3, 3);
        Assert.assertEquals(rm.getSize(), 3);
        rm.filter(4, 4);
        //Assert.assertEquals(rm.getSize(), 0);
        //System.out.println(rm);
    }
}
