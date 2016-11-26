/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.master.entity;

import com.dimata.qdep.entity.Entity;


public class Keahlian extends Entity {
    
    private String NamaKeahlian = "";

    /**
     * @return the NamaKeahlian
     */
    public String getNamaKeahlian() {
        return NamaKeahlian;
    }

    /**
     * @param NamaKeahlian the NamaKeahlian to set
     */
    public void setNamaKeahlian(String NamaKeahlian) {
        this.NamaKeahlian = NamaKeahlian;
    }

}
