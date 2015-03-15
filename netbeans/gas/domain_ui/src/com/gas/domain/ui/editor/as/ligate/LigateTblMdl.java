/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.ligate;

import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqList;
import com.gas.domain.core.as.Overhang;
import com.gas.domain.core.as.AsHelper;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author dq
 */
public class LigateTblMdl extends AbstractTableModel {

    public enum MOD {

        none, exo5to3, exo3to5, klenowFillIn, partialFillIn
    };

    public enum dNTP {

        dATP, dTTP, dCTP, dGTP, dNTP
    };
    private static final String GREEN = "007200";
    private static final String RED = "C41233";
    private static final String BLUNT_TPL = "<html><font color=#%s>%s</font></html>";
    private static final String STRIKE_2nd_TPL = "<html><font color=#%s>%s<br/><strike>%s</strike></font></html>";
    private static final String STRIKE_1st_TPL = "<html><font color=#%s><strike>%s</strike><br/>%s</font></html>";
    private static final String STRIKE_BOTH_TPL = "<html><font color=#%s><strike>%s</strike><br/><strike>%s</strike></font></html>";
    private static final String NO_STRIKE_TPL = "<html><font color=#%s>%s<br/>%s</font></html>";
    final static String COL_NO = "No";
    final static String COL_5END = "5' end";
    final static String COL_FRAGMENT = "Fragment";
    final static String COL_3END = "3' end";
    private static String[] COL_NAMES = {COL_NO, COL_5END, COL_FRAGMENT, COL_3END};
    TblRowList rows = new TblRowList();

    @Override
    public Object getValueAt(int row, int column) {

        Object ret = null;
        AnnotatedSeq as = rows.get(row).as;
        MOD mod = rows.get(row).mod;
        String colName = getColumnName(column);
        if(colName.equals(COL_NO)){
            return (row + 1) + "";
        } else if (colName.equals(COL_5END)) { // start overhang
            Overhang overhangStart = AsHelper.getStartOverhang(as);
            boolean compatible = rows.get(row).startCompatible;
            String color = compatible ? GREEN : RED;
            if (overhangStart == null) {
                ret = String.format(BLUNT_TPL, color, "Blunt");
            } else {
                final String seq = as.getOverhangSeq(overhangStart);
                final String revSeq = StrUtil.reverse(seq);
                final String compSeq = BioUtil.complement(seq);
                final String revCompSeq = StrUtil.reverse(compSeq);
                String template = null;
                
                if (overhangStart.isStrand()) { // 5' overhang       
                    String data2nd = compSeq;
                    if (mod == MOD.klenowFillIn) {
                        template = NO_STRIKE_TPL;
                    } else if (mod == MOD.exo5to3) {
                        template = STRIKE_BOTH_TPL;
                    } else if (mod == MOD.partialFillIn) {
                        List<dNTP> dNTPList = rows.get(row).dNTPList;
                        if (dNTPList.isEmpty()) {
                            template = STRIKE_2nd_TPL;
                        } else {
                            template = NO_STRIKE_TPL;
                            String fillInSeqs = rows.getPartialFillInSeq(revCompSeq, dNTPList);
                            fillInSeqs = StrUtil.reverse(fillInSeqs);
                            data2nd = insertStrikedBases(fillInSeqs, compSeq, true);
                        }
                    } else {
                        template = STRIKE_2nd_TPL;
                    }
                    ret = String.format(template, color, seq, data2nd);
                } else { // 3' overhang
                    if (mod == MOD.exo3to5) {
                        template = STRIKE_BOTH_TPL;
                    } else {
                        template = STRIKE_1st_TPL;
                    }
                    ret = String.format(template, color, revCompSeq, revSeq);
                }
            }
        } else if (colName.equals(COL_FRAGMENT)) {
            ret = as.getName();
        } else if (colName.equals(COL_3END)) { // end overhang
            final Overhang overhangEnd = AsHelper.getEndOverhang(as);
            boolean compatible = rows.get(row).endCompatible;
            String color = compatible ? GREEN : RED;
            String tpl = null;
            if (overhangEnd == null) {
                ret = String.format(BLUNT_TPL, color, "Blunt");
            } else {
                String seq = as.getOverhangSeq(overhangEnd);
                final String compSeq = BioUtil.complement(seq);
                final String revCompSeq = StrUtil.reverse(compSeq);
                String data1st = revCompSeq;
                data1st = seq;
                String data2nd = StrUtil.reverse(seq);
                data2nd = compSeq;
                if (overhangEnd.isStrand()) { // 3' overhang
                    if (mod == MOD.exo3to5) {
                        tpl = STRIKE_BOTH_TPL;
                    } else {
                        tpl = STRIKE_2nd_TPL;
                    }
                } else { // 5' overhang
                    if (mod == MOD.klenowFillIn) {
                        tpl = NO_STRIKE_TPL;
                    } else if (mod == MOD.partialFillIn) {
                        List<dNTP> dNTPList = rows.get(row).dNTPList;
                        tpl = NO_STRIKE_TPL;
                        final String fillInSeqs = rows.getPartialFillInSeq(revCompSeq, dNTPList);
                        data1st = insertStrikedBases(fillInSeqs, data1st, false);
                    } else if (mod == MOD.exo5to3) {
                        tpl = STRIKE_BOTH_TPL;
                    } else {
                        tpl = STRIKE_1st_TPL;
                    }
                }
                ret = String.format(tpl, color, data1st, data2nd);
            }
        }
        return ret;
    }

