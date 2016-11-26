/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.master.entity;

import com.dimata.qdep.entity.Entity;
import java.util.Date;


public class DetailJadwal extends Entity {
    
    private long IdJadwal;
    private long IdProyek;
    private long NIK;
    private Date JamMulai = new Date();
    private Date JamSelesai = new Date();

    /**
     * @return the IdJadwal
     */
    public long getIdJadwal() {
        return IdJadwal;
    }

    /**
     * @param IdJadwal the IdJadwal to set
     */
    public void setIdJadwal(long IdJadwal) {
        this.IdJadwal = IdJadwal;
    }

    /**
     * @return the IdProyek
     */
    public long getIdProyek() {
        return IdProyek;
    }

    /**
     * @param IdProyek the IdProyek to set
     */
    public void setIdProyek(long IdProyek) {
        this.IdProyek = IdProyek;
    }

    /**
     * @return the NIK
     */
    public long getNIK() {
        return NIK;
    }

    /**
     * @param NIK the NIK to set
     */
    public void setNIK(long NIK) {
        this.NIK = NIK;
    }

    /**
     * @return the JamMulai
     */
    public Date getJamMulai() {
        return JamMulai;
    }

    /**
     * @param JamMulai the JamMulai to set
     */
    public void setJamMulai(Date JamMulai) {
        this.JamMulai = JamMulai;
    }

    /**
     * @return the JamSelesai
     */
    public Date getJamSelesai() {
        return JamSelesai;
    }

    /**
     * @param JamSelesai the JamSelesai to set
     */
    public void setJamSelesai(Date JamSelesai) {
        this.JamSelesai = JamSelesai;
    }
}
