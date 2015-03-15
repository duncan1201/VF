/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import java.io.File;
import java.io.InputStream;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.as.util.AnnotatedSeqWriter;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import org.biojavax.SimpleNamespace;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class AnnotatedSeqTest {

    public AnnotatedSeqTest() {
    }

    //@Test
    public void testClone() {
        System.out.println("clone");
        InputStream inputStream = AnnotatedSeqTest.class.getResourceAsStream("XM_003497231.gb");
        AnnotatedSeq instance = AnnotatedSeqParser.singleParse(inputStream, new FlexGenbankFormat());
        AnnotatedSeq cloned = instance.clone();
        AnnotatedSeqWriter.toFile(cloned, new File("D:\\tmp\\cloned.gb"));
        AnnotatedSeqWriter.toFile(instance, new File("D:\\tmp\\instance.gb"));
    }

    @Test
    public void testABC() {
        BufferedReader br = null;
        SimpleNamespace ns = null;

        try {
            InputStreamReader r = new InputStreamReader(AnnotatedSeqTest.class.getResourceAsStream("XM_003497231.gb"));
            br = new BufferedReader(r);
            ns = new SimpleNamespace("biojava");

            // You can use any of the convenience methods found in the BioJava 1.6 API
            RichSequenceIterator rsi = RichSequence.IOTools.readGenbankRNA(br, ns);

            // Since a single file can contain more than a sequence, you need to iterate over
            // rsi to get the information.
            while (rsi.hasNext()) {
                RichSequence rs = rsi.nextRichSequence();
                System.out.println(rs.getName());
            }
        } catch (Exception be) {
            be.printStackTrace();
            System.exit(-1);
        }

    }
}
