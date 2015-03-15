/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.tigr.util;

import com.gas.common.ui.util.CommonUtil;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.Trace;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.biojava.bio.chromatogram.UnsupportedChromatogramFormatException;
import org.biojava.bio.program.abi.ABIFParser;
import org.biojava.bio.symbol.AtomicSymbol;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.bio.symbol.IntegerAlphabet;
import org.biojava.bio.symbol.Symbol;

/**
 *
 * @author dq
 */
/**
 * An extension of {@link ABIFParser} that reads the particular fields from the
 * ABIF that contain the chromatogram data and initializes the fields in its
 * enclosing
 * <code>ABIFChromatogram</code> instance.
 */
public class ABIParsir extends ABIFParser {

    private String fileName = null;

    public ABIParsir(InputStream in)
            throws IOException, UnsupportedChromatogramFormatException {
        super(in);
    }

    public ABIParsir(File f)
            throws IOException, UnsupportedChromatogramFormatException {
        super(f);
        fileName = f.getName();
    }

    public Kromatogram parse()
            throws IOException, UnsupportedChromatogramFormatException {
        Kromatogram ret = new Kromatogram();

        if (fileName != null) {
            ret.setFileName(fileName);
        }

        // read filter-wheel-order tag
        char[] fwo_ = new char[4];
        ABIFParser.TaggedDataRecord fwoRec = getDataRecord("FWO_", 1);
        if (fwoRec == null) {
            throw new UnsupportedChromatogramFormatException("No FWO_ (1) record in ABIF file, therefore no trace data");
        }
        fwo_[0] = (char) ((fwoRec.dataRecord >>> 24) & 0xff);
        fwo_[1] = (char) ((fwoRec.dataRecord >>> 16) & 0xff);
        fwo_[2] = (char) ((fwoRec.dataRecord >>> 8) & 0xff);
        fwo_[3] = (char) ((fwoRec.dataRecord) & 0xff);

        Symbol sym;

        for (int i = 0; i < 4; i++) {

            try {
                sym = ABIFParser.decodeDNAToken(fwo_[i]);
            } catch (IllegalSymbolException ise) {
                throw new UnsupportedChromatogramFormatException("An unexpected character (" + fwo_[i] + ") was found in the FWO_ tag.  Parsing cannot continue.");
            }
            if (!(sym instanceof AtomicSymbol)) {
                throw new UnsupportedChromatogramFormatException("An unexpected character (" + fwo_[i] + ") was found in the FWO_ tag.  Parsing cannot continue.");
            }
            int[] trace = parseTrace((AtomicSymbol) sym, i + 9);
            ret.getTraces().put(sym.getName(), new Trace(sym.getName(), trace));
        }

        List<Integer> offsets = parseOffsets();
        ret.setOffsets(offsets);

        List<Character> bases = parseBases();
        ret.setBases(bases);

        int[] qvs = parseQVs();
        ret.setQualityValues(CommonUtil.toList(qvs));

        getDataAccess().finishedReading();
        return ret;
    }

    private int[] parseQVs() {
        int[] ret = null;


        ABIFParser.TaggedDataRecord pcon1 = getDataRecord("PCON", 1);
        if (pcon1 != null) {
            byte[] offsetData = pcon1.offsetData;
            ret = new int[(int) pcon1.numberOfElements];
            for (int i = 0; i < offsetData.length; i++) {
                ret[i] = offsetData[i];
            }
        }
        return ret;

    }

    private int[] parseTrace(AtomicSymbol sym, int whichData) throws IOException, UnsupportedChromatogramFormatException {
        TaggedDataRecord dataPtr = getDataRecord("DATA", whichData);
        if (dataPtr.numberOfElements > Integer.MAX_VALUE) {
            throw new UnsupportedChromatogramFormatException("Chromatogram has more than " + Integer.MAX_VALUE + " trace samples -- can't handle it");
        }
        int count = (int) dataPtr.numberOfElements;
        int[] trace = new int[count];
        int max = -1;

        if (dataPtr.elementLength == 2) {
            byte[] shortArray = dataPtr.offsetData;
            int i = 0;
            for (int s = 0; s < shortArray.length; s += 2) {
                trace[i] = ((short) ((shortArray[s] << 8) | (shortArray[s + 1] & 0xff))) & 0xffff;
                max = Math.max(trace[i++], max);
            }
        } else if (dataPtr.elementLength == 1) {
            byte[] byteArray = dataPtr.offsetData;
            for (int i = 0; i < byteArray.length; i++) {
                trace[i] = byteArray[i] & 0xff;
                max = Math.max(trace[i], max);
            }
        } else {
            throw new UnsupportedChromatogramFormatException("Only 8- and 16-bit trace samples are supported");
        }

        return trace;
    }

    private List<Character> parseBases() throws IOException, UnsupportedChromatogramFormatException {
        List<Character> ret = new ArrayList<Character>();
        // call letters are int PBAS1
        TaggedDataRecord basesPtr = getDataRecord("PBAS", 1);

        // then read the base calls
        byte[] byteArray = basesPtr.offsetData;
        for (int i = 0; i < byteArray.length; i++) {
            //dna.add(ABIFParser.decodeDNAToken((char) byteArray[i]));
            char c = (char) byteArray[i];
            ret.add(c);
        }

        return ret;

    }

    private List<Integer> parseOffsets() throws IOException, UnsupportedChromatogramFormatException {
        List<Integer> ret = null;
        // do offsets, then call letters
        // offsets are in PLOC1 (we'll use the possibly-edited stream)
        TaggedDataRecord offsetsPtr = getDataRecord("PLOC", 1);
        // call letters are int PBAS1
        int count = (int) offsetsPtr.numberOfElements;
        // the list of called bases
        List dna = new ArrayList(count);
        ret = new ArrayList<Integer>();
        // the list of offsets
        List offsets = new ArrayList(count);
        // start reading offsets, creating SimpleBaseCalls along the way
        if (offsetsPtr.elementLength == 2) {
            byte[] shortArray = offsetsPtr.offsetData;
            int i = 0;
            for (int s = 0; s < shortArray.length; s += 2) {
                int os = ((short) ((shortArray[s] << 8) | (shortArray[s + 1] & 0xff))) & 0xffff;
                ret.add(os);
                //offsets.add(integerAlphabet.getSymbol(((short) ((shortArray[s] << 8) | (shortArray[s + 1] & 0xff))) & 0xffff));
                i++;
            }
        } else if (offsetsPtr.elementLength == 1) {
            byte[] byteArray = offsetsPtr.offsetData;
            IntegerAlphabet integerAlphabet = IntegerAlphabet.getInstance();
            for (int i = 0; i < byteArray.length; i++) {
                int os = byteArray[i] & 0xff;
                ret.add(os);
                offsets.add(integerAlphabet.getSymbol(byteArray[i] & 0xff));
            }
        } else {
            throw new IllegalStateException("Only 8- and 16-bit trace samples are supported");
        }

        return ret;

    }
}