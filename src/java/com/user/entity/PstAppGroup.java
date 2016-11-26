/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.user.entity;

import java.util.*;
import java.sql.*;
import java.io.*;

import com.dimata.qdep.entity.*;
import com.dimata.qdep.db.*;
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;

public class PstAppGroup extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    public static final String TBL_APP_GROUP            = "app_group";

    public static final int FLD_GROUP_ID		= 0;
    public static final int FLD_GROUP_NAME		= 1;
    public static final int FLD_DESCRIPTION 		= 2;
    
    
    public static  final String[] fieldNames = {
        "GROUP_ID",
        "GROUP_NAME",
        "DESCRIPTION"
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING
    };

    public PstAppGroup() {
    }

    public PstAppGroup(int i) throws DBException {
        super(new PstAppGroup());
    }

    public PstAppGroup(String sOid) throws DBException {
        super(new PstAppGroup(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstAppGroup(long lOid) throws DBException {
        super(new PstAppGroup(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        }catch(Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_APP_GROUP;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstAppGroup().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception{
        AppGroup entObj = PstAppGroup.fetchExc(ent.getOID());
        ent = (Entity)entObj;
        return entObj.getOID();
    }

    public static AppGroup fetchExc (long oid) throws DBException{
        AppGroup entObj = new AppGroup();
        try {
            PstAppGroup pstObj = new PstAppGroup(oid);
            entObj.setOID(oid);
            entObj.setGroupName(pstObj.getString(FLD_GROUP_NAME));
            entObj.setDescription(pstObj.getString(FLD_DESCRIPTION));
            return entObj;
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return entObj;
    }

    public long insertExc(Entity ent) {
        try{
            return PstAppGroup.insertExc((AppGroup) ent);
        } catch (Exception e){
            System.out.println(" EXC " + e);
            return 0;
        }
    }

    public static long insertExc(AppGroup entObj) throws DBException {
        PstAppGroup pstObj = new PstAppGroup(0);
        
        pstObj.setString(FLD_GROUP_NAME, entObj.getGroupName());
        pstObj.setString(FLD_DESCRIPTION, entObj.getDescription());
        
        pstObj.insert();
        entObj.setOID(pstObj.getlong(FLD_GROUP_ID));
        return entObj.getOID();
    }

    public long updateExc(Entity ent) throws Exception{
        return updateExc((AppGroup) ent);
    }

    public static long updateExc(AppGroup entObj) throws DBException{
        if( (entObj!=null) && (entObj.getOID() != 0)) {
            try {
                PstAppGroup pstObj = new PstAppGroup(entObj.getOID());

                pstObj.setString(FLD_GROUP_NAME, entObj.getGroupName());
                pstObj.setString(FLD_DESCRIPTION, entObj.getDescription());
                
                pstObj.update();
                return entObj.getOID();
            }catch(Exception e) {
                System.out.println(e);
            }
        }
        return 0;
    }

    public long deleteExc(Entity ent) throws Exception{
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
	}
	return deleteExc(ent.getOID());
    }

    public static int deleteExc(long oid) throws DBException{
        try {
            PstAppGroup pstObj = new PstAppGroup(oid);
            pstObj.delete();
            return 999;
        }catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public static Vector listAll(){
        return list(0, 500, "","");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order){
	Vector lists = new Vector();
	DBResultSet dbrs = null;
	try {
            String sql = "SELECT * FROM " + TBL_APP_GROUP;
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if(order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            if(limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                AppGroup appgroup = new AppGroup();
                resultToObject(rs, appgroup);
                lists.add(appgroup);
            }
            rs.close();
            return lists;
	}catch(Exception e){
            System.out.println(e);
	}finally{
            DBResultSet.close(dbrs);
	}
	return new Vector();
    }

    private static void resultToObject(ResultSet rs, AppGroup appgroup){
	try{
            appgroup.setOID(rs.getLong(PstAppGroup.fieldNames[PstAppGroup.FLD_GROUP_ID]));
            appgroup.setGroupName(rs.getString(PstAppGroup.fieldNames[PstAppGroup.FLD_GROUP_NAME]));
            appgroup.setDescription(rs.getString(PstAppGroup.fieldNames[PstAppGroup.FLD_DESCRIPTION]));
	}catch(Exception e){ }
    }

    public static int getCount(String whereClause){
	DBResultSet dbrs = null;
	try {
            String sql = "SELECT COUNT("+ PstAppGroup.fieldNames[PstAppGroup.FLD_GROUP_ID] + ") FROM " + TBL_APP_GROUP;
            if(whereClause != null && whereClause.length() > 0)
        	sql = sql + " WHERE " + whereClause;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int count = 0;
            while(rs.next()) { count = rs.getInt(1); }
            rs.close();
            return count;
	}catch(Exception e) {
            return 0;
	}finally {
            DBResultSet.close(dbrs);
	}
    }

    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause){
	String order = "";
	int size = getCount(whereClause);
	int start = 0;
	boolean found =false;
	for(int i=0; (i < size) && !found ; i=i+recordToGet){
            Vector list =  list(i,recordToGet, whereClause, orderClause);
            start = i;
            if(list.size()>0){
                for(int ls=0;ls<list.size();ls++){
                    AppGroup appgroup = (AppGroup)list.get(ls);
                    if(oid == appgroup.getOID())
                    found=true;
                }
            }
        }
	if((start >= size) && (size > 0))
	    start = start - recordToGet;
	return start;
    }

    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + mdl;
    	if(start == 0)
            cmd =  Command.FIRST;
        else{
            if(start == (vectSize-recordToGet))
                cmd = Command.LAST;
            else{
            	start = start + recordToGet;
             	if(start <= (vectSize - recordToGet)){
                    cmd = Command.NEXT;
                    System.out.println("next.......................");
             	}else{
                    start = start - recordToGet;
                    if(start > 0){
                         cmd = Command.PREV;
                         System.out.println("prev.......................");
                    }
                }
            }
        }
        return cmd;
    }
    
     public static boolean checkGroupName(String GroupName){
	DBResultSet dbrs = null;
	boolean result = false;
	try{
            String sql = "SELECT * FROM " + TBL_APP_GROUP + " WHERE " +
                PstAppGroup.fieldNames[PstAppGroup.FLD_GROUP_NAME] + " = '" + GroupName +"'";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) { result = true; }
            rs.close();
	}catch(Exception e){
            System.out.println("err : "+e.toString());
	}finally{
            DBResultSet.close(dbrs);
            return result;
	}
    }
}
