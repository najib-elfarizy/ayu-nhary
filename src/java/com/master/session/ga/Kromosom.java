/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.master.session.ga;

import java.util.Vector;

public class Kromosom {
    
    Gen[] arrayGen;
    float nilaiFitness;
    
    public Kromosom(Gen[] arrayGen){
        this.arrayGen = arrayGen;
    }
    
    public Gen getGen(int index){
        return arrayGen[index];
    }
    
    public Gen[] getArrayGen(){
        return arrayGen;
    } 
    
    public int getSize(){
        return arrayGen.length;
    }
    
    public float getNilaiFitness(){
        Vector listPegawai = new Vector();
        float totalWaktu = 0;
        for(int i=0; i<arrayGen.length; i++){
            if(!listPegawai.contains(arrayGen[i].getIdPegawai())){
                listPegawai.add(arrayGen[i].getIdPegawai());
            }
            totalWaktu = totalWaktu + arrayGen[i].getWaktu();
        }
        
        int n = listPegawai.size();
        float m = totalWaktu/n;
        float jumlah = 0;
        for(int i=0; i<arrayGen.length; i++){
            float x = (arrayGen[i].getWaktu() - m) * (arrayGen[i].getWaktu() - m);
            jumlah = jumlah + x;
        }
        float sqrt = (float) Math.sqrt(jumlah);
        this.nilaiFitness = sqrt/(n-1);
        return nilaiFitness/100;
    }

}
