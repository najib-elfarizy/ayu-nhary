/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.user.form;

import java.util.*;
import com.dimata.util.*;
import com.dimata.qdep.form.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.db.*;
import com.control.ExtendedFRMMessage;
import com.user.entity.*;

public class CtrlAppConfig extends Control implements I_Language {

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
    private AppConfig AppConfig;
    private PstAppConfig pstAppConfig;
    private FrmAppConfig frmAppConfig;
    private String whereClause;
    int language = LANGUAGE_DEFAULT;

    @SuppressWarnings("empty-statement")
    public CtrlAppConfig(Hashtable paramValues) {
        msgString = "";
        whereClause = "";
        AppConfig = new AppConfig();
        try {
            pstAppConfig = new PstAppConfig(0);
        } catch (Exception e) {;
        }
        frmAppConfig = new FrmAppConfig(paramValues, AppConfig);
    }

    @SuppressWarnings("static-access")
    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmAppConfig.addError(frmAppConfig.FRM_CONFIG_NAME, resultText[language][RSLT_EST_CODE_EXIST]);
                return resultText[language][RSLT_EST_CODE_EXIST];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }

    private int getControlMsgId(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                return RSLT_EST_CODE_EXIST;
            default:
                return RSLT_UNKNOWN_ERROR;
        }
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public AppConfig getAppConfig() {
        return AppConfig;
    }

    public FrmAppConfig getForm() {
        return frmAppConfig;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public String getMessage() {
        return msgString;
    }

    @Override
    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidAppConfig) throws DBException {
        msgString = "";
        int errCode = -1;
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;
            case Command.SAVE:
                if (oidAppConfig != 0) {
                    try {
                        AppConfig = PstAppConfig.fetchExc(oidAppConfig);
                    } catch (Exception exc) {
                    }
                }
                this.AppConfig = frmAppConfig.requestEntityObject(AppConfig);
                if (frmAppConfig.errorSize() > 0) {
                    msgString = ExtendedFRMMessage.getMsg(ExtendedFRMMessage.MSG_INCOMPLETE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (AppConfig.getOID() == 0) {
                    if (PstAppConfig.checkConfigName(AppConfig.getConfigName()) == true) {
                        msgString = ExtendedFRMMessage.getMsg(ExtendedFRMMessage.MSG_INCOMPLETE);
                        frmAppConfig.addError(1, "Code sudah digunakan");
                        return RSLT_EST_CODE_EXIST;
                    }
                    try {
                        long oid = PstAppConfig.insertExc(this.AppConfig);
                        if (oid == ExtendedFRMMessage.NONE) {
                            msgString = ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_SAVED);
                            msgString = getErrMessage(excCode);
                            excCode = 0;
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    try {
                        long oid = PstAppConfig.updateExc(this.AppConfig);
                        if (oid == ExtendedFRMMessage.NONE) {
                            msgString = ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_SAVED);
                            msgString = getErrMessage(excCode);
                            excCode = 0;
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
            case Command.EDIT:
                if (oidAppConfig != 0) {
                    try {
                        AppConfig = PstAppConfig.fetchExc(oidAppConfig);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
            case Command.ASK:
                if (oidAppConfig != 0) {
                    try {
                        AppConfig = PstAppConfig.fetchExc(oidAppConfig);
                        msgString = ExtendedFRMMessage.getMessage(ExtendedFRMMessage.MSG_ASKDEL);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
            case Command.DELETE:
                if (oidAppConfig != 0) {
                    try {
                        rsCode = PstUserGroup.deleteByUser(oidAppConfig);
                        if (rsCode == ExtendedFRMMessage.NONE) {
                            msgString = ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_DELETED);
                            errCode = ExtendedFRMMessage.ERR_DELETED;
                        } else {
                            rsCode = PstAppConfig.deleteExc(oidAppConfig);
                            if (rsCode == ExtendedFRMMessage.NONE) {
                                msgString = ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_DELETED);
                                errCode = ExtendedFRMMessage.ERR_DELETED;
                            } else {
                                msgString = "Data Berhasil Dihapus";
                            }
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
            case Command.BACK:
            default:
        }
        return rsCode;
    }

    public String getErrMessage(int errCode) {
        switch (errCode) {
            case ExtendedFRMMessage.ERR_DELETED:
                return "Tidak Bisa Menghapus AppConfig";
            case ExtendedFRMMessage.ERR_SAVED:
                if (frmAppConfig.getFieldSize() > 0) {
                    return "Tidak Bisa Menyimpan AppConfig, Karena Data Tidak Lengkap";
                } else {
                    return "Tidak Bisa Menyimpan AppConfig, Karena ID Sudah Ada";
                }
            default:
                return "Tidak Bisa Menyimpan AppConfig";
        }
    }
}
