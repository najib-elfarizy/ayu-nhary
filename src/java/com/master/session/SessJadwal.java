/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.master.session;

import com.master.entity.*;
import com.master.session.ga.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class SessJadwal {
    
    public static Kromosom crossover(Kromosom induk1, Kromosom induk2, float probCrossover){
        //one point crossover
        //bangkitkan titik acak gen sebanyak 1 sampai jumlah gen - 1;
        Gen[] gen_induk1 = induk1.getArrayGen();
        Gen[] gen_induk2 = induk2.getArrayGen();
        Gen[] gen_anak = new Gen[gen_induk1.length];
        Random ran = new Random();
        for(int i=0; i<gen_induk1.length; i++){
            gen_anak[i] = crossover_karyawan(gen_induk1[i], gen_induk2[i]);
        }
        Kromosom anak = new Kromosom(gen_anak);
        return anak;
        
    }
    
    private static Gen crossover_karyawan(Gen gen1, Gen gen2){
        //id karyawan gen2 ditukar selama memenuhi syarat tidak ada id yang sama pada gen1
        gen1 = new Gen(gen1.getIdProyek(), gen2.getIdKaryawan(), gen1.getWaktu());
        return gen1;
    }
    
    
    public static Kromosom mutasi(Kromosom kromosom, float prob_mutasi){
        //hitung total jumlah gen dalam kromosom yang dimutasi
        int jumlah_total = 0;
        Gen[] gen = kromosom.getArrayGen();
        for(int j=0; j<gen.length; j++){
            jumlah_total = jumlah_total + 1;
        }
        //jumlah mutasi total jumlah gen dikali probabilitas mutasi
        int jumlah_mutasi = (int)(jumlah_total * prob_mutasi);
        //menentukan gen yang dimutasi sejumlah jumlah mutasi dan memberi nilai random sebesar 1 sampai jumlah total gen
        Random ran = new Random();
        ArrayList<Integer> nilai_random = new ArrayList<Integer>(); 
        for(int i=0; i<jumlah_mutasi; i++){
            nilai_random.add(1 + (ran.nextInt()*jumlah_total)); 
        }
        int index_gen  = 0;
        //karyawan tidak boleh sama dengan induk 1
        ArrayList<Long> tempKaryawan = new ArrayList<Long>();            
        for(int j=0; j<gen.length; j++){
            tempKaryawan.add(gen[j].getIdKaryawan());
        }
        for(int j=0; j<gen.length; j++){
            index_gen = index_gen + 1;
            gen[j] = mutasi_karyawan(gen[j], tempKaryawan);
        }
        kromosom = new Kromosom(gen);
        return kromosom;
    }
    
    private static Gen mutasi_karyawan(Gen gen, ArrayList<Long> tempKaryawan){
        try{
            Proyek proyek = PstProyek.fetchExc(gen.getIdProyek());
            Karyawan karyawan = new Karyawan();
            Vector listKaryawan = PstKaryawan.listAll();
            Random ran = new Random();
            int num = ran.nextInt(listKaryawan.size());        
            karyawan = (Karyawan)listKaryawan.get(num);
            gen = new Gen(proyek.getOID(), karyawan.getOID(), gen.getWaktu());
        }catch(Exception e){
            e.printStackTrace();
        }
        return gen;   
    }
    
    public static void savejadwal(Kromosom kromosom, String namajadwal){
        Jadwal jadwal = new Jadwal();
        jadwal.setNamaJadwal(namajadwal);
        jadwal.setNilaiFitness(kromosom.getNilaiFitness());
        System.out.println(kromosom.getNilaiFitness());
        jadwal.setStatus(Jadwal.STATUS_NOT_CONFIRM);
        try{
            long oidjadwal = PstJadwal.insertExc(jadwal);
            Gen[] arrayGen = kromosom.getArrayGen();
            for(int j=0; j<arrayGen.length; j++){
                Proyek proyek = PstProyek.fetchExc(arrayGen[j].getIdProyek());
                DetailJadwal detailjadwal = new DetailJadwal();
                detailjadwal.setIdJadwal(oidjadwal);
                detailjadwal.setIdProyek(proyek.getOID());
                detailjadwal.setJamMulai(proyek.getWaktuMulai());
                detailjadwal.setJamSelesai(proyek.getWaktuSelesai());
                detailjadwal.setNIK(arrayGen[j].getIdKaryawan());
                PstDetailJadwal.insertExc(detailjadwal);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
   
}
