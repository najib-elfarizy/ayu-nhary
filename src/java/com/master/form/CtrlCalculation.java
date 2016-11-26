/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.master.form;

import com.control.ExtendedFRMMessage;
import com.dimata.qdep.db.DBException;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.system.*;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import com.master.entity.*;
import com.master.session.SessJadwal;
import com.master.session.ga.*;
import java.io.*;
import java.util.Hashtable;
import java.util.Vector;


public class CtrlCalculation extends Control implements I_Language {
    
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "No perkiraan sudah ada", "Data tidak lengkap"},
	{"Success", "Can not be processed", "Estimation code exist", "Data incomplete"}
    };
    
    private int start;
    private String msgString;
    private Calculation calculation;
    private FrmCalculation frmCalculation;
    private Vector listDetailJadwal;
    private String whereClause;
    int language = LANGUAGE_DEFAULT;
    
    @SuppressWarnings("empty-statement")
    public CtrlCalculation(Hashtable paramValues){
	msgString = "";
        whereClause = "";
	calculation = new Calculation();
	frmCalculation = new FrmCalculation(paramValues, calculation);
        listDetailJadwal =  new Vector();
    }
    
    @SuppressWarnings("static-access")
    private String getSystemMessage(int msgCode){
	switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
		return resultText[language][RSLT_EST_CODE_EXIST];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }
    
    private int getControlMsgId(int msgCode){
	switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                return RSLT_EST_CODE_EXIST;
            default:
		return RSLT_UNKNOWN_ERROR;
	}
    }
    public int getLanguage(){ return language; }

    public void setLanguage(int language){ this.language = language; }
    
    public Calculation getCalculation() { return calculation; }

    public FrmCalculation getForm() { return frmCalculation; }
    
    public Vector getListDetailJadwal() { return listDetailJadwal; }
    
    public String getWhereClause() { return whereClause; }

    public String getMessage(){ return msgString; }

    @Override
    public int getStart() { return start; }
    
    public int action(int cmd, long oidCalculation, String imageDirectory) throws DBException, FileNotFoundException, IOException{
        int errCode = -1;
	int excCode = I_DBExceptionInfo.NO_EXCEPTION;
	int rsCode = RSLT_OK;
	switch(cmd){
            case Command.SUBMIT:
                
                //hapus jadwal lama
                Vector listJadwalLama = PstJadwal.list(0, 0, PstJadwal.fieldNames[PstJadwal.FLD_STATUS]+" = '"+Jadwal.STATUS_NOT_CONFIRM+"'", "");
                if(listJadwalLama.size()>0){
                    for(int i=0; i<listJadwalLama.size(); i++){
                        Jadwal jadwal = (Jadwal)listJadwalLama.get(i);
                        try{
                        PstDetailJadwal.deleteByJadwal(jadwal.getOID());
                        PstJadwal.deleteExc(jadwal.getOID());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                
                
                //memperoleh parameter
                this.calculation = frmCalculation.requestEntityObject(calculation);
                int jumlahpopulasi = calculation.getJumlahPopulasi(); //jumlah populasi
                float probcrossover = calculation.getProbabilitasCrossover()/100; //probabilitas crossover
                float probmutasi = calculation.getProbabilitasMutasi()/100; //probabilitas mutasi
                
                //memperoleh jadwal sebagai populasi sebanyak jumlahpopulasi
                Vector listJadwal = PstJadwal.list(0, jumlahpopulasi, PstJadwal.fieldNames[PstJadwal.FLD_STATUS]+" = '"+Jadwal.STATUS_NEW+"'", "");
                if(jumlahpopulasi>listJadwal.size()){
                    msgString = "Jumlah Populasi yang Tersedia Sebanyak Max "+listJadwal.size();
                    return RSLT_UNKNOWN_ERROR;
                }
                
                //algoritma genetika
                //pembentukan kromosom sebanyak jumlahpopulasi
                //kromosom = jadwal, gen = detailjadwal
                Kromosom[] arrayKromosom = new Kromosom[jumlahpopulasi];
                for(int i=0; i<listJadwal.size(); i++){
                    Jadwal jadwal = (Jadwal)listJadwal.get(i);
                    Vector listDetailJadwal = PstDetailJadwal.list(0, 0, PstDetailJadwal.fieldNames[PstDetailJadwal.FLD_ID_JADWAL]+" = '"+jadwal.getOID()+"'", "");
                    Gen[] arrayGen = new Gen[listDetailJadwal.size()];
                    for(int j=0; j<listDetailJadwal.size(); j++){
                        DetailJadwal detailjadwal = (DetailJadwal)listDetailJadwal.get(j);
                        try{
                            Proyek proyek = PstProyek.fetchExc(detailjadwal.getIdProyek());
                            arrayGen[j] = new Gen(proyek.getOID(), detailjadwal.getNIK(), proyek.getTotal());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    arrayKromosom[i] = new Kromosom(arrayGen);
                }
                //kromosom dengan 2 fitnes terbaik akan dicrossover
                Kromosom[] induk = new Kromosom[2];
                float max = arrayKromosom[0].getNilaiFitness();
                induk[0] = arrayKromosom[0];
                float altmax = arrayKromosom[1].getNilaiFitness();
                induk[1] = arrayKromosom[1];
                System.out.println(arrayKromosom[0].getNilaiFitness());
                System.out.println(arrayKromosom[1].getNilaiFitness());
                for(int i=2; i<arrayKromosom.length; i++){
                    System.out.println(arrayKromosom[i].getNilaiFitness());
                    if(arrayKromosom[i].getNilaiFitness()<max){
                        induk[0] = arrayKromosom[i];
                    }else{
                        if(arrayKromosom[i].getNilaiFitness()<altmax){
                            induk[1] = arrayKromosom[i];
                        }
                    }
                }
                Kromosom crossover = SessJadwal.crossover(induk[0], induk[1], probcrossover);
                System.out.println("Crossover: "+crossover.getNilaiFitness());
                SessJadwal.savejadwal(crossover, "Jadwal Crossover");
                Kromosom mutasi = SessJadwal.mutasi(crossover, probmutasi);
                System.out.println("Mutasi: "+mutasi.getNilaiFitness());
                for(int i=0; i<arrayKromosom.length; i++){
                    SessJadwal.savejadwal(arrayKromosom[i], "Jadwal "+(i+1));
                }
                SessJadwal.savejadwal(mutasi, "Jadwal Mutasi");
                
                break;
            
            case Command.DETAIL:
                
                if(oidCalculation!=0){
                    listDetailJadwal = PstDetailJadwal.list(0, 0, PstDetailJadwal.fieldNames[PstDetailJadwal.FLD_ID_JADWAL]+" = '"+oidCalculation+"'", "");
                }
                
                break;
                
            case Command.SAVE:
                
                if(oidCalculation!=0){
                    try{
                        Jadwal jadwal = PstJadwal.fetchExc(oidCalculation);
                        jadwal.setStatus(Jadwal.STATUS_CONFIRM);
                        jadwal.setNamaJadwal("Jadwal Confirm");
                        Vector listDetailJadwalConfirm = PstDetailJadwal.list(0, 0, PstDetailJadwal.fieldNames[PstDetailJadwal.FLD_ID_JADWAL]+" = '"+oidCalculation+"'", "");
                        Vector listJadwalDelete = PstJadwal.list(0, 0, PstJadwal.fieldNames[PstJadwal.FLD_STATUS]+" = '"+Jadwal.STATUS_NOT_CONFIRM+"'"+
                                " OR "+PstJadwal.fieldNames[PstJadwal.FLD_STATUS]+" = '"+Jadwal.STATUS_CONFIRM+"'", "");
                        if(listJadwalDelete.size()>0){
                            for(int i=0; i<listJadwalDelete.size(); i++){
                                Jadwal delete = (Jadwal)listJadwalDelete.get(i);
                                PstDetailJadwal.deleteByJadwal(delete.getOID());
                                PstJadwal.deleteExc(delete.getOID());
                            }
                        }
                        oidCalculation = PstJadwal.insertExc(jadwal);
                        for(int i=0; i<listDetailJadwalConfirm.size(); i++){
                            DetailJadwal detail = (DetailJadwal)listDetailJadwalConfirm.get(i);
                            detail.setIdJadwal(oidCalculation);
                            try{
                                PstDetailJadwal.insertExc(detail);
                            }catch(Exception e){}
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    
                }
                
                break;
                
            default:
                break;
        }
        return rsCode;
    }
    
 public String getErrMessage(int errCode){
        switch (errCode){
            case ExtendedFRMMessage.ERR_DELETED :
                return "Tidak Bisa Menghapus Karyawan";
            case ExtendedFRMMessage.ERR_SAVED :
                if(frmCalculation.getFieldSize()>0)
                    return "Tidak Bisa Menyimpan Calculation, Karena Data Tidak Lengkap";
                else
                    return "Tidak Bisa Menyimpan Karyawan, Karena ID Sudah Ada";
            default:
                return "Tidak Bisa Menyimpan Calculation";
        }
    }
}

