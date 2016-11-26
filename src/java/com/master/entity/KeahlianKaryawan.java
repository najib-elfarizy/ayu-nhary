/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.master.entity;

import com.dimata.qdep.entity.Entity;


public class KeahlianKaryawan extends Entity {
    
    private long IdKaryawan;
    private long IdKeahlian;

    /**
     * @return the IdKaryawan
     */
    public long getIdKaryawan() {
        return IdKaryawan;
    }

    /**
     * @param IdKaryawan the IdKaryawan to set
     */
    public void setIdKaryawan(long IdKaryawan) {
        this.IdKaryawan = IdKaryawan;
    }

    /**
     * @return the IdKeahlian
     */
    public long getIdKeahlian() {
        return IdKeahlian;
    }

    /**
     * @param IdKeahlian the IdKeahlian to set
     */
    public void setIdKeahlian(long IdKeahlian) {
        this.IdKeahlian = IdKeahlian;
    }

}
