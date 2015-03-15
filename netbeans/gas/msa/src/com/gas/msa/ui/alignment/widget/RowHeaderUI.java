/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import com.gas.common.ui.core.StringComparator;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.light.Text;
import com.gas.common.ui.light.TextList;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.MSA;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
public class RowHeaderUI extends JComponent {

    private MSA msa;
    private Font textFont = FontUtil.getDefaultSansSerifFont();
    TextList textList = new TextList();
    String selectedRow;
    JPopupMenu popupMenu;

    public RowHeaderUI() {
        hookupListeners();
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    public boolean isRectSet(){
        return textList.isRectSet();
    }
    
    private void hookupListeners(){
        addPropertyChangeListener(new RowHeaderUIListeners.PtyListener());
    }    

    @Override
    public void paintComponent(Graphics g) {
        Dimension size = getSize();
        if (size.width <= 0 || size.height <= 0) {
            return;
        }
        layoutUIObjects();
        Graphics2D g2d = (Graphics2D) g;
        _paint(g2d);
    }

    private void _paint(Graphics2D g) {
        textList.setBgColor(ColorCnst.ALICE_BLUE);
        textList.paint(g, SwingConstants.LEFT);
    }

    public void setMsa(MSA msa) {
        MSA old = this.msa;
        this.msa = msa;
        firePropertyChange("msa", old, this.msa);
    }
    
    JPopupMenu getPopupMenu(){
        if(popupMenu == null){
            popupMenu = new JPopupMenu();
            popupMenu.add(new MSAActions.EditNameAction(this));
        }
        return popupMenu;
    }

    void createUIObjects() {
        MSAScroll msaScroll = UIUtil.getParent(this, MSAScroll.class);
        int widthCandidate = msaScroll.getCornerUI().calculatePreferredWidth();
        int widthCandidate2 = calculatePreferredWidth();
        int width = Math.max(widthCandidate, widthCandidate2);
        UIUtil.setPreferredWidth(this, width);

        textList.clear();
        StringList entryNames = msa.getEntriesNames();
        Collections.sort(entryNames, new StringComparator(true, false));
        Iterator<String> itr = entryNames.iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            Text text = new Text();
            text.setFont(textFont);
            text.setStr(str);
            if(str.equalsIgnoreCase(selectedRow)){
                text.setSelected(true);
            }
            textList.add(text);
        }
        layoutUIObjects();
    }
    
    void setSelectedRow(String str){
        String old = this.selectedRow;
        this.selectedRow = str;
        firePropertyChange("selectedRow", old, this.selectedRow);
    }

    void layoutUIObjects() {
        Dimension size = getSize();
        MSAScroll msaScroll = UIUtil.getParent(this, MSAScroll.class);
        Rectangle2D rect2d = msaScroll.getViewUI().getMsaComp().getAlignmentListRect();
        if (size.width != 0 && size.height != 0 && rect2d != null && rect2d.getHeight() != 0) {
            textList.setRect(new Rectangle2D.Double(0, 0, size.width, rect2d.getHeight()));
            textList.setVertical(true);
            textList.layout();
        }        
    }

    protected Integer calculatePreferredWidth() {
        final Insets insets = UIUtil.getDefaultInsets();
        final FontMetrics fm = FontUtil.getFontMetrics(textFont);
        StringList strList = msa.getEntriesNames();
        Integer ret = fm.stringWidth(strList.longest()) + insets.left + insets.right;
        return ret;
    }
}
