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


public class CtrlDetailJadwal extends Control implements I_Language {
    
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
    private DetailJadwal detailjadwal;
    private PstDetailJadwal pstDetailJadwal;
    private FrmDetailJadwal frmDetailJadwal;
    private String whereClause;
    int language = LANGUAGE_DEFAULT;
    
    @SuppressWarnings("empty-statement")
    public CtrlDetailJadwal(Hashtable paramValues){
	msgString = "";
        whereClause = "";
	detailjadwal = new DetailJadwal();
	try{
            pstDetailJadwal = new PstDetailJadwal(0);
	}catch(Exception e){;}
	frmDetailJadwal = new FrmDetailJadwal(paramValues, detailjadwal);
    }
    
    @SuppressWarnings("static-access")
    private String getSystemMessage(int msgCode){
	switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
		this.frmDetailJadwal.addError(FrmDetailJadwal.FRM_ID_PROYEK, resultText[language][RSLT_EST_CODE_EXIST] );
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
    
    public DetailJadwal getDetailJadwal() { return detailjadwal; }

    public FrmDetailJadwal getForm() { return frmDetailJadwal; }
    
    public String getWhereClause() { return whereClause; }

    public String getMessage(){ return msgString; }

    @Override
    public int getStart() { return start; }
    
    public int action(int cmd, long oidDetailJadwal, String imageDirectory) throws DBException, FileNotFoundException, IOException{
        int errCode = -1;
	int excCode = I_DBExceptionInfo.NO_EXCEPTION;
	int rsCode = RSLT_OK;
	switch(cmd){ 
            case Command.ADD:
                break;
            case Command.SAVE:
                if(oidDetailJadwal!=0){
                    try{
                        detailjadwal = PstDetailJadwal.fetchExc(oidDetailJadwal);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                detailjadwal = frmDetailJadwal.requestEntityObject(detailjadwal);
                if(frmDetailJadwal.errorSize()>0){
                    msgString = "Terdapat Error Pada Pengisian Data";
                    return RSLT_UNKNOWN_ERROR;
                }
                if(oidDetailJadwal==0){
                    try{
                        oidDetailJadwal = PstDetailJadwal.insertExc(detailjadwal);
                        if(oidDetailJadwal!=0) msgString = "Data Berhasil Disimpan";
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    try{
                        oidDetailJadwal = PstDetailJadwal.updateExc(detailjadwal);
                        if(oidDetailJadwal!=0) msgString = "Data Berhasil Diperbaharui";
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case Command.EDIT:
                if(oidDetailJadwal!=0){
                    try{
                        detailjadwal = PstDetailJadwal.fetchExc(oidDetailJadwal);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    detailjadwal = frmDetailJadwal.requestEntityObject(detailjadwal);
                }
                break;
            case Command.DELETE:
                if(oidDetailJadwal!=0){
                    try{
                        long status = PstDetailJadwal.deleteExc(oidDetailJadwal);
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
                if(frmDetailJadwal.getFieldSize()>0)
                    return "Tidak Bisa Menyimpan DetailJadwal, Karena Data Tidak Lengkap";
                else
                    return "Tidak Bisa Menyimpan Karyawan, Karena ID Sudah Ada";
            default:
                return "Tidak Bisa Menyimpan DetailJadwal";
        }
    }
}

