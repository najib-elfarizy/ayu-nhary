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

public class CtrlAppGroup extends Control implements I_Language {
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
    private AppGroup AppGroup;
    private PstAppGroup pstAppGroup;
    private FrmAppGroup frmAppGroup;
    private String whereClause;
    int language = LANGUAGE_DEFAULT;

    @SuppressWarnings("empty-statement")
    public CtrlAppGroup(Hashtable paramValues){
	msgString = "";
        whereClause = "";
	AppGroup = new AppGroup();
	try{
            pstAppGroup = new PstAppGroup(0);
	}catch(Exception e){;}
	frmAppGroup = new FrmAppGroup(paramValues, AppGroup);
    }

    @SuppressWarnings("static-access")
    private String getSystemMessage(int msgCode){
	switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
		this.frmAppGroup.addError(frmAppGroup.FRM_GROUP_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

    public AppGroup getAppGroup() { return AppGroup; }

    public FrmAppGroup getForm() { return frmAppGroup; }
    
    public String getWhereClause() { return whereClause; }

    public String getMessage(){ return msgString; }

    @Override
    public int getStart() { return start; }

    public int action(int cmd, long oidAppGroup, String imageDirectory) throws DBException, FileNotFoundException, IOException{
	msgString = "";
        int errCode = -1;
	int excCode = I_DBExceptionInfo.NO_EXCEPTION;
	int rsCode = RSLT_OK;
	switch(cmd){
            case Command.ADD :
		break;
            case Command.SAVE :
		if(oidAppGroup != 0){
                    try{
			AppGroup = PstAppGroup.fetchExc(oidAppGroup);
                    }catch(Exception exc){}
                }
		this.AppGroup = frmAppGroup.requestEntityObject(AppGroup);
                if(frmAppGroup.errorSize()>0) {
                    msgString = ExtendedFRMMessage.getMsg(ExtendedFRMMessage.MSG_INCOMPLETE);
                    return RSLT_FORM_INCOMPLETE ;
                }
		if(AppGroup.getOID()==0){
                    if(PstAppGroup.checkGroupName(AppGroup.getGroupName())==true){
                    msgString = ExtendedFRMMessage.getMsg(ExtendedFRMMessage.MSG_INCOMPLETE);
                    frmAppGroup.addError(1, "Group Name Telah Digunakan");
                    return RSLT_EST_CODE_EXIST;
                    }
                    try{
                        long oid = PstAppGroup.insertExc(this.AppGroup);                        
                        if(oid == ExtendedFRMMessage.NONE){
                            msgString = ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_SAVED);
                            msgString = getErrMessage(excCode);
                            excCode=0;
                        }
                        else{
                            Vector GroupPriv = this.frmAppGroup.getGroupPriv(this.AppGroup.getOID());
                            if(PstGroupPriv.setAllGroupPriv(this.AppGroup.getOID(), GroupPriv))
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
                        long oid = PstAppGroup.updateExc(this.AppGroup);
                        if(oid == ExtendedFRMMessage.NONE){
                            msgString = ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_SAVED);
                            msgString = getErrMessage(excCode);
                            excCode=0;
                        }
                        else{
                            Vector GroupPriv = this.frmAppGroup.getGroupPriv(this.AppGroup.getOID());
                            if(PstGroupPriv.setAllGroupPriv(this.AppGroup.getOID(), GroupPriv))
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
		break;
            case Command.EDIT :
		if (oidAppGroup != 0) {
                    try {
			AppGroup = PstAppGroup.fetchExc(oidAppGroup);
                    } catch (DBException dbexc){
			excCode = dbexc.getErrorCode();
			msgString = getSystemMessage(excCode);
                    } catch (Exception exc){
			msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
		}
		break;
            case Command.ASK :
		if (oidAppGroup != 0) {
                    try {
			AppGroup = PstAppGroup.fetchExc(oidAppGroup);
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
                if((PstUserGroup.checkGroup(oidAppGroup)==true)){
                    msgString = "Group Tidak Dapat Dihapus Karena Terdapat User Dalam Group Ini";
                    return RSLT_EST_CODE_EXIST;
                }else{
                    if (oidAppGroup != 0){
                        try{
                            rsCode = PstGroupPriv.deleteByGroup(oidAppGroup);
                            if(rsCode == ExtendedFRMMessage.NONE){
                                msgString = ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_DELETED);
                                errCode = ExtendedFRMMessage.ERR_DELETED;
                            } else {
                            rsCode = PstAppGroup.deleteExc(oidAppGroup);
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
            case Command.BACK :
            default :
	}
	return rsCode;
    }

    public String getErrMessage(int errCode){
        switch (errCode){
            case ExtendedFRMMessage.ERR_DELETED :
                return "Tidak Bisa Menghapus AppGroup";
            case ExtendedFRMMessage.ERR_SAVED :
                if(frmAppGroup.getFieldSize()>0)
                    return "Tidak Bisa Menyimpan AppGroup, Karena Data Tidak Lengkap";
                else
                    return "Tidak Bisa Menyimpan AppGroup, Karena ID Sudah Ada";
            default:
                return "Tidak Bisa Menyimpan AppGroup";
        }
    }
}
