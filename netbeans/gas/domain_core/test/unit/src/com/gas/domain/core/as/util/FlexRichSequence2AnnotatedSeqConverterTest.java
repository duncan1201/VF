/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as.util;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.domain.core.flexrs.IFlexRichSequence;
import java.util.Date;
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
public class FlexRichSequence2AnnotatedSeqConverterTest {

    public FlexRichSequence2AnnotatedSeqConverterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of to method, of class FlexRichSequence2AnnotatedSeqConverter.
     */
    @Test
    public void testTo() {
        System.out.println("to");
        String name = "Teest";
        Date importedDate = new Date();
        Date lastModified = new Date();
        IFlexRichSequence rs = RichSequenceParser.singleParse(this.getClass(), "data/NM_053396_modified.gb", new FlexGenbankFormat());
        AnnotatedSeq result = FlexRichSequence2AnnotatedSeqConverter.to(name, importedDate, lastModified, rs);

        System.out.print("");
    }
}
