/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.dynamicTable;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.pdb.PDBDoc;
import com.gas.domain.core.pubmed.PubmedArticle;
import com.gas.domain.core.ren.RENList;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.TigrProject;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.Exceptions;
import org.openide.util.datatransfer.MultiTransferObject;

/**
 *
 * @author dq
 */
public class TableTransferable implements Transferable {

    private List<DataFlavor> dfs = new ArrayList<DataFlavor>();
    private List list = new ArrayList();

    public TableTransferable() {
    }

    public void addTransferData(Object obj) {
        list.add(obj);
        DataFlavor df = createDataFlavor(obj);
        dfs.add(df);
    }

    public void clear() {
        dfs.clear();
        list.clear();
    }

    private DataFlavor createDataFlavor(Object obj) {
        DataFlavor ret = null;
        if (obj instanceof AnnotatedSeq) {
            ret = new DataFlavor(AnnotatedSeq.class, AnnotatedSeq.class.getSimpleName());
        } else if (obj instanceof PubmedArticle) {
            ret = new DataFlavor(PubmedArticle.class, PubmedArticle.class.getSimpleName());
        } else if (obj instanceof PDBDoc) {
            ret = new DataFlavor(PDBDoc.class, PDBDoc.class.getSimpleName());
        } else if (obj instanceof RENList) {
            ret = new DataFlavor(RENList.class, RENList.class.getSimpleName());
        } else if (obj instanceof MSA) {
            ret = new DataFlavor(MSA.class, MSA.class.getSimpleName());
        } else if (obj instanceof TigrProject) {
            ret = new DataFlavor(TigrProject.class, TigrProject.class.getSimpleName());
        } else if (obj instanceof Kromatogram) {
            ret = new DataFlavor(Kromatogram.class, Kromatogram.class.getSimpleName());
        } else {
            throw new IllegalArgumentException(String.format("Class '%s' not supported", obj.getClass().toString()));
        }
        return ret;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return dfs.toArray(new DataFlavor[dfs.size()]);
    }

    public DataFlavor getDomainDataFlavor() {
        DataFlavor ret = null;
        for (DataFlavor df : dfs) {
            if (df.getRepresentationClass().isAssignableFrom(AnnotatedSeq.class)) {
                ret = df;
                break;
            } else if (df.getRepresentationClass().isAssignableFrom(PubmedArticle.class)) {
                ret = df;
                break;
            } else if (df.getRepresentationClass().isAssignableFrom(PDBDoc.class)) {
                ret = df;
                break;
            } else if (df.getRepresentationClass().isAssignableFrom(RENList.class)) {
                ret = df;
                break;
            } else if (df.getRepresentationClass().isAssignableFrom(MSA.class)) {
                ret = df;
                break;
            } else if (df.getRepresentationClass().isAssignableFrom(TigrProject.class)) {
                ret = df;
                break;
            } else if (df.getRepresentationClass().isAssignableFrom(Kromatogram.class)) {
                ret = df;
                break;
            } else {
                throw new IllegalArgumentException(String.format("'%s' not supported", df.getRepresentationClass().toString()));
            }
        }
        return ret;
    }

    public Object getDomainTransferData() {
        Object ret = null;
        DataFlavor df = getDomainDataFlavor();
        try {
            if (df != null) {
                ret = getTransferData(df);
            }
        } catch (UnsupportedFlavorException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {

        boolean ret = false;
        if (flavor.getRepresentationClass().getName().equals(MultiTransferObject.class.getName())) {
            //ret = true;
            //return ret;
        }
        for (DataFlavor df : dfs) {
            String n1 = df.getHumanPresentableName();
            String n2 = flavor.getHumanPresentableName();
            if (n1.equals(n2)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        Object ret = null;
        for (int i = 0; i < dfs.size(); i++) {
            if (dfs.get(i) == flavor) {
                ret = list.get(i);
                break;
            }
        }
        return ret;
    }
}
