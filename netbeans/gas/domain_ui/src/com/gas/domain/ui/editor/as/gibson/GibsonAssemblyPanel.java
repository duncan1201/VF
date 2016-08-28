/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor.as.gibson;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.RichSeparator;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.LocUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.common.ui.util.Unicodes;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqList;
import com.gas.domain.core.as.Overhang;
import com.gas.domain.core.primer3.GbOutput;
import com.gas.domain.core.primer3.IPrimer3Service;
import com.gas.domain.core.primer3.OverlapPrimer;
import com.gas.domain.core.primer3.UserInput;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class GibsonAssemblyPanel extends JPanel {

    AnnotatedSeqList asList;
    UserInput userInput;
    JSpinner minTmOverlapSpinner;
    JSpinner minLengthOverlapSpinner;
    JSpinner maxLengthOverlapSpinner;
    JSpinner minTmAnnealingSpinner;
    JSpinner maxTmAnnealingSpinner;
    JButton generateBtn;
    JButton moveLeftBtn;
    JButton moveRightBtn;
    JLabel progressLabel;
    JSpinner primerSaltMonoSpinner;
    JSpinner primerSaltDivaSpinner;
    JSpinner primerDnaConcSpinner;
    JSpinner primerDntpSpinner;
    PrimersPreview primersPreview;
    JPanel panelCenter;
    PrimerDetailsPanel primerDetailsPanel;
    PrimersTable primersTable;
    DialogDescriptor dialogDescriptor;
    NotificationLineSupport notificationLineSupport;
    
    final static String PRIMERS_DETAILS_PANEL = "primersDetailsPanel";
    final static String SETTINGS_PANEL = "settingsPanel";

    public GibsonAssemblyPanel(AnnotatedSeqList asList, UserInput userInput) {
        this.asList = asList;
        this.userInput = userInput;
        initComponents();
        hookupListeners();
    }
    
    private JPanel createCenterPanel(){
        CardLayout layout = new CardLayout();
        JPanel ret = new JPanel(layout);
        JPanel _primerDetailsPanel = createPrimerDetailsPanel();
        JPanel settingsPanel = createSettingsPanel();
        ret.add(_primerDetailsPanel, PRIMERS_DETAILS_PANEL);
        ret.add(settingsPanel, SETTINGS_PANEL);
        
        layout.show(ret, SETTINGS_PANEL);
        return ret;
    }

    private void initComponents() {
        UIUtil.setDefaultBorder(this);
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel panel = createFragsPanel();
        add(panel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 0.5;
        panelCenter = createCenterPanel();
        add(panelCenter, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 0.5;
        panel = createPrimersPanel();
        add(panel, c);
    }

    private JPanel createPrimersPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        ret.setBorder(BorderFactory.createTitledBorder( //
                new LineBorder(Color.GRAY), //
                "Primers", //
                TitledBorder.LEFT, //
                TitledBorder.TOP, //
                UIManager.getFont("TitledBorder.font")));
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        primersTable = new PrimersTable();
        ret.add(new JScrollPane(primersTable), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;        
        int test2 = primersTable.getPreferredSize().width;     
        Component filler = Box.createRigidArea(new Dimension(test2, 1));
        ret.add(filler, c);
        
        return ret;
    }

    void setOverlapPrimers(List<OverlapPrimer> overlapPrimers) {
        primersPreview.repaint();
        primersTable.setOverlapPrimers(overlapPrimers);
    }
    
    List<OverlapPrimer> getOverlapPrimers(){
        return primersTable.getOverlapPrimers();
    }

    GbOutput getGbOutput() {
        GbOutput ret = new GbOutput();
        ret.setOverlapPrimers(new HashSet<OverlapPrimer>(primersTable.getOverlapPrimers()));
        return ret;
    }

    private void hookupListeners() {
        moveLeftBtn.addActionListener(new GibsonAssemblyListeners.BtnsListener());
        moveRightBtn.addActionListener(new GibsonAssemblyListeners.BtnsListener());
        generateBtn.addActionListener(new GibsonAssemblyListeners.BtnsListener());

        minTmAnnealingSpinner.addChangeListener(new GibsonAssemblyListeners.SpinnersListener(this));
        maxTmAnnealingSpinner.addChangeListener(new GibsonAssemblyListeners.SpinnersListener(this));
        minLengthOverlapSpinner.addChangeListener(new GibsonAssemblyListeners.SpinnersListener(this));
        maxLengthOverlapSpinner.addChangeListener(new GibsonAssemblyListeners.SpinnersListener(this));
    }

    void setDialogDescriptor(DialogDescriptor dialogDescriptor) {
        this.dialogDescriptor = dialogDescriptor;
        this.notificationLineSupport = dialogDescriptor.createNotificationLineSupport();
    }

    void validateInput() {
        if (this.dialogDescriptor == null) {
            return;
        }
        Number lengthMin = (Number) minTmAnnealingSpinner.getValue();
        Number lengthMax = (Number) maxTmAnnealingSpinner.getValue();

        Number lengthMinOverlap = (Number) minLengthOverlapSpinner.getValue();
        Number lengthMaxOverlap = (Number) maxLengthOverlapSpinner.getValue();
        
        List<OverlapPrimer> overlapPrimers = primersTable.getOverlapPrimers();

        if (lengthMin.floatValue() > lengthMax.floatValue()) {
            dialogDescriptor.setValid(false);
            notificationLineSupport.setInformationMessage("Min. annealing Tm > Max. annealing Tm");
        } else if (lengthMinOverlap.floatValue() > lengthMaxOverlap.floatValue()) {
            dialogDescriptor.setValid(false);
            notificationLineSupport.setInformationMessage("Min. overlap length > Max. overlap length");
        } else if (overlapPrimers == null || overlapPrimers.isEmpty()) {
            dialogDescriptor.setValid(false);
            notificationLineSupport.setInformationMessage("Please generate primers");
        } else {
            dialogDescriptor.setValid(true);
            notificationLineSupport.clearMessages();
        }
    }
    
    JPanel createSequence(){
        JPanel ret = new JPanel(new BorderLayout());
        return ret;
    }
    
    JPanel createPrimerDetailsPanel(){
        primerDetailsPanel = new PrimerDetailsPanel();             
        return primerDetailsPanel;
    }
    
    void showCenterPanel(String name){
        CardLayout cardLayout = (CardLayout)panelCenter.getLayout();
        cardLayout.show(panelCenter, name);
    }

    JPanel createSettingsPanel() {
        JPanel ret = new JPanel(new BorderLayout());

        RichSeparator separator = new RichSeparator("Settings");
        ret.add(separator, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel panel = createConcentrationsPanel();
        content.add(panel, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.NORTH;
        panel = createAnnealingRegion();
        content.add(panel, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        panel = createOverlapRegionPanel();
        content.add(panel, c);

        ret.add(content, BorderLayout.CENTER);
        return ret;
    }

    JPanel createOverlapRegionPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        ret.setBorder(BorderFactory.createTitledBorder( //
                new LineBorder(Color.GRAY), //
                "Overlapping region", // 
                TitledBorder.LEFT, //
                TitledBorder.TOP, //
                UIManager.getFont("TitledBorder.font")));
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel(String.format("Min Tm(%s)", Unicodes.CELSIUS)), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        JPanel panel = createOverlapTmPanel();
        ret.add(panel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Length"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        JPanel lengthPanel = createOverlapLengthPanel();
        ret.add(lengthPanel, c);

        return ret;
    }

    JPanel createOverlapTmPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        minTmOverlapSpinner = new JSpinner(new SpinnerNumberModel(48.0f, 1f, 110f, 0.1f));
        UIUtil.setPreferredWidthByPrototype(minTmOverlapSpinner, 1000);
        ret.add(minTmOverlapSpinner, c);

        c = new GridBagConstraints();
        JButton btn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.INFO_16), false);
        btn.setToolTipText(String.format("Assuming A-T pair = 2%s and G-C pair = 4%s", Unicodes.CELSIUS, Unicodes.CELSIUS));
        ret.add(btn, c);
        return ret;
    }

    JPanel createOverlapLengthPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        minLengthOverlapSpinner = new JSpinner(new SpinnerNumberModel(15, 1, 110, 1));
        UIUtil.setPreferredWidthByPrototype(minLengthOverlapSpinner, 1000);
        ret.add(minLengthOverlapSpinner, c);

        c = new GridBagConstraints();
        ret.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        maxLengthOverlapSpinner = new JSpinner(new SpinnerNumberModel(40, 1, 110, 1));
        UIUtil.setPreferredWidthByPrototype(maxLengthOverlapSpinner, 1000);
        ret.add(maxLengthOverlapSpinner, c);

        c = new GridBagConstraints();
        JButton btn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.INFO_16), false);
        btn.setToolTipText("<html>A minimum overlap of 15 is suggested when the total number of fragments is less than 4.<br/>A minimum overlap of 20 is suggested when 4 or more fragments are assembled.</html>");
        ret.add(btn, c);

        return ret;
    }

    JPanel createAnnealingRegion() {
        JPanel ret = new JPanel(new GridBagLayout());
        ret.setBorder(BorderFactory.createTitledBorder( //
                new LineBorder(Color.GRAY), //
                "Annealing region", //
                TitledBorder.LEFT, //
                TitledBorder.TOP, //
                UIManager.getFont("TitledBorder.font")));
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel(String.format("Tm(%s)", Unicodes.CELSIUS)), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        JPanel panel = createAnnealingTmPanel();
        ret.add(panel, c);

        return ret;
    }

    JPanel createConcentrationsPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        ret.setBorder(BorderFactory.createTitledBorder( //
                new LineBorder(Color.GRAY), //
                "Reagents Conc.", //
                TitledBorder.LEFT, //
                TitledBorder.TOP, //
                UIManager.getFont("TitledBorder.font")));
        GridBagConstraints c = null;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Monovalent cation conc.(mM)"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        Float primerSaltMono = this.userInput.getFloat(UserInput.PRIMER_SALT_MONOVALENT);
        primerSaltMonoSpinner = new JSpinner(new SpinnerNumberModel(primerSaltMono.doubleValue(), 0.0, 150.0, 1.0));
        primerSaltMonoSpinner.setValue(primerSaltMono.doubleValue());
        UIUtil.setPreferredWidthByPrototype(primerSaltMonoSpinner, 1000);
        ret.add(primerSaltMonoSpinner, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Divalent cation conc.(mM)"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        Float primerDivalentConc = this.userInput.getFloat(UserInput.PRIMER_SALT_DIVALENT);
        primerSaltDivaSpinner = new JSpinner(new SpinnerNumberModel(primerDivalentConc.doubleValue(), 0.0, 150.0, 0.1));
        UIUtil.setPreferredWidthByPrototype(primerSaltDivaSpinner, 1000);
        ret.add(primerSaltDivaSpinner, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Annealing oligo conc.(nM)"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        Float primerDnaConc = this.userInput.getFloat(UserInput.PRIMER_DNA_CONC);
        primerDnaConcSpinner = new JSpinner(new SpinnerNumberModel(primerDnaConc.doubleValue(), 1.0, 150.0, 1.0));
        UIUtil.setPreferredWidthByPrototype(primerDnaConcSpinner, 1000);
        ret.add(primerDnaConcSpinner, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("dNTP conc.(mM)"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        Float primerDntpConc = this.userInput.getFloat(UserInput.PRIMER_DNTP_CONC);
        primerDntpSpinner = new JSpinner(new SpinnerNumberModel(primerDntpConc.doubleValue(), 0.0, 150.0, 0.1));
        UIUtil.setPreferredWidthByPrototype(primerDntpSpinner, 1000);
        ret.add(primerDntpSpinner, c);

        return ret;
    }

    private List<Integer> getBoundaries() {
        int lengthPrev = 0;
        List<Integer> boundaries = new ArrayList<Integer>();
        for (int i = 0; i < asList.size(); i++) {
            AnnotatedSeq cur = asList.get(i);
            boundaries.add(lengthPrev + 1);
            lengthPrev += cur.getLength();
        }
        return boundaries;
    }    

    /**
     * the overlap sequence consists of equal number of nucleotides from both
     * side
     */
    OverlapSeqList getOverlapSeqList(int boundary) {
        IGibsonService gibsonSvc = Lookup.getDefault().lookup(IGibsonService.class);
        final int minLengthOverlap = getMinLengthOverlap();
        final int maxLengthOverlap = getMaxLengthOverlap();
        String wholeSeq = gibsonSvc.getFinalConstruct(asList);
        int wholeLength = wholeSeq.length();

        OverlapSeqList overlapSeqList = new OverlapSeqList();
        int start = boundary;
        int end = boundary;
        int width = 1;
        while (width < maxLengthOverlap) {
            if (width % 2 == 0) {
                start = start - 1;
                end = end + 1;
            } else {
                start = start - 1;
            }
            if (start < 1) {
                start = wholeLength + start;
            }
            if (end > wholeLength) {
                end = end % wholeLength;
            }
            width = LocUtil.width(start, end, wholeLength).intValue();
            String seq = BioUtil.subsequence(wholeSeq, start, end);
            OverlapSeq overlapSeq = new OverlapSeq();
            overlapSeq.setStart(start);
            overlapSeq.setEnd(end);
            overlapSeq.setSeq(seq);
            overlapSeq.setTotalLength(wholeLength);

            overlapSeqList.add(overlapSeq);
        }

        return overlapSeqList;
    }

    JPanel createAnnealingTmPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        Float minTm = userInput.getFloat("PRIMER_MIN_TM");
        minTmAnnealingSpinner = new JSpinner(new SpinnerNumberModel(minTm.intValue(), 1f, 100f, 0.1f));
        UIUtil.setPreferredWidthByPrototype(minTmAnnealingSpinner, 1000);
        ret.add(minTmAnnealingSpinner, c);

        c = new GridBagConstraints();
        ret.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        Float maxTm = userInput.getFloat("PRIMER_MAX_TM");
        maxTmAnnealingSpinner = new JSpinner(new SpinnerNumberModel(maxTm.intValue(), 1f, 100f, 0.1f));
        UIUtil.setPreferredWidthByPrototype(maxTmAnnealingSpinner, 1234);
        ret.add(maxTmAnnealingSpinner, c);

        c = new GridBagConstraints();
        JButton btn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.INFO_16), false);
        btn.setToolTipText("The annealing portion is designed using Primer3");
        ret.add(btn, c);

        return ret;
    }

    Float getMinTmOverlap() {
        Number number = (Number) minTmOverlapSpinner.getValue();
        return number.floatValue();
    }

    Float getMinTmAnnealing() {
        Number number = (Number) minTmAnnealingSpinner.getValue();
        return number.floatValue();
    }

    Float getMaxTmAnnealing() {
        Number number = (Number) maxTmAnnealingSpinner.getValue();
        return number.floatValue();
    }

    Integer getMinLengthOverlap() {
        return (Integer) minLengthOverlapSpinner.getValue();
    }

    /**
     * if 5' overhangs, do NOT count the overhang length 
     * because 5' exonuclease will chew the 5' overhangs back
     */
    List<UserInput> getUserInputs() {
        updateUserInputFromUI();
        List<UserInput> ret = new ArrayList<UserInput>();
        Integer lengthTotalPrev = 0;
        for (AnnotatedSeq as : asList) {
            UserInput ui = this.userInput.clone();
            ui.getData().put("SEQUENCE_ID", as.getName());
            Integer forceLeftStart = lengthTotalPrev + 1;
            ui.getData().put("SEQUENCE_FORCE_LEFT_START", forceLeftStart.toString());
            Overhang overhangStart = as.getStartOverhang();
            int lengthEffective = as.getLength();
            if(overhangStart != null && overhangStart.isFivePrime()){
                lengthEffective -= overhangStart.getLength();
            }
            Overhang overhangEnd = as.getEndOverhang();
            if(overhangEnd != null && overhangEnd.isFivePrime()){
                lengthEffective -= overhangEnd.getLength();
            }
            Integer forceRightStart = lengthTotalPrev + lengthEffective;
            ui.getData().put("SEQUENCE_FORCE_RIGHT_START", forceRightStart.toString());
            lengthTotalPrev += lengthEffective;

            ui.getData().put("PRIMER_NUM_RETURN", "1");
            ret.add(ui);
        }
        return ret;
    }

    private void updateUserInputFromUI() {
        Integer lengthMin = IPrimer3Service.PRIMER_LENGTH_MIN;
        Integer lengthMax = IPrimer3Service.PRIMER_LENGTH_MAX;
        Integer lengthOpt = Math.round(lengthMin * 0.5f + lengthMax * 0.5f);

        userInput.getData().put("PRIMER_MIN_SIZE", lengthMin.toString());
        userInput.getData().put("PRIMER_OPT_SIZE", lengthOpt.toString());
        userInput.getData().put("PRIMER_MAX_SIZE", IPrimer3Service.PRIMER_LENGTH_MAX.toString());

        Number tmMin = (Number) minTmAnnealingSpinner.getValue();
        Number tmMax = (Number) maxTmAnnealingSpinner.getValue();
        Float tmOpt = (tmMin.floatValue() + tmMax.floatValue()) / 2;

        userInput.getData().put("PRIMER_MIN_TM", tmMin.toString());
        userInput.getData().put("PRIMER_OPT_TM", tmOpt.toString());
        userInput.getData().put("PRIMER_MAX_TM", tmMax.toString());

        userInput.getData().put(UserInput.PRIMER_SALT_MONOVALENT, primerSaltMonoSpinner.getValue().toString());
        userInput.getData().put(UserInput.PRIMER_SALT_DIVALENT, primerSaltDivaSpinner.getValue().toString());
        userInput.getData().put(UserInput.PRIMER_DNTP_CONC, primerDntpSpinner.getValue().toString());
        userInput.getData().put(UserInput.PRIMER_DNA_CONC, primerDnaConcSpinner.getValue().toString());
    }

    Integer getMaxLengthOverlap() {
        return (Integer) maxLengthOverlapSpinner.getValue();
    }

    AnnotatedSeqList getAsList() {
        return this.asList;
    }

    JPanel createFragsPanel() {
        JPanel ret = new JPanel(new BorderLayout());
        RichSeparator separator = new RichSeparator("Fragments");
        ret.add(separator, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        final GibsonTable table = new GibsonTable();
        table.addAnnotatedSeqs(asList);
        primersPreview = new PrimersPreview(asList);
        content.add(primersPreview, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        JPanel panel = createFragsBtnPanel();
        content.add(panel, c);

        ret.add(content, BorderLayout.CENTER);
        return ret;
    }

    JPanel createFragsBtnPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridx = 0;

        c = new GridBagConstraints();
        c.gridx = gridx;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        moveLeftBtn = new JButton(ImageHelper.createImageIcon(ImageNames.ARROW_LEFT_16));
        moveLeftBtn.setActionCommand("moveLeft");
        moveLeftBtn.setEnabled(false);
        ret.add(moveLeftBtn, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        moveRightBtn = new JButton(ImageHelper.createImageIcon(ImageNames.ARROW_RIGHT_16));
        moveRightBtn.setActionCommand("moveRight");
        moveRightBtn.setEnabled(false);
        ret.add(moveRightBtn, c);

        c = new GridBagConstraints();
        c.gridx = gridx;
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 2;
        progressLabel = new JLabel();       
        progressLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        progressLabel.setIcon(ImageHelper.createImageIcon(ImageNames.EMPTY_16));
        ret.add(progressLabel, c);
        
        c = new GridBagConstraints();
        c.gridx = gridx;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 1;
        Component comp = Box.createRigidArea(new Dimension(1, 1));
        ret.add(comp, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = gridx;
        c.gridwidth = 2;
        c.weightx = 1;
        generateBtn = new JButton("Get Primers");
        generateBtn.setActionCommand("generate");
        ret.add(generateBtn, c);

        return ret;
    }
}
