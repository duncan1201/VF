/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.ctrl.general;

import com.gas.common.ui.color.ColorProviderFetcher;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.combo.ImgComboRenderer;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.matrix.api.IMatrixService;
import com.gas.common.ui.matrix.api.MatrixList;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.msa.SeqLogoParam;
import com.gas.domain.ui.pref.MSAPref;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;
import java.util.Vector;
import javax.swing.*;
import org.jdesktop.swingx.JXHyperlink;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class PropertiesPanel extends JPanel {

    private WeakReference<JComboBox> colorComboRef;
    private JCheckBox coverageBox;
    private JSpinner coverageSpinner;
    private JCheckBox seqLogoBox;
    private JSpinner seqLogoSpinner;
    WeakReference<JCheckBox> sampleCorrectRef;
    private JCheckBox qualityBox;
    private JSpinner qualitySpinner;
    WeakReference<JLabel> matrixLabelRef;
    WeakReference<JComboBox> matrixComboRef;
    static boolean populatingUI;

    public PropertiesPanel() {
        initComponents();
        hookupListeners();
    }

    private void initComponents() {
        Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(createColorPanel(), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(createCoveragePanel(), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(createSeqlogoPanel(), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(createQualityPanel(), c);
    }

    private JPanel createColorPanel() {
        JPanel ret = new JPanel();

        GridBagConstraints c = new GridBagConstraints();
        ret.add(new JLabel("Colors:"), c);

        c = new GridBagConstraints();
        JComboBox colorCombo = new JComboBox();
        colorCombo.setFocusable(false);
        colorCombo.setRenderer(new ImgComboRenderer());
        ret.add(colorCombo, c);
        colorComboRef = new WeakReference<JComboBox>(colorCombo);
        return ret;
    }

    private JPanel createCoveragePanel() {
        JPanel ret = new JPanel();
        ret.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.anchor = GridBagConstraints.WEST;
        coverageBox = new JCheckBox("Coverage Plot");
        ret.add(coverageBox, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        coverageSpinner = new JSpinner();
        coverageSpinner.setModel(new SpinnerNumberModel(1, 1, 100, 1));
        ret.add(coverageSpinner, c);

        return ret;
    }

    private JPanel createQualityPanel() {
        JPanel ret = new JPanel();
        ret.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.anchor = GridBagConstraints.WEST;
        qualityBox = new JCheckBox("Quality Plot");
        ret.add(qualityBox, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        qualitySpinner = new JSpinner();
        qualitySpinner.setModel(new SpinnerNumberModel(1, 1, 100, 1));
        ret.add(qualitySpinner, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridwidth = 2;
        JPanel matrixPanel = createMatrixPanel();
        ret.add(matrixPanel, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridwidth = 2;
        JPanel clustalxPanel = createClustalxPanel();
        ret.add(clustalxPanel, c);

        return ret;
    }
    
    private JPanel createClustalxPanel(){
        JPanel ret = new JPanel();
        ret.add(new JLabel("<html><i>Based on</i></html>"));
        JXHyperlink link = new JXHyperlink(new AbstractAction(){

            final String uri = "http://dx.doi.org/10.1093/nar/25.24.4876";
            
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonUtil.browse(uri);
            }
        });
        link.setFocusable(false);
        link.setText("<html><i>ClustalX</i></html>");
        ret.add(link);
        return ret;
    }
    
    private JPanel createMatrixPanel(){
        JPanel ret = new JPanel();
        GridBagConstraints c;
        
        c = new GridBagConstraints();
        JLabel matrixLabel = new JLabel("Scoring Matrix:");
        matrixLabelRef = new WeakReference<JLabel>(matrixLabel);
        ret.add(matrixLabel, c);
        
        c = new GridBagConstraints();
        JComboBox matrixCombo = new JComboBox();
        matrixComboRef = new WeakReference<JComboBox>(matrixCombo);
        ret.add(matrixCombo, c);
        return ret;
    }

    private JPanel createSeqlogoPanel() {
        JPanel ret = new JPanel();
        ret.setLayout(new GridBagLayout());
        int gridy = 0;

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.anchor = GridBagConstraints.WEST;
        seqLogoBox = new JCheckBox("Sequence Logo");
        ret.add(seqLogoBox, c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        seqLogoSpinner = new JSpinner();
        seqLogoSpinner.setModel(new SpinnerNumberModel(1, 1, 300, 1));
        ret.add(seqLogoSpinner, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.gridwidth = 2;
        JCheckBox sampleCorrection = new JCheckBox("Small Sample Correction");
        sampleCorrectRef = new WeakReference<JCheckBox>(sampleCorrection);
        ret.add(sampleCorrection, c);

        return ret;
    }

    private void hookupListeners() {
        coverageBox.addItemListener(new PropertiesPanelListeners.CoveragePlotListener());
        qualityBox.addItemListener(new PropertiesPanelListeners.QualityPlotListener());
        seqLogoBox.addItemListener(new PropertiesPanelListeners.SeqLogoListener());
        sampleCorrectRef.get().addItemListener(new PropertiesPanelListeners.SampleCorrectListener());
        colorComboRef.get().addActionListener(new PropertiesPanelListeners.ColorComboListener());

        coverageSpinner.addChangeListener(new PropertiesPanelListeners.CoverageHeightListener());
        qualitySpinner.addChangeListener(new PropertiesPanelListeners.QualityHeightListener());
        seqLogoSpinner.addChangeListener(new PropertiesPanelListeners.SeqLogoHeightListener());
        
        matrixComboRef.get().addActionListener(new PropertiesPanelListeners.MatrixListener());
    }

    protected void populateUI(MSA msa) {
        populatingUI = true;

        boolean dna = msa.isDnaByGuess();
        reinitColorCombo(dna);
        initMatrixCombo(msa.getSubMatrix(), dna);
        if (msa.getSeqLogoParam() == null) {
            msa.setSeqLogoParam(new SeqLogoParam());
        }
        sampleCorrectRef.get().setSelected(msa.getSeqLogoParam().isSmallSampleCorrection());
        coverageSpinner.setValue(msa.getMsaSetting().getCoverageHeight());
        seqLogoSpinner.setValue(msa.getMsaSetting().getSeqLogoHeight());
        qualitySpinner.setValue(msa.getMsaSetting().getQualityHeight());
            
        coverageBox.setSelected(msa.getMsaSetting().isCoverageDisplay());
        seqLogoBox.setSelected(msa.getMsaSetting().isSeqLogoDisplay());
        qualityBox.setSelected(msa.getMsaSetting().isQualityDisplay());
        
        populatingUI = false;
    }
    
    private void initMatrixCombo(String matrixName, boolean dna){
        IMatrixService matrixService = Lookup.getDefault().lookup(IMatrixService.class);
        MatrixList matrixList = matrixService.getAllMatrices();
        if(dna){
            StringList names = matrixList.getDnaMatrices().getNames();
            matrixComboRef.get().setModel(new DefaultComboBoxModel(names.toArray(new String[names.size()])));
        }else{
            StringList names = matrixList.getProteinMatrices().getNames();
            matrixComboRef.get().setModel(new DefaultComboBoxModel(names.toArray(new String[names.size()])));
        }
        if(matrixName != null){
            matrixComboRef.get().setSelectedItem(matrixName);
        }
    }

    private void reinitColorCombo(boolean dna) {
        MSAPref.KEY key = dna ? MSAPref.KEY.DNA : MSAPref.KEY.PROTEIN;
        String selectedName = MSAPref.getInstance().getColorProviderName(key);
        IColorProvider selectedColorProvider = ColorProviderFetcher.getColorProvider(selectedName);
        ColorProviderFetcher.TYPE type = dna ? ColorProviderFetcher.TYPE.DNA : ColorProviderFetcher.TYPE.PROTEIN;
        Vector<IColorProvider> colorProviders = ColorProviderFetcher.getColorProviders(type, Vector.class);

        colorComboRef.get().setModel(new DefaultComboBoxModel(colorProviders));
        colorComboRef.get().setSelectedItem(selectedColorProvider);
    }
}
