/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.user.form;

import java.util.*;
import com.dimata.qdep.form.*;
import com.control.*;
import com.user.entity.*;

public class FrmAppConfig extends FRMHandlerMultipart implements I_FRMInterface, I_ExtendedFRMType {
    
    private AppConfig appconfig;
    private Hashtable parameters;

    public static final String FRM_NAME                 = "frm_appconfig" ;

    public static final int FRM_CONFIG_NAME = 0;
    public static final int FRM_CONFIG_VALUE = 1;

    public static  final String[] fieldNames = {
        "CONFIG_NAME",
        "CONFIG_VALUE"
    };

    public static int[] fieldTypes = {
       TYPE_STRING + ENTRY_REQUIRED,
       TYPE_STRING + ENTRY_REQUIRED
    };

    public FrmAppConfig(){
    }

    public FrmAppConfig(AppConfig appconfig){
        this.appconfig = appconfig;
    }

    public FrmAppConfig(Hashtable paramValues, AppConfig appconfig){
        super(new FrmAppConfig(appconfig), paramValues);
	this.appconfig = appconfig;
    }

    public String getFormName() { return FRM_NAME; }

    public int[] getFieldTypes() { return fieldTypes; }

    public String[] getFieldNames() { return fieldNames; }

    public int getFieldSize() { return fieldNames.length; }

    public AppConfig getEntityObject(){ return appconfig; }

    @Override
    public Hashtable getParamValue() { return parameters; }

    public AppConfig requestEntityObject(AppConfig appconfig) {
        this.setAndCheckParamValues();
        appconfig.setConfigName(this.getString(FRM_CONFIG_NAME));
        appconfig.setConfigValue(this.getString(FRM_CONFIG_VALUE));
        return appconfig;
    }

}