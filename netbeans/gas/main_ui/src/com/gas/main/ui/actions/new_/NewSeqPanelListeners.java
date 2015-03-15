/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.new_;

import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.UIUtil;
import static com.gas.main.ui.actions.new_.NewSeqPanel.CMD_PROTEIN;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;

/**
 *
 * @author dq
 */
public class NewSeqPanelListeners {
    
    private static Logger logger = Logger.getLogger(NewSeqPanelListeners.class.getName());
    
    static class TypeListener implements ActionListener {

        WeakReference<NewSeqPanel> ref;
        
        TypeListener(NewSeqPanel p){
            ref = new WeakReference<NewSeqPanel>(p);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {            
            ref.get().validateInput();            
        }
    }
    
    static class SeqCaretListener implements CaretListener {

        private WeakReference<NewSeqPanel> ref;
        
        SeqCaretListener(NewSeqPanel p){
            ref = new WeakReference<NewSeqPanel>(p);
        }
        
        @Override
        public void caretUpdate(CaretEvent e) {
            int dot = e.getDot();
            int mark = e.getMark();
            ref.get().updateCaretPosition(dot, mark);
        }
    }
    
    static class NameKeyAdapter extends KeyAdapter {
        
        private WeakReference<NewSeqPanel> ref;
        
        NameKeyAdapter(NewSeqPanel newSeqPanel){
            ref = new WeakReference<NewSeqPanel>(newSeqPanel);
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
            ref.get().validateInput();            
        }
    }
    
    static class DocKeyAdapter extends KeyAdapter {
        
        private WeakReference<NewSeqPanel> panelRef;
        
        DocKeyAdapter(NewSeqPanel newSeqPanel){
            panelRef = new WeakReference<NewSeqPanel>(newSeqPanel);
        }
        
        @Override
        public void keyTyped(KeyEvent e) {                        
            boolean isDNAType = panelRef.get().isDNAType();
            boolean isProteinType = panelRef.get().isProteinType();
            int keyCode = e.getKeyCode();
            char keyChar = e.getKeyChar();
            
            
            if(isDNAType){
                boolean isDNA = BioUtil.isDNA(keyChar);
                if(!isDNA){
                    e.consume();                
                }
            }else if(isProteinType){
                boolean isProtein = BioUtil.isProtein(keyChar);
                if(!isProtein){
                    e.consume();
                }
            }            
                        
            logger.info("keyCode" + e.getKeyCode());
            logger.info("keyChar" + e.getKeyChar());
        }                
    }
    
    static class DocListener implements DocumentListener{

        private WeakReference<NewSeqPanel> ref;
        
        DocListener(NewSeqPanel p){
            ref = new WeakReference<NewSeqPanel>(p);
        }
        
        @Override
        public void insertUpdate(DocumentEvent e) {
            process(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            process(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            process(e);
        }
        
        private void process(DocumentEvent e){            
            ref.get().validateInput();
            ref.get().updateStatusPane();
        }
    }
}
