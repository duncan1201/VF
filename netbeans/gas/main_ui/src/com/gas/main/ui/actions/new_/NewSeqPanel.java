/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.new_;

import com.gas.common.ui.misc.UppercaseDocumentFilter;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;

/**
 *
 * @author dq
 */
public class NewSeqPanel extends JPanel {

    private JTextField nameField;
    private JTextField descField;
    private JTextPane statusPane;
    private JTextPane textPane;
    private JLabel caretPosLabel;
    private JRadioButton dnaTypeBtn;
    private JRadioButton primerTypeBtn;
    private JRadioButton proteinTypeBtn;
    //private JTextField organismField;
    //private JButton addOrganismBtn;
    private DialogDescriptor dialogDescriptor;
    private NotificationLineSupport notificationLineSupport;
    public static final String CMD_DNA = "cmd_dna";
    public static final String CMD_PRIMER = "cmd_primer";
    public static final String CMD_PROTEIN = "cmd_protein";

    public NewSeqPanel() {
        createComponents();
        hookupListeners();
    }

    public void setDialogDescriptor(DialogDescriptor dialogDescriptor) {
        this.dialogDescriptor = dialogDescriptor;
        this.notificationLineSupport = dialogDescriptor.createNotificationLineSupport();
    }

    public void updateStatusPane() {
        final int total = textPane.getText().length();
        final FontMetrics fm = textPane.getFontMetrics(textPane.getFont());

        int width = textPane.getVisibleRect().width - textPane.getInsets().left - textPane.getInsets().right;

        final int charPerLine = width / fm.charWidth('A');

        StringBuilder t = new StringBuilder();
        t.append("<html>");

        int lineStart = 1;
        int lineEnd = lineStart + charPerLine - 1;
        while (lineStart <= total) {
            t.append(lineStart);
            t.append('-');
            t.append(Math.min(lineEnd, total));
            if (lineEnd < total) {
                t.append("<br>");
            }

            lineStart = lineStart + charPerLine;
            lineEnd = lineStart + charPerLine - 1;
        }
        t.append("</html>");
        statusPane.setText(t.toString());
    }

    void validateInput() {
        boolean isDNAType = isDNAType();
        boolean isPrimerType = isPrimerType();
        boolean isProteinType = isProteinType();
        String seq = textPane.getText().trim();
        boolean valid = true;
        String msg = "";
        // check sequence
        if (seq.isEmpty()) {
            valid = false;
            msg = "No Sequence entered";
        } else {
            if (isDNAType || isPrimerType) {
                boolean areDNAs = BioUtil.areDNAs(seq);
                if (!areDNAs) {
                    valid = false;
                    msg = "The sequence contains non-nucleotide symbols";
                }
            } else if (isProteinType) {
                boolean areProteins = BioUtil.areProteins(seq);
                if (!areProteins) {
                    valid = false;
                    msg = "The sequence contains non-amino acid symbols";
                }
            }
        }

        // check name
        if (getSeqName().isEmpty()) {
            valid = false;
            msg = "Sequence name cannot be empty";
        }

        dialogDescriptor.setValid(valid);
        if (!valid) {
            notificationLineSupport.setInformationMessage(msg);
        } else {
            notificationLineSupport.clearMessages();
        }
    }

    String getSeq() {
        return textPane.getText();
    }

    String getSeqName() {
        return nameField.getText();
    }

    String getSeqDescription() {
        return descField.getText();
    }

    boolean isDNAType() {
        return dnaTypeBtn.isSelected();
    }

    boolean isPrimerType() {
        return primerTypeBtn.isSelected();
    }

    boolean isProteinType() {
        return proteinTypeBtn.isSelected();
    }

