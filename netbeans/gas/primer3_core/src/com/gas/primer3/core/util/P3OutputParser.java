package com.gas.primer3.core.util;

import com.gas.common.ui.util.ReflectHelper;
import com.gas.primer3.core.output.OUTPUT_TAG_REGEX;
import com.gas.domain.core.primer3.Oligo;
import com.gas.domain.core.primer3.OligoElement;
import com.gas.domain.core.primer3.P3Output;
import com.gas.primer3.core.api.BoulderIOUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class P3OutputParser {

    public static P3Output parse(String str) {
        return parse(str.getBytes());
    }

    public static void parse(byte[] bytes, P3Output p3output) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        parse(inputStream, p3output);
    }

    public static P3Output parse(byte[] bytes) {
        if(bytes == null){
            return null;
        }
        P3Output ret = new P3Output();
        parse(bytes, ret);
        return ret;
    }

    public static P3Output parse(File file) {
        P3Output ret = null;
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            ret = new P3Output();
            parse(inputStream, ret);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(P3OutputParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    static void parse(InputStream inputStream, P3Output ret) {
        Map<String, String> properties = BoulderIOUtil.parse(inputStream);

        String name;
        try {
            Iterator<String> names = properties.keySet().iterator();
            while (names.hasNext()) {
                name = names.next();

                for (String[] c : PARSING_CONFIGS) {
                    String reg = c[0];
                    String which = c[1];
                    String methodName = c[2];
                    String dataType = c[3];
                    if (name.matches(reg)) {
                        String valueStr = properties.get(name);
                        Object param = null;
                        Class paramType = null;

                        Oligo oligo = null;
                        if (which.equalsIgnoreCase("oligo")
                                || which.equalsIgnoreCase("left")
                                || which.equalsIgnoreCase("internal")
                                || which.equalsIgnoreCase("right")) {
                            Integer oligoNo = getOligoIndex(name);
                            oligo = ret.getOligoByNo(oligoNo);
                            if(oligo == null){
                                oligo = new Oligo(oligoNo);
                                ret.getOligos().add(oligo);
                            }
                        }
                        OligoElement oligoElement = null;
                        if (which.equalsIgnoreCase("left")) {
                            oligoElement = oligo.getLeft();
                            if (oligoElement == null) {
                                oligoElement = new OligoElement("left");
                                oligoElement.setNo(oligo.getNo());
                                oligo.setLeft(oligoElement);
                            }
                        } else if (which.equalsIgnoreCase("right")) {
                            oligoElement = oligo.getRight();
                            if (oligoElement == null) {
                                oligoElement = new OligoElement("right");
                                oligoElement.setNo(oligo.getNo());
                                oligo.setRight(oligoElement);
                            }
                        } else if (which.equalsIgnoreCase("internal")) {
                            oligoElement = oligo.getInternal();
                            if (oligoElement == null) {
                                oligoElement = new OligoElement("internal");
                                oligoElement.setNo(oligo.getNo());
                                oligoElement.setForward(true);
                                oligo.setInternal(oligoElement);
                            }
                        }
                        if (dataType.equalsIgnoreCase("float")) {
                            paramType = Float.class;
                            param = Float.parseFloat(valueStr);
                        } else if (dataType.equalsIgnoreCase("integer")) {
                            paramType = Integer.class;
                            param = Integer.parseInt(valueStr);
                        } else if (dataType.equalsIgnoreCase("string")) {
                            paramType = String.class;
                            param = (String) valueStr;
                        } else {
                            throw new UnsupportedOperationException();
                        }
                        if (which.equalsIgnoreCase("p3output")) {
                            ReflectHelper.invoke(ret, methodName, new Class[]{paramType}, new Object[]{param});
                        } else if (which.equalsIgnoreCase("oligo") && methodName != null && !methodName.isEmpty()) {
                            ReflectHelper.invoke(oligo, methodName, new Class[]{paramType}, new Object[]{param});
                            System.out.print("");
                        } else if ((which.equalsIgnoreCase("left") || which.equalsIgnoreCase("internal") || which.equalsIgnoreCase("right"))
                                && paramType != null && param != null
                                && methodName != null && !methodName.isEmpty()) {
                            ReflectHelper.invoke(oligoElement, methodName, new Class[]{paramType}, new Object[]{param});
                            System.out.print("");
                        }
                        break;
                    }
                }                
            }   
            Iterator<Oligo> itr = ret.getOligos().iterator();
            while(itr.hasNext()){
                Oligo oligo = itr.next();
                oligo.setSeqTemplateForOligoElements(ret.getSequenceTemplate());
            }
        } catch (Exception io) {
            io.printStackTrace();
        }
    }
    private static final String[][] PARSING_CONFIGS = {
        {OUTPUT_TAG_REGEX.PRIMER_LEFT, "left", "setStartLength", "String"},
        {OUTPUT_TAG_REGEX.PRIMER_INTERNAL, "internal", "setStartLength", "String"},
        {OUTPUT_TAG_REGEX.PRIMER_RIGHT, "right", "setStartLength", "String"},        
        
        {OUTPUT_TAG_REGEX.PRIMER_LEFT_EXPLAIN, "p3output", "setPrimerLeftExplain", "String"},
        {OUTPUT_TAG_REGEX.PRIMER_INTERNAL_EXPLAIN, "p3output", "setPrimerInternalExplain", "String"},
        {OUTPUT_TAG_REGEX.PRIMER_PAIR_EXPLAIN, "p3output", "setPrimerPairExplain", "String"},
        {OUTPUT_TAG_REGEX.PRIMER_RIGHT_EXPLAIN, "p3output", "setPrimerRightExplain", "String"}, 
        
        {OUTPUT_TAG_REGEX.PRIMER_LEFT_PROBLEMS, "left", "setProblems", "String"},                
        {OUTPUT_TAG_REGEX.PRIMER_INTERNAL_PROBLEMS, "internal", "setProblems", "String"},
        {OUTPUT_TAG_REGEX.PRIMER_RIGHT_PROBLEMS, "right", "setProblems", "String"},    
        
        {OUTPUT_TAG_REGEX.PRIMER_LEFT_HAIRPIN, "left", "setHairpin", "Float"},
        {OUTPUT_TAG_REGEX.PRIMER_INTERNAL_HAIRPIN, "internal", "setHairpin", "Float"},
        {OUTPUT_TAG_REGEX.PRIMER_RIGHT_HAIRPIN, "right", "setHairpin", "Float"},
        
        {OUTPUT_TAG_REGEX.PRIMER_LEFT_GC_PERCENT, "left", "setGc", "Float"},
        {OUTPUT_TAG_REGEX.PRIMER_INTERNAL_GC_PERCENT, "internal", "setGc", "Float"},
        {OUTPUT_TAG_REGEX.PRIMER_RIGHT_GC_PERCENT, "right", "setGc", "Float"},        
        
        {OUTPUT_TAG_REGEX.PRIMER_LEFT_SELF_ANY, "left", "setSelfAny", "Float"},
        {OUTPUT_TAG_REGEX.PRIMER_INTERNAL_SELF_ANY, "internal", "setSelfAny", "Float"},
        {OUTPUT_TAG_REGEX.PRIMER_RIGHT_SELF_ANY, "right", "setSelfAny", "Float"},
        
        {OUTPUT_TAG_REGEX.PRIMER_LEFT_SELF_END, "left", "setSelfEnd", "Float"},
        {OUTPUT_TAG_REGEX.PRIMER_INTERNAL_SELF_END, "internal", "setSelfEnd", "Float"},
        {OUTPUT_TAG_REGEX.PRIMER_RIGHT_SELF_END, "right", "setSelfEnd", "Float"},        
        
        {OUTPUT_TAG_REGEX.PRIMER_LEFT_SEQ, "left", "setSeq", "String"},
        {OUTPUT_TAG_REGEX.PRIMER_INTERNAL_SEQ, "internal", "setSeq", "String"},
        {OUTPUT_TAG_REGEX.PRIMER_RIGHT_SEQ, "right", "setSeq", "String"},                
               
        {OUTPUT_TAG_REGEX.PRIMER_LEFT_TM, "left", "setTm", "Float"},
        {OUTPUT_TAG_REGEX.PRIMER_INTERNAL_TM, "internal", "setTm", "Float"},
        {OUTPUT_TAG_REGEX.PRIMER_RIGHT_TM, "right", "setTm", "Float"},
        
        {OUTPUT_TAG_REGEX.PRIMER_PAIR_PRODUCT_SIZE, "oligo", "setProductSize", "Integer"},
        {OUTPUT_TAG_REGEX.PRIMER_PAIR_COMPL_ANY, "oligo", "setComplAny", "Float"},
        {OUTPUT_TAG_REGEX.PRIMER_PAIR_COMPL_END, "oligo", "setComplEnd", "Float"},
        
        {OUTPUT_TAG_REGEX.SEQUENCE_TEMPLATE_REGEX, "p3output", "setSequenceTemplate", "String"},   
        {OUTPUT_TAG_REGEX.PRIMER_TASK, "p3output", "setPrimerTask", "String"},   
    
    };

    protected static Integer getOligoIndex(String tag) {
        Integer ret = null;
        String[] tokens = tag.split("_");
        for (String tok : tokens) {
            if (tok.matches("[0-9]")) {
                ret = Integer.parseInt(tok);
            }
        }
        return ret;
    }
}
