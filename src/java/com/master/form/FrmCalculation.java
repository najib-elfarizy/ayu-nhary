/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.master.form;

import com.control.FRMHandlerMultipart;
import com.control.I_ExtendedFRMType;
import com.dimata.qdep.form.I_FRMInterface;
import com.master.entity.Calculation;
import java.util.Hashtable;

public class FrmCalculation extends FRMHandlerMultipart implements I_FRMInterface, I_ExtendedFRMType {
    
    private Calculation ent;
    private Hashtable parameters;
    
    public static final String FRM_NAME                     = "frm_calculation";
    
    public static final int FRM_JUMLAH_POPULASI             = 0;
    public static final int FRM_PROBABILITAS_CROSSOVER      = 1;
    public static final int FRM_PROBABILITAS_MUTASI         = 2;
    public static final int FRM_ID_JADWAL                   = 3;

    public static final String[] fieldNames = {
        "JUMLAH_POPULASI",
        "PROBABILITAS_CROSSOVER",
        "PROBABILITAS_MUTASI",
        "ID_JADWAL"
    };
    
    public static int[] fieldTypes = {
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG
    };
    
    public FrmCalculation(){
    }

    public FrmCalculation(Calculation ent){
        this.ent = ent;
    }

    public FrmCalculation(Hashtable paramValues, Calculation ent){
        super(new FrmCalculation(ent), paramValues);
	this.ent = ent;
    }

    public String getFormName() { return FRM_NAME; }

    public int[] getFieldTypes() { return fieldTypes; }

    public String[] getFieldNames() { return fieldNames; }

    public int getFieldSize() { return fieldNames.length; }

    public Calculation getEntityObject(){ return ent; }

    @Override
    public Hashtable getParamValue() { return parameters; }

    public Calculation requestEntityObject(Calculation ent) {
        this.setAndCheckParamValues();       
        ent.setJumlahPopulasi(this.getInt(FRM_JUMLAH_POPULASI));
        ent.setProbabilitasCrossover(this.getFloat(FRM_PROBABILITAS_CROSSOVER));
        ent.setProbabilitasMutasi(this.getFloat(FRM_PROBABILITAS_MUTASI));
        return ent;
    }
   
}
