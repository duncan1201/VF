/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import java.util.Date;

/**
 *
 * @author dq
 */
class RegisCode {

    long serialNo;
    String email;
    Date purchasedDate;
    Date maxProductReleaseDate;
    String productName;
    String productEdition;

    RegisCode(){}
    
    //static email [product name][product edition][max product release date] [serial no]
    RegisCode(String clear) {
        String[] splits = clear.split(" ");
        email = splits[1];
        productName = splits[2];
        productEdition = splits[3];
        maxProductReleaseDate = new Date(Long.parseLong(splits[4]));
        serialNo = Long.parseLong(splits[5]);
    }
    
    static boolean isRegisCode(String secret){
        boolean ret = false;
        String clear = LicenseUtil.decrypt(secret);
        if (clear != null) {
            String[] splits = clear.split(" ");
            ret = splits.length > 0 && splits[0].equals("static");
        }
        return ret;    
    }
    
    static RegisCode parse(String hex){
        String clear = LicenseUtil.decrypt(hex);
        RegisCode ret = new RegisCode(clear);
        return ret;
    }

    long getSerialNo() {
        return serialNo;
    }

    void setSerialNo(long serialNo) {
        this.serialNo = serialNo;
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    Date getPurchasedDate() {
        return purchasedDate;
    }

    void setPurchasedDate(Date purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    Date getMaxProductReleaseDate() {
        return maxProductReleaseDate;
    }

    void setMaxProductReleaseDate(Date maxProductReleaseDate) {
        this.maxProductReleaseDate = maxProductReleaseDate;
    }

    String getProductName() {
        return productName;
    }

    void setProductName(String productName) {
        this.productName = productName;
    }

    String getProductEdition() {
        return productEdition;
    }

    void setProductEdition(String productEdition) {
        this.productEdition = productEdition;
    }
}
