/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.tigr.util;

import com.gas.common.ui.util.CommonUtil;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.Trace;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.biojava.BioJavaHelper;
import org.biojava.bio.alignment.Alignment;
import org.biojava.bio.chromatogram.Chromatogram;
import org.biojava.bio.chromatogram.ChromatogramFactory;
import org.biojava.bio.chromatogram.UnsupportedChromatogramFormatException;
import org.biojava.bio.program.scf.SCF;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.bio.symbol.IntegerAlphabet;
import org.biojava.bio.symbol.Symbol;

/**
 *
 * @author dq
 */
public class KromatogramParser {

    public static Kromatogram parse(File file) {

        Kromatogram ret = new Kromatogram();

        Chromatogram chroma;
        try {
            FileInputStream fileIn = new FileInputStream(file);
            int magic = magicFromStream(fileIn);
            if (magic == ChromatogramFactory.ABI_MAGIC) {
                ABIParsir parsir = new ABIParsir(file);
                ret = parsir.parse();
            } else if (magic == ChromatogramFactory.SCF_MAGIC) {
                chroma = ChromatogramFactory.create(file);
                Alignment alignment = chroma.getBaseCalls();
                // labels:
                // dna, trace-offsets,
                // quality-a, quality-c, quality-g, quality-t,
                // substitution-probability, overcall-probability,
                // undercall-probability
                List labels = alignment.getLabels();
                List<Integer> offsets = new ArrayList<Integer>();

                int[] qvTs = new int[alignment.length()];
                int[] qvAs = new int[alignment.length()];
                int[] qvCs = new int[alignment.length()];
                int[] qvGs = new int[alignment.length()];

                int[] qvS = new int[alignment.length()];

                for (int i = 0; i < alignment.length(); i++) {
                    if (labels.contains(org.biojava.bio.chromatogram.Chromatogram.OFFSETS)) {
                        IntegerAlphabet.IntegerSymbol traceSymbol = (IntegerAlphabet.IntegerSymbol) alignment.symbolAt(org.biojava.bio.chromatogram.Chromatogram.OFFSETS, i + 1);
                        offsets.add(traceSymbol.intValue());
                    }

                    if (labels.contains(SCF.PROB_NUC_T)
                            && labels.contains(SCF.PROB_NUC_A)
                            && labels.contains(SCF.PROB_NUC_C)
                            && labels.contains(SCF.PROB_NUC_G)) {

                        IntegerAlphabet.IntegerSymbol qvT = (IntegerAlphabet.IntegerSymbol) alignment.symbolAt(SCF.PROB_NUC_T, i + 1);
                        IntegerAlphabet.IntegerSymbol qvC = (IntegerAlphabet.IntegerSymbol) alignment.symbolAt(SCF.PROB_NUC_C, i + 1);
                        IntegerAlphabet.IntegerSymbol qvG = (IntegerAlphabet.IntegerSymbol) alignment.symbolAt(SCF.PROB_NUC_G, i + 1);
                        IntegerAlphabet.IntegerSymbol qvA = (IntegerAlphabet.IntegerSymbol) alignment.symbolAt(SCF.PROB_NUC_A, i + 1);

                        qvTs[i] = qvT.intValue();
                        qvCs[i] = qvC.intValue();
                        qvGs[i] = qvG.intValue();
                        qvAs[i] = qvA.intValue();

                        qvS[i] = Math.max(Math.max(qvT.intValue(), qvC.intValue()),
                                Math.max(qvG.intValue(), qvA.intValue()));
                    }
                }
                if (labels.contains(org.biojava.bio.chromatogram.Chromatogram.OFFSETS)) {
                    ret.setOffsets(offsets);
                }

                if (labels.contains(SCF.PROB_NUC_T)
                        && labels.contains(SCF.PROB_NUC_A)
                        && labels.contains(SCF.PROB_NUC_C)
                        && labels.contains(SCF.PROB_NUC_G)) {
                    ret.setQualityValues(CommonUtil.toList(qvS));
                }

                Iterator itr = alignment.iterator();
                StringBuilder sequence = new StringBuilder();
                List<Character> bases = new ArrayList<Character>();
                while (itr.hasNext()) {
                    Symbol symbol = (Symbol) itr.next();
                    Character character = BioJavaHelper.getToken(symbol);
                    if (character == null) {
                        character = 'n';
                    }
                    bases.add(character);
                }
                ret.setBases(bases);
                ret.setFileName(file.getName());

                ret.getTraces().put(DNATools.a().getName(), new Trace(DNATools.a().getName(), chroma.getTrace(DNATools.a())));
                ret.getTraces().put(DNATools.t().getName(), new Trace(DNATools.t().getName(), chroma.getTrace(DNATools.t())));
                ret.getTraces().put(DNATools.c().getName(), new Trace(DNATools.c().getName(), chroma.getTrace(DNATools.c())));
                ret.getTraces().put(DNATools.g().getName(), new Trace(DNATools.g().getName(), chroma.getTrace(DNATools.g())));
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (UnsupportedChromatogramFormatException ex) {
            ex.printStackTrace();
        } catch (IllegalSymbolException ex) {
            ex.printStackTrace();
        }


        return ret;
    }

    private static int makeMagic(byte[] magic) {
        return (magic[0] << 24) | (magic[1] << 16) | (magic[2] << 8) | (magic[3]);
    }

    /**
     * Reads the next four bytes from a stream to build a 32-bit magic number.
     *
     * @param src the source InputStream
     * @return an integer representing the magic number
     * @throws IOException if data could not be read from src
     */
    private static int magicFromStream(InputStream src) throws IOException {
        byte[] magicBytes = new byte[4];
        src.read(magicBytes);
        return makeMagic(magicBytes);
    }
}
