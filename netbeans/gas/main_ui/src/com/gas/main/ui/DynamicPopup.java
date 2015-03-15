/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.ui.banner.IGWCloningActionService;
import com.gas.domain.ui.banner.IImportActionsFactory;
import com.gas.domain.ui.banner.IMSAActionFactory;
import com.gas.domain.ui.banner.ITOPOCloningActionFactory;
import com.gas.domain.ui.banner.TableActions;
import com.gas.domain.ui.dynamicTable.DynamicTable;
import com.gas.domain.ui.dynamicTable.IDynamicPopup;
import com.gas.domain.ui.editor.as.gibson.GibsonAssemblyAction;
import com.gas.domain.ui.editor.as.ligate.actions.CircularizeAction;
import com.gas.domain.ui.editor.as.ligate.actions.LigateAction;
import com.gas.domain.ui.editor.as.ligate.actions.RENAnalysisAction;
import com.gas.tigr.core.actions.TigrAction;
import java.util.List;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class DynamicPopup extends JPopupMenu implements IDynamicPopup {
    
    static IImportActionsFactory importFactory = Lookup.getDefault().lookup(IImportActionsFactory.class);
    static ITOPOCloningActionFactory topoCloningFactory = Lookup.getDefault().lookup(ITOPOCloningActionFactory.class);
    static IGWCloningActionService gwCloningService = Lookup.getDefault().lookup(IGWCloningActionService.class);
    static IMSAActionFactory msaActionFactory = Lookup.getDefault().lookup(IMSAActionFactory.class);
    TableActions.SelectAllAction selectAll;    
    TableActions.UnselectAllAction unselectAll;
    Action msaAction;
    TigrAction tigrAction;
    RENAnalysisAction renAnalysisAction;
    CircularizeAction circularizeAction;
    LigateAction ligateAction;
    TableActions.RecycleAction recycleAction;
    TableActions.DelAction delAction;
    TableActions.CutAction cutAction;
    TableActions.CopyAction copyAction;
    TableActions.PasteAction pasteAction;
    TableActions.RestoreAction restoreAction;
    Action importFromFileAction;
    TableActions.FlipAction flipAction;
    
    Action topoCloningAction = topoCloningFactory.createTOPOCloningAction();
    Action createTAInsertAction = topoCloningFactory.createTAInsertAction();
    Action topoTACloningAction = topoCloningFactory.createTOPOTACloningAction();
    Action createDirTOPOInsertAction = topoCloningFactory.createDirectionalTOPOInsertAction();
    Action dirTOPOCloningAction = topoCloningFactory.createDirectionalTOPOCloningAction();
    
    Action annotateAttSiteAction = gwCloningService.createAnnotateAttSitesAction();
    Action addAttbSiteAction = gwCloningService.createAddAttBSitesAction();
    Action lrAction = gwCloningService.createLRAction();
    Action bpAction = gwCloningService.createBPAction();
    Action oneClickAction = gwCloningService.createOneClickAction();
    
    GibsonAssemblyAction gibsonAction;
    
    JMenu cloningMenu;
    JMenu gatewayMenu;
    JMenu topoMenu;
    JMenu gibsonMenu;
    DynamicTable table;
    LicenseService licenseService = LicenseService.getInstance();

    DynamicPopup(DynamicTable dynamicTable) {
        this.table = dynamicTable;

        // multiple sequence alignment
        msaAction = msaActionFactory.createMSAAction();
        add(msaAction);
        tigrAction = new TigrAction();
        add(tigrAction);
        addSeparator();

        circularizeAction = new CircularizeAction();
        add(circularizeAction);
        
        addSeparator();
        
        // ligate action
        cloningMenu = new JMenu("Cloning");
        cloningMenu.setIcon(ImageHelper.createImageIcon(ImageNames.CLONING_16));
        populateCloningMenu(cloningMenu);
        add(cloningMenu);

        topoMenu = new JMenu("TOPO");
        topoMenu.setIcon(ImageHelper.createImageIcon(ImageNames.TOPO_16));
        add(topoMenu);
        populateTOPOMenu(topoMenu);

        gatewayMenu = new JMenu("Gateway");
        gatewayMenu.setIcon(ImageHelper.createImageIcon(ImageNames.GATEWAY_16));
        add(gatewayMenu);
        populateGatewayMenu(gatewayMenu);
  
        gibsonAction = new GibsonAssemblyAction();
        add(gibsonAction);

        addSeparator();
        
        
        
        addSeparator();

        // edit name/description  

        // select all
        selectAll = new TableActions.SelectAllAction(dynamicTable);
        add(selectAll);
        // unselect all
        unselectAll = new TableActions.UnselectAllAction(dynamicTable);
        add(unselectAll);

        // Recycle action
        recycleAction = new TableActions.RecycleAction(dynamicTable);
        add(recycleAction);

        // delete action
        delAction = new TableActions.DelAction(dynamicTable);
        add(delAction);

        // cut
        cutAction = new TableActions.CutAction();
        add(cutAction);

        // copy 
        copyAction = new TableActions.CopyAction();
        add(copyAction);

        // paste
        pasteAction = new TableActions.PasteAction();
        add(pasteAction);

        // restore
        restoreAction = new TableActions.RestoreAction(dynamicTable);
        add(restoreAction);
        
        addSeparator();
        
        importFromFileAction = importFactory.createImportFromFileAction();
        add(importFromFileAction);
        
        addSeparator();

        // flip
        flipAction = new TableActions.FlipAction(dynamicTable);
        add(flipAction);
    }
    
    private void populateCloningMenu(JMenu menu){
        renAnalysisAction = new RENAnalysisAction();
        menu.add(renAnalysisAction);
        ligateAction = new LigateAction();
        menu.add(ligateAction);
    }

    private void populateTOPOMenu(JMenu menu) {
        menu.add(topoCloningAction);
        menu.addSeparator();
        menu.add(createTAInsertAction);
        menu.add(topoTACloningAction);
        menu.addSeparator();
        menu.add(createDirTOPOInsertAction);
        menu.add(dirTOPOCloningAction);
    }

    private void populateGatewayMenu(JMenu menu) {
        menu.add(annotateAttSiteAction);
        menu.add(addAttbSiteAction);
        menu.add(bpAction);
        menu.add(lrAction);
        menu.add(oneClickAction);
    }

    @Override
    public void setFolder(Folder folder) {
        int linearNucleotideCount = 0;
        int aminoAcidCount = 0;
        int nucleotideCount = 0;
        int msaDNACount = 0;
        int msaProteinCount = 0;
        List checkedObjs = table.getCheckedObjects();
        List<Kromatogram> checkedKromatograms = table.getCheckedObjects(Kromatogram.class);
        List<MSA> checkedMSAs = table.getCheckedObjects(MSA.class);
        for (MSA msa : checkedMSAs) {
            if (msa.isDNA()) {
                msaDNACount++;
            } else {
                msaProteinCount++;
            }
        }
        List<AnnotatedSeq> checkedASs = table.getCheckedObjects(AnnotatedSeq.class);
        for (AnnotatedSeq obj : checkedASs) {
            AnnotatedSeq as = (AnnotatedSeq) obj;
            if (AsHelper.isNucleotide(as)) {
                nucleotideCount++;
                if (!as.isCircular()) {
                    linearNucleotideCount++;
                }
            } else if (AsHelper.isAminoAcid(as)) {
                aminoAcidCount++;
            }
        }
        boolean isRecycleBin = folder.isRecycleBin();
        boolean isNCBIFolder = folder.isNCBIFolder();

        updateCloningEnablement(isNCBIFolder, isRecycleBin, licenseService.isBasicMode(), nucleotideCount);
        
        copyAction.setEnabled(checkedObjs.size() > 0 && !isRecycleBin);
        cutAction.setEnabled(checkedObjs.size() > 0);

        delAction.setEnabled(checkedObjs.size() > 0);
        delAction.setFolder(folder);
        flipAction.setEnabled(!isNCBIFolder && !isRecycleBin && nucleotideCount == 1);
        renAnalysisAction.setEnabled(RENAnalysisAction.getEnablement());
        ligateAction.setEnabled(linearNucleotideCount > 0 && !isRecycleBin && !isNCBIFolder);
        tigrAction.setEnabled(checkedKromatograms.size() > 1 && !isRecycleBin && !isNCBIFolder);
        msaAction.setEnabled(isMSAEnable(msaDNACount, msaProteinCount, aminoAcidCount, nucleotideCount, isRecycleBin, isNCBIFolder));
        pasteAction.setEnabled(!isNCBIFolder && !isRecycleBin);
        recycleAction.setEnabled(checkedObjs.size() > 0 && !isRecycleBin && !isNCBIFolder);
        recycleAction.setFolder(folder);
        restoreAction.setEnabled(isRecycleBin);
        importFromFileAction.setEnabled(!isNCBIFolder && !isRecycleBin);
    }
    
    private void updateCloningEnablement(boolean isNCBIFolder, boolean isRecycleBin, boolean isBasicMode, int nucleotideCount){
        topoMenu.setEnabled(!isNCBIFolder && !isRecycleBin && !isBasicMode);
        createTAInsertAction.setEnabled(nucleotideCount > 0);
        createDirTOPOInsertAction.setEnabled(nucleotideCount > 0);
        gatewayMenu.setEnabled(!isNCBIFolder && !isRecycleBin && !isBasicMode && nucleotideCount > 0);
        gibsonAction.setEnabled(!isNCBIFolder && !isRecycleBin && !isBasicMode && nucleotideCount > 1);
        addAttbSiteAction.setEnabled(gwCloningService.getAddAttBSitesActionEnablement());
        importFromFileAction.setEnabled(!isNCBIFolder && !isRecycleBin);
    }

    private boolean isMSAEnable(int msaDNACount, int msaProteinCount, int aminoAcidCount, int nucleotideCount, boolean isRecycleBin, boolean isNCBIFolder) {
        boolean ret;
        if (msaProteinCount > 0 || msaDNACount > 0) {
            if (msaProteinCount > 0) {
                if (msaProteinCount == 1) {
                    ret = msaDNACount == 0 && nucleotideCount == 0 && aminoAcidCount > 0;
                } else { // > 1
                    ret = msaDNACount == 0 && nucleotideCount == 0 && aminoAcidCount == 0;
                }
            } else {
                if (msaDNACount == 1) {
                    ret = msaProteinCount == 0 && aminoAcidCount == 0 && nucleotideCount > 0;
                } else { // > 1
                    ret = msaProteinCount == 0 && aminoAcidCount == 0 && nucleotideCount == 0;
                }
            }
        } else {
            ret = (aminoAcidCount > 1 && nucleotideCount == 0)
                    || (aminoAcidCount == 0 && nucleotideCount > 1);
        }
        ret = ret && !isRecycleBin;
        ret = ret && !isNCBIFolder;
        ret = ret && !licenseService.isBasicMode();
        return ret;
    }
}
