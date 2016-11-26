/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.master.entity;

import com.dimata.qdep.entity.Entity;


public class Calculation extends Entity {
    
    private int JumlahPopulasi = 2;
    private float ProbabilitasCrossover = 80;
    private float ProbabilitasMutasi = 80;

    /**
     * @return the JumlahPopulasi
     */
    public int getJumlahPopulasi() {
        return JumlahPopulasi;
    }

    /**
     * @param JumlahPopulasi the JumlahPopulasi to set
     */
    public void setJumlahPopulasi(int JumlahPopulasi) {
        this.JumlahPopulasi = JumlahPopulasi;
    }

    /**
     * @return the ProbabilitasCrossover
     */
    public float getProbabilitasCrossover() {
        return ProbabilitasCrossover;
    }

    /**
     * @param ProbabilitasCrossover the ProbabilitasCrossover to set
     */
    public void setProbabilitasCrossover(float ProbabilitasCrossover) {
        this.ProbabilitasCrossover = ProbabilitasCrossover;
    }

    /**
     * @return the ProbabilitasMutasi
     */
    public float getProbabilitasMutasi() {
        return ProbabilitasMutasi;
    }

    /**
     * @param ProbabilitasMutasi the ProbabilitasMutasi to set
     */
    public void setProbabilitasMutasi(float ProbabilitasMutasi) {
        this.ProbabilitasMutasi = ProbabilitasMutasi;
    }

}
