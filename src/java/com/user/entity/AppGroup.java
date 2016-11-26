/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.user.entity;

import com.dimata.qdep.entity.Entity;


public class AppGroup extends Entity {
    
    private String GroupName="";
    private String Description="";

    /**
     * @return the GroupName
     */
    public String getGroupName() {
        return GroupName;
    }

    /**
     * @param GroupName the GroupName to set
     */
    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
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
