/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.msa;

import com.gas.common.ui.util.CommonUtil;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author dq
 */
public class MSASetting implements Cloneable{
    private Integer hibernateId;
    private int zoom = 100;
    private int seqLogoHeight = 110;
    private int qualityHeight = 30;
    private int coverageHeight = 45;
    private boolean coverageDisplay = true;
    private boolean seqLogoDisplay = true;
    private boolean qualityDisplay = true;
    private String selectedTab = "alignment";
    private String treeEdgeTransform = "NONE";
    private String treeForm = "RECTANGLE";
    private boolean treeEdgeLabelDisplay = false;
    private boolean nodeLabelDisplay = true;
    private int sigDigits = 3;
    private int lineWidth = 1;
    private transient PropertyChangeSupport propertyChangeSupport;

    public MSASetting(){
        propertyChangeSupport = new PropertyChangeSupport(this);
    }        
    
    public void addPropertyChangeListener(PropertyChangeListener l){
        propertyChangeSupport.addPropertyChangeListener(l);
    }
    
    @Override
    public MSASetting clone(){
        MSASetting ret = CommonUtil.cloneSimple(this);
        return ret;
    }
    
    public void touchAll(){
    }
    
    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getSigDigits() {
        return sigDigits;
    }

    public void setSigDigits(int sigDigits) {
        this.sigDigits = sigDigits;
    }

    public String getTreeForm() {
        return treeForm;
    }

    public void setTreeForm(String treeForm) {
        this.treeForm = treeForm;
    }

    public boolean isTreeEdgeLabelDisplay() {
        return treeEdgeLabelDisplay;
    }

    public void setTreeEdgeLabelDisplay(boolean treeEdgeLabelDisplay) {
        this.treeEdgeLabelDisplay = treeEdgeLabelDisplay;
    }

    public boolean isNodeLabelDisplay() {
        return nodeLabelDisplay;
    }

    public void setNodeLabelDisplay(boolean nodeLabelDisplay) {
        this.nodeLabelDisplay = nodeLabelDisplay;
    }

    public String getSelectedTab() {
        return selectedTab;
    }

    public String getTreeEdgeTransform() {
        return treeEdgeTransform;
    }

    public void setTreeEdgeTransform(String treeEdgeTransform) {
        this.treeEdgeTransform = treeEdgeTransform;
    }

    public int getSeqLogoHeight() {
        return seqLogoHeight;
    }

    public void setSeqLogoHeight(int seqLogoHeight) {
        this.seqLogoHeight = seqLogoHeight;
    }

    public int getQualityHeight() {
        return qualityHeight;
    }

    public void setQualityHeight(int qualityHeight) {
        this.qualityHeight = qualityHeight;
    }

    public int getCoverageHeight() {
        return coverageHeight;
    }

    public void setCoverageHeight(int coverageHeight) {
        this.coverageHeight = coverageHeight;
    }

    public boolean isCoverageDisplay() {
        return coverageDisplay;
    }

    public void setCoverageDisplay(boolean coverageDisplay) {
        this.coverageDisplay = coverageDisplay;
    }

    public boolean isSeqLogoDisplay() {
        return seqLogoDisplay;
    }

    public void setSeqLogoDisplay(boolean seqLogoDisplay) {
        this.seqLogoDisplay = seqLogoDisplay;
    }

    public boolean isQualityDisplay() {
        return qualityDisplay;
    }

    public void setQualityDisplay(boolean qualityDisplay) {
        this.qualityDisplay = qualityDisplay;
    }
    
    /**
     * @param selectedTab valid values are: tree, alignment
     */
    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        int old = this.zoom;
        this.zoom = zoom;
        propertyChangeSupport.firePropertyChange("zoom", old, this.zoom);
    }
    
}
