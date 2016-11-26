/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.user.form;

import java.util.*;
import com.dimata.qdep.form.*;
import com.control.*;
import com.user.entity.*;

public class FrmAppGroup extends FRMHandlerMultipart implements I_FRMInterface, I_ExtendedFRMType {
    
    private AppGroup appgroup;
    private Hashtable parameters;

    public static final String FRM_NAME                 = "frm_appgroup" ;
    public static final int FRM_GROUP_ID		= 0;
    public static final int FRM_GROUP_NAME		= 1;
    public static final int FRM_DESCRIPTION		= 2;
    public static final int FRM_GROUP_PRIV              = 3;

    public static  final String[] fieldNames = {
        "GROUP_ID",
        "GROUP_NAME",
        "DESCRIPTION",
        "GROUP_PRIV"
    };

    public static int[] fieldTypes = {
       TYPE_LONG,
       TYPE_STRING + ENTRY_REQUIRED,
       TYPE_STRING,
       TYPE_LONG
    };

    public FrmAppGroup(){
    }

    public FrmAppGroup(AppGroup appgroup){
        this.appgroup = appgroup;
    }

    public FrmAppGroup(Hashtable paramValues, AppGroup appgroup){
        super(new FrmAppGroup(appgroup), paramValues);
	this.appgroup = appgroup;
    }

    public String getFormName() { return FRM_NAME; }

    public int[] getFieldTypes() { return fieldTypes; }

    public String[] getFieldNames() { return fieldNames; }

    public int getFieldSize() { return fieldNames.length; }

    public AppGroup getEntityObject(){ return appgroup; }

    @Override
    public Hashtable getParamValue() { return parameters; }

    public AppGroup requestEntityObject(AppGroup appgroup) {
        this.setAndCheckParamValues();
        appgroup.setGroupName(this.getString(FRM_GROUP_NAME));
        appgroup.setDescription(this.getString(FRM_DESCRIPTION));
        return appgroup;
    }
    
    public Vector getGroupPriv (long groupOID){
        Vector GroupPrivs = new Vector(1,1);
        Vector PrivOIDs = getVectorLong(FRM_GROUP_PRIV);
        if (PrivOIDs==null)
            return GroupPrivs;
        int max = PrivOIDs.size();
        for(int i=0; i< max; i++){
            long privOID = ((Long)PrivOIDs.get(i)).longValue();
            GroupPriv GroupPriv = new GroupPriv();
            GroupPriv.setIdGroup(groupOID);
            GroupPriv.setIdPriv(privOID);
            GroupPrivs.add(GroupPriv);
        }
        return GroupPrivs;
    }
    
}
