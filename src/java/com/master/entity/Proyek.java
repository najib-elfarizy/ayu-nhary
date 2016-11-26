/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.master.entity;

import com.dimata.qdep.entity.Entity;
import java.util.Date;


public class Proyek extends Entity {
    
    private String NamaProyek = "";
    private Date WaktuMulai = new Date();
    private Date WaktuSelesai = new Date();
    private float Total;

    /**
     * @return the NamaProyek
     */
    public String getNamaProyek() {
        return NamaProyek;
    }

    /**
     * @param NamaProyek the NamaProyek to set
     */
    public void setNamaProyek(String NamaProyek) {
        this.NamaProyek = NamaProyek;
    }

    /**
     * @return the WaktuMulai
     */
    public Date getWaktuMulai() {
        return WaktuMulai;
    }

    /**
     * @param WaktuMulai the WaktuMulai to set
     */
    public void setWaktuMulai(Date WaktuMulai) {
        this.WaktuMulai = WaktuMulai;
    }

    /**
     * @return the WaktuSelesai
     */
    public Date getWaktuSelesai() {
        return WaktuSelesai;
    }

    /**
     * @param WaktuSelesai the WaktuSelesai to set
     */
    public void setWaktuSelesai(Date WaktuSelesai) {
        this.WaktuSelesai = WaktuSelesai;
    }

    /**
     * @return the Total
     */
    public float getTotal() {
        return Total;
    }

    /**
     * @param Total the Total to set
     */
    public void setTotal(float Total) {
        this.Total = Total;
    }

}
