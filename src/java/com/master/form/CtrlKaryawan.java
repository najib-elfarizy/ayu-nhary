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
import java.io.*;
import java.util.Hashtable;
import java.util.Vector;


public class CtrlKaryawan extends Control implements I_Language {
    
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
    private Karyawan karyawan;
    private PstKaryawan pstKaryawan;
    private FrmKaryawan frmKaryawan;
    private String whereClause;
    int language = LANGUAGE_DEFAULT;
    
    @SuppressWarnings("empty-statement")
    public CtrlKaryawan(Hashtable paramValues){
	msgString = "";
        whereClause = "";
	karyawan = new Karyawan();
	try{
            pstKaryawan = new PstKaryawan(0);
	}catch(Exception e){;}
	frmKaryawan = new FrmKaryawan(paramValues, karyawan);
    }
    
    @SuppressWarnings("static-access")
    private String getSystemMessage(int msgCode){
	switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
		this.frmKaryawan.addError(FrmKaryawan.FRM_NAMA, resultText[language][RSLT_EST_CODE_EXIST] );
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
    
    public Karyawan getKaryawan() { return karyawan; }

    public FrmKaryawan getForm() { return frmKaryawan; }
    
    public String getWhereClause() { return whereClause; }

    public String getMessage(){ return msgString; }

    @Override
    public int getStart() { return start; }
    
    public int action(int cmd, long oidKaryawan, String imageDirectory) throws DBException, FileNotFoundException, IOException{
        int errCode = -1;
	int excCode = I_DBExceptionInfo.NO_EXCEPTION;
	int rsCode = RSLT_OK;
        String NIK = "";
	switch(cmd){ 
            case Command.ADD:
                break;
            case Command.SAVE:
                if(oidKaryawan!=0){
                    try{
                        karyawan = PstKaryawan.fetchExc(oidKaryawan);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                karyawan = frmKaryawan.requestEntityObject(karyawan);
                if(oidKaryawan==0){
                    try{
                        oidKaryawan = PstKaryawan.insertExc(karyawan);
                        KeahlianKaryawan kk = frmKaryawan.requestKeahlianKaryawan(oidKaryawan);
                        oidKaryawan = PstKeahlianKaryawan.insertExc(kk);
                        if(oidKaryawan!=0){  msgString = "Data Berhasil Disimpan";
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    try{
                        oidKaryawan = PstKaryawan.updateExc(karyawan);
                        KeahlianKaryawan kk = frmKaryawan.requestKeahlianKaryawan(oidKaryawan);
                        oidKaryawan = PstKeahlianKaryawan.updateExc(kk);
                        if(oidKaryawan!=0){ msgString = "Data Berhasil Diperbaharui";
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case Command.EDIT:
                if(oidKaryawan!=0){
                    try{
                        karyawan = PstKaryawan.fetchExc(oidKaryawan);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case Command.DELETE:
                if(oidKaryawan!=0){
                    try{
                        long status = PstKaryawan.deleteExc(oidKaryawan);
                        if(status==1) msgString = "Data Berhasil Dihapus";
                        else msgString = "Error Hapus Data, Data Mungkin Digunakan";
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
                if(frmKaryawan.getFieldSize()>0)
                    return "Tidak Bisa Menyimpan Karyawan, Karena Data Tidak Lengkap";
                else
                    return "Tidak Bisa Menyimpan Karyawan, Karena ID Sudah Ada";
            default:
                return "Tidak Bisa Menyimpan Karyawan";
        }
    }
}

