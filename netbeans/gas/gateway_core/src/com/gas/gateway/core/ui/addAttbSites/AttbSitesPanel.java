/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.addAttbSites;

import com.gas.gateway.core.service.api.PrimerAdapter;
import com.gas.gateway.core.service.api.RecomType;
import com.gas.gateway.core.ui.api.IAttBSiteUIServices;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class AttbSitesPanel extends JPanel {

    private final String ONE = "ONE";
    private final String PRO2 = "PRO2";
    private final String PRO3 = "PRO3";
    private final String PRO4 = "PRO4";
    private final String THREE = "THREE";    
    private OnePanel onePanel;
    private Pro2Panel pro2Panel;
    private Pro3Panel pro3Panel;
    private Pro4Panel pro4Panel;
    private ThreePanel threePanel;    

    AttbSitesPanel() {
        TitledBorder border = BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "attB Sites", TitledBorder.LEFT, TitledBorder.TOP);
        setBorder(border);

        LayoutManager layout = null;
        layout = new CardLayout();
        setLayout(layout);

        onePanel = new OnePanel();
        add(onePanel, ONE);
        pro2Panel = new Pro2Panel();
        add(pro2Panel, PRO2);
        pro3Panel = new Pro3Panel();
        add(pro3Panel, PRO3);
        pro4Panel = new Pro4Panel();
        add(pro4Panel, PRO4);
        threePanel = new ThreePanel();
        add(threePanel, THREE);

        hookupListeners();
    }

    private void hookupListeners() {
        onePanel.b1_b2.addItemListener(new AttbSitesPanelListeners.AttbSiteRadioBtnListener());
        pro2Panel.b1_b5r.addItemListener(new AttbSitesPanelListeners.AttbSiteRadioBtnListener());
        pro2Panel.b5_b2.addItemListener(new AttbSitesPanelListeners.AttbSiteRadioBtnListener());
        pro3Panel.b1_b4.addItemListener(new AttbSitesPanelListeners.AttbSiteRadioBtnListener());
        pro3Panel.b4r_b3r.addItemListener(new AttbSitesPanelListeners.AttbSiteRadioBtnListener());
        pro3Panel.b3_b2.addItemListener(new AttbSitesPanelListeners.AttbSiteRadioBtnListener());
        pro4Panel.b1_b5r.addItemListener(new AttbSitesPanelListeners.AttbSiteRadioBtnListener());
        pro4Panel.b5_b4.addItemListener(new AttbSitesPanelListeners.AttbSiteRadioBtnListener());
        pro4Panel.b4r_b3r.addItemListener(new AttbSitesPanelListeners.AttbSiteRadioBtnListener());
        pro4Panel.b3_b2.addItemListener(new AttbSitesPanelListeners.AttbSiteRadioBtnListener());

        threePanel.b1_b2.addItemListener(new AttbSitesPanelListeners.AttbSiteRadioBtnListener());
        threePanel.b2r_b3.addItemListener(new AttbSitesPanelListeners.AttbSiteRadioBtnListener());
        threePanel.b4_b1r.addItemListener(new AttbSitesPanelListeners.AttbSiteRadioBtnListener());
    }

    protected PrimerAdapter getLeftPrimerAdapter() {
        PrimerAdapter ret = null;
        if (onePanel.isVisible()) {
            ret = onePanel.getLeft();
        } else if (pro2Panel.isVisible()) {
            ret = pro2Panel.getLeft();
        } else if (pro3Panel.isVisible()) {
            ret = pro3Panel.getLeft();
        } else if (pro4Panel.isVisible()) {
            ret = pro4Panel.getLeft();
        } else if(threePanel.isVisible()){
            ret = threePanel.getLeft();
        }
        return ret;
    }

    protected PrimerAdapter getRightPrimerAdapter() {
        PrimerAdapter ret = null;
        if (onePanel.isVisible()) {
            ret = onePanel.getRight();
        } else if (pro2Panel.isVisible()) {
            ret = pro2Panel.getRight();
        } else if (pro3Panel.isVisible()) {
            ret = pro3Panel.getRight();
        } else if (pro4Panel.isVisible()) {
            ret = pro4Panel.getRight();
        } else if(threePanel.isVisible()){
            ret = threePanel.getRight();
        }
        return ret;
    }

    public void showRecombinationType(RecomType recombType) {
        CardLayout layout = (CardLayout) getLayout();
        if (recombType == RecomType.GW) {
            layout.show(this, ONE);
        } else if (recombType == RecomType.Pro2) {
            layout.show(this, PRO2);
        } else if (recombType == RecomType.Pro3) {
            layout.show(this, PRO3);
        } else if (recombType == RecomType.Pro4) {
            layout.show(this, PRO4);
        } else if (recombType == RecomType.ThreeFrag) {
            layout.show(this, THREE);
        }
    }
}

