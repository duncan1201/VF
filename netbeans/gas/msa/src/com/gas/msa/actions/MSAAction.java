/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.actions;

import com.gas.clustalw.core.service.api.IClustalwService;
import com.gas.domain.core.msa.clustalw.ClustalwParam;
import com.gas.clustalw.core.ui.msa.ClustalWUI;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.core.StringSet;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.common.ui.util.FileHelper;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.database.core.msa.service.api.IMSAService;
import com.gas.database.core.msa.service.api.IMSAUIService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqList;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.fasta.Fasta;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.msa.clustalw.GeneralParam;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.msa.MSAList;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.msa.MSAPanel;
import com.gas.msa.ui.MSAEditor;
import com.gas.muscle.service.api.IMuscleService;
import com.gas.domain.core.msa.muscle.MuscleParam;
import com.gas.domain.core.msa.vfmsa.IVfMsaService;
import com.gas.domain.core.msa.vfmsa.IVfMsaUI;
import com.gas.domain.core.msa.vfmsa.VfMsaParam;
import com.gas.muscle.ui.MuscleUI;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.AbstractAction;
import org.apache.commons.io.FileUtils;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IMSAUIService.class)
public class MSAAction extends AbstractAction implements IMSAUIService {

    private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
    private IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
    private IVfMsaService vfMsaService = Lookup.getDefault().lookup(IVfMsaService.class);
    private IMSAService msaService = Lookup.getDefault().lookup(IMSAService.class);
    private IClustalwService clustalService = Lookup.getDefault().lookup(IClustalwService.class);
    private IMuscleService muscleService = Lookup.getDefault().lookup(IMuscleService.class);
    //private IMafftService mafftService = Lookup.getDefault().lookup(IMafftService.class);

    public MSAAction() {
        super("Multiple Sequence Alignment...", ImageHelper.createImageIcon(ImageNames.ATG_16));
    }

    /**
     * 1) Make sure the data are of the same type(all proteins or all
     * nucleotides) 2) Make sure at least two sequences are selected
     */
    @Override
    public boolean validate(List<AnnotatedSeq> input, List<MSA> profiles) {
        boolean ret = true;
        String title = "Can not perform alignment";
        String msg = null;
        if (profiles == null || profiles.isEmpty()) {
            if (input == null || input.size() < 2) {
                msg = "Please select at least two sequences";
                ret = false;
            } else {
                boolean isNucleotide = input.get(0).isNucleotide();
                for (AnnotatedSeq as : input) {
                    if (as.isNucleotide() != isNucleotide) {
                        msg = "Please select molecules of the same type";
                        ret = false;
                        break;
                    }
                }
            }
        } else {
            if (profiles.size() > 2) {
                msg = "Please select no more than two alignments for profile alignment";
                ret = false;
            } else if (profiles.size() == 2) {
                if (input.size() > 0) {
                    msg = String.format("<html>Two aligments and %d sequence(s) selected. <br/><br/>Please unselect one alignment or the sequences for profile alignment</html>", input.size());
                    ret = false;
                } else {
                    MSAList msaList = msaService.getFull(profiles);
                    ret = checkDuplicateSeqs(msaList, new AnnotatedSeqList(input));
                }
            } else if (profiles.size() == 1) {
                if (input.size() == 0) {
                    msg = "<html>Only one alignment is selected. <br/><br/>Please select another alignment or sequences for profile alignment</html>";
                    ret = false;
                } else {
                    MSAList msaList = msaService.getFull(profiles);
                    ret = checkDuplicateSeqs(msaList, new AnnotatedSeqList(input));
                }
            }
        }
        if (!ret && msg != null && !msg.isEmpty()) {
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle(title);
            DialogDisplayer.getDefault().notify(m);
        }
        return ret;
    }

