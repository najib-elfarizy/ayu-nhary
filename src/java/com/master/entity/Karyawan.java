/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.master.entity;

import com.dimata.qdep.entity.Entity;
import java.util.Date;


public class Karyawan extends Entity {

    private String NamaKaryawan = "";
    private Date TanggalLahir;
    private String Jabatan = "";
    private String Alamat = "";

    /**
     * @return the NamaKaryawan
     */
    public String getNamaKaryawan() {
        return NamaKaryawan;
    }

    /**
     * @param NamaKaryawan the NamaKaryawan to set
     */
    public void setNamaKaryawan(String NamaKaryawan) {
        this.NamaKaryawan = NamaKaryawan;
    }

    /**
     * @return the TanggalLahir
     */
    public Date getTanggalLahir() {
        return TanggalLahir;
    }

    /**
     * @param TanggalLahir the TanggalLahir to set
     */
    public void setTanggalLahir(Date TanggalLahir) {
        this.TanggalLahir = TanggalLahir;
    }

    /**
     * @return the Jabatan
     */
    public String getJabatan() {
        return Jabatan;
    }

    /**
     * @param Jabatan the Jabatan to set
     */
    public void setJabatan(String Jabatan) {
        this.Jabatan = Jabatan;
    }

    /**
     * @return the Alamat
     */
    public String getAlamat() {
        return Alamat;
    }

    /**
     * @param Alamat the Alamat to set
     */
    public void setAlamat(String Alamat) {
        this.Alamat = Alamat;
    }
    
}
