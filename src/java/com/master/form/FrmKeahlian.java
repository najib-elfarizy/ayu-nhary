/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.master.form;

import com.control.FRMHandlerMultipart;
import com.control.I_ExtendedFRMType;
import com.dimata.qdep.form.I_FRMInterface;
import com.master.entity.Keahlian;
import java.util.Hashtable;

public class FrmKeahlian extends FRMHandlerMultipart implements I_FRMInterface, I_ExtendedFRMType {
    
    private Keahlian ent;
    private Hashtable parameters;
    
    public static final String FRM_NAME                 = "frm_keahlian";
    
    public static final int FRM_NAMA_KEAHLIAN           = 0;

    public static final String[] fieldNames = {
        "NAMA_KEAHLIAN",    
    };
    
    public static int[] fieldTypes = {
        TYPE_STRING + ENTRY_REQUIRED,
    };
    
    public FrmKeahlian(){
    }

    public FrmKeahlian(Keahlian ent){
        this.ent = ent;
    }

    public FrmKeahlian(Hashtable paramValues, Keahlian ent){
        super(new FrmKeahlian(ent), paramValues);
	this.ent = ent;
    }

    public String getFormName() { return FRM_NAME; }

    public int[] getFieldTypes() { return fieldTypes; }

    public String[] getFieldNames() { return fieldNames; }

    public int getFieldSize() { return fieldNames.length; }

    public Keahlian getEntityObject(){ return ent; }

    @Override
    public Hashtable getParamValue() { return parameters; }

    public Keahlian requestEntityObject(Keahlian ent) {
        this.setAndCheckParamValues();       
         ent.setNamaKeahlian(this.getString(FRM_NAMA_KEAHLIAN));
        return ent;
    }
   
}
