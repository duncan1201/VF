/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.editor;

/**
 *
 * @author dq
 */
public interface IZoomable {
    /**
     * @param zoom min is 1
     */
    void setZoom(int zoom);
    boolean canZoomOut(int zoom);
}
