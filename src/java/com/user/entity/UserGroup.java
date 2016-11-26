/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.user.entity;

import com.dimata.qdep.entity.Entity;


public class UserGroup extends Entity {

    private long idUser;
    private long idGroup;

    /**
     * @return the idUser
     */
    public long getIdUser() {
        return idUser;
    }

    /**
     * @param idUser the idUser to set
     */
    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    /**
     * @return the idGroup
     */
    public long getIdGroup() {
        return idGroup;
    }

    /**
     * @param idGroup the idGroup to set
     */
    public void setIdGroup(long idGroup) {
        this.idGroup = idGroup;
    }
    
}
