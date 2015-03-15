/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as.util;

import com.gas.common.ui.util.FileHelper;
import java.util.Iterator;
import org.biojavax.SimpleComment;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import java.io.File;
import org.biojavax.bio.seq.RichSequence;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dunqiang
 */
public class RichSequenceWriterTest {

    static RichSequence NM_053396_modified_gb = null;
    static RichSequence AAB07223_gp;

    public RichSequenceWriterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        NM_053396_modified_gb = RichSequenceParser.singleParse(RichSequenceWriterTest.class, "data/NM_053396_modified.gb", new FlexGenbankFormat());
        AAB07223_gp = RichSequenceParser.singleParse(RichSequenceWriterTest.class, "data/AAB07223.gp", new FlexGenbankFormat());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testToString() {
    }

    @Test
    public void testToFile_3args_1() {
    }

    @Test
    public void testToFile_3args_2() {
        int commentSize = NM_053396_modified_gb.getComments().size();
        System.out.println("comments size=" + commentSize);
        Iterator<Object> commentItr = NM_053396_modified_gb.getComments().iterator();
        while (commentItr.hasNext()) {
            SimpleComment sc = (SimpleComment) commentItr.next();
            System.out.println("sc.getComment()" + sc.getComment());
        }
        SimpleComment a = new SimpleComment("Translation=1234567890", 2);
        SimpleComment b = new SimpleComment("Translation2=AAABBABABA", 2);
        NM_053396_modified_gb.getComments().add(a);
        NM_053396_modified_gb.getComments().add(b);
        String str = RichSequenceWriter.toString(NM_053396_modified_gb, new FlexGenbankFormat());
        System.out.println(str);
        FileHelper.toFile(new File("d:\\tmp\\sequence.gb"), str);
    }
}
