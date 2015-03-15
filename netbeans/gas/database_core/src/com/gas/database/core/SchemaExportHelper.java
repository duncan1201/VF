package com.gas.database.core;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class SchemaExportHelper {

    public static void justCreate(Configuration cfg, String outputFile) {
        SchemaExport export = new SchemaExport(cfg);

        if (outputFile != null) {
            export.setOutputFile(outputFile);
        }

        export.create(true, true);
    }

    public static void justCreate(Configuration cfg) {
        if (cfg == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }
        justCreate(cfg, null);
    }
}
