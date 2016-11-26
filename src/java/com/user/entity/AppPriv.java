/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.user.entity;

import com.dimata.qdep.entity.Entity;


public class AppPriv extends Entity {
    
    public static final int FLD_MAINTENANCE_MASTER_DATA = 1;
    public static final int FLD_USER_MANAGEMENT         = 2;
    
    public static String[] privNames = new String[]{
        "","Maintenance Master Data","User Management"
    };
    
    private String PrivName="";
    private String Description="";

    /**
     * @return the PrivName
     */
    public String getPrivName() {
        return PrivName;
    }

    /**
     * @param PrivName the PrivName to set
     */
    public void setPrivName(String PrivName) {
        this.PrivName = PrivName;
    }

    /**
     * @return the Description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * @param Description the Description to set
     */
    public void setDescription(String Description) {
        this.Description = Description;
    }

}
