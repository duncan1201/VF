/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui.editor;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.ren.IRENListService;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RENList;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.AbstractSavableEditor;
import com.gas.domain.ui.editor.IMySavable;
import com.gas.domain.ui.editor.renlist.api.IRENListEditor;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.domain.ui.misc.Helper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.ServiceProvider;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//com.gas.enzyme.ui.editor//RENListTopComponent//EN",
        autostore = false)
@TopComponent.Description(preferredID = "RENListTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ServiceProvider(service = IRENListEditor.class)
public final class RENListEditor extends AbstractSavableEditor implements IRENListEditor {

    private RENList renList;
    JTable renTable;
    ControlPanel controlPanel;
    private RENListTableModel renlistTableModel;
    private IRENListService renListService = Lookup.getDefault().lookup(IRENListService.class);

    public RENListEditor() {
        initComponents();
        hookupListeners();
    }
    
    private void hookupListeners(){
        renTable.getSelectionModel().addListSelectionListener(new RENListEditorListeners.TableSelectListener(this));
    }

    private void initComponents() {

        LayoutManager layout = null;
        layout = new BorderLayout();
        this.setLayout(layout);

        renTable = new JTable() {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row,
                    int column) {
                Component ret = super.prepareRenderer(renderer, row, column);

                if (!ret.getBackground().equals(getSelectionBackground())) {
                    if (row % 2 == 0) {
                        ret.setBackground(ColorCnst.ALICE_BLUE);
                    } else {
                        ret.setBackground(Color.WHITE);
                    }
                }
                return ret;
            }
        };
        renTable.setAutoCreateRowSorter(true);
        renlistTableModel = new RENListTableModel();
        renTable.setModel(renlistTableModel);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(renTable);

        add(scrollPane, BorderLayout.CENTER);

        controlPanel = new ControlPanel();
        add(controlPanel, BorderLayout.NORTH);

        associateLookup(new AbstractLookup(ic));

        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
    }// </editor-fold>

    @Override
    public void componentClosed() {
        cancelTimer();
        String hId = Helper.getHibernateId(renList);
        if (hId != null) {
            BannerTC.getInstance().unselectByHibernateId(hId);
        } else {
            BannerTC.getInstance().unselect(renList);
        }
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public RENList getRENList() {
        return renList;
    }

    @Override
    public JTable getRenTable() {
        return renTable;
    }

    @Override
    public void setRENList(RENList renList) {
        this.renList = renList;
        renlistTableModel.set(new ArrayList<REN>(renList.getReadOnlyRens()));
        UIUtil.setTopCompName(this, renList.getName());
        UIUtil.setTopCompIcon(this, ImageHelper.createImage(ImageNames.CUT_16));

        if (renList.getHibernateId() != null) {
            BannerTC.getInstance().setCheckRowByHibernateId(renList.getHibernateId(), true);
        } else {
            BannerTC.getInstance().setCheckRow(renList, true);
        }
    }

    @Override
    public Class<? extends AbstractSavable> getMySavableClass() {
        return MySavable.class;
    }

    @Override
    public IFolderElement getFolderElement() {
        return renList;
    }
    
    @Override
    public String getStatusLineText() {
        return "";
    }    

    public class MySavable extends AbstractSavable implements IMySavable, Icon {

        private Icon icon;

        public MySavable() {
            register();
        }

        private Icon getIcon() {
            if (icon == null) {
                icon = ImageHelper.createImageIcon(RENListEditor.this.getIcon());
            }
            return icon;
        }

        @Override
        protected String findDisplayName() {
            return renList.getName();
        }

        @Override
        protected void handleSave() throws IOException {
            renList.setLastModifiedDate(new Date());
            renListService.merge(renList);
            tc().ic.remove(this);
            UIUtil.styleTopCompName(tc(), false, false);
            unregister();
            BannerTC.getInstance().updataRowByHibernateId(renList);
        }

        RENListEditor tc() {
            return RENListEditor.this;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof MySavable) {
                MySavable m = (MySavable) obj;
                return tc() == m.tc();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return tc().hashCode();
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            getIcon().paintIcon(c, g, x, y);
        }

        @Override
        public int getIconWidth() {
            return getIcon().getIconWidth();
        }

        @Override
        public int getIconHeight() {
            return getIcon().getIconHeight();
        }

        @Override
        public void handleCloseWithoutSaving() {
            tc().ic.remove(this);
            unregister();
        }
    }
}
