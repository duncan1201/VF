/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.translation;

import com.gas.common.ui.IReclaimable;
import com.gas.common.ui.button.WideComboBox;
import com.gas.common.ui.color.ColorProviderFetcher;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.combo.ImgComboRenderer;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.geneticCode.api.GeneticCodeTable;
import com.gas.domain.core.geneticCode.api.IGeneticCodeTableService;
import com.gas.common.ui.util.Pref;
import com.gas.domain.core.as.TranslationResult;
import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class TranslationPanel extends JPanel implements IReclaimable {

    protected WideComboBox geneticCodeBox;
    private WeakReference<JCheckBox> plus3Ref;
    private WeakReference<JCheckBox> plus2Ref;
    private WeakReference<JCheckBox> plus1Ref;
    private WeakReference<JCheckBox> minus1Ref;
    private WeakReference<JCheckBox> minus2Ref;
    private WeakReference<JCheckBox> minus3Ref;
    boolean populatingUI;
    protected JComboBox colorCombo;
    private IGeneticCodeTableService tableService = Lookup.getDefault().lookup(IGeneticCodeTableService.class);
    private Set<TranslationResult> translationResults = new HashSet<TranslationResult>();

    public TranslationPanel() {
        initComponents();

        hookupListeners();
    }

    private void initComponents() {
        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        LayoutManager layout = null;
        layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c = null;


        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel("Genetic Code:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        List<GeneticCodeTable> tables = tableService.getTables();

        JPanel geneticCodePanel = createGeneticCodePanel(tables);

        final String prototype = "12345678901234567890123";
        FontMetrics fm = FontUtil.getFontMetrics(this);
        final int width = fm.stringWidth(prototype);
        UIUtil.setPreferredWidth(geneticCodePanel, width);

        add(geneticCodePanel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel("Frames:"), c);

        JPanel tmp = createTmpPanel();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(tmp, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(new JLabel("Colors:"), c);

        Pref.ColorProviderPrefs pref = Pref.ColorProviderPrefs.getInstance();
        String colorProviderName = pref.getColorProviderName(Pref.ColorProviderPrefs.KEY.TRANSLATION);

        Vector<IColorProvider> colorProviders = ColorProviderFetcher.getColorProviders(ColorProviderFetcher.TYPE.PROTEIN, Vector.class);

        colorCombo = new JComboBox(colorProviders);
        colorCombo.setRenderer(new ImgComboRenderer());
        IColorProvider selected = ColorProviderFetcher.getColorProvider(colorProviderName);
        if(selected != null){
            colorCombo.setSelectedItem(selected);
        }
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.WEST;
        add(colorCombo, c);

    }

    private JPanel createGeneticCodePanel(List<GeneticCodeTable> options) {
        JPanel ret = new JPanel(new BorderLayout());
        geneticCodeBox = new WideComboBox(options);
        ret.add(geneticCodeBox, BorderLayout.CENTER);
        return ret;
    }

    private JPanel createTmpPanel() {
        JPanel tmp = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        JCheckBox plus1 = new JCheckBox("+1");
        plus1Ref = new WeakReference<JCheckBox>(plus1);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        tmp.add(plus1, c);

        JCheckBox plus2 = new JCheckBox("+2");
        plus2Ref = new WeakReference<JCheckBox>(plus2);
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        tmp.add(plus2, c);

        JCheckBox plus3 = new JCheckBox("+3");
        plus3Ref = new WeakReference<JCheckBox>(plus3);
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        tmp.add(plus3, c);

        JCheckBox minus1 = new JCheckBox("-1");
        minus1Ref = new WeakReference<JCheckBox>(minus1);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        tmp.add(minus1, c);

        JCheckBox minus2 = new JCheckBox("-2");
        minus2Ref = new WeakReference<JCheckBox>(minus2);
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        tmp.add(minus2, c);

        JCheckBox minus3 = new JCheckBox("-3");
        minus3Ref = new WeakReference<JCheckBox>(minus3);
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        tmp.add(minus3, c);

        return tmp;
    }

    @Override
    public void cleanup() {
        colorCombo = null;
    }

    public JComboBox getColorCombo() {
        return colorCombo;
    }

    protected int[] getSelectedFrames() {
        int[] ret = null;
        List<Integer> retList = new ArrayList<Integer>();
        if (minus1Ref.get().isSelected()) {
            retList.add(-1);
        }
        if (minus2Ref.get().isSelected()) {
            retList.add(-2);
        }
        if (minus3Ref.get().isSelected()) {
            retList.add(-3);
        }
        if (plus1Ref.get().isSelected()) {
            retList.add(1);
        }
        if (plus2Ref.get().isSelected()) {
            retList.add(2);
        }
        if (plus3Ref.get().isSelected()) {
            retList.add(3);
        }

        ret = new int[retList.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = retList.get(i);
        }
        return ret;
    }

    public void setTranslationResults(Set<TranslationResult> translationResults) {
        Set<TranslationResult> old = this.translationResults;
        this.translationResults = translationResults;
        firePropertyChange("translationResults", old, this.translationResults);
    }

    void populateUI() {
        populatingUI = true;
        Iterator<TranslationResult> itr = this.translationResults.iterator();
        String tableName = null;
        while (itr.hasNext()) {
            TranslationResult tr = itr.next();
            if (tableName == null) {
                tableName = tr.getTableName();
            }
            Integer frame = tr.getFrame();
            if (frame.intValue() == 1) {
                plus1Ref.get().setSelected(true);
            } else if (frame.intValue() == 2) {
                plus2Ref.get().setSelected(true);
            } else if (frame.intValue() == 3) {
                plus3Ref.get().setSelected(true);
            } else if (frame.intValue() == -1) {
                minus1Ref.get().setSelected(true);
            } else if (frame.intValue() == -2) {
                minus2Ref.get().setSelected(true);
            } else if (frame.intValue() == -3) {
                minus3Ref.get().setSelected(true);
            }
        }
        if (tableName != null) {
            int itemCount = geneticCodeBox.getItemCount();
            for (int i = 0; i < itemCount; i++) {
                GeneticCodeTable table = (GeneticCodeTable) geneticCodeBox.getItemAt(i);
                if (table.getName().equals(tableName)) {
                    geneticCodeBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        populatingUI = false;
    }

    private void hookupListeners() {

        geneticCodeBox.addActionListener(new TranslationPanelListeners.GeneticCodeTableListener(TranslationPanel.this));
        TranslationPanelListeners.FrameListener frameListener = new TranslationPanelListeners.FrameListener();

        minus1Ref.get().addItemListener(frameListener);

        minus2Ref.get().addItemListener(frameListener);

        minus3Ref.get().addItemListener(frameListener);

        plus1Ref.get().addItemListener(frameListener);

        plus2Ref.get().addItemListener(frameListener);

        plus3Ref.get().addItemListener(frameListener);

        colorCombo.addActionListener(new TranslationPanelListeners.ColorComboListener(this));

        addPropertyChangeListener(new TranslationPanelListeners.PtyListener());
    }
}
