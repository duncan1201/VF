/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.schema.api;

/**
 *
 * @author dq
 */
public interface ISchemaUpgrader {
    Integer getFromVersion();
    Integer getToVersion();
    String[] getSqls();
}
