/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.user.form;

import java.io.*;
import java.util.*;
import com.dimata.util.*;
import com.dimata.qdep.form.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.db.*;
import com.control.ExtendedFRMMessage;
import com.user.entity.*;

public class CtrlAppPriv extends Control implements I_Language {
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
    private AppPriv AppPriv;
    private PstAppPriv pstAppPriv;
    private FrmAppPriv frmAppPriv;
    private String whereClause;
    int language = LANGUAGE_DEFAULT;

    @SuppressWarnings("empty-statement")
    public CtrlAppPriv(Hashtable paramValues){
	msgString = "";
        whereClause = "";
	AppPriv = new AppPriv();
	try{
            pstAppPriv = new PstAppPriv(0);
	}catch(Exception e){;}
	frmAppPriv = new FrmAppPriv(paramValues, AppPriv);
    }

    @SuppressWarnings("static-access")
    private String getSystemMessage(int msgCode){
	switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
		this.frmAppPriv.addError(frmAppPriv.FRM_PRIV_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

    public AppPriv getAppPriv() { return AppPriv; }

    public FrmAppPriv getForm() { return frmAppPriv; }
    
    public String getWhereClause() { return whereClause; }

    public String getMessage(){ return msgString; }

    @Override
    public int getStart() { return start; }

    public int action(int cmd, long oidAppPriv, String imageDirectory) throws DBException, FileNotFoundException, IOException{
	msgString = "";
        int errCode = -1;
	int excCode = I_DBExceptionInfo.NO_EXCEPTION;
	int rsCode = RSLT_OK;
	switch(cmd){
            case Command.ADD :
		break;
            case Command.SAVE :
		if(oidAppPriv != 0){
                    try{
			AppPriv = PstAppPriv.fetchExc(oidAppPriv);
                    }catch(Exception exc){}
                }
		this.AppPriv = frmAppPriv.requestEntityObject(AppPriv);
                if(frmAppPriv.errorSize()>0) {
                    msgString = ExtendedFRMMessage.getMsg(ExtendedFRMMessage.MSG_INCOMPLETE);
                    return RSLT_FORM_INCOMPLETE ;
                }
		if(AppPriv.getOID()==0){
                    if(PstAppPriv.checkPrivName(AppPriv.getPrivName())==true){
                    msgString = ExtendedFRMMessage.getMsg(ExtendedFRMMessage.MSG_INCOMPLETE);
                    frmAppPriv.addError(1, "Nama Privilege Telah Digunakan");
                    return RSLT_EST_CODE_EXIST;
                    }
                    try{
                        long oid = PstAppPriv.insertExc(this.AppPriv);
                        msgString = "Data Berhasil Disimpan";
                        this.AppPriv = new AppPriv();
                    }catch(DBException dbexc){
                        excCode = dbexc.getErrorCode();
			msgString = getSystemMessage(excCode);
			return getControlMsgId(excCode);
                    }catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
			return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
		}else{
                    try {
                        long oid = PstAppPriv.updateExc(this.AppPriv);
                        msgString = "Data Berhasil Diperbaharui";
                        this.AppPriv = new AppPriv();
                    }catch (DBException dbexc){
			excCode = dbexc.getErrorCode();
			msgString = getSystemMessage(excCode);
                    }catch (Exception exc){
			msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
		break;
            case Command.EDIT :
		if (oidAppPriv != 0) {
                    try {
			AppPriv = PstAppPriv.fetchExc(oidAppPriv);
                    } catch (DBException dbexc){
			excCode = dbexc.getErrorCode();
			msgString = getSystemMessage(excCode);
                    } catch (Exception exc){
			msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
		}
		break;
            case Command.ASK :
		if (oidAppPriv != 0) {
                    try {
			AppPriv = PstAppPriv.fetchExc(oidAppPriv);
                        msgString = ExtendedFRMMessage.getMessage(ExtendedFRMMessage.MSG_ASKDEL);
                    } catch (DBException dbexc){
			excCode = dbexc.getErrorCode();
			msgString = getSystemMessage(excCode);
                    } catch (Exception exc){
			msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
		}
		break;
            case Command.DELETE :
                if (oidAppPriv != 0){
                    try{
                        rsCode = PstAppPriv.deleteExc(oidAppPriv);
                        if(rsCode == ExtendedFRMMessage.NONE){
                            msgString = ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_DELETED);
                            errCode = ExtendedFRMMessage.ERR_DELETED;
                        }
                          else
                            msgString = "Data Berhasil Dihapus";
                            this.AppPriv = new AppPriv();
//                         }
                    } catch(DBException dbexc){
			excCode = dbexc.getErrorCode();
			msgString = getSystemMessage(excCode);
                    } catch(Exception exc){
			msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
            case Command.LOAD :
                this.AppPriv = PstAppPriv.fetchExc(oidAppPriv);
                frmAppPriv.clearError();
                break;
            case Command.ACTIVATE :
                this.AppPriv = PstAppPriv.fetchExc(oidAppPriv);
                frmAppPriv.clearError();
                break;
            case Command.SUBMIT :
                this.AppPriv = PstAppPriv.fetchExc(oidAppPriv);
                try{
                   msgString = "Data Berhasil Disimpan";                   
                   this.AppPriv = new AppPriv();
                }catch(Exception e){;}
                frmAppPriv.clearError();
                break;
            case Command.CONFIRM :
                this.AppPriv = PstAppPriv.fetchExc(oidAppPriv);
                long IDAppPrivObj = frmAppPriv.requestPrivilegeObjectID();
                try{
                    msgString = "Data Berhasil Dihapus";
                    this.AppPriv = new AppPriv();
                }catch(Exception e){;}
                frmAppPriv.clearError();
                break;
            case Command.LIST :
                this.AppPriv = PstAppPriv.fetchExc(oidAppPriv);
                frmAppPriv.clearError();
                break;
            default :
	}
	return rsCode;
    }

    public String getErrMessage(int errCode){
        switch (errCode){
            case ExtendedFRMMessage.ERR_DELETED :
                return "Tidak Bisa Menghapus AppPriv";
            case ExtendedFRMMessage.ERR_SAVED :
                if(frmAppPriv.getFieldSize()>0)
                    return "Tidak Bisa Menyimpan AppPriv, Karena Data Tidak Lengkap";
                else
                    return "Tidak Bisa Menyimpan AppPriv, Karena ID Sudah Ada";
            default:
                return "Tidak Bisa Menyimpan AppPriv";
        }
    }
}