class OnePanel extends JPanel {
    
    private IAttBSiteUIServices service = Lookup.getDefault().lookup(IAttBSiteUIServices.class);
    protected AttbSiteRadioBtn b1_b2;
    
    public OnePanel(){        
        b1_b2 = AttbSiteRadioBtnFactory.create(PrimerAdapter.attB1.clone(), PrimerAdapter.attB2.clone(), true, false, true);
        add(b1_b2);     
    }
    
    protected PrimerAdapter getLeft() {      
        return b1_b2.getLeft();
    }

    protected PrimerAdapter getRight() {       
        return b1_b2.getRight();
    }    
}

class Pro2Panel extends JPanel {

    protected AttbSiteRadioBtn b1_b5r;
    protected AttbSiteRadioBtn b5_b2;

    public Pro2Panel() {

        b1_b5r = AttbSiteRadioBtnFactory.create(PrimerAdapter.attB1.clone(), PrimerAdapter.attB5r.clone(), true, true, true);
        add(b1_b5r);

        b5_b2 = AttbSiteRadioBtnFactory.create(PrimerAdapter.attB5.clone(), PrimerAdapter.attB2.clone(), true, false);

        add(b5_b2);

        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(b1_b5r.getRealBtn());
        btnGroup.add(b5_b2.getRealBtn());
    }

    protected PrimerAdapter getLeft() {
        AttbSiteRadioBtn selected = getSelectedRadioBtn();        
        PrimerAdapter ret = null;
        if(selected != null){
            ret = selected.getLeft();
        }
        return ret;
    }

    protected PrimerAdapter getRight() {
        AttbSiteRadioBtn selected = getSelectedRadioBtn();
        PrimerAdapter ret = null;
        if(selected != null){
            ret = selected.getRight();
        }
        return ret;
    }

    private AttbSiteRadioBtn getSelectedRadioBtn() {
        AttbSiteRadioBtn ret = null;
        if (b1_b5r.isSelected()) {
            ret = b1_b5r;
        } else if (b5_b2.isSelected()) {
            ret = b5_b2;
        }
        return ret;
    }
}

class ThreePanel extends JPanel {

    protected AttbSiteRadioBtn b4_b1r; // 3-fragment
    protected AttbSiteRadioBtn b1_b2; // 3-fragment
    protected AttbSiteRadioBtn b2r_b3;  // 3-fragment

    public ThreePanel() {
        b4_b1r = AttbSiteRadioBtnFactory.create(PrimerAdapter.attB4_3FRAG.clone(), PrimerAdapter.attB1r.clone(), true, true, true);
        add(b4_b1r);

        b1_b2 = AttbSiteRadioBtnFactory.create(PrimerAdapter.attB1.clone(), PrimerAdapter.attB2.clone(), true, false);
        add(b1_b2);

        b2r_b3 = AttbSiteRadioBtnFactory.create(PrimerAdapter.attB2r.clone(), PrimerAdapter.attB3_3FRAG.clone(), false, false);
        add(b2r_b3);

        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(b4_b1r.getRealBtn());
        btnGroup.add(b1_b2.getRealBtn());
        btnGroup.add(b2r_b3.getRealBtn());
    }

    protected PrimerAdapter getLeft() {
        AttbSiteRadioBtn selected = getSelectedRadioBtn();
        PrimerAdapter ret = null;
        if(selected != null){
            ret = selected.getLeft();
        }
        return ret;
    }

    protected PrimerAdapter getRight() {
        AttbSiteRadioBtn selected = getSelectedRadioBtn();
        PrimerAdapter ret = null;
        if(selected != null){
            ret = selected.getRight();
        }
        return ret;
    }

    private AttbSiteRadioBtn getSelectedRadioBtn() {
        AttbSiteRadioBtn ret = null;
        if (b4_b1r.isSelected()) {
            ret = b4_b1r;
        } else if (b1_b2.isSelected()) {
            ret = b1_b2;
        } else if (b2r_b3.isSelected()) {
            ret = b2r_b3;
        }
        return ret;
    }
}

