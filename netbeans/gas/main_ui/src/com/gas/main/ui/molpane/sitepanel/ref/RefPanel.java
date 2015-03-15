/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.ref;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.jcomp.IconButton;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.UIUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.ref.WeakReference;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author dunqiang
 */
public class RefPanel extends JPanel {

    private String title;
    private String remarks;
    private String publisher;
    private String pmid;
    private JLabel locationPane;
    private JLabel remarksPane;
    private JLabel titlePane;
    private JLabel pmidPane;
    private IconButton downloadBtn;

    public RefPanel() {
        super(new BorderLayout());
        UIUtil.setLeftInsets(this, 5);
        setBackground(Color.WHITE);
        // title

        titlePane = new JLabel();
        Font font = titlePane.getFont().deriveFont(Font.BOLD);
        titlePane.setFont(font);
        add(titlePane, BorderLayout.NORTH);

        // left insets for the rest of component.
        final int LEFT_INSETS = 8;
        // remarks

        remarksPane = new JLabel();
        UIUtil.setLeftInsets(remarksPane, LEFT_INSETS);
        add(remarksPane, BorderLayout.CENTER);

        // location
        JPanel southPanel = new JPanel(new GridBagLayout());
        southPanel.setBackground(null);
        locationPane = new JLabel();
        locationPane.setBorder(BorderFactory.createEmptyBorder());
        locationPane.setForeground(ColorCnst.LINK_COLOR);        
        locationPane.addMouseListener(new RefPanelListeners.LocationMouseListener(new WeakReference<RefPanel>(this)));
        
        UIUtil.setLeftInsets(locationPane, LEFT_INSETS);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.8;
        c.fill = GridBagConstraints.HORIZONTAL;
        southPanel.add(locationPane, c);

        // download icon
        ImageIcon icon = ImageHelper.createImageIcon(ImageNames.DOWNLOAD_16);
        downloadBtn = new IconButton(icon);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;                
        southPanel.add(downloadBtn, c);

        // pmid
        pmidPane = new JLabel();
        UIUtil.setLeftInsets(pmidPane, LEFT_INSETS);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        southPanel.add(pmidPane, c);

        add(southPanel, BorderLayout.SOUTH);

        hookupListeners();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new RefPanelListeners.PtyListener());
    }

    public IconButton getDownloadBtn() {
        return downloadBtn;
    }

    protected JLabel getLocationPane() {
        return locationPane;
    }

    protected JLabel getPmidPane() {
        return pmidPane;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        String old = this.publisher;
        this.publisher = publisher;
        firePropertyChange("publisher", old, this.publisher);       
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        String old = this.pmid;
        this.pmid = pmid;
        firePropertyChange("pmid", old, this.pmid);
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
        if (remarks != null && !remarks.isEmpty()) {
            remarksPane.setVisible(true);
            remarksPane.setText(remarks);
        } else {
            remarksPane.setVisible(false);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        titlePane.setText(title);
    }
}
