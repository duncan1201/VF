/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.dynamicTable;

import com.gas.common.ui.core.CharSet;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.CommonUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.IFolderElementList;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.pdb.PDBDoc;
import com.gas.domain.core.pubmed.PubmedArticle;
import com.gas.domain.core.ren.RENList;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.TigrProject;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.domain.ui.misc.Helper;
import java.util.*;
import javax.swing.table.AbstractTableModel;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author dunqiang
 */
public class DynamicTableModel extends AbstractTableModel {

    public static final String CHECK_COL = "checkboxColumn";
    public static final String ICON_COL = "iconColumn";
    public static final String NAME_COL = "nameColumn";
    public static final String FORM_COL = "formColumn";
    public static final String DESC_COL = "descColumn";
    public static final String ACCESSION_COL = "accessionColumn";
    public static final String DATE_COL = "dateColumn";
    public static final String SIZE_COL = "sizeColumn";
    public static final String LENGTH_COL = "lengthColumn";
    public static final String JOURNAL_TITLE_COL = "journalTitleColumn";
    public static final String DOI_COL = "doiColumn";
    public static final String PDB_ID_COL = "pdbId";
    public static final String CLASSIFICATION_COL = "classificationColumn";
    private static final String[] asColumns = {CHECK_COL, ICON_COL, NAME_COL, FORM_COL, DESC_COL, ACCESSION_COL, DATE_COL, LENGTH_COL};
    private static final String[] renColumns = {CHECK_COL, ICON_COL, NAME_COL, DESC_COL, SIZE_COL, DATE_COL};
    private static final String[] abstractColumns = {CHECK_COL, ICON_COL, NAME_COL, JOURNAL_TITLE_COL, DOI_COL, ACCESSION_COL};
    private static final String[] PDBColumns = {CHECK_COL, ICON_COL, NAME_COL, CLASSIFICATION_COL, DOI_COL, PDB_ID_COL};
    private static final String[] TigrPtColumns = {CHECK_COL, ICON_COL, NAME_COL, DESC_COL, DATE_COL};
    private static final String[] KromatogramColumns = {CHECK_COL, ICON_COL, NAME_COL, DESC_COL, DATE_COL, LENGTH_COL};
    private static final String[] MSAColumns = {CHECK_COL, ICON_COL, NAME_COL, DESC_COL, DATE_COL, LENGTH_COL};
    private List<IFolderElement> data = new ArrayList<IFolderElement>();
    private List<Boolean> selections = new ArrayList<Boolean>();
    private boolean asPresent;
    private boolean renPresent;
    private boolean abstractPresent;
    private boolean pdbPresent;
    private boolean shortgunPtPresent;
    private boolean msaPresent;
    private boolean kromatogramPresent;
    private List<String> columnNames = new ArrayList<String>();
    private boolean nameEditable = true;
    private boolean descEditable = true;
    IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);

    public DynamicTableModel() {
    }

    void setData(List data) {
        this.data = data;
        this.selections.clear();
        for (int i = 0; i < data.size(); i++) {
            this.selections.add(false);
        }
        resetColumns();
        this.fireTableDataChanged();
    }

    public void setNameEditable(boolean nameEditable) {
        this.nameEditable = nameEditable;
    }

    public void setDescEditable(boolean descEditable) {
        this.descEditable = descEditable;
    }

    protected void updateRowByHibernateId(IFolderElement e) {
        Integer row = null;
        String hId = e.getHibernateId();
        for (int i = 0; i < data.size(); i++) {
            IFolderElement fe = (IFolderElement) data.get(i);
            if (fe.getHibernateId().equals(hId)) {
                data.set(i, e);
                row = i;
                break;
            }
        }
        if (row != null) {
            fireTableRowsUpdated(row, row);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean ret = false;
        final String colName = getColumnName(columnIndex);
        final Object row = getRow(rowIndex);
        if (CHECK_COL.equalsIgnoreCase(colName)) {
            ret = true;
        } else if (NAME_COL.equalsIgnoreCase(colName)) {
            if(row instanceof PDBDoc || row instanceof PubmedArticle){
                return false;
            }else{
                ret = nameEditable;
            }
        } else if (DESC_COL.equalsIgnoreCase(colName)) {            
            List<String> names = getColumnNames(row);
            ret = names.contains(DESC_COL) && descEditable;
        }
        return ret;
    }

    private void checkExclusively(int rowIndex) {
        boolean tableDataChanged = false;
        for (int i = 0; i < selections.size(); i++) {
            if (rowIndex != i && selections.get(i).booleanValue() == true) {
                selections.set(i, false);
                tableDataChanged = true;
            } else if (rowIndex == i && selections.get(i).booleanValue() == false) {
                selections.set(i, true);
                tableDataChanged = true;
            }
        }
        if (tableDataChanged) {
            fireTableDataChanged();
        }
    }

    public void updateCheckBox(boolean aValue, int rowIndex, boolean exclusively) {
        if (aValue && exclusively) {
            checkExclusively(rowIndex);
        } else {
            setValueAt(aValue, rowIndex, 0);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        final IFolderElement row = getRow(rowIndex);
        final String colName = getColumnName(columnIndex);
        if (colName.equals(CHECK_COL)) {
            selections.set(rowIndex, (Boolean) aValue);
            fireTableCellUpdated(rowIndex, columnIndex);
        } else if (colName.equals(NAME_COL)) {
            final String newName = (String) aValue;
            IFolderElement ele = (IFolderElement) row;
            final String oldName = ele.getName();
            
            Folder folder = ExplorerTC.getInstance().getSelectedFolder();            
            StringList names = folder.getElementNames(row.getClass());
            if(newName.isEmpty() || names.contains(newName)){
                return ;
            }
            
            if (!newName.equals(oldName)) {
                if (ele instanceof AnnotatedSeq) {
                    CharSet intersect = AnnotatedSeq.forbiddenChars.intersect(newName.toCharArray());
                    if (!intersect.isEmpty()) {
                        String ins = String.format("The following characters are not allowed: %s", AnnotatedSeq.forbiddenChars.toString());
                        String errorMsg = String.format("\"%s\" %s not allowed", intersect.toString(), intersect.size() > 1 ? "are" : "is");
                        String msg = String.format(CNST.ERROR_FORMAT, errorMsg, ins);
                        String title = "Rename";
                        DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                        m.setTitle(title);
                        DialogDisplayer.getDefault().notify(m);
                    } else {
                        asService.rename((AnnotatedSeq) ele, newName);
                        ele.setName(newName);
                        ele.setLastModifiedDate(new Date());
                        fireTableCellUpdated(rowIndex, columnIndex);
                    }
                } else {
                    ele.setName(newName);
                    ele.setLastModifiedDate(new Date());
                    fireTableCellUpdated(rowIndex, columnIndex);
                }
            }
        } else if (colName.equals(DESC_COL)) {
            final String newDesc = (String) aValue;            
            boolean updated = updateDescription(row, newDesc);
            if (updated) {
                fireTableCellUpdated(rowIndex, columnIndex);
            }

        }
    }

    private boolean updateDescription(IFolderElement obj, String desc) {
        boolean ret = false;
        String oldDesc = obj.getDesc();
        if (oldDesc == null || !oldDesc.equals(desc)) {
            obj.setDesc(desc);
            obj.setLastModifiedDate(new Date());
            ret = true;
        }
        return ret;
    }

    protected List<Integer> updateData(List<IFolderElement> uData) {
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < uData.size(); i++) {
            Integer row = updateDatum(uData.get(i));
            if (row != null) {
                indices.add(i);
                fireTableRowsUpdated(row, row);
            }
        }
        return indices;

    }

    protected void addDataFront(IFolderElementList _data, boolean fire) {
        for (int i = 0; i < _data.size(); i++) {
            this.data.add(0, _data.get(i));
            this.selections.add(0, false);
        }
        if (fire) {
            resetColumns();

            if (!_data.isEmpty()) {
                this.fireTableRowsInserted(0, Math.max(0, _data.size() - 1));
            }
        }
    }

    /**
     * Update the data if it's the same
     */
    private Integer updateDatum(IFolderElement uDatum) {
        Integer row = null;
        for (int i = 0; i < data.size(); i++) {
            if (isSame((IFolderElement) uDatum, (IFolderElement) data.get(i))) {
                data.set(i, uDatum);
                row = i;
                this.fireTableRowsUpdated(i, i);
                break;
            }
        }
        return row;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        String name = getColumnName(columnIndex);
        if (name.equals(CHECK_COL)) {
            return Boolean.class;
        } else if (name.equals(ICON_COL)) {
            return String.class;
        } else if (name.equals(NAME_COL)) {
            return String.class;
        } else if (name.equals(FORM_COL)) {
            return String.class;
        } else if (name.equals(DESC_COL)) {
            return String.class;
        } else if (name.equals(ACCESSION_COL)) {
            return String.class;
        } else if (name.equals(DATE_COL)) {
            return Date.class;
        } else if (name.equals(SIZE_COL)) {
            return Integer.class;
        } else if (name.equals(LENGTH_COL)) {
            return String.class;
        } else if (name.equals(JOURNAL_TITLE_COL)) {
            return String.class;
        } else {
            return Object.class;
        }
    }

    public void clear() {
        this.data.clear();
        this.selections.clear();
        resetColumns();
    }

    void addOrUpdateOrDeleteData(IFolderElementList list) {
        addOrUpdateOrDeleteData(list, true);
    }

    void addOrUpdateData(IFolderElementList list, boolean front) {
        // update data first, record down the index.
        List<Integer> updatedIndices = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            Integer row = updateDatum(list.get(i));
            if (row != null) {
                updatedIndices.add(i);
            }
        }
        // remove by the indices.
        Collections.sort(updatedIndices);
        for (int i = updatedIndices.size() - 1; i > -1; i--) {
            list.remove(updatedIndices.get(i).intValue());
        }

        // add the remaining data to the table
        if (front) {
            addDataFront(list, true);

        } else {
            appendData(list);
        }
    }

    /**
     * <ol>
     * <li>Get all the hibernate all ids of <b>data</b>, and make a
     * <b>data-backup</b>
     * <li>Update existing table data, then remove the corresponding datum from
     * <b>data</b>
     * <li>Add remaining <b>data</b> to the table
     * <li>Remove the records whose id is not in the ids of <b>data-backup</b>
     * </ol>
     */
    protected void addOrUpdateOrDeleteData(IFolderElementList list, boolean front) {
        // get all the hids       
        List<String> hIds = new ArrayList<String>();
        Iterator<IFolderElement> itr = list.iterator();
        while (itr.hasNext()) {
            IFolderElement fe = itr.next();
            String hId = fe.getHibernateId();
            if (hId != null) {
                hIds.add(hId);
            }
        }
        List<String> clonedIds = CommonUtil.copyOf(hIds);
        IFolderElementList clonedList = new IFolderElementList(list);

        // update data first, record down the index.
        List<Integer> updatedIndices = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            Integer row = updateDatum(list.get(i));
            if (row != null) {
                updatedIndices.add(i);
            }
        }
        // remove by the indices.
        Collections.sort(updatedIndices);
        for (int i = updatedIndices.size() - 1; i > -1; i--) {
            list.remove(updatedIndices.get(i).intValue());
        }

        // add the remaining data to the table
        if (front) {
            addDataFront(list, true);
        } else {
            appendData(list);
        }

        // remove 
        for (int i = 0; i < this.data.size(); i++) {
            IFolderElement fe = (IFolderElement) this.data.get(i);
            String hId = fe.getHibernateId();
            boolean delete;
            if (!clonedIds.isEmpty()) {
                delete = !clonedIds.contains(hId);
            } else {
                delete = !clonedList.contains(fe);
            }
            if (delete) {
                this.data.remove(i);
                this.selections.remove(i);
                i--;
                this.fireTableRowsDeleted(i, i);
            }
            resetColumns();
        }
        //this.fireTableDataChanged();
    }

    private void appendData(List<IFolderElement> list) {
        for (IFolderElement obj : list) {
            appendDatum(obj);
        }
    }

    private void appendDatum(IFolderElement obj) {
        if (obj instanceof AnnotatedSeq || obj instanceof PubmedArticle || obj instanceof RENList || obj instanceof MSA) {
            this.data.add(obj);
            this.selections.add(false);
        } else {
            throw new IllegalArgumentException(String.format("Class '%s' not supported!", obj.getClass().toString()));
        }
        resetColumns();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return _getColumnCount();
    }

    @Override
    public String getColumnName(int column) {
        return getColumnNames().get(column);
    }

    protected List<String> getColumnNames() {
        if (columnNames.isEmpty()) {
            columnNames = createColumnNames();
        }
        return columnNames;
    }

    public int getColumnIndex(String str) {
        List<String> colNames = getColumnNames();
        return colNames.indexOf(str);
    }

    public void del(List list) {
        boolean success = false;
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            boolean ok = del(o);
            if (ok) {
                i--;
            }
            success = success || ok;
        }
        if (success) {
            this.fireTableDataChanged();
            resetColumns();
        }
    }

    private boolean del(Object deleted) {
        boolean ret = false;
        Integer row = null;
        for (int i = 0; i < data.size(); i++) {
            Object obj = data.get(i);
            if (isSame((IFolderElement) deleted, (IFolderElement) obj)) {
                data.remove(i);
                selections.remove(i);
                row = i;
                ret = true;
                break;
            }
        }
        return ret;
    }

    private boolean isSame(IFolderElement e, IFolderElement e2) {
        boolean ret = false;
        if (e.getHibernateId() != null && e2.getHibernateId() != null && e.getHibernateId().equals(e2.getHibernateId())) {
            ret = true;
        } else if (e == e2) {
            ret = true;
        }
        return ret;
    }

    private void resetColumns() {
        int beforeCount = getColumnCount();
        updatePresence();
        columnNames.clear();
        int afterCount = getColumnCount();
        if (beforeCount != afterCount) {
            this.fireTableStructureChanged();
        }
    }

    public IFolderElement getRow(int i) {
        if (i < data.size()) {
            return data.get(i);
        } else {
            return null;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object ret = null;

        boolean selected = selections.get(rowIndex);
        Object obj = data.get(rowIndex);
        String colName = getColumnName(columnIndex);
        if (colName.equals(CHECK_COL)) {
            ret = selected;
        } else {
            if (obj instanceof AnnotatedSeq) {
                ret = getValueAt((AnnotatedSeq) obj, colName);
            } else if (obj instanceof RENList) {
                ret = getValueAt((RENList) obj, colName);
            } else if (obj instanceof PubmedArticle) {
                ret = getValueAt((PubmedArticle) obj, colName);
            } else if (obj instanceof PDBDoc) {
                ret = getValueAt((PDBDoc) obj, colName);
            } else if (obj instanceof TigrProject) {
                ret = getValueAt((TigrProject) obj, colName);
            } else if (obj instanceof MSA) {
                ret = getValueAt((MSA) obj, colName);
            } else if(obj instanceof Kromatogram){
                ret = getValueAt((Kromatogram)obj, colName);
            }else {
                throw new IllegalArgumentException(String.format("Data type '%s' not supported", obj.getClass().toString()));
            }
        }
        return ret;
    }

    protected void updateCheckBoxs(boolean checked) {
        for (int i = 0; i < selections.size(); i++) {
            selections.set(i, checked);
        }
        fireTableDataChanged();
    }

    private Object getValueAt(MSA msa, final String colName) {
        Object ret = "";
        List<String> colNames = getColumnNames();
        if (colName.equals(ICON_COL)) {
            ret = ImageNames.ATG_16;
        } else if (colName.equals(NAME_COL)) {
            ret = msa.getName();
        } else if (colName.equals(DESC_COL)) {
            ret = msa.getDesc();
        } else if (colName.equals(DATE_COL)) {
            ret = msa.getLastModifiedDate();
        } else if (colName.equals(FORM_COL)) {
            ret = "Linear";
        } else if (colName.equals(LENGTH_COL)) {
            ret = msa.getLength().toString();
        } else if (colNames.contains(colName)) {
            ret = "-";
        } else {
            throw new IllegalArgumentException();
        }
        return ret;
    }

    private Object getValueAt(TigrProject p, String colName) {
        Object ret = "";
        List<String> colNames = getColumnNames();
        if (colName.equals(ICON_COL)) {
            ret = ImageNames.SANGER_16;
        } else if (colName.equals(NAME_COL)) {
            //ret = p.isRead() ? p.getName() : String.format(BOLD_TPL, p.getName());
            ret = p.getName();
        } else if (colName.equals(DESC_COL)) {
            //ret = p.isRead() ? p.getDesc() : String.format(BOLD_TPL, p.getDesc());
            ret = p.getDesc();
        } else if (colName.equals(DATE_COL)) {
            ret = p.getLastModifiedDate();
        } else if (colNames.contains(colName)) {            
            ret = "-";        
        } else {
            throw new IllegalArgumentException(String.format("Column \"%s\" doesnot exist", colName));
        }
        return ret;
    }
    
    private Object getValueAt(Kromatogram kromatogram, String colName){
        Object ret = null;
        List<String> colNames = getColumnNames();
        if(colName.equals(ICON_COL)){
            ret = ImageNames.TRACES_16;
        }else if(colName.equals(NAME_COL)){
            ret = kromatogram.getName();
        }else if(colName.equals(DESC_COL)){
            ret = kromatogram.getDesc();
        }else if(colName.equals(DATE_COL)){
            ret = kromatogram.getLastModifiedDate();
        }else if(colName.equals(LENGTH_COL)){
            ret = kromatogram.getLength().toString();
        }else{
            ret = "";
        }
        return ret;
    }

    private Object getValueAt(PDBDoc doc, String colName) {
        Object ret = "";
        List<String> colNames = getColumnNames();
        if (colName.equals(ICON_COL)) {
            ret = ImageNames.STRUCTURE_16;
        } else if (colName.equals(NAME_COL)) {
            ret = doc.getName();
        } else if (colName.equals(PDB_ID_COL)) {
            ret = doc.getPdbId();
        } else if (colName.equals(DOI_COL)) {
            ret = doc.getDoi();
        } else if (colName.equals(CLASSIFICATION_COL)) {
            ret = doc.getClassification();
        } else if (colNames.contains(colName)) {
            ret = "-";
        } else {
            throw new IllegalArgumentException(String.format("Column \"%s\" doesnot exist", colName));
        }
        return ret;
    }

    private Object getValueAt(AnnotatedSeq as, String colName) {
        Object ret = "";
        List<String> colNames = getColumnNames();

        if (colName.equals(ICON_COL)) {
            if (AsHelper.isNucleotide(as)) {
                if (AsHelper.isFragment(as)) {
                    ret = ImageNames.FRAGMENT_16;
                } else if (as.isOligo()) {
                    ret = ImageNames.PRIMER_SINGLE_16;
                } else {
                    ret = ImageNames.NUCLEOTIDE_16;
                }
            } else {
                ret = ImageNames.PROTEIN_16;
            }
        } else if (colName.equals(NAME_COL)) {
            ret = as.getName();
        } else if (colName.equals(FORM_COL)) {
            ret = as.isCircular() ? "Circular" : "Linear";
        } else if (colName.equals(DESC_COL)) {
            ret = as.getDesc();
        } else if (colName.equals(ACCESSION_COL)) {
            ret = as.getAccession();
        } else if (colName.equals(DATE_COL)) {
            ret = as.getLastModifiedDate();
        } else if (colName.equals(LENGTH_COL)) {
            ret = as.getLength().toString();
        } else if (colNames.contains(colName)) {
            ret = "-";
        } else {
            throw new IllegalArgumentException(String.format("Column \"%s\" doesnot exist", colName));
        }
        return ret;
    }

    private Object getValueAt(RENList renList, String colName) {
        Object ret = "";
        List<String> colNames = getColumnNames();

        if (colName.equals(ICON_COL)) {
            ret = ImageNames.CUT_16;
        } else if (colName.equals(NAME_COL)) {
            ret = renList.getName();
        } else if (colName.equals(DESC_COL)) {
            if (renList.getDesc() == null) {
                ret = "";
            } else {
                ret = renList.getDesc();
            }
        } else if (colName.equals(SIZE_COL)) {
            ret = renList.getSize();
        } else if (colName.equals(DATE_COL)) {
            ret = renList.getLastModifiedDate();
        } else if (colNames.contains(colName)) {
            ret = "-";
        } else {
            throw new IllegalArgumentException(String.format("Column \"%s\" doesnot exist", colName));
        }
        return ret;
    }

    private Object getValueAt(PubmedArticle article, String colName) {
        Object ret = "";
        List<String> colNames = getColumnNames();
        if (colName.equals(ICON_COL)) {
            ret = ImageNames.PUBLICATION_16;
        } else if (colName.equals(NAME_COL)) {
            ret = article.getTitle();
        } else if (colName.equals(JOURNAL_TITLE_COL)) {
            ret = article.getJournalTitle();
        } else if (colName.equals(ACCESSION_COL)) {
            ret = article.getPmid();
        } else if (colName.equals(DOI_COL)) {
            ret = article.getDoi();
        } else if (colNames.contains(colName)) {
            ret = "-";
        } else {
            throw new IllegalArgumentException(String.format("Column \"%s\" doesnot exist", colName));
        }
        return ret;
    }

    private int _getColumnCount() {
        List<String> cn = getColumnNames();
        return cn.size();
    }

    private List<String> createColumnNames() {
        Set<String> nameSet = new LinkedHashSet<String>();
        if (isAsPresent()) {
            nameSet.addAll(Arrays.asList(asColumns));
        }
        if (isRenPresent()) {
            nameSet.addAll(Arrays.asList(renColumns));
        }
        if (isAbstractPresent()) {
            nameSet.addAll(Arrays.asList(abstractColumns));
        }
        if (isPdbPresent()) {
            nameSet.addAll(Arrays.asList(PDBColumns));
        }
        if (isShortgunPtPresent()) {
            nameSet.addAll(Arrays.asList(TigrPtColumns));
        }
        if (isMsaPresent()) {
            nameSet.addAll(Arrays.asList(MSAColumns));
        }
        if (isKromatogramPresent()){
            nameSet.addAll(Arrays.asList(KromatogramColumns));
        }
        List<String> ret = new ArrayList<String>(nameSet);
        return ret;
    }

    public boolean isKromatogramPresent() {
        return kromatogramPresent;
    }

    private List<String> getColumnNames(Object obj) {
        List<String> ret = null;
        if (obj instanceof AnnotatedSeq) {
            ret = Arrays.asList(asColumns);
        } else if (obj instanceof RENList) {
            ret = Arrays.asList(renColumns);
        } else if (obj instanceof PubmedArticle) {
            ret = Arrays.asList(abstractColumns);
        } else if (obj instanceof PDBDoc) {
            ret = Arrays.asList(PDBColumns);
        } else if (obj instanceof TigrProject) {
            ret = Arrays.asList(TigrPtColumns);
        } else if (obj instanceof MSA) {
            ret = Arrays.asList(MSAColumns);
        } else if(obj instanceof Kromatogram){
            ret = Arrays.asList(KromatogramColumns);
        } else {
            throw new UnsupportedOperationException();
        }
        return ret;
    }

    public boolean isPdbPresent() {
        return pdbPresent;
    }

    public void setPdbPresent(boolean pdbPresent) {
        this.pdbPresent = pdbPresent;
    }

    public boolean isAbstractPresent() {
        return abstractPresent;
    }

    public boolean isAsPresent() {
        return asPresent;
    }

    public boolean isRenPresent() {
        return renPresent;
    }

    public boolean isShortgunPtPresent() {
        return shortgunPtPresent;
    }

    public boolean isMsaPresent() {
        return msaPresent;
    }

    private void updatePresence() {
        asPresent = false;
        renPresent = false;
        abstractPresent = false;
        pdbPresent = false;

        Iterator itr = data.iterator();
        while (itr.hasNext()) {
            Object obj = itr.next();
            if (obj instanceof AnnotatedSeq) {
                asPresent = true;
            } else if (obj instanceof RENList) {
                renPresent = true;
            } else if (obj instanceof PubmedArticle) {
                abstractPresent = true;
            } else if (obj instanceof PDBDoc) {
                pdbPresent = true;
            } else if (obj instanceof TigrProject) {
                shortgunPtPresent = true;
            } else if (obj instanceof MSA) {
                msaPresent = true;
            } else if(obj instanceof Kromatogram){
                kromatogramPresent = true;
            } else {
                throw new IllegalArgumentException(String.format("Class '%s' not supported", obj.getClass().toString()));
            }
        }
    }

    public Integer getModelId(IFolderElement obj) {
        Integer ret = null;
        for (int i = 0; i < data.size() && obj != null; i++) {
            if (obj == data.get(i) || obj.equals(data.get(i))) {
                ret = i;
                break;
            }
        }
        if(ret == null && obj.getHibernateId() != null){
            ret = getModelIdByHibernateId(obj.getHibernateId());
        }
        return ret;
    }

    /**
     * @return 0-based
     */
    public Integer getModelIdByHibernateId(String hId) {
        Integer ret = null;
        for (int i = 0; i < data.size(); i++) {
            String id = Helper.getHibernateId(data.get(i));
            if (id != null && id.equals(hId)) {
                ret = i;
                break;
            }
        }
        return ret;
    }
}