    public AnnotatedSeqList getModifiedData() {
        AnnotatedSeqList ret = new AnnotatedSeqList();
        for (int i = 0; i < this.rows.size(); i++) {
            AnnotatedSeq as = this.rows.get(i).as;
            final TblRow row = rows.get(i);
            final MOD mod = row.mod;

            if (mod == MOD.none) {
                ret.add(as);
            } else if (mod == MOD.klenowFillIn) {
                AsHelper.remove5Overhang(as);
                ret.add(as);
            } else if (mod == MOD.exo3to5) {
                int startOvLength = 0;
                int endOvLength = 0;
                Overhang startOverhang = as.getStartOverhang();
                if (startOverhang != null && startOverhang.isThreePrime()) {
                    startOvLength = startOverhang.getLength();
                }
                Overhang endOverhang = as.getEndOverhang();
                if (endOverhang != null && endOverhang.isThreePrime()) {
                    endOvLength = endOverhang.getLength();
                }
                if (startOvLength != 0 || endOvLength != 0) {
                    AnnotatedSeq subAs = AsHelper.subAs(as, startOvLength + 1, as.getLength() - endOvLength);
                    subAs.setHibernateId(as.getHibernateId());
                    subAs.remove3Overhang();
                    ret.add(subAs);
                } else {
                    ret.add(as);
                }

            } else if (mod == MOD.exo5to3) {
                int startOvLength = 0;
                int endOvLength = 0;
                Overhang startOverhang = as.getStartOverhang();
                if (startOverhang != null && startOverhang.isFivePrime()) {
                    startOvLength = startOverhang.getLength();
                }
                Overhang endOverhang = as.getEndOverhang();
                if (endOverhang != null && endOverhang.isFivePrime()) {
                    endOvLength = endOverhang.getLength();
                }
                if (startOvLength != 0 || endOvLength != 0) {
                    AnnotatedSeq subAs = AsHelper.subAs(as, startOvLength + 1, as.getLength() - endOvLength);
                    subAs.setHibernateId(as.getHibernateId());
                    subAs.remove5Overhang();
                    ret.add(subAs);
                } else {
                    ret.add(as);
                }
            } else if (mod == MOD.partialFillIn) {

                Overhang ovStart = as.getStartOverhang();
                Overhang ovEnd = as.getEndOverhang();
                if (ovStart != null) {
                    String overhangModified = row.getModifiedOverhang(true);
                    if (overhangModified.length() > 0) {
                        ovStart.setLength(overhangModified.length());
                    }else if(overhangModified.length() == 0){
                        as.removeStartOverhang();
                    }
                }
                if (ovEnd != null) {
                    String overhangModified = row.getModifiedOverhang(false);
                    if (overhangModified.length() > 0) {
                        ovEnd.setLength(overhangModified.length());
                    }else if(overhangModified.length() == 0){
                        as.removeEndOverhang();
                    }                    
                }
                ret.add(as);
            }
        }
        return ret;
    }

    protected void updateNTP(int i, dNTP _dNTP, boolean on) {
        List<dNTP> dNTPList = rows.get(i).dNTPList;

        if (dNTPList.contains(_dNTP) && !on) {
            dNTPList.remove(_dNTP);
        } else if (!dNTPList.contains(_dNTP) && on) {
            dNTPList.add(_dNTP);
        }
    }

    protected List<dNTP> get_dNTP(int row) {
        List<dNTP> ret = rows.get(row).dNTPList;
        if (ret == null) {
            ret = new ArrayList<dNTP>();
        }
        return ret;
    }

    private String insertStrikedBases(String partialSeq, String completeSeq, boolean strikeFront) {
        StringBuilder ret = new StringBuilder();
        int count = completeSeq.length() - partialSeq.length();

        if (count > 0) {
            if (strikeFront) {
                ret.append("<strike>");
                ret.append(completeSeq.substring(0, count));
                ret.append("</strike>");
                ret.append(partialSeq);
            } else {
                ret.append(partialSeq);
                ret.append("<strike>");
                ret.append(completeSeq.substring(completeSeq.length() - count, completeSeq.length()));
                ret.append("</strike>");
            }
        } else {
            ret.append(partialSeq);
        }
        return ret.toString();
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }

    @Override
    public int getColumnCount() {
        return COL_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COL_NAMES[column];
    }

    AnnotatedSeq getAs(int i) {
        return rows.get(i).as;
    }

    /**
     * @param rowIndex: 0-based
     */
    void setRow(int rowIndex, AnnotatedSeq as) {
        rows.get(rowIndex).as = as;
    }

    public void setMod(int index, MOD mod) {
        rows.get(index).mod = mod;
    }

    public MOD getMod(int index) {
        return rows.get(index).mod;
    }

    public void setData(List<AnnotatedSeq> objs) {
        rows.clear();
        for (int i = 0; i < objs.size(); i++) {
            rows.add(new TblRow(objs.get(i)));
        }
    }

    protected boolean moveUp(int row) {
        boolean ret = false;
        if (row > 0) {
            ret = true;

            // switch
            TblRow curRow = rows.get(row);
            TblRow prevRow = rows.get(row - 1);
            rows.set(row, prevRow);
            rows.set(row - 1, curRow);

            fireTableDataChanged();
        }
        return ret;
    }

    protected boolean moveDown(int row) {
        boolean ret = false;

        if (row + 1 < rows.size()) {
            ret = true;

            // switch
            TblRow curRow = rows.get(row);
            TblRow nextRow = rows.get(row + 1);
            rows.set(row, nextRow);
            rows.set(row + 1, curRow);

            fireTableDataChanged();
        }
        return ret;
    }

    @Override
    public void fireTableDataChanged() {
        updateCompatibilities();
        super.fireTableDataChanged();
    }

    void updateCompatibilities() {
        rows.updateCompatibilities();
    }
}
