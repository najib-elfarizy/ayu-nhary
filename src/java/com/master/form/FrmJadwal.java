/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.master.form;

import com.control.FRMHandlerMultipart;
import com.control.I_ExtendedFRMType;
import com.dimata.qdep.form.I_FRMInterface;
import com.master.entity.Jadwal;
import java.util.Hashtable;

public class FrmJadwal extends FRMHandlerMultipart implements I_FRMInterface, I_ExtendedFRMType {
    
    private Jadwal ent;
    private Hashtable parameters;
    
    public static final String FRM_NAME                 = "frm_keahlian";
    
    public static final int FRM_NAMA_KEAHLIAN           = 0;

    public static final String[] fieldNames = {
        "NAMA_KEAHLIAN",    
    };
    
    public static int[] fieldTypes = {
        TYPE_STRING + ENTRY_REQUIRED,
    };
    
    public FrmJadwal(){
    }

    public FrmJadwal(Jadwal ent){
        this.ent = ent;
    }

    public FrmJadwal(Hashtable paramValues, Jadwal ent){
        super(new FrmJadwal(ent), paramValues);
	this.ent = ent;
    }

    public String getFormName() { return FRM_NAME; }

    public int[] getFieldTypes() { return fieldTypes; }

    public String[] getFieldNames() { return fieldNames; }

    public int getFieldSize() { return fieldNames.length; }

    public Jadwal getEntityObject(){ return ent; }

    @Override
    public Hashtable getParamValue() { return parameters; }

    public Jadwal requestEntityObject(Jadwal ent) {
        this.setAndCheckParamValues();       
         ent.setNamaJadwal(this.getString(FRM_NAMA_KEAHLIAN));
        return ent;
    }
   
}
