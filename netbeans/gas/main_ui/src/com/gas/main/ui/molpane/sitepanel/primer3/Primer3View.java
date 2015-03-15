/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.main.ui.molpane.sitepanel.primer3.task.TaskPanel;
import com.gas.common.ui.IReclaimable;
import com.gas.common.ui.accordian2.OutlookPane;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.main.ui.molpane.MolPane;
import com.gas.domain.core.primer3.UserInput;
import java.awt.BorderLayout;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author dq
 */
class Primer3View extends JPanel implements IReclaimable {

    private WeakReference<TaskPanel> taskPanelRef;
    private RegionsProductSizeView regionsProductSizeView;
    private PrimerPickingView primerPickingView;
    private InternalOligoView internalOligoView;

    Primer3View() {
        setLayout(new BorderLayout());

        OutlookPane pane = new OutlookPane();
        pane.setContentBgColor(CNST.BG);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setSelectedColor(pane.getColor());

        TaskPanel taskPanel = new TaskPanel();
        taskPanelRef = new WeakReference<TaskPanel>(taskPanel);
        pane.addBar("Task", ImageHelper.createImageIcon(ImageNames.TASK_GREEN_16), taskPanel);

        regionsProductSizeView = new RegionsProductSizeView();
        pane.addBar("Regions & Product Size", ImageHelper.createImageIcon(ImageNames.REGION_TARGET_16), regionsProductSizeView);

        primerPickingView = new PrimerPickingView();
        pane.addBar("Primer Picking Conditions", ImageHelper.createImageIcon(ImageNames.PRIMER_SINGLE_16), primerPickingView);

        internalOligoView = new InternalOligoView();
        pane.addBar("DNA Probe Picking Conditions", ImageHelper.createImageIcon(ImageNames.PROBE_16), internalOligoView);

        add(pane, BorderLayout.CENTER);
        pane.show("Task");
    }

    @Override
    public void cleanup() {
        regionsProductSizeView = null;
        primerPickingView = null;
    }

    void populateUI(UserInput userInput) {
        MolPane molPane = UIUtil.getParent(this, MolPane.class);
        if (molPane == null) {
            return;
        }
        AnnotatedSeq as = molPane.getAs();

        taskPanelRef.get().populateUI(userInput);
        regionsProductSizeView.populateUI(userInput, as);
        primerPickingView.populateUI(userInput);
        internalOligoView.populateUI(userInput);
    }

    public List<String> validateInput(AnnotatedSeq as) {
        List<String> ret = new ArrayList<String>();
        ret.addAll(taskPanelRef.get().validateInput(as));
        if(!ret.isEmpty()){
            return ret;
        }
        ret.addAll(regionsProductSizeView.validateInput());
        MolPane molPane = UIUtil.getParent(this, MolPane.class);
        int asLength = molPane.getAs().getLength();
        Integer min = (Integer)regionsProductSizeView.PRIMER_PRODUCT_SIZE_RANGE_MIN.getValue();
        if(asLength < min){
            ret.add("Minimum product size too large");
        }
        if(!ret.isEmpty()){
            return ret;
        }        
        ret.addAll(primerPickingView.validateInput());
        if(!ret.isEmpty()){
            return ret;
        }        
        ret.addAll(internalOligoView.validateInput());
        if(!ret.isEmpty()){
            return ret;
        }        
        return ret;
    }

    void updateUserInputFromUI() {
        Primer3Panel primer3Panel = UIUtil.getParent(this, Primer3Panel.class);
        MolPane molPane = UIUtil.getParent(this, MolPane.class);
        AnnotatedSeq as = molPane.getAs();
        UserInput userInput = primer3Panel.getP3output().getUserInput();
        
        taskPanelRef.get().updateUserInputFromUI(userInput, as);
        regionsProductSizeView.updateUserInputFromUI(userInput);
        primerPickingView.updateUserInputFromUI(userInput);
        internalOligoView.updateUserInputFromUI(userInput);
    }
    
    TaskPanel getTaskPanel(){
        return taskPanelRef.get();
    }

    PrimerPickingView getPrimerPickingView() {
        return primerPickingView;
    }

    InternalOligoView getInternalOligoView() {
        return internalOligoView;
    }
}
