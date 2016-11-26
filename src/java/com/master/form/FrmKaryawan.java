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
import java.util.Hashtable;
import java.util.Vector;

public class FrmKaryawan extends FRMHandlerMultipart implements I_FRMInterface, I_ExtendedFRMType {
    
    private Karyawan ent;
    private Hashtable parameters;
    
    public static final String FRM_NAME         = "frm_karyawan";
    
    public static final int FRM_NAMA                = 0;
    public static final int FRM_TANGGAL_LAHIR       = 1;
    public static final int FRM_JABATAN             = 2;
    public static final int FRM_ALAMAT              = 3;
    public static final int FRM_KEAHLIAN_KARYAWAN   = 4;

    public static final String[] fieldNames = {
        "NAMA",
        "TANGGAL_LAHIR",
        "JABATAN",
        "ALAMAT",
        "KEAHLIAN_KARYAWAN"
    };
    
    public static int[] fieldTypes = {
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG
    };
    
    public FrmKaryawan(){
    }

    public FrmKaryawan(Karyawan ent){
        this.ent = ent;
    }

    public FrmKaryawan(Hashtable paramValues, Karyawan ent){
        super(new FrmKaryawan(ent), paramValues);
	this.ent = ent;
    }

    public String getFormName() { return FRM_NAME; }

    public int[] getFieldTypes() { return fieldTypes; }

    public String[] getFieldNames() { return fieldNames; }

    public int getFieldSize() { return fieldNames.length; }

    public Karyawan getEntityObject(){ return ent; }

    @Override
    public Hashtable getParamValue() { return parameters; }

    public Karyawan requestEntityObject(Karyawan ent) {
        this.setAndCheckParamValues();       
        ent.setNamaKaryawan(this.getString(FRM_NAMA));
        ent.setTanggalLahir(this.getDate(FRM_TANGGAL_LAHIR));
        ent.setJabatan(this.getString(FRM_JABATAN));
        ent.setAlamat(this.getString(FRM_ALAMAT));
        return ent;
    }
    
    public KeahlianKaryawan requestKeahlianKaryawan(long oidKaryawan){
        this.setAndCheckParamValues();   
        KeahlianKaryawan kk = new KeahlianKaryawan();
        kk.setIdKaryawan(oidKaryawan);
        kk.setIdKeahlian(this.getLong(FRM_KEAHLIAN_KARYAWAN));
        return kk;
    }
    
   
}
