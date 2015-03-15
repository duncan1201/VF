/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import java.util.Locale;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author dq
 */
public class UppercaseDocumentFilter extends DocumentFilter {

    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offset, String text,
            AttributeSet attr) throws BadLocationException {

        fb.insertString(offset, text.toUpperCase(Locale.ENGLISH), attr);
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
            AttributeSet attrs) throws BadLocationException {

        fb.replace(offset, length, text.toUpperCase(Locale.ENGLISH), attrs);
    }
}
