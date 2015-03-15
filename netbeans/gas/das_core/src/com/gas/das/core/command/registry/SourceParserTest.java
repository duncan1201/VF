package com.gas.das.core.command.registry;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.gas.das.core.command.registry.DasSources.Source;

public class SourceParserTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int das_1_53 = 0;
        int das_1_6 = 0;
        int das_unknown = 0;

        DasSourcesParser parser = new DasSourcesParser();
        DasSources ret = parser.parse(DasSourcesParser.class, "source_response.xml");
        Set<Source> srcs = ret.getSources();
        for (Source src : srcs) {
            Set<DasSources.Version> versions = src.getVersions();
            Iterator<DasSources.Version> vs = versions.iterator();
            while (vs.hasNext()) {
                DasSources.Version version = vs.next();
                if (version != null) {
                    String spec = version.getProperties().get("spec");
                    if (spec != null) {
                        if (spec.equals("DAS/1.53E")) {
                            das_1_53++;
                        } else if (spec.equals("DAS/1.6E")) {
                            das_1_6++;
                        } else {
                            das_unknown++;
                        }
                    } else {
                        das_unknown++;
                    }
                }
            }


        }

        System.out.println(das_1_53);
        System.out.println(das_1_6);
        System.out.println(das_unknown);
    }
}
