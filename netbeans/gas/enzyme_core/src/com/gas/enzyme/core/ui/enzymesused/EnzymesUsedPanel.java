/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EnzymesUsedContentPanel.java
 *
 * Created on Nov 26, 2011, 6:47:35 AM
 */
package com.gas.enzyme.core.ui.enzymesused;

import com.gas.common.ui.IReclaimable;
import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.combo.BorderComboRenderer;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.jcomp.CardPanel;
import com.gas.common.ui.jcomp.StringListPanel;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.ren.IRENListService;
import com.gas.database.core.service.api.IHibernateConfigService;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RENList;
import com.gas.domain.core.ren.RENSet;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.core.ren.RMap.InputParams;
import javax.swing.JButton;
import javax.swing.JComboBox;
import org.openide.util.Lookup;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author dunqiang
 */
public class EnzymesUsedPanel extends javax.swing.JPanel implements IReclaimable {

    protected IRENListService renListService = null;
    private JComboBox enzymeCombo;
    private CardPanel enzymeListCardPanel;
    private RMap.InputParams inputParams;
    private JButton newBtn;

    /**
     * Creates new form EnzymesUsedContentPanel
     */
    public EnzymesUsedPanel() {
        initService();
        initComponents();
        hookupListeners();
        int selectedIndex = enzymeCombo.getSelectedIndex();
        enzymeCombo.setSelectedIndex(selectedIndex);
    }

    @Override
    public void cleanup() {
        enzymeCombo = null;
        enzymeListCardPanel = null;
        newBtn = null;
    }
    
    public void setInputParams(RMap.InputParams inputParams){
        RMap.InputParams old = this.inputParams;
        this.inputParams = inputParams;
        firePropertyChange("inputParams", old, this.inputParams);
    }

    public InputParams getInputParams() {
        return inputParams;
    }
    
    private void hookupListeners() {
        newBtn.addActionListener(new EnzymesUsedPanelListeners.NewBtnListener());
        getEnzymeCombo().addActionListener(new EnzymesUsedPanelListeners.EnzymeComboListener(this));
        addPropertyChangeListener(new EnzymesUsedPanelListeners.PtyListener());
    }     
    
    public RENList getSelectedRENList(){
        BorderComboRenderer.Item item = (BorderComboRenderer.Item) enzymeCombo.getSelectedItem();
        RENList ret = (RENList) item.getData();
        return ret;
    }
    
    public void setSelectedRENList(String renListName){
        for(int i = 0; i < enzymeCombo.getItemCount(); i++){
            BorderComboRenderer.Item item = (BorderComboRenderer.Item)enzymeCombo.getItemAt(i);
            RENList renList = (RENList)item.getData();
            if(renList.getName().equalsIgnoreCase(renListName)){               
                enzymeCombo.setSelectedIndex(i);
                break;
            }
        }
    }
    
    public void setSelectedRENs(Set<String> str){
        CardPanel cardPanel = getEnzymeListCardPanel();
        StringListPanel strListPanel = (StringListPanel) cardPanel.getCurrentCard();
        strListPanel.setSelections(str);
    }

    public RENSet getSelectedRENs() {
        RENSet ret = new RENSet();
        CardPanel cardPanel = getEnzymeListCardPanel();
        StringListPanel strListPanel = (StringListPanel) cardPanel.getCurrentCard();

        List<String> selected = strListPanel.getSelected();
        String[] selectedRENNames = selected.toArray(new String[selected.size()]);
        Arrays.sort(selectedRENNames);

        BorderComboRenderer.Item item = (BorderComboRenderer.Item) enzymeCombo.getSelectedItem();
        RENList renList = (RENList) item.getData();

        Iterator<REN> itr = renList.getIterator();
        while (itr.hasNext()) {
            REN ren = itr.next();
            if (Arrays.binarySearch(selectedRENNames, ren.getName()) > -1) {
                ret.add(ren);
            }
        }
        return ret;
    }

    public CardPanel getEnzymeListCardPanel() {
        return enzymeListCardPanel;
    }

    private BorderComboRenderer.Item[] getComboItem() {
        BorderComboRenderer.Item[] ret = null;

        List<BorderComboRenderer.Item> retList = new ArrayList<BorderComboRenderer.Item>();

        Iterator<RENList> deletables = getRENLists(true, true).iterator();
        while (deletables.hasNext()) {
            RENList renList = deletables.next();
            boolean hasNext = deletables.hasNext();
            BorderComboRenderer.Item item = new BorderComboRenderer.Item();

            item.setBottomSeparator(!hasNext);

            item.setData(renList);
            retList.add(item);
        }

        Iterator<RENList> undeletables = getRENLists(false, true).iterator();
        while (undeletables.hasNext()) {
            RENList renList = undeletables.next();
            BorderComboRenderer.Item item = new BorderComboRenderer.Item();
            item.setData(renList);
            retList.add(item);
        }

        ret = retList.toArray(new BorderComboRenderer.Item[retList.size()]);
        return ret;
    }

    private List<RENList> getRENLists(boolean deletable, boolean sort) {
        List<RENList> renLists = renListService.getAllDeletable(deletable);
        if (sort) {
            Collections.sort(renLists, new RENList.NameComparator());
        }
        return renLists;
    }

    private void initService() {
        IHibernateConfigService configService = Lookup.getDefault().lookup(IHibernateConfigService.class);
        renListService = Lookup.getDefault().lookup(IRENListService.class);
        renListService.setConfig(configService.getDefaultConfiguration());
    }

    public JComboBox getEnzymeCombo() {
        return enzymeCombo;
    }
    
    protected void reinitEnzymeCombo(){   
        enzymeCombo.setModel(new DefaultComboBoxModel(getComboItem()));
    }

 
    private void initComponents() {
        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(0, insets.left, 0, 0));
        
        enzymeCombo = new JComboBox(getComboItem());
        enzymeCombo.setRenderer(new BorderComboRenderer());
        newBtn = new JButton(ImageHelper.createImageIcon(ImageNames.PLUS_16));
        setSelectedRENList(IRENListService.COMMON_3_CUTTERS);
        FlatBtn.decorate(newBtn, true);
        
        enzymeListCardPanel = new CardPanel();
        FontMetrics fm = FontUtil.getFontMetrics(enzymeListCardPanel);
        UIUtil.setPreferredHeight(enzymeListCardPanel, fm.getHeight() * 7);       

        LayoutManager layout = null;
        layout = new GridBagLayout();
        this.setLayout(layout);
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        add(enzymeCombo, c);

        c = new GridBagConstraints();
        c.gridy = 0;
        add(newBtn, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0f;
        c.weighty = 1.0f;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        add(enzymeListCardPanel, c);

    }// </editor-fold>                        
}
