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

public class CtrlAppUser extends Control implements I_Language {
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
    private AppUser AppUser;
    private PstAppUser pstAppUser;
    private FrmAppUser frmAppUser;
    private String whereClause;
    int language = LANGUAGE_DEFAULT;

    @SuppressWarnings("empty-statement")
    public CtrlAppUser(Hashtable paramValues){
	msgString = "";
        whereClause = "";
	AppUser = new AppUser();
	try{
            pstAppUser = new PstAppUser(0);
	}catch(Exception e){;}
	frmAppUser = new FrmAppUser(paramValues, AppUser);
    }

    @SuppressWarnings("static-access")
    private String getSystemMessage(int msgCode){
	switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
		this.frmAppUser.addError(frmAppUser.FRM_LOGIN_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

    public AppUser getAppUser() { return AppUser; }

    public FrmAppUser getForm() { return frmAppUser; }
    
    public String getWhereClause() { return whereClause; }

    public String getMessage(){ return msgString; }

    @Override
    public int getStart() { return start; }

    public int action(int cmd, long oidAppUser, String imageDirectory) throws DBException, FileNotFoundException, IOException{
	msgString = "";
        int errCode = -1;
	int excCode = I_DBExceptionInfo.NO_EXCEPTION;
	int rsCode = RSLT_OK;
	switch(cmd){
            case Command.ADD :
		break;
            case Command.SAVE :
		if(oidAppUser != 0){
                    try{
			AppUser = PstAppUser.fetchExc(oidAppUser);
                    }catch(Exception exc){}
                }
		this.AppUser = frmAppUser.requestEntityObject(AppUser);
                if(frmAppUser.errorSize()>0) {
                    msgString = ExtendedFRMMessage.getMsg(ExtendedFRMMessage.MSG_INCOMPLETE);
                    return RSLT_FORM_INCOMPLETE ;
                }
                if(AppUser.getPassword().equals(AppUser.getConfirmPassword())){
		if(AppUser.getOID()==0){
                    if(PstAppUser.checkLoginID(AppUser.getLoginId())==true){
                    msgString = ExtendedFRMMessage.getMsg(ExtendedFRMMessage.MSG_INCOMPLETE);
                    frmAppUser.addError(1, "LoginID Telah Digunakan");
                    return RSLT_EST_CODE_EXIST;
                    }
                    try{
                        long oid = PstAppUser.insertExc(this.AppUser);                        
                        if(oid == ExtendedFRMMessage.NONE){
                            msgString = ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_SAVED);
                            msgString = getErrMessage(excCode);
                            excCode=0;
                        }
                        else{
                            Vector UserGroup = this.frmAppUser.getUserGroup(this.AppUser.getOID());
                            if(PstUserGroup.setAllUserGroup(this.AppUser.getOID(), UserGroup))
                                msgString = "Data Berhasil Disimpan";
                            else{
                                msgString = ExtendedFRMMessage.getMsg(ExtendedFRMMessage.ERR_SAVED);
                                excCode=0;
                            }
                        }
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
                        long oid = PstAppUser.updateExc(this.AppUser);
                        if(oid == ExtendedFRMMessage.NONE){
                            msgString = ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_SAVED);
                            msgString = getErrMessage(excCode);
                            excCode=0;
                        }
                        else{
                            Vector UserGroup = this.frmAppUser.getUserGroup(this.AppUser.getOID());
                            if(PstUserGroup.setAllUserGroup(this.AppUser.getOID(), UserGroup))
                                msgString = "Data Berhasil Diperbaharui";
                            else{
                                msgString = ExtendedFRMMessage.getMsg(ExtendedFRMMessage.ERR_SAVED);
                                excCode=0;
                            }
                        }
                    }catch (DBException dbexc){
			excCode = dbexc.getErrorCode();
			msgString = getSystemMessage(excCode);
                    }catch (Exception exc){
			msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                } else {
                    msgString = ExtendedFRMMessage.getMsg(ExtendedFRMMessage.MSG_INCOMPLETE);
                    frmAppUser.addError(3, "Password Tidak Sesuai");
                    excCode=0;
                }
		break;
            case Command.EDIT :
		if (oidAppUser != 0) {
                    try {
			AppUser = PstAppUser.fetchExc(oidAppUser);
                    } catch (DBException dbexc){
			excCode = dbexc.getErrorCode();
			msgString = getSystemMessage(excCode);
                    } catch (Exception exc){
			msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
		}
		break;
            case Command.ASK :
		if (oidAppUser != 0) {
                    try {
			AppUser = PstAppUser.fetchExc(oidAppUser);
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
                if((PstAppUser.fetchExc(oidAppUser)).getUserStatus()==2){
                    msgString = "User Tidak Dapat Dihapus Karena Sedang Online";
                    return RSLT_EST_CODE_EXIST;
                }else{
                    if (oidAppUser != 0){
                        try{
                            rsCode = PstUserGroup.deleteByUser(oidAppUser);
                            if(rsCode == ExtendedFRMMessage.NONE){
                                msgString = ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_DELETED);
                                errCode = ExtendedFRMMessage.ERR_DELETED;
                            } else {
                            rsCode = PstAppUser.deleteExc(oidAppUser);
                            if(rsCode == ExtendedFRMMessage.NONE){
                                msgString = ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_DELETED);
                                errCode = ExtendedFRMMessage.ERR_DELETED;
                            }
                              else
                                msgString = "Data Berhasil Dihapus";
                             }
                        } catch(DBException dbexc){
                            excCode = dbexc.getErrorCode();
                            msgString = getSystemMessage(excCode);
                        } catch(Exception exc){
                            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        }
                    }
                }
                break;
            case Command.ASSIGN:
                this.AppUser = frmAppUser.requestEntityObject(AppUser);
                if(frmAppUser.errorSize()>0) {
                    msgString = ExtendedFRMMessage.getMsg(ExtendedFRMMessage.MSG_INCOMPLETE);
                    return RSLT_FORM_INCOMPLETE ;
                }
                if(AppUser.getPassword().equals(AppUser.getConfirmPassword())){
		if(AppUser.getOID()==0){
                    if(PstAppUser.checkLoginID(AppUser.getLoginId())==true){
                    msgString = "LoginID Telah Digunakan";
                    return RSLT_EST_CODE_EXIST;
                    }
                    try{
                        long oid = PstAppUser.insertExc(this.AppUser);                        
                        if(oid == ExtendedFRMMessage.NONE){
                            msgString = ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_SAVED);
                            msgString = getErrMessage(excCode);
                            excCode=0;
                        }
                        else{
                            Vector UserGroup = this.frmAppUser.getUserGroup(this.AppUser.getOID());
                            if(PstUserGroup.setAllUserGroup(this.AppUser.getOID(), UserGroup)){
                                msgString = "Registrasi Berhasil";
                                AppUser = new AppUser();
                                return RSLT_OK;
                        }else{
                                msgString = ExtendedFRMMessage.getMsg(ExtendedFRMMessage.ERR_SAVED);
                                excCode=0;
                            }
                        }
                    }catch(DBException dbexc){
                        excCode = dbexc.getErrorCode();
			msgString = getSystemMessage(excCode);
			return getControlMsgId(excCode);
                    }catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
			return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
		}} else {
                    msgString = ExtendedFRMMessage.getMsg(ExtendedFRMMessage.MSG_INCOMPLETE);
                    frmAppUser.addError(3, "Password Tidak Sesuai");
                    excCode=0;
                }
                break;
            case Command.BACK :
            default :
	}
	return rsCode;
    }

    public String getErrMessage(int errCode){
        switch (errCode){
            case ExtendedFRMMessage.ERR_DELETED :
                return "Tidak Bisa Menghapus AppUser";
            case ExtendedFRMMessage.ERR_SAVED :
                if(frmAppUser.getFieldSize()>0)
                    return "Tidak Bisa Menyimpan AppUser, Karena Data Tidak Lengkap";
                else
                    return "Tidak Bisa Menyimpan AppUser, Karena ID Sudah Ada";
            default:
                return "Tidak Bisa Menyimpan AppUser";
        }
    }
}
