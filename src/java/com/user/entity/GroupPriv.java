/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.user.entity;

import com.dimata.qdep.entity.Entity;


public class GroupPriv extends Entity {

    private long idGroup;
    private long idPriv;

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

    /**
     * @return the idPriv
     */
    public long getIdPriv() {
        return idPriv;
    }

    /**
     * @param idPriv the idPriv to set
     */
    public void setIdPriv(long idPriv) {
        this.idPriv = idPriv;
    }
}
