/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.common;

import com.gas.database.core.msa.service.api.CounterList;
import com.gas.database.core.msa.service.api.Counter;
import com.gas.database.core.msa.service.api.IConservationService;
import com.gas.common.ui.core.CharList;
import com.gas.common.ui.core.FloatList;
import com.gas.common.ui.matrix.api.Matrix;
import java.util.Iterator;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IConservationService.class)
public class ConservationService implements IConservationService {

    @Override
    public int[] calculate(CounterList counterList, Matrix matrix) {
        FloatList floatList = new FloatList();

        for (int i = 0; i < counterList.size(); i++) {
            Counter counter = counterList.get(i);
            float qualityScore = qualityScore(counter, matrix);
            floatList.add(qualityScore);
        }
        // normalized the result
        boolean equals = floatList.areEquals(0f);
        if (equals) {
            floatList.setAll(1f);
        }
        floatList.scale(-1);
        float min = floatList.getMin();
        if (min < 0) {
            floatList.translate(-min + 1);
        }
        floatList.scale(100);
        for (int i = 0; i < counterList.size(); i++) {
            floatList.set(i, floatList.get(i) * counterList.get(i).getLetterFrequency());
        }
        return floatList.toIntArray();
    }

    /**
     * Sr = C(r,Aij)
     *
     * @param r: 1-based
     * @return the rth dimension of S
     */
    private Integer s(Character r, Character Aij, Matrix matrix) {
        Integer ret = null;
        Short retShort = matrix.getScore(r, Aij);
        if (retShort != null) {
            ret = retShort.intValue();
        }
        return ret;
    }

    /**
     * Xr = (SUM(Fij * C(i,r)))/ m 1<=i<=R
     *
     * @return the rth dimension of X
     */
    private float x(char r, Counter counter, Matrix matrix) {
        int sum = 0;
        CharList residues = matrix.getResidues();
        Iterator<Character> itrResidues = residues.iterator();
        while (itrResidues.hasNext()) {
            Character i = itrResidues.next();
            int Fij = counter.get(i);
            if (Fij != 0) {
                int c_i_r = s(r, i, matrix);
                sum += (Fij * c_i_r);
            }
        }
        float ret = 1.0f * sum / counter.getTotalCount();
        //System.out.println(String.format("X%s=%f", r, ret));
        return ret;
    }

    /**
     * Di = SQRT(SUM(Xr - Sr)(Xr - Sr)) 1<=i<=R Quailty Score = SUM(Di)/M
     */
    private float qualityScore(Counter counter, Matrix matrix) {
        float sumOfDi = 0;
        Iterator<Character> itrCounter = counter.keySet().iterator();
        while (itrCounter.hasNext()) {
            Character Aij = itrCounter.next();
            int countAij = counter.get(Aij);
            CharList residues = matrix.getResidues();
            Iterator<Character> itr = residues.iterator();
            float di = 0;
            float sum = 0;
            while (itr.hasNext()) {
                Character r = itr.next();
                float Xr = x(r, counter, matrix);
                Integer Sr = s(r, Aij, matrix);
                if (Sr != null) {
                    sum += (Xr - Sr) * (Xr - Sr) * countAij;
                }
            }
            di = (float) Math.sqrt(sum);
            sumOfDi += di;
        }
        return sumOfDi / counter.getTotalCount();
    }
}