    /**
     * @param data: could
     */
    @Override
    public void openDialog(final List<AnnotatedSeq> input, final List<MSA> profiles) {
        boolean ok = validate(input, profiles);
        if (!ok) {
            return;
        }
        ClustalwParam clustalParam = new ClustalwParam();
        MuscleParam muscleParam = new MuscleParam();

        MSAPanel msaPanel = null;
        DialogDescriptor dialogDescriptor = null;

        if (profiles.isEmpty()) {
            msaPanel = new MSAPanel(null, null);
            dialogDescriptor = new DialogDescriptor(msaPanel, "");
            dialogDescriptor.setTitle(String.format("Aligning %d sequences", input.size()));
        } else {
            if (profiles.size() == 1) {
                msaPanel = new MSAPanel(profiles.get(0).getName(), null);
                dialogDescriptor = new DialogDescriptor(msaPanel, "");
                dialogDescriptor.setTitle(String.format("Aligning %d sequence%s to an existing alignment", input.size(), input.size() > 1 ? "s" : ""));
            } else if (profiles.size() == 2) {
                msaPanel = new MSAPanel(profiles.get(0).getName(), profiles.get(1).getName());
                dialogDescriptor = new DialogDescriptor(msaPanel, "Aligning two alignments");
            }
        }
        // set data type
        boolean isNucleotide;
        if (!input.isEmpty()) {
            isNucleotide = input.get(0).isNucleotide();
        } else {
            isNucleotide = profiles.get(0).isDNA();
        }
        clustalParam.getGeneralParam().setType(isNucleotide ? GeneralParam.TYPE.DNA : GeneralParam.TYPE.PROTEIN);

        if (msaPanel.getVfMsaUI() != null) {
            msaPanel.getVfMsaUI().setAminoAcids(!isNucleotide);
        }
        msaPanel.getClustalWUI().setMsaParam(clustalParam);
        msaPanel.getMuscleUI().setMuscleParam(muscleParam);

        Integer answer = (Integer) DialogDisplayer.getDefault().notify(dialogDescriptor);
        if (answer.equals(DialogDescriptor.OK_OPTION)) {

            final Component selected = msaPanel.getSelected();
            final String profile1 = msaPanel.getProfile1();
            final String profile2 = msaPanel.getProfile2();
            Frame mainWindow = WindowManager.getDefault().getMainWindow();
            ProgressHelper.showProgressDialogAndRun(mainWindow, "Running VectorFriends aligner...A", new ProgRunnable() {
                List<AnnotatedSeq> inputFull;
                MSAList profilesFull;
                MSA msa;

                @Override
                public void run(ProgressHandle handle) {
                    checkExecutable(selected);
                    handle.progress("Preparing data...");
                    handle.setIndeterminate(true);
                    File inFile = null;
                    File fileProfile1 = null;
                    File fileProfile2 = null;
                    if (!input.isEmpty()) {
                        inputFull = asService.getFull(input);
                        Fasta fasta = AsHelper.toFasta(inputFull);
                        inFile = FileHelper.toFile(fasta.toString());
                    }
                    if (!profiles.isEmpty()) {
                        profilesFull = msaService.getFull(profiles);
                        boolean valid = checkDuplicateSeqs(profilesFull, new AnnotatedSeqList(input));
                        if (!valid) {
                            return;
                        }

                        if (profile1 != null) {
                            Fasta fastaProfile1 = profilesFull.get(profile1).toFasta();
                            fileProfile1 = FileHelper.toFile(fastaProfile1.toString());
                        }
                        if (profile2 != null) {
                            Fasta fastaProfile2 = profilesFull.get(profile2).toFasta();
                            fileProfile2 = FileHelper.toFile(fastaProfile2.toString());
                        }
                    } else {
                        // check identical sequences
                        boolean identical = areIdenticalSeqs(new AnnotatedSeqList(inputFull));
                        if (identical) {
                            DialogDescriptor.Message m = new DialogDescriptor.Message(String.format("The selected %d sequences are identical", inputFull.size()), DialogDescriptor.INFORMATION_MESSAGE);
                            m.setTitle("Identical Sequences");
                            DialogDisplayer.getDefault().notify(m);
                            return;
                        }
                    }
                    if (selected instanceof ClustalWUI) {

                        ClustalWUI c = (ClustalWUI) selected;
                        ClustalwParam clustalwParam = c.getClustalwParam();

                        if (!profiles.isEmpty()) {
                            clustalwParam.getDataParams().setProfile1(fileProfile1);
                            if (fileProfile2 != null) {
                                clustalwParam.getDataParams().setProfile2(fileProfile2);
                            } else {
                                clustalwParam.getDataParams().setProfile2(inFile);
                            }
                        } else {
                            clustalwParam.getDataParams().setInfile(inFile);
                        }
                        handle.progress("Running ClustalW...");

                        msa = clustalService.msa(clustalwParam);
                        msaService.createConsensus(msa);
                        msaService.createQualityScores(msa);
                    } else if (selected instanceof MuscleUI) {
                        MuscleUI muscleUI = (MuscleUI) selected;
                        MuscleParam muscleUIParams = muscleUI.getMuscleParam();
                        if (!profiles.isEmpty()) {
                            muscleUIParams.setIn1(fileProfile1);
                            if (fileProfile2 != null) {
                                muscleUIParams.setIn2(fileProfile2);
                            } else {
                                muscleUIParams.setIn2(inFile);
                            }
                        } else if (inFile != null) {
                            muscleUIParams.setIn(inFile);
                        }
                        handle.progress("Running Muscle...");

                        msa = muscleService.align(muscleUIParams);
                        msaService.createConsensus(msa);
                        msaService.createQualityScores(msa);
                    } else if (selected instanceof IVfMsaUI) {
                        handle.progress("Running VectorFriends aligner...");
                        IVfMsaUI vfMsaParam = (IVfMsaUI) selected;
                        VfMsaParam param = vfMsaParam.getVfMsaParam();
                        msa = vfMsaService.getMSA(inputFull, param);
                        if (msa.isDnaByGuess()) {
                            msa.setType("DNA");
                        } else {
                            msa.setType("Protein");
                        }
                        msaService.createConsensus(msa);
                        msaService.createQualityScores(msa);
                    } else {
                        throw new UnsupportedOperationException();
                    }
                    handle.progress("Finishing...");
                }

                @Override
                public void done(ProgressHandle handle) {
                    if (msa == null) {
                        return;
                    }
                    Folder folder = BannerTC.getInstance().getFolderPanel().getFolder();
                    final String msaName = folder.getNewElementName("Alignment");
                    msa.setName(msaName);
                    msa.setDesc(String.format("Alignment of %d %s sequences", msa.getEntriesCount(), msa.isDNA() ? "nucleotide" : "protein"));
                    folder.addObject(msa);
                    folderService.merge(folder);

                    final Folder updatedFolder = folderService.loadWithRecursiveDataAndParentAndChildren(folder.getHibernateId());

                    msa = updatedFolder.getElement(msaName, MSA.class);
                    IMSAService serviceMSA = Lookup.getDefault().lookup(IMSAService.class);
                    msa = serviceMSA.getByHibernateIds(msa.getHibernateId()).get(0);

                    // must update the top component first, then open msa editor
                    BannerTC.getInstance().updateFolder(updatedFolder);
                    ExplorerTC.getInstance().updateFolder(updatedFolder);

                    MSAEditor editor = new MSAEditor();
                    editor.setMsa(msa);
                    editor.open();
                    editor.requestActive();
                }
            }, "Multiple Sequence Alignment");
        }
    }

