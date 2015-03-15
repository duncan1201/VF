/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.gibson;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.primer3.IPrimer3Service;
import com.gas.domain.core.primer3.Oligo;
import com.gas.domain.core.primer3.OligoElement;
import com.gas.domain.core.primer3.OverlapPrimer;
import com.gas.domain.core.primer3.P3Output;
import com.gas.domain.core.primer3.UserInput;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class GibsonAssemblyListeners {

    static class BtnsListener extends AbstractAction {

        static Logger logger = Logger.getLogger(BtnsListener.class.getName());
        IGibsonService gibsonSvc = Lookup.getDefault().lookup(IGibsonService.class);
        IPrimer3Service primer3Svc = Lookup.getDefault().lookup(IPrimer3Service.class);
        IBasicTmService basicTmService = Lookup.getDefault().lookup(IBasicTmService.class);

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();
            GibsonAssemblyPanel panel = UIUtil.getParent(src, GibsonAssemblyPanel.class);
            String cmd = e.getActionCommand();
            if (cmd.equals("generate")) {
                handleGenerate(panel);
            } else if (cmd.equals("moveLeft")) {
                handleMove(panel, SwingConstants.LEFT);
            } else if (cmd.equals("moveRight")) {
                handleMove(panel, SwingConstants.RIGHT);
            }
        }

        private void handleMove(GibsonAssemblyPanel panel, int dir) {
            AnnotatedSeq as = panel.primersPreview.getSelectedAs();
            if (dir == SwingConstants.LEFT) {
                panel.asList.moveLeft(as);
                panel.primersPreview.moveLeft(as);
            } else if (dir == SwingConstants.RIGHT) {
                panel.asList.moveRight(as);
                panel.primersPreview.moveRight(as);
            }
            panel.primersPreview.repaint();
        }

        private void handleGenerate(final GibsonAssemblyPanel panel) {

            final SwingWorker worker = new SwingWorker() {
                List<OverlapPrimer> overlapPrimers = new ArrayList<OverlapPrimer>();

                @Override
                protected Object doInBackground() throws Exception {
                    panel.progressLabel.setIcon(ImageHelper.createImageIcon(ImageNames.CIRCLE_BALL_16));
                    panel.generateBtn.setEnabled(false);
                    Float minTmOverlap = panel.getMinTmOverlap();
                    int minLengthOverlap = panel.getMinLengthOverlap();
                    final float minTmAnnealing = panel.getMinTmAnnealing();
                    final float maxTmAnnealing = panel.getMaxTmAnnealing();
                    String finalConst = gibsonSvc.getFinalConstruct(panel.getAsList());
                    List<UserInput> userInputs = panel.getUserInputs();
                    for (UserInput userInput : userInputs) {
                        userInput.setSequenceTemplate(finalConst);

                        Oligo oligo = gibsonSvc.generateAnnealingPrimerPair(userInput, minTmAnnealing, maxTmAnnealing);

                        OligoElement oeLeft = oligo.getLeft();
                        OligoElement oeRight = oligo.getRight();
                        Integer startLeft = userInput.getInt("SEQUENCE_FORCE_LEFT_START");
                        Integer startRight = userInput.getInt("SEQUENCE_FORCE_RIGHT_START");

                        OverlapSeqList candidates = panel.getOverlapSeqList(startLeft);
                        OverlapSeqList results = basicTmService.pickPrimers(minTmOverlap, minLengthOverlap, candidates);
                        Collections.sort(results, new OverlapSeq.LengthComparator());
                        OverlapSeq overlapSeqLeft = results.get(0);

                        OverlapPrimer oPrimerLeft = new OverlapPrimer();
                        oPrimerLeft.setName(userInput.getData().get("SEQUENCE_ID"));
                        oPrimerLeft.setFlappyEnd(new OverlapPrimer.FlappyEnd(overlapSeqLeft.length() / 2));
                        oPrimerLeft.setOligoElement(oeLeft);
                        oPrimerLeft.setOverlapLength(overlapSeqLeft.length());
                        String seq = StrUtil.sub(finalConst, oPrimerLeft.calculateStart(finalConst.length()), oPrimerLeft.calculateEnd(finalConst.length()));
                        oPrimerLeft.setSeq(seq);
                        overlapPrimers.add(oPrimerLeft);

                        candidates = panel.getOverlapSeqList(startRight);
                        results = basicTmService.pickPrimers(minTmOverlap, minLengthOverlap, candidates);
                        Collections.sort(results, new OverlapSeq.LengthComparator());
                        OverlapSeq overlapSeqRight = results.get(0);

                        OverlapPrimer oPrimerRight = new OverlapPrimer();
                        oPrimerRight.setName(userInput.getData().get("SEQUENCE_ID"));
                        oPrimerRight.setFlappyEnd(new OverlapPrimer.FlappyEnd(overlapSeqRight.length() / 2));
                        oPrimerRight.setOligoElement(oeRight);
                        oPrimerRight.setOverlapLength(overlapSeqRight.length());
                        seq = StrUtil.sub(finalConst, oPrimerRight.calculateStart(finalConst.length()), oPrimerRight.calculateEnd(finalConst.length()));
                        seq = BioUtil.reverseComplement(seq);
                        oPrimerRight.setSeq(seq);
                        
                        overlapPrimers.add(oPrimerRight);
                        System.out.print("");
                    }
                    return null;
                }

                @Override
                protected void done() {
                    panel.progressLabel.setIcon(ImageHelper.createImageIcon(ImageNames.EMPTY_16));
                    panel.generateBtn.setEnabled(true);
                    panel.setOverlapPrimers(overlapPrimers);
                    panel.primersPreview.clearSelection();
                    panel.validateInput();
                }
            };
            worker.execute();
        }
    }

    static class SpinnersListener implements ChangeListener {

        GibsonAssemblyPanel panel;

        SpinnersListener(GibsonAssemblyPanel panel) {
            this.panel = panel;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            panel.validateInput();
        }
    }
}
