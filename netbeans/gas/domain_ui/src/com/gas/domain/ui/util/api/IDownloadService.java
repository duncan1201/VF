/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.util.api;

/**
 *
 * @author dq
 */
public interface IDownloadService {

    public enum AUTHORITY {

        NCBI
    };

    void downloadAndOpen(String db, String id);
}
