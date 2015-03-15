/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.ctrl.param;

import com.gas.clustalw.core.service.api.IClustalwService;
import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.common.ui.util.FileHelper;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.msa.service.api.IMSAService;
import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.msa.clustalw.ClustalwParam;
import com.gas.domain.core.msa.clustalw.GeneralParam;
import com.gas.domain.core.msa.muscle.MuscleParam;
import com.gas.domain.core.msa.vfmsa.IVfMsaService;
import com.gas.domain.core.msa.vfmsa.VfMsaParam;
import com.gas.domain.ui.editor.msa.api.IMSAEditor;
import com.gas.muscle.service.api.IMuscleService;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;
import javax.swing.JButton;
import org.openide.util.Lookup;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
class ParamsPanelListeners {

    static class PlayListener implements ActionListener {

        IMSAService msaService = Lookup.getDefault().lookup(IMSAService.class);
        IMuscleService muscleService = Lookup.getDefault().lookup(IMuscleService.class);
        IClustalwService clustalwService = Lookup.getDefault().lookup(IClustalwService.class);
        IVfMsaService vfMsaService = Lookup.getDefault().lookup(IVfMsaService.class);

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();
            final ParamsPanel paramsPanel = UIUtil.getParent(src, ParamsPanel.class);
            final IMSAEditor editor = UIUtil.getParent(src, IMSAEditor.class);
            final MSA msa = editor.getMsa();
            Frame frame = WindowManager.getDefault().getMainWindow();
            ProgressHelper.showProgressDialogAndRun(frame, "Running VectorFriends Aligner...W", new ProgRunnable() {
                MSA msaNew;

                @Override
                public void run(ProgressHandle handle) {
                    handle.setIndeterminate(true);
                    handle.progress("Preparing Data...");
                    Fasta fasta = new Fasta(msa.getEntriesMapCopy()).removeGaps();
                    File fileIn = FileHelper.toFile(fasta.toString());
                    if (paramsPanel.muscleUI != null) {
                        MuscleParam muscleParam = paramsPanel.muscleUI.getMuscleParam();                        
                        muscleParam.setIn(fileIn);
                        handle.progress("Running Muscle...");
                        msaNew = muscleService.align(muscleParam);

                    } else if (paramsPanel.clustalwUI != null) {
                        ClustalwParam clusalwParam = paramsPanel.clustalwUI.getClustalwParam();
                        clusalwParam.getDataParams().setInfile(fileIn);
                        boolean isDNA = fasta.isDNAByGuess();
                        clusalwParam.getGeneralParam().setType(isDNA? GeneralParam.TYPE.DNA: GeneralParam.TYPE.PROTEIN);
                        handle.progress("Running ClustalW...");
                        msaNew = clustalwService.msa(clusalwParam);
                    }else if(paramsPanel.vfMsaUI != null){
                        handle.progress("Running VectorFriends Aligner...");
                        VfMsaParam vfMsaParam = paramsPanel.vfMsaUI.getVfMsaParam();
                        Map<String, String> entries = msa.getEntriesMapCopy();
                        msaNew = vfMsaService.getMSA(entries, vfMsaParam, !msa.isDNA());
                        System.out.println();                        
                    }else{
                        throw new IllegalArgumentException();
                    }
                    handle.progress("Finishing...");
                }

                @Override
                public void done(ProgressHandle handle) {
                    // update current one
                    msa.setEntries(msaNew.getEntries());
                    msaService.createConsensus(msa);
                    msaService.createQualityScores(msa);
                    editor.refreshUI();
                    editor.setCanSave();
                }
            }, "Multiple Sequence Alignment");

        }
    }
}
