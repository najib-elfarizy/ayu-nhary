/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.user.entity;

import java.util.*;
import java.sql.*;

import com.dimata.qdep.entity.*;
import com.dimata.qdep.db.*;
import com.dimata.util.lang.I_Language;

public class PstGroupPriv extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    public static final String TBL_GROUP_PRIV            = "group_priv";
    public static final int FLD_GROUP_ID                 = 0;
    public static final int FLD_PRIV_ID                  = 1;    
    
    public static  final String[] fieldNames = {
        "GROUP_ID",
        "PRIV_ID"
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_FK + TYPE_LONG,
        TYPE_PK + TYPE_FK + TYPE_LONG
    };

    public PstGroupPriv() {
    }

    public PstGroupPriv(int i) throws DBException {
        super(new PstGroupPriv());
    }

    public PstGroupPriv(String sOid) throws DBException {
        super(new PstGroupPriv(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstGroupPriv(long groupOID, long privOID) throws DBException {
        super(new PstGroupPriv(0));
        if(!locate(groupOID, privOID))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_GROUP_PRIV;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstGroupPriv().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception{
        GroupPriv entObj = PstGroupPriv.fetchExc(ent.getOID(0), ent.getOID(1));
        ent = (Entity)entObj;
        return entObj.getOID();
    }

    public static GroupPriv fetchExc(long groupOID, long privOID) throws DBException{
        GroupPriv entObj = new GroupPriv();
        try {
            PstGroupPriv pstObj = new PstGroupPriv(groupOID, privOID);
            entObj.setIdGroup(groupOID);
            entObj.setIdPriv(privOID);
            return entObj;
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return entObj;
    }

    public long insertExc(Entity ent) {
        return PstGroupPriv.insertExc((GroupPriv) ent);
    }

    public static long insertExc(GroupPriv entObj) {
        try {
            PstGroupPriv pstObj = new PstGroupPriv(0);
            
            pstObj.setLong(FLD_GROUP_ID, entObj.getIdGroup());            
            pstObj.setLong(FLD_PRIV_ID, entObj.getIdPriv());
            
            pstObj.insert();
            return entObj.getIdGroup();
        } catch(DBException e) {
            System.out.println(e);
        }
        return 0;
    }

    public long updateExc(Entity ent) throws Exception{
        return updateExc((GroupPriv) ent);
    }

    public static long updateExc(GroupPriv entObj) throws DBException{
        if( (entObj!=null) && (entObj.getOID() != 0)) {
            try {
                PstGroupPriv pstObj = new PstGroupPriv(entObj.getIdGroup(), entObj.getIdPriv());
                
                pstObj.setLong(FLD_GROUP_ID, entObj.getIdGroup());            
                pstObj.setLong(FLD_PRIV_ID, entObj.getIdPriv());
                
                pstObj.update();
                return entObj.getIdGroup();
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
	return deleteExc(ent.getOID(0), ent.getOID(1));
    }

    public static long deleteExc(long groupOID, long privOID) throws DBException {
        try {
            PstGroupPriv pstObj = new PstGroupPriv(groupOID, privOID);
            pstObj.delete();
            return groupOID;
        }catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public static int deleteByGroup(long oid) throws DBException, SQLException {
        Connection conn = DBHandler.getConnection();
        Statement stmt = conn.createStatement();
        PstGroupPriv pstObj = new PstGroupPriv();
        try {
            String sql = "DELETE FROM " + pstObj.getTableName() +
                         " WHERE " + PstGroupPriv.fieldNames[PstGroupPriv.FLD_GROUP_ID] +
                         " = '" + oid +"'";
            stmt.executeUpdate(sql);
            return 999;
        }catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public static int deleteByPriv(long oid) throws DBException, SQLException {
        Connection conn = DBHandler.getConnection();
        Statement stmt = conn.createStatement();
        PstGroupPriv pstObj = new PstGroupPriv();
        try {
            String sql = "DELETE FROM " + pstObj.getTableName() +
                         " WHERE " + PstGroupPriv.fieldNames[PstGroupPriv.FLD_PRIV_ID] +
                         " = '" + oid +"'";
            stmt.executeUpdate(sql);
            return 999;
        }catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public static Vector listAll(){
        return list(0, 500, null, null);
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order){
	Vector lists = new Vector();
	DBResultSet dbrs = null;
	try {
            String sql = "SELECT * FROM " + TBL_GROUP_PRIV;
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
                GroupPriv grouppriv = new GroupPriv();
                resultToObject(rs, grouppriv);
                lists.add(grouppriv);
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

    private static void resultToObject(ResultSet rs, GroupPriv grouppriv){
	try{
            grouppriv.setIdGroup(rs.getLong(PstGroupPriv.fieldNames[PstGroupPriv.FLD_GROUP_ID]));
            grouppriv.setIdPriv(rs.getLong(PstGroupPriv.fieldNames[PstGroupPriv.FLD_PRIV_ID]));
	}catch(Exception e){ }
    }
    
    public static boolean setAllGroupPriv(long oid, Vector vector) throws DBException, SQLException {
        if(PstGroupPriv.deleteByGroup(oid) == 0)
            return false;
        if(vector == null || vector.size() == 0)
            return true;
        for(int i = 0; i < vector.size(); i++) {
            GroupPriv grouppriv = (GroupPriv)vector.get(i);
            if(PstGroupPriv.insertExc(grouppriv) == 0)
                return false;
        }
        return true;
    }
    
    public static boolean checkPriv(long privOID){
	DBResultSet dbrs = null;
	boolean result = false;
	try{
            String sql = "SELECT * FROM " + TBL_GROUP_PRIV + " WHERE " +
                PstGroupPriv.fieldNames[PstGroupPriv.FLD_PRIV_ID] + " = '" + privOID +"'";
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