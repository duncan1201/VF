/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.geneticCode.api;

import com.gas.domain.core.as.TranslationResult;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author dq
 */
public interface IGeneticCodeTableService {

    String translate(String nucleotides, int tableId);

    GeneticCodeTable getTable(int id);

    GeneticCodeTable getTable(String name);

    GeneticCodeTableList getTables();

    GeneticCodeTableList getTables(Comparator<GeneticCodeTable> c);

    List<TranslationResult> translate(String nucleotides, String tableName, int[] frames);
}