class Pro3Panel extends JPanel {

    protected AttbSiteRadioBtn b1_b4; // 3-fragment
    protected AttbSiteRadioBtn b4r_b3r; // 3-fragment
    protected AttbSiteRadioBtn b3_b2;  // 3-fragment

    Pro3Panel() {
        b1_b4 = AttbSiteRadioBtnFactory.create(PrimerAdapter.attB1.clone(), PrimerAdapter.attB4.clone(), true, false, true);
        add(b1_b4);

        b4r_b3r = AttbSiteRadioBtnFactory.create(PrimerAdapter.attB4r.clone(), PrimerAdapter.attB3r.clone(), false, true);
        add(b4r_b3r);

        b3_b2 = AttbSiteRadioBtnFactory.create(PrimerAdapter.attB3.clone(), PrimerAdapter.attB2.clone(), true, false);
        add(b3_b2);

        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(b1_b4.getRealBtn());
        btnGroup.add(b4r_b3r.getRealBtn());
        btnGroup.add(b3_b2.getRealBtn());
    }

    protected PrimerAdapter getLeft() {
        AttbSiteRadioBtn selected = getSelectedRadioBtn();
        PrimerAdapter ret = null;
        if(selected != null){
            ret = selected.getLeft();
        }
        return ret;
    }

    protected PrimerAdapter getRight() {
        AttbSiteRadioBtn selected = getSelectedRadioBtn();
        PrimerAdapter ret = null;
        if(selected != null){
            ret = selected.getRight();
        }
        return ret;
    }

    private AttbSiteRadioBtn getSelectedRadioBtn() {
        AttbSiteRadioBtn ret = null;
        if (b1_b4.isSelected()) {
            ret = b1_b4;
        } else if (b4r_b3r.isSelected()) {
            ret = b4r_b3r;
        } else if (b3_b2.isSelected()) {
            ret = b3_b2;
        }
        return ret;
    }
}

class Pro4Panel extends JPanel {

    protected AttbSiteRadioBtn b1_b5r;
    protected AttbSiteRadioBtn b5_b4;
    protected AttbSiteRadioBtn b4r_b3r;
    protected AttbSiteRadioBtn b3_b2;

    public Pro4Panel() {
        b1_b5r = AttbSiteRadioBtnFactory.create(PrimerAdapter.attB1.clone(), PrimerAdapter.attB5r.clone(), true, true, true);
        b5_b4 = AttbSiteRadioBtnFactory.create(PrimerAdapter.attB5.clone(), PrimerAdapter.attB4.clone(), true, false);
        b4r_b3r = AttbSiteRadioBtnFactory.create(PrimerAdapter.attB4r.clone(), PrimerAdapter.attB3r.clone(), false, true);
        b3_b2 = AttbSiteRadioBtnFactory.create(PrimerAdapter.attB3.clone(), PrimerAdapter.attB2.clone(), true, false);

        add(b1_b5r);
        add(b5_b4);
        add(b4r_b3r);
        add(b3_b2);

        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(b1_b5r.getRealBtn());
        btnGroup.add(b5_b4.getRealBtn());
        btnGroup.add(b4r_b3r.getRealBtn());
        btnGroup.add(b3_b2.getRealBtn());
    }

    protected PrimerAdapter getLeft() {
        AttbSiteRadioBtn selected = getSelectedRadioBtn();
        PrimerAdapter ret = null;
        if(selected != null){
            ret = selected.getLeft();
        }
        return ret;
    }

    private AttbSiteRadioBtn getSelectedRadioBtn() {
        AttbSiteRadioBtn ret = null;
        if (b1_b5r.isSelected()) {
            ret = b1_b5r;
        } else if (b5_b4.isSelected()) {
            ret = b5_b4;
        } else if (b4r_b3r.isSelected()) {
            ret = b4r_b3r;
        } else if (b3_b2.isSelected()) {
            ret = b3_b2;
        }
        return ret;
    }

    protected PrimerAdapter getRight() {
        AttbSiteRadioBtn selected = getSelectedRadioBtn();
        PrimerAdapter ret = null;
        if(selected != null){
            ret = selected.getRight();
        }
        return ret;
    }
}