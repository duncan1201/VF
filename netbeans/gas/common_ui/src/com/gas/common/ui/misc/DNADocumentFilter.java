/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import com.gas.common.ui.util.BioUtil;
import java.util.Locale;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author adm_LiaoDu
 */
public class DNADocumentFilter extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, int offset, String string,
            AttributeSet attr) throws BadLocationException {       
        boolean areDNAs = BioUtil.areDNAs(string);
        if(areDNAs){
            fb.insertString(offset, string.toUpperCase(Locale.ENGLISH), attr);
        }
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
            AttributeSet attrs) throws BadLocationException {
        boolean areDNAs = BioUtil.areDNAs(text);
        if(areDNAs){
            fb.replace(offset, length, text.toUpperCase(Locale.ENGLISH), attrs);
        }        
    }
}
