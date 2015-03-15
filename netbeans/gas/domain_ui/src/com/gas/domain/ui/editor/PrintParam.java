/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor;

import java.awt.print.PageFormat;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Locale;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 *
 * @author dq
 */
public class PrintParam {

    private PrintRequestAttributeSet printReqAttSet;
    private PageFormat pageFormat;
    private Integer pageCount;
    private Integer pageNo;
    private Boolean printDate = false;
    private Boolean printPageNo = true;
    private Boolean printName = true;
    private Boolean visibleArea = true;
    private Double zoomScale = 0.6;
    private PropertyChangeSupport propertyChangeSupport;

    public PrintParam() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public PageFormat getPageFormat() {
        return pageFormat;
    }

    public JobName getJobName() {
        JobName ret = (JobName) this.getPrintReqAttSet().get(JobName.class);
        return ret;
    }

    public void setJobName(String name) {
        JobName jobName = new JobName(name, Locale.ENGLISH);
        this.getPrintReqAttSet().add(jobName);
    }

    private void setPageFormat(PageFormat pageFormat) {
        PageFormat old = this.pageFormat;
        this.pageFormat = pageFormat;
        propertyChangeSupport.firePropertyChange("pageFormat", old, this.pageFormat);
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        Integer old = this.pageCount;
        this.pageCount = pageCount;
        propertyChangeSupport.firePropertyChange("pageCount", old, this.pageCount);
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        Integer old = this.pageNo;
        this.pageNo = pageNo;
        propertyChangeSupport.firePropertyChange("pageNo", old, this.pageNo);
    }

    public void setPrintDate(Boolean printDate) {
        Boolean old = this.printDate;
        this.printDate = printDate;
        propertyChangeSupport.firePropertyChange("printDate", old, this.printDate);
    }

    public Boolean getPrintDate() {
        return printDate;
    }

    public PrintRequestAttributeSet getPrintReqAttSet() {
        return printReqAttSet;
    }

    public void setPrintReqAttSet(PrintRequestAttributeSet printReqAttSet) {
        this.printReqAttSet = printReqAttSet;
    }

    public Boolean getPrintPageNo() {
        return printPageNo;
    }

    public void setPrintPageNo(Boolean printPageNo) {
        Boolean old = this.printPageNo;
        this.printPageNo = printPageNo;
        propertyChangeSupport.firePropertyChange("printPageNo", old, this.printPageNo);
    }

    public Double getZoomScale() {
        return zoomScale;
    }

    public void setZoomScale(Double zoomScale) {
        Double old = this.zoomScale;
        this.zoomScale = zoomScale;
        propertyChangeSupport.firePropertyChange("zoomScale", old, this.zoomScale);
    }

    public Boolean getVisibleArea() {
        return visibleArea;
    }

    public void setVisibleArea(Boolean visibleArea) {
        Boolean old = this.visibleArea;
        this.visibleArea = visibleArea;
        propertyChangeSupport.firePropertyChange("visibleArea", old, this.visibleArea);
    }

    public Boolean getPrintName() {
        return printName;
    }

    public void setPrintName(Boolean printName) {
        Boolean old = this.printName;
        this.printName = printName;
        propertyChangeSupport.firePropertyChange("printName", old, this.printName);
    }

    public void mergePageFormat(PageFormat pf) {
        if (pageFormat == null) {
            setPageFormat(pf);
            return;
        }

        EqualsBuilder eb = new EqualsBuilder();
        eb.append(pf.getHeight(), pageFormat.getHeight());
        eb.append(pf.getImageableHeight(), pageFormat.getImageableHeight());
        eb.append(pf.getImageableWidth(), pageFormat.getImageableWidth());
        eb.append(pf.getImageableX(), pageFormat.getImageableX());
        eb.append(pf.getImageableY(), pageFormat.getImageableY());
        eb.append(pf.getOrientation(), pageFormat.getOrientation());
        eb.append(pf.getWidth(), pageFormat.getWidth());

        Boolean same = eb.build();
        if (!same) {
            setPageFormat(pf);
        }
    }
}
