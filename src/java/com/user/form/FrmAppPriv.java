/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.user.form;

import java.util.*;
import com.dimata.qdep.form.*;
import com.control.*;
import com.user.entity.*;

public class FrmAppPriv extends FRMHandlerMultipart implements I_FRMInterface, I_ExtendedFRMType {
    
    private AppPriv apppriv;
    private Hashtable parameters;

    public static final String FRM_NAME                 = "frm_apppriv" ;
    public static final int FRM_PRIV_ID                 = 0;
    public static final int FRM_PRIV_NAME		= 1;
    public static final int FRM_DESCRIPTION		= 2;
    public static final int FRM_G1_IDX  	 	= 3;
    public static final int FRM_G2_IDX	 		= 4;
    public static final int FRM_OBJ_IDX	 		= 5;
    public static final int FRM_COMMANDS	 	= 6;
    public static final int FRM_PRIV_OBJ_ID             = 7;

    public static  final String[] fieldNames = {
        "PRIV_ID",
        "PRIV_NAME",
        "DESCRIPTION",
        "G1_IDX",
        "G2_IDX",
        "OBJ_IDX",
        "COMMANDS",
        "PRIV_OBJ_ID"
    };

    public static int[] fieldTypes = {
       TYPE_LONG,
       TYPE_STRING + ENTRY_REQUIRED,
       TYPE_STRING,
       TYPE_INT,
       TYPE_INT,
       TYPE_INT,
       TYPE_INT + TYPE_COLLECTION,
       TYPE_LONG
    };

    public FrmAppPriv(){
    }

    public FrmAppPriv(AppPriv apppriv){
        this.apppriv = apppriv;
    }

    public FrmAppPriv(Hashtable paramValues, AppPriv apppriv){
        super(new FrmAppPriv(apppriv), paramValues);
	this.apppriv = apppriv;
    }

    public String getFormName() { return FRM_NAME; }

    public int[] getFieldTypes() { return fieldTypes; }

    public String[] getFieldNames() { return fieldNames; }

    public int getFieldSize() { return fieldNames.length; }

    public AppPriv getEntityObject(){ return apppriv; }

    @Override
    public Hashtable getParamValue() { return parameters; }

    public AppPriv requestEntityObject(AppPriv apppriv) {
        this.setAndCheckParamValues();
        apppriv.setPrivName(this.getString(FRM_PRIV_NAME));
        apppriv.setDescription(this.getString(FRM_DESCRIPTION));
        return apppriv;
    }
    
    public long requestPrivilegeObjectID(){
        this.setAndCheckParamValues();
        long IDAppPrivObj = this.getLong(FRM_PRIV_OBJ_ID);
        return IDAppPrivObj;
    }

}