    private void checkExecutable(Component selected) {
        if (selected instanceof ClustalWUI) {
            if (!clustalService.isExecutablePresent()) {
                File dir = clustalService.getExecutableDirectory();
                try {
                    FileUtils.copyFileToDirectory(new File("//Users//Dunqiang//Documents//clustalw2"), dir);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        } else if (selected instanceof MuscleUI) {
        }
    }

    /**
     *
     */
    private boolean areIdenticalSeqs(AnnotatedSeqList asList) {
        return asList.areBasesIdentical();
    }

    /**
     * @return true if pass the validation
     */
    private boolean checkDuplicateSeqs(MSAList msaList, AnnotatedSeqList asList) {
        if (asList == null || asList.isEmpty()) {
            StringSet dupNames = msaList.getDuplicateEntryNames();
            if (!dupNames.isEmpty()) {
                final String msg = String.format("<html>The following sequence%s %s duplicate in both alignments:<br/>%s<br/>Please rename or remove the duplicate sequence%s</html>", dupNames.size() > 1 ? "s" : "", dupNames.size() > 1 ? "are" : "is", dupNames.toHtmlList(), dupNames.size() > 1 ? "s" : "");
                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle("Can not perform profile alignment");
                DialogDisplayer.getDefault().notify(m);
                return false;
            }
        } else {
            StringList asNames = asList.getNames();
            StringList entryNames = msaList.getEntryNames();
            StringList dupNames = asNames.intersect(entryNames);
            if (!dupNames.isEmpty()) {
                final String msg = String.format("<html>The following sequence%s %s duplicate:<br/>%s<br/>Please rename or remove the duplicate sequence%s</html>", dupNames.size() > 1 ? "s" : "", dupNames.size() > 1 ? "are" : "is", dupNames.toHtmList(), dupNames.size() > 1 ? "s" : "");
                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle("Can not perform profile alignment");
                DialogDisplayer.getDefault().notify(m);
                return false;
            }
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<AnnotatedSeq> seqs = BannerTC.getInstance().getCheckedObjects(AnnotatedSeq.class);
        List<MSA> profiles = BannerTC.getInstance().getCheckedObjects(MSA.class);

        openDialog(seqs, profiles);

    }
}
