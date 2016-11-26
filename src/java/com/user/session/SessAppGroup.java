/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.user.session;

import com.user.entity.*;
import java.util.*;
import java.sql.*;                      
import com.dimata.qdep.db.*;


public class SessAppGroup {


    public SessAppGroup() {

    }
    

    public static Vector getGroupPriv(long groupID){

        PstGroupPriv  pstGroupPriv = new PstGroupPriv();
    	PstAppPriv  pstAppPrivilege = new PstAppPriv();
        Vector lists = new Vector();        
        DBResultSet dbrs=null;

        try {                  
            String sql = "SELECT AGP."+PstGroupPriv.fieldNames[PstGroupPriv.FLD_GROUP_ID]+
                         ", AGP."+PstGroupPriv.fieldNames[PstGroupPriv.FLD_PRIV_ID]+
                         ", AP."+PstAppPriv.fieldNames[PstAppPriv.FLD_PRIV_NAME]+
                         ", AP."+PstAppPriv.fieldNames[PstAppPriv.FLD_DESCRIPTION]+
                         " FROM "+pstGroupPriv.getTableName()+ " AS AGP ," +
                         pstAppPrivilege.getTableName() + " AS AP "+
                         "WHERE AGP."+PstGroupPriv.fieldNames[PstGroupPriv.FLD_GROUP_ID]+" = '"+ 
                          groupID +"'"+
                         " AND AGP."+PstGroupPriv.fieldNames[PstGroupPriv.FLD_PRIV_ID]+" = "+
                         "AP."+PstAppPriv.fieldNames[PstAppPriv.FLD_PRIV_ID];

            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                AppPriv appPriv = new AppPriv();
                resultToObject(rs, appPriv);
                lists.add(appPriv);
            }
            return lists;

       }catch(Exception e) {
            System.out.println(e);            
       }

        finally {
            DBResultSet.close(dbrs);
        }        

       return new Vector();

    }    
    
    private static void resultToObject(ResultSet rs, AppPriv appPriv) {

        try {

            appPriv.setOID(rs.getLong(PstAppPriv.fieldNames[PstAppPriv.FLD_PRIV_ID]));
            appPriv.setPrivName(rs.getString(PstAppPriv.fieldNames[PstAppPriv.FLD_PRIV_NAME]));
            appPriv.setDescription(rs.getString(PstAppPriv.fieldNames[PstAppPriv.FLD_DESCRIPTION]));           

        }catch(Exception e){

            System.out.println("resultToObject() " + e.toString());

        }

    }    
    

    public static boolean setGroupPriv(long groupOID, Vector vector) {

        try {
        if( PstGroupPriv.deleteByGroup(groupOID)==0)
            return false;

        if(vector == null || vector.size() == 0) 
            return true;

        for(int i = 0; i < vector.size(); i++) {
            GroupPriv gp = (GroupPriv)vector.get(i);
            if(PstGroupPriv.insertExc(gp) ==0)
                return false;

        }
        } catch (Exception e){}

        return true;

    }

   
}

