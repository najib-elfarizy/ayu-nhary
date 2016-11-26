/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.master.entity;

import com.dimata.qdep.entity.Entity;


public class Jadwal extends Entity {
    
    public static int STATUS_CONFIRM = 1;
    public static int STATUS_NOT_CONFIRM = 2;
    public static int STATUS_NEW = 0;
    
    private String NamaJadwal = "";
    private int Status;
    private float NilaiFitness;

    /**
     * @return the NamaJadwal
     */
    public String getNamaJadwal() {
        return NamaJadwal;
    }

    /**
     * @param NamaJadwal the NamaJadwal to set
     */
    public void setNamaJadwal(String NamaJadwal) {
        this.NamaJadwal = NamaJadwal;
    }

    /**
     * @return the NilaiFitness
     */
    public float getNilaiFitness() {
        return NilaiFitness;
    }

    /**
     * @param NilaiFitness the NilaiFitness to set
     */
    public void setNilaiFitness(float NilaiFitness) {
        this.NilaiFitness = NilaiFitness;
    }

    /**
     * @return the Status
     */
    public int getStatus() {
        return Status;
    }

    /**
     * @param Status the Status to set
     */
    public void setStatus(int Status) {
        this.Status = Status;
    }

}
