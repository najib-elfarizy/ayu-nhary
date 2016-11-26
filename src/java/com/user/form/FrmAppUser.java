/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.user.form;

import java.util.*;
import com.dimata.qdep.form.*;
import com.control.*;
import com.user.entity.*;

public class FrmAppUser extends FRMHandlerMultipart implements I_FRMInterface, I_ExtendedFRMType {
    
    private AppUser appuser;
    private Hashtable parameters;

    public static final String FRM_NAME                 = "frm_appuser" ;
    public static final int FRM_USER_ID			= 0;
    public static final int FRM_LOGIN_ID		= 1;
    public static final int FRM_PASSWORD		= 2;
    public static final int FRM_CFRM_PASSWORD		= 3;
    public static final int FRM_FULL_NAME		= 4;
    public static final int FRM_EMAIL			= 5;
    public static final int FRM_USER_STATUS		= 6;
    public static final int FRM_USER_GROUP              = 7;

    public static  final String[] fieldNames = {
        "USER_ID",
        "LOGIN_ID",
        "PASSWORD",
        "CONFIRM_PASSWORD",
        "FULL_NAME",
        "EMAIL",
        "USER_STATUS",
        "USER_GROUP"
    };

    public static int[] fieldTypes = {
       TYPE_LONG,
       TYPE_STRING + ENTRY_REQUIRED,
       TYPE_STRING + ENTRY_REQUIRED,
       TYPE_STRING,
       TYPE_STRING + ENTRY_REQUIRED,
       TYPE_STRING + FORMAT_EMAIL,
       TYPE_INT,
       TYPE_LONG
    };

    public FrmAppUser(){
    }

    public FrmAppUser(AppUser appuser){
        this.appuser = appuser;
    }

    public FrmAppUser(Hashtable paramValues, AppUser appuser){
        super(new FrmAppUser(appuser), paramValues);
	this.appuser = appuser;
    }

    public String getFormName() { return FRM_NAME; }

    public int[] getFieldTypes() { return fieldTypes; }

    public String[] getFieldNames() { return fieldNames; }

    public int getFieldSize() { return fieldNames.length; }

    public AppUser getEntityObject(){ return appuser; }

    @Override
    public Hashtable getParamValue() { return parameters; }

    public AppUser requestEntityObject(AppUser appuser) {
        this.setAndCheckParamValues();
        appuser.setLoginId(this.getString(FRM_LOGIN_ID));
        appuser.setPassword(this.getString(FRM_PASSWORD));
        appuser.setConfirmPassword(this.getString(FRM_CFRM_PASSWORD));
        appuser.setFullName(this.getString(FRM_FULL_NAME));
        appuser.setEmail(this.getString(FRM_EMAIL));
        appuser.setUserStatus(this.getInt(FRM_USER_STATUS));
        return appuser;
    }

    public Vector getUserGroup (long userOID){
        Vector UserGroups = new Vector(1,1);
        Vector GroupOIDs = getVectorLong(FRM_USER_GROUP);
        if (GroupOIDs==null)
            return UserGroups;
        int max = GroupOIDs.size();
        for(int i=0; i< max; i++){
            long groupOID = ((Long)GroupOIDs.get(i)).longValue();
            UserGroup UserGroup = new UserGroup();
            UserGroup.setIdUser(userOID);
            UserGroup.setIdGroup(groupOID);
            UserGroups.add(UserGroup);
        }
        return UserGroups;
    }
}