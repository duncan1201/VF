/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service.api;

/**
 *
 * @author dq
 */
public interface IAttPosService {
    Integer getAttPos(AttSite site, AttSite site2);
    Integer getAttPos2(AttSite site, AttSite site2);
}
