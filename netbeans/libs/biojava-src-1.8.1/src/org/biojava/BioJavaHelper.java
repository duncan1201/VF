/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.biojava;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.RNATools;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojava.bio.symbol.Alphabet;
import org.biojava.bio.symbol.AtomicSymbol;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.bio.symbol.SimpleAtomicSymbol;
import org.biojava.bio.symbol.SimpleSymbolList;
import org.biojava.bio.symbol.Symbol;
import org.biojava.bio.symbol.SymbolList;
import org.biojavax.bio.seq.RichSequence;

/**
 *
 * @author dq
 */
public class BioJavaHelper {

    private static final Logger LOG = Logger
            .getLogger(BioJavaHelper.class.getName());    
    
    public static Character getToken(Symbol symbol) {

        Alphabet dnaAlphabet = DNATools.getDNA();
        Alphabet proteinAlphabet = ProteinTools.getAlphabet();
        Alphabet rnaAlphabet = RNATools.getRNA();

        dnaAlphabet.getGapSymbol();
        Character ret = null;

        if (dnaAlphabet.contains(symbol)) {
            try {
                ret = DNATools.dnaToken(symbol);
            } catch (IllegalSymbolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (proteinAlphabet.contains(symbol)) {
            SymbolTokenization tokenization = RichSequence.IOTools.getProteinParser();
            try {
                String tmp;
                tmp = tokenization.tokenizeSymbol(symbol);
                ret = tmp.charAt(0);
            } catch (IllegalSymbolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (rnaAlphabet.contains(symbol)) {
            SymbolTokenization tokenization = RichSequence.IOTools.getRNAParser();
            try {
                String tmp;
                tmp = tokenization.tokenizeSymbol(symbol);
                ret = tmp.charAt(0);
            } catch (IllegalSymbolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (symbol instanceof SimpleAtomicSymbol) {
            SimpleAtomicSymbol simpleAtomicSymbol = (SimpleAtomicSymbol) symbol;
            Alphabet alphabet = simpleAtomicSymbol.getMatches();

            Iterator symbols = simpleAtomicSymbol.getSymbols().iterator();
            while (symbols.hasNext()) {

                Symbol tmp = (Symbol) symbols.next();
                while (tmp instanceof AtomicSymbol) {
                    if (tmp.getName().equals(DNATools.a().getName())) {
                        return 'a';
                    } else if (tmp.getName().equals(DNATools.t().getName())) {
                        return 't';
                    } else if (tmp.getName().equals(DNATools.c().getName())) {
                        return 'c';
                    } else if (tmp.getName().equals(DNATools.g().getName())) {
                        return 'g';
                    }
                }
            }
        }
        return ret;
    }
    
    public static String toAA3LetterCode(Character c){
        String r = null;
        SymbolList ret;
        try {
            ret = ProteinTools.createProtein(c.toString());
            r = ret.symbolAt(1).getName();
        } catch (IllegalSymbolException ex) {
            Logger.getLogger(BioJavaHelper.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return r;
    }
    
    public static SymbolList toSymbolList(String symbols) {
        SymbolList ret = SymbolList.EMPTY_LIST;
        try {
            if (SymbolsValidator.validateDNA(symbols)) {
                ret = DNATools.createDNA(symbols);
            } else if (SymbolsValidator.validateRNA(symbols)) {
                ret = RNATools.createRNA(symbols);
            } else if (SymbolsValidator.validateProtein(symbols)) {
                SymbolTokenization p = ProteinTools.getAlphabet().getTokenization("token");

                ret = new SimpleSymbolList(p, symbols);
            } else {
                throw new IllegalArgumentException("illegalSymbols");
            }
        } catch (Exception e) {
            LOG.severe(e.getStackTrace().toString());
            e.printStackTrace();
        }

        return ret;
    }
    
    public static SymbolList toDNASymbolList(String symbols) {
        SymbolList ret = SymbolList.EMPTY_LIST;
        try {            
            ret = DNATools.createDNA(symbols);            
        } catch (IllegalSymbolException e) {
            //logger.severe(e.getStackTrace().toString());
            //e.printStackTrace();
            throw new IllegalArgumentException("Not DNA symbols");
        }

        return ret;
    }    
}
