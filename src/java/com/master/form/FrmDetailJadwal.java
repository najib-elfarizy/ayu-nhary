/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.master.form;

import com.control.FRMHandlerMultipart;
import com.control.I_ExtendedFRMType;
import com.dimata.qdep.form.I_FRMInterface;
import com.master.entity.*;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

public class FrmDetailJadwal extends FRMHandlerMultipart implements I_FRMInterface, I_ExtendedFRMType {
    
    private DetailJadwal ent;
    private Hashtable parameters;
    
    public static final String FRM_NAME         = "frm_karyawan";
    
    public static final int FRM_ID_DETAIL_JADWAL       = 0;
    public static final int FRM_ID_JADWAL              = 1;
    public static final int FRM_ID_PROYEK              = 2;
    public static final int FRM_NIP                    = 3;
    public static final int FRM_JAM_MULAI              = 4;
    public static final int FRM_JAM_SELESAI            = 5;
    public static final int FRM_TANGGAL_MULAI          = 5;
    public static final int FRM_TANGGAL_SELESAI         = 6;

    public static final String[] fieldNames = {
        "ID_DETAIL_JADWAL",
        "ID_JADWAL",
        "ID_PROYEK",
        "NIP",
        "JAM_MULAI",
        "JAM_SELESAI",
        "TANGGAL_MULAI",
        "TANGGAL_SELESAI"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_DATE
    };
    
    public FrmDetailJadwal(){
    }

    public FrmDetailJadwal(DetailJadwal ent){
        this.ent = ent;
    }

    public FrmDetailJadwal(Hashtable paramValues, DetailJadwal ent){
        super(new FrmDetailJadwal(ent), paramValues);
	this.ent = ent;
    }

    public String getFormName() { return FRM_NAME; }

    public int[] getFieldTypes() { return fieldTypes; }

    public String[] getFieldNames() { return fieldNames; }

    public int getFieldSize() { return fieldNames.length; }

    public DetailJadwal getEntityObject(){ return ent; }

    @Override
    public Hashtable getParamValue() { return parameters; }

    public DetailJadwal requestEntityObject(DetailJadwal ent) {
        this.setAndCheckParamValues();       
        ent.setIdJadwal(this.getLong(FRM_ID_JADWAL));
        ent.setIdProyek(this.getLong(FRM_ID_PROYEK));
        ent.setNIP(this.getLong(FRM_NIP));
        ent.setJamMulai(dateTime(this.getDate(FRM_TANGGAL_MULAI),this.getDate(FRM_JAM_MULAI)));
        ent.setJamSelesai(dateTime(this.getDate(FRM_TANGGAL_SELESAI),this.getDate(FRM_JAM_SELESAI)));
        return ent;
    }
    
    private Date dateTime(Date date, Date time) {
    return new Date(
         date.getYear(), date.getMonth(), date.getDay(), 
         time.getHours(), time.getMinutes(), time.getSeconds()
       );
    } 
    
   
}
