package com.gas.das.core.command.type;

import java.util.Set;

public class DasTypesParserTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        DasTypesParser parser = new DasTypesParser();
        DasTypes dasTypes = parser.parse(DasTypesParser.class, "types_response_hg19.xml");
        DasTypes.Gff gff = dasTypes.getGff();
        Set<DasTypes.Segment> segments = gff.getSegments();
        for (DasTypes.Segment seg : segments) {
            String version = seg.getVersion();
            //System.out.println(version);
            Set<DasTypes.Type> types = seg.getTypes();
            for (DasTypes.Type type : types) {
                System.out.println(type.getId() + "\t" + type.getCategory() + "\t" + type.getMethod());
            }
        }
        System.out.print("");
    }
}
