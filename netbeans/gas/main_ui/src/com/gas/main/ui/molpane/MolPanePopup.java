/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.primer3.IPrimer3Service;
import com.gas.domain.core.primer3.OligoElement;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.ui.banner.IGWCloningActionService;
import com.gas.domain.ui.editor.as.IDigestAction;
import com.gas.domain.ui.editor.as.IDigestActionFactory;
import com.gas.main.ui.actions.imports.ExportCurrentEditorAction;
import com.gas.main.ui.molpane.action.Convert2OligoAction;
import com.gas.main.ui.molpane.action.CopyBasesReverseToClipboardAction;
import com.gas.main.ui.molpane.action.CopyBasesToClipboardAction;
import com.gas.main.ui.molpane.action.DelAllPrimersAction;
import com.gas.main.ui.molpane.action.DelByTypeAction;
import com.gas.main.ui.molpane.action.DelCurFeatureAction;
import com.gas.main.ui.molpane.action.EditFeatureAction;
import com.gas.main.ui.molpane.action.EditPrimerAction;
import com.gas.main.ui.molpane.action.ExtractAction;
import com.gas.main.ui.molpane.action.NewFeatureAction;
import com.gas.main.ui.molpane.action.PCRAction;
import com.gas.main.ui.molpane.action.PasteBasesAction;
import com.gas.main.ui.molpane.extractaa.ExtractAAaction;
import java.lang.ref.WeakReference;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class MolPanePopup extends JPopupMenu {

    private WeakReference<MolPane> parentRef;
    ExportCurrentEditorAction exportAction;
    IDigestAction digestAction;
    RMap.EntryList selectedEntries;
    Feture selectedFeture;
    boolean oligo;
    PCRAction pcrAction;
    Convert2OligoAction convert2oligoAction;
    NewFeatureAction createFetureAction;
    EditFeatureAction editFetureAction;
    EditPrimerAction editPrimerAction;
    CopyBasesToClipboardAction copyBasesAction;
    CopyBasesReverseToClipboardAction copyBasesReverseAction;
    ExtractAction extractAction;

    public MolPanePopup(MolPane parent) {
        this.parentRef = new WeakReference<MolPane>(parent);

        exportAction = new ExportCurrentEditorAction("Export...", ImageHelper.createImageIcon(ImageNames.DOC_ARROW_UP_16));
        add(exportAction);
        copyBasesAction = new CopyBasesToClipboardAction(parent);
        add(copyBasesAction);
        copyBasesReverseAction = new CopyBasesReverseToClipboardAction(this.parentRef);
        add(copyBasesReverseAction);        
        PasteBasesAction pasteAction = new PasteBasesAction(parent.getCaretParent());
        add(pasteAction);
        
        extractAction = new ExtractAction();
        add(extractAction);

        addSeparator();

        // annotation
        JMenu menu = new JMenu("Annotation");
        menu.setIcon(ImageHelper.createImageIcon(ImageNames.FEATURE_16));
        populateAnnotationMenu(menu);
        add(menu);                
        addSeparator();
        
        //translation        
        add(new ExtractAAaction(this.parentRef.get()));
        addSeparator();
        // primer
        JMenu primerMenu = new JMenu("Primer Bind Options");
        primerMenu.setIcon(ImageHelper.createImageIcon(ImageNames.PRIMER_16));
        populatePrimerMenu(primerMenu);
        add(primerMenu);
        
        IDigestActionFactory factory = Lookup.getDefault().lookup(IDigestActionFactory.class);
        digestAction = factory.create();
        add((Action) digestAction);
        convert2oligoAction = new Convert2OligoAction(this.parentRef.get());
        add(convert2oligoAction);
        pcrAction = new PCRAction(this.parentRef.get());
        add(pcrAction);
    }

    public void updateEnablementBasedOnSelection() {
        LocList locList = parentRef.get().getSelections();

        copyBasesAction.setEnabled(!locList.isEmpty());
        copyBasesReverseAction.setEnabled(!locList.isEmpty());
        extractAction.setEnabled(!locList.isEmpty());

        JMenuItem delByType = UIUtil.getMenuItem(this, DelByTypeAction.class);
        
        // update digest action
        JMenuItem digestActionMenuItem = UIUtil.getMenuItem(this, IDigestAction.class);
        digestActionMenuItem.setEnabled(selectedEntries != null && !selectedEntries.isEmpty());
        digestAction.setAs(parentRef.get().getAs());
        digestAction.setSelectedEntries(selectedEntries);
        if (selectedEntries != null && !selectedEntries.isEmpty()) {
            digestActionMenuItem.setText(String.format("Digest using %s", selectedEntries.toString(false)));
            delByType.setText("Delete all restriction sites");
            DelByTypeAction action = (DelByTypeAction)delByType.getAction();
            action.setRestrictionSites(true);
        } else {
            digestActionMenuItem.setText("Digest(No site selected)");
        }

        //
        String displayName = null;
        String noStr = null;
        boolean primerBinding = false;
        boolean overhang = false;
        boolean orf = false;
        if (selectedFeture != null) {
            primerBinding = selectedFeture.getKey().equalsIgnoreCase(FetureKeyCnst.PRIMER_BINDING_SITE);
            if (primerBinding) {
                displayName = selectedFeture.getDisplayName();
                noStr = displayName.replaceAll("[a-zA-Z]", "").trim();
            }
            overhang = selectedFeture.getKey().equalsIgnoreCase(FetureKeyCnst.OVERHANG);
            orf = selectedFeture.getKey().equalsIgnoreCase(FetureKeyCnst.ORF);
        }
        JMenuItem delCurFeture = UIUtil.getMenuItem(this, DelCurFeatureAction.class);
        JMenuItem createFeture = UIUtil.getMenuItem(this, NewFeatureAction.class);
        JMenuItem editFeature = UIUtil.getMenuItem(this, EditFeatureAction.class);
        
        JMenuItem editPrimer = UIUtil.getMenuItem(this, EditPrimerAction.class);
        JMenuItem delAllPrimers = UIUtil.getMenuItem(this, DelAllPrimersAction.class);

        delCurFeture.setEnabled(selectedFeture != null && !primerBinding && !overhang && !orf);
        delByType.setEnabled((selectedFeture != null && !primerBinding) || (selectedEntries != null && !selectedEntries.isEmpty()));
        editFeature.setEnabled(selectedFeture != null && !primerBinding && !overhang && !orf);
        createFeture.setEnabled(editFeature.isEnabled());
        
        editPrimer.setEnabled(selectedFeture != null && primerBinding);
        delAllPrimers.setEnabled(selectedFeture != null && primerBinding);
        convert2oligoAction.setEnabled(selectedFeture != null && !oligo && selectedFeture.getKey().equals(FetureKeyCnst.PRIMER_BINDING_SITE));
        convert2oligoAction.setEnabled(!locList.isEmpty() && locList.get(0).width() <= IPrimer3Service.PRIMER_LENGTH_MAX);
        pcrAction.setEnabled(selectedFeture != null && !oligo && selectedFeture.getKey().equals(FetureKeyCnst.PRIMER_BINDING_SITE));

        if (selectedFeture != null) {
            DelCurFeatureAction delCurFetureAction = (DelCurFeatureAction) delCurFeture.getAction();
            delCurFetureAction.setFeture(selectedFeture);

            delByType.setText(String.format("Delete all %ss", primerBinding? "annotations": selectedFeture.getKey()));
            DelByTypeAction delFetureByTypeAction = (DelByTypeAction) delByType.getAction();
            delFetureByTypeAction.setFeatureType(selectedFeture.getKey());

            editFetureAction.setFeture(selectedFeture);

            if (noStr != null && displayName != null) {
                boolean isLeft = StrUtil.containsIgnoreCase(displayName, "forward");
                boolean isRight = StrUtil.containsIgnoreCase(displayName, "reverse");
                boolean isProbe = StrUtil.containsIgnoreCase(displayName, "probe");
                convert2oligoAction.setNo(Integer.parseInt(noStr) - 1);               
                if (isLeft) {
                    convert2oligoAction.setWhich("left");
                } else if (isRight) {
                    convert2oligoAction.setWhich("right");
                } else if (isProbe) {
                    convert2oligoAction.setWhich("internal");
                }
            }

            if (pcrAction.isEnabled()) {
                if (noStr != null && !noStr.isEmpty()) {
                    pcrAction.setNo(Integer.parseInt(noStr) - 1);
                }
            }
        }else{
            convert2oligoAction.setNo(null);
            convert2oligoAction.setWhich(null);
        }
    }
    
    private void populatePrimerMenu(JMenu menu){
        editPrimerAction = new EditPrimerAction(this.parentRef);
        menu.add(editPrimerAction);
        DelAllPrimersAction delAllPrimersAction = new DelAllPrimersAction(this.parentRef);        
        menu.add(delAllPrimersAction);
    }

    private void populateAnnotationMenu(JMenu menu) {
        IGWCloningActionService factory = Lookup.getDefault().lookup(IGWCloningActionService.class);
        menu.add(factory.createAnnotateAttSitesAction());
        menu.addSeparator();
        createFetureAction = new NewFeatureAction(this.parentRef);
        menu.add(createFetureAction);
        editFetureAction = new EditFeatureAction(this.parentRef);
        menu.add(editFetureAction);
        menu.add(new DelCurFeatureAction(this.parentRef));
        menu.add(new DelByTypeAction(this.parentRef));
    }

    public void setFolderElementNames(StringList names) {
        extractAction.setExistingNames(names);
    }

    public void setRen(RMap.EntryList selectedEntries) {
        this.selectedEntries = selectedEntries;
    }

    public void setOligo(boolean oligo) {
        this.oligo = oligo;
    }    

    public void setFeture(final Feture feture) {
        this.selectedFeture = feture;
        if(feture != null){
            updateMenuItemActions(feture);
        }
    }
    
    private void updateMenuItemActions(Feture feture){
        JMenuItem delCurFeture = UIUtil.getMenuItem(this, DelCurFeatureAction.class);
        JMenuItem delFetureByType = UIUtil.getMenuItem(this, DelByTypeAction.class);

        if (feture != null) {
            final String fetureKey = feture.getKey();
            
            DelCurFeatureAction delCurFetureAction = (DelCurFeatureAction) delCurFeture.getAction();
            delCurFetureAction.setFeture(feture);

            delFetureByType.setText(String.format("Delete all %ss", feture.getKey()));
            DelByTypeAction delFetureByTypeAction = (DelByTypeAction) delFetureByType.getAction();
            delFetureByTypeAction.setFeatureType(feture.getKey());

            editFetureAction.setFeture(feture);
            
            if(fetureKey.equals(FetureKeyCnst.PRIMER_BINDING_SITE)){
                editPrimerAction.setFeture(feture);
            }
        }    
    }
}
