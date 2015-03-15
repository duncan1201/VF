/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.analysis;

import com.gas.common.ui.core.FloatList;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dq
 */
public class FramePlotAnalysis {

    public void aaa(String bases, int windowSize, int stepSize) {
        if (windowSize % 3 != 0) {
            throw new IllegalArgumentException(String.format("Window size is not multiples of 3: %d", windowSize));
        }
        List<FloatList> ret = new ArrayList<FloatList>();
        int endIndex = 0;
        for (int startIndex = 0; endIndex < bases.length();) {

            String sub = bases.substring(startIndex, endIndex);

            startIndex += stepSize;
            endIndex = startIndex + windowSize;
        }
    }

    private void _a(String subSeq, float[] ret) {
        for (int i = 0; i < subSeq.length(); i++) {
            int frame = i % 3;
            int count = 0;
            char ch = subSeq.charAt(i);
            if (ch == 'g' || ch == 'G' || ch == 'c' || ch == 'C') {
                count++;
            }
            float f = 1.0f * count / subSeq.length();
        }
    }
}