    private void createComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        add(createTextInputComponent(), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        add(createStatusPanel(), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel basicInfo = createBasicInfoPanel();
        add(basicInfo, c);
    }

    void updateCaretPosition(Integer dot, Integer mark) {
        if (dot != null) {
            if (dot.equals(mark)) {
                caretPosLabel.setText(String.format("%d bp", dot + 1));
            } else {
                caretPosLabel.setText(String.format("(%d bp - %d bp) %d bp", Math.min(mark + 1, dot + 1), Math.max(dot + 1, mark + 1) - 1, Math.abs(mark - dot)));
            }
        } else {
            caretPosLabel.setText("");
        }
    }

    private JPanel createStatusPanel() {
        JPanel ret = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        caretPosLabel = new JLabel("1 bp");
        ret.add(caretPosLabel);
        return ret;
    }

    private void hookupListeners() {
        NewSeqPanelListeners.DocListener docListener = new NewSeqPanelListeners.DocListener(this);
        NewSeqPanelListeners.DocKeyAdapter docKeyListener = new NewSeqPanelListeners.DocKeyAdapter(this);
        NewSeqPanelListeners.NameKeyAdapter nameKeyListener = new NewSeqPanelListeners.NameKeyAdapter(this);
        NewSeqPanelListeners.TypeListener typeListener = new NewSeqPanelListeners.TypeListener(this);
        NewSeqPanelListeners.SeqCaretListener caretListener = new NewSeqPanelListeners.SeqCaretListener(this);

        textPane.getDocument().addDocumentListener(docListener);
        textPane.addKeyListener(docKeyListener);
        textPane.addCaretListener(caretListener);
        nameField.addKeyListener(nameKeyListener);

        dnaTypeBtn.addActionListener(typeListener);
        proteinTypeBtn.addActionListener(typeListener);
    }

    
    private JComponent createTextInputComponent() {
        final FontMetrics fm = FontUtil.getDefaultMSFontMetrics();

        statusPane = new JTextPane();
        statusPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        statusPane.setContentType("text/html");
        statusPane.setEditable(false);
        statusPane.setFont(FontUtil.getDefaultMSFont());
        statusPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        UIUtil.setPreferredWidth(statusPane, fm.stringWidth("12345-67890"));
        
        textPane = new JTextPane();
        ((AbstractDocument) textPane.getDocument()).setDocumentFilter(new UppercaseDocumentFilter());

        textPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        textPane.setFont(FontUtil.getDefaultMSFont());
        textPane.getCharacterAttributes();

        UIUtil.setPreferredHeight(textPane, fm.getHeight() * 19);
        UIUtil.setPreferredWidth(textPane, Math.round((int) textPane.getPreferredSize().getHeight() * 1.6f));
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setRowHeaderView(statusPane);

        return scrollPane;
    }

    private JPanel createBasicInfoPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c = null;

        int gridx = 0, gridy = 0;
        // row 1
        c = new GridBagConstraints();
        c.gridx = gridx++;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        ret.add(new JLabel("Name"), c);

        nameField = new JTextField();
        c = new GridBagConstraints();
        c.gridx = gridx;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        ret.add(nameField, c);

        // row 2
        gridx = 0;
        c = new GridBagConstraints();
        c.gridx = gridx++;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        ret.add(new JLabel("Type"), c);

        c = new GridBagConstraints();
        c.gridx = gridx;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        ret.add(createTypePanel(), c);

        // row 3
        /*
         gridx = 0;
         c = new GridBagConstraints();
         c.gridx = gridx;
         c.gridy = gridy;
         c.anchor = GridBagConstraints.EAST;
         ret.add(new JLabel("Organism"), c);

         c = new GridBagConstraints();
         c.gridx = ++gridx;
         c.gridy = gridy++;
         c.fill = GridBagConstraints.HORIZONTAL;
         c.weightx = 1.0;
         ret.add(createOrganismPanel(), c);
         */
        // row 4
        gridx = 0;
        c = new GridBagConstraints();
        c.gridx = gridx;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        ret.add(new JLabel("Description"), c);

        c = new GridBagConstraints();
        c.gridx = ++gridx;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        descField = new JTextField();
        ret.add(descField, c);
        return ret;
    }

    private JPanel createTypePanel() {
        JPanel ret = new JPanel(new FlowLayout(FlowLayout.LEFT));

        dnaTypeBtn = new JRadioButton("DNA", true);
        dnaTypeBtn.setActionCommand(CMD_DNA);
        ret.add(dnaTypeBtn);

        primerTypeBtn = new JRadioButton("Primer");
        primerTypeBtn.setActionCommand(CMD_PRIMER);
        ret.add(primerTypeBtn);

        proteinTypeBtn = new JRadioButton("protein");
        proteinTypeBtn.setActionCommand(CMD_PROTEIN);
        ret.add(proteinTypeBtn);

        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(dnaTypeBtn);
        btnGroup.add(primerTypeBtn);
        btnGroup.add(proteinTypeBtn);

        return ret;
    }
}
