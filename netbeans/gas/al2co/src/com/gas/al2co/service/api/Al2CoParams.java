/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.al2co.service.api;

import com.gas.al2co.service.api.IAl2CoService.ConsCalMethod;
import com.gas.al2co.service.api.IAl2CoService.MatrixTrans;
import com.gas.al2co.service.api.IAl2CoService.WeightingScheme;
import java.io.File;

/**
 *
 * @author dq
 */
public class Al2CoParams {
    private File in;
    private File out;
    private File scoringMatrixFile;
    private IAl2CoService.WeightingScheme weightingScheme = IAl2CoService.WeightingScheme.Independent_count;
    private IAl2CoService.ConsCalMethod consCalMethod = IAl2CoService.ConsCalMethod.Entropy;
    private boolean normalize = true;
    private IAl2CoService.MatrixTrans matrixTrans = IAl2CoService.MatrixTrans.No_Trans;
    private Integer windowSize = 1;
    private Float gapFraction = 0.5f;
    private boolean excludeFirst = false;
    
    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();        
        ret.append(" -i ");
        ret.append(in.getAbsolutePath());
        ret.append(" -o ");
        ret.append(out.getAbsolutePath());
        if(scoringMatrixFile != null){
            ret.append(" -s ");
            ret.append(scoringMatrixFile.getAbsolutePath());
        }
        ret.append(" -m ");
        ret.append(matrixTrans.getValue());
        ret.append(" -f ");
        ret.append(weightingScheme.getValue());
        ret.append(" -c ");
        ret.append(consCalMethod.getValue());
        ret.append(" -w ");
        ret.append(windowSize);
        ret.append(" -n ");
        ret.append(normalize ? "T": "F");
        ret.append(" -e ");
        ret.append(excludeFirst ? "T": "F");
        ret.append(" -g ");
        ret.append(gapFraction);
        
        return ret.toString();
    }

    public ConsCalMethod getConsCalMethod() {
        return consCalMethod;
    }

    public void setConsCalMethod(ConsCalMethod consCalMethod) {
        this.consCalMethod = consCalMethod;
    }

    public boolean isExcludeFirst() {
        return excludeFirst;
    }

    public void setExcludeFirst(boolean excludeFirst) {
        this.excludeFirst = excludeFirst;
    }

    public Float getGapFraction() {
        return gapFraction;
    }

    public void setGapFraction(Float gapFraction) {
        this.gapFraction = gapFraction;
    }

    public File getIn() {
        return in;
    }

    public void setIn(File in) {
        this.in = in;
    }

    public MatrixTrans getMatrixTrans() {
        return matrixTrans;
    }

    public void setMatrixTrans(MatrixTrans matrixTrans) {
        this.matrixTrans = matrixTrans;
    }

    public boolean isNormalize() {
        return normalize;
    }

    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
    }

    public File getOut() {
        return out;
    }

    public void setOut(File out) {
        this.out = out;
    }

    public File getScoringMatrixFile() {
        return scoringMatrixFile;
    }

    public void setScoringMatrixFile(File scoringMatrixFile) {
        this.scoringMatrixFile = scoringMatrixFile;
    }

    public WeightingScheme getWeightingScheme() {
        return weightingScheme;
    }

    public void setWeightingScheme(WeightingScheme weightingScheme) {
        this.weightingScheme = weightingScheme;
    }

    public Integer getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(Integer windowSize) {
        this.windowSize = windowSize;
    }
    
}
