/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.geneticCode;

import com.gas.domain.core.geneticCode.api.GeneticCodeTable;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.as.TranslationResult;
import com.gas.domain.core.geneticCode.api.GeneticCodeTableList;
import com.gas.domain.core.geneticCode.api.IGeneticCodeTableService;
import java.util.*;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IGeneticCodeTableService.class)
public class GeneticCodeTableService implements IGeneticCodeTableService {

    private GeneticCodeTableList geneticCodeTables;
    private List<String> tableNames = new ArrayList<String>();
    private Map<String, Integer> name2idMap = new HashMap<String, Integer>();

    public GeneticCodeTableService() {
        TableParser parser = new TableParser();
        geneticCodeTables = parser.parse(GeneticCodeTableService.class, "genetic_code_table.txt");
        Iterator<GeneticCodeTable> itr = geneticCodeTables.iterator();
        while (itr.hasNext()) {
            GeneticCodeTable table = itr.next();
            String name = table.getName();
            Integer id = table.getId();
            tableNames.add(name);

            name2idMap.put(name, id);
        }
    }

    @Override
    public List<TranslationResult> translate(String nucleotides, String tableName, int[] frames) {
        List<TranslationResult> ret = new ArrayList<TranslationResult>();
        GeneticCodeTable table = getTable(tableName);
        String rcNucleotides = BioUtil.reverseComplement(nucleotides);
        for (int frame : frames) {
            TranslationResult result = new TranslationResult();
            result.setFrame(frame);
            result.setTableName(tableName);
            if (frame > 0) {
                String translated = table.translate(nucleotides.substring(frame - 1));
                result.setData(translated);
                result.setStartPos(frame);
            } else {
                String translated = table.translate(rcNucleotides.substring(-frame - 1));
                translated = StrUtil.reverse(translated);
                result.setData(translated);
                int startPos = (nucleotides.length() - (-frame - 1)) % 3 + 1;
                result.setStartPos(startPos);
            }
            ret.add(result);
        }
        return ret;
    }

    private Map<String, Integer> getName2IdMap() {
        return name2idMap;
    }

    public List<String> getTableNames() {
        return tableNames;
    }

    @Override
    public String translate(String nucleotides, int tableId) {
        GeneticCodeTable table = getTable(tableId);
        return table.translate(nucleotides);
    }

    @Override
    public GeneticCodeTable getTable(int id) {
        GeneticCodeTable ret = null;
        Iterator<GeneticCodeTable> itr = geneticCodeTables.iterator();
        while (itr.hasNext()) {
            GeneticCodeTable table = itr.next();
            if (table.getId().intValue() == id) {
                ret = table;
                break;
            }
        }
        return ret;
    }

    @Override
    public GeneticCodeTable getTable(String name) {
        GeneticCodeTable ret = null;
        Iterator<GeneticCodeTable> itr = geneticCodeTables.iterator();
        while (itr.hasNext()) {
            GeneticCodeTable table = itr.next();
            if (table.getName().equalsIgnoreCase(name)) {
                ret = table;
                break;
            }
        }
        return ret;
    }

    @Override
    public GeneticCodeTableList getTables() {
        return getTables(new GeneticCodeTable.TableIdComparator());
    }

    @Override
    public GeneticCodeTableList getTables(Comparator<GeneticCodeTable> c) {
        Collections.sort(geneticCodeTables, c);
        return geneticCodeTables;
    }
}
