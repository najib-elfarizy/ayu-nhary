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

public class FrmPegawai extends FRMHandlerMultipart implements I_FRMInterface, I_ExtendedFRMType {

    private Pegawai ent;
    private Hashtable parameters;

    public static final String FRM_NAME = "frm_pegawai";

    public static final int FRM_NAMA = 0;
    public static final int FRM_JENIS_KELAMIN = 1;
    public static final int FRM_TEMPAT_LAHIR = 2;
    public static final int FRM_TANGGAL_LAHIR = 3;
    public static final int FRM_ALAMAT = 4;
    public static final int FRM_PENDIDIKAN = 5;
    public static final int FRM_EMAIL = 6;
    public static final int FRM_JABATAN = 7;

    public static final String[] fieldNames = {
        "NAMA_PEGAWAI",
        "JENIS_KELAMIN",
        "TEMPAT_LAHIR",
        "TGL_LAHIR",
        "ALAMAT",
        "PENDIDIKAN",
        "EMAIL",
        "JABATAN"
    };

    public static int[] fieldTypes = {
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING
    };

    public FrmPegawai() {
    }

    public FrmPegawai(Pegawai ent) {
        this.ent = ent;
    }

    public FrmPegawai(Hashtable paramValues, Pegawai ent) {
        super(new FrmPegawai(ent), paramValues);
        this.ent = ent;
    }

    public String getFormName() {
        return FRM_NAME;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public Pegawai getEntityObject() {
        return ent;
    }

    @Override
    public Hashtable getParamValue() {
        return parameters;
    }

    public Pegawai requestEntityObject(Pegawai ent) {
        this.setAndCheckParamValues();
        ent.setNamaPegawai(this.getString(FRM_NAMA));
        ent.setJenisKelamin(this.getString(FRM_JENIS_KELAMIN));
        ent.setTempatLahir(this.getString(FRM_TEMPAT_LAHIR));
        ent.setTanggalLahir(this.getDate(FRM_TANGGAL_LAHIR));
        ent.setAlamat(this.getString(FRM_ALAMAT));
        ent.setPendidikan(this.getString(FRM_PENDIDIKAN));
        ent.setEmail(this.getString(FRM_EMAIL));
        ent.setJabatan(this.getString(FRM_JABATAN));
        return ent;
    }

}
