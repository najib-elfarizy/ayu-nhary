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

public class CtrlPegawai extends Control implements I_Language {

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
    private Pegawai pegawai;
    private PstPegawai pstPegawai;
    private FrmPegawai frmPegawai;
    private String whereClause;
    int language = LANGUAGE_DEFAULT;

    @SuppressWarnings("empty-statement")
    public CtrlPegawai(Hashtable paramValues) {
        msgString = "";
        whereClause = "";
        pegawai = new Pegawai();
        try {
            pstPegawai = new PstPegawai(0);
        } catch (Exception e) {;
        }
        frmPegawai = new FrmPegawai(paramValues, pegawai);
    }

    @SuppressWarnings("static-access")
    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmPegawai.addError(FrmPegawai.FRM_NAMA, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public Pegawai getPegawai() {
        return pegawai;
    }

    public FrmPegawai getForm() {
        return frmPegawai;
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

    public int action(int cmd, long oidPegawai, String imageDirectory) throws DBException, FileNotFoundException, IOException {
        int errCode = -1;
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        String NIK = "";
        switch (cmd) {
            case Command.ADD:
                break;
            case Command.SAVE:
                if (oidPegawai != 0) {
                    try {
                        pegawai = PstPegawai.fetchExc(oidPegawai);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                pegawai = frmPegawai.requestEntityObject(pegawai);
                if (oidPegawai == 0) {
                    try {
                        oidPegawai = PstPegawai.insertExc(pegawai);
                        if (oidPegawai != 0) {
                            msgString = "Data Berhasil Disimpan";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        oidPegawai = PstPegawai.updateExc(pegawai);
                        if (oidPegawai != 0) {
                            msgString = "Data Berhasil Diperbaharui";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Command.EDIT:
                if (oidPegawai != 0) {
                    try {
                        pegawai = PstPegawai.fetchExc(oidPegawai);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Command.DELETE:
                if (oidPegawai != 0) {
                    try {
                        long status = PstPegawai.deleteExc(oidPegawai);
                        if (status == 1) {
                            msgString = "Data Berhasil Dihapus";
                        } else {
                            msgString = "Error Hapus Data, Data Mungkin Digunakan";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
        return rsCode;
    }

    public String getErrMessage(int errCode) {
        switch (errCode) {
            case ExtendedFRMMessage.ERR_DELETED:
                return "Tidak Bisa Menghapus Pegawai";
            case ExtendedFRMMessage.ERR_SAVED:
                if (frmPegawai.getFieldSize() > 0) {
                    return "Tidak Bisa Menyimpan Pegawai, Karena Data Tidak Lengkap";
                } else {
                    return "Tidak Bisa Menyimpan Pegawai, Karena ID Sudah Ada";
                }
            default:
                return "Tidak Bisa Menyimpan Pegawai";
        }
    }
}
