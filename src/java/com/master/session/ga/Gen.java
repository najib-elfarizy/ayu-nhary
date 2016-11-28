/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.master.session.ga;


public class Gen {
    
    private long idProyek;
    private long idPegawai;
    private float waktu;
    
    public Gen(long idProyek, long idPegawai, float waktu){
        this.idProyek = idProyek;
        this.idPegawai = idPegawai;
        this.waktu = waktu;
    }

    /**
     * @return the idProyek
     */
    public long getIdProyek() {
        return idProyek;
    }

    /**
     * @return the idPegawai
     */
    public long getIdPegawai() {
        return idPegawai;
    }

    /**
     * @return the totalWaktu
     */
    public float getWaktu() {
        return waktu;
    }

}
