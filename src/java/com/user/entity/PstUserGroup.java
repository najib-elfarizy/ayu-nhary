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

public class PstUserGroup extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    public static final String TBL_USER_GROUP            = "user_group";
    public static final int FLD_USER_ID                  = 0;
    public static final int FLD_GROUP_ID                 = 1;    
    
    public static  final String[] fieldNames = {
        "USER_ID",
        "GROUP_ID"
    } ;
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_FK + TYPE_LONG,
        TYPE_PK + TYPE_FK + TYPE_LONG
    };

    public PstUserGroup() {
    }

    public PstUserGroup(int i) throws DBException {
        super(new PstUserGroup());
    }

    public PstUserGroup(String sOid) throws DBException {
        super(new PstUserGroup(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstUserGroup(long userOID, long groupOID) throws DBException {
        super(new PstUserGroup(0));
        if(!locate(userOID, groupOID))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_USER_GROUP;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstUserGroup().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception{
        UserGroup entObj = PstUserGroup.fetchExc(ent.getOID(0), ent.getOID(1));
        ent = (Entity)entObj;
        return entObj.getOID();
    }

    public static UserGroup fetchExc(long userOID, long groupOID) throws DBException{
        UserGroup entObj = new UserGroup();
        try {
            PstUserGroup pstObj = new PstUserGroup(userOID, groupOID);
            entObj.setIdUser(userOID);
            entObj.setIdGroup(groupOID);
            return entObj;
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return entObj;
    }

    public long insertExc(Entity ent) {
        return PstUserGroup.insertExc((UserGroup) ent);
    }

    public static long insertExc(UserGroup entObj) {
        try {
            PstUserGroup pstObj = new PstUserGroup(0);
            
            pstObj.setLong(FLD_USER_ID, entObj.getIdUser());            
            pstObj.setLong(FLD_GROUP_ID, entObj.getIdGroup());
            
            pstObj.insert();
            return entObj.getIdUser();
        } catch(DBException e) {
            System.out.println(e);
        }
        return 0;
    }

    public long updateExc(Entity ent) throws Exception{
        return updateExc((UserGroup) ent);
    }

    public static long updateExc(UserGroup entObj) throws DBException{
        if( (entObj!=null) && (entObj.getOID() != 0)) {
            try {
                PstUserGroup pstObj = new PstUserGroup(entObj.getIdUser(), entObj.getIdGroup());
                
                pstObj.setLong(FLD_USER_ID, entObj.getIdUser());            
                pstObj.setLong(FLD_GROUP_ID, entObj.getIdGroup());
                
                pstObj.update();
                return entObj.getIdUser();
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

    public static long deleteExc(long userOID, long groupOID) throws DBException {
        try {
            PstUserGroup pstObj = new PstUserGroup(userOID, groupOID);
            pstObj.delete();
            return userOID;
        }catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public static int deleteByUser(long oid) throws DBException, SQLException {
        Connection conn = DBHandler.getConnection();
        Statement stmt = conn.createStatement();
        PstUserGroup pstObj = new PstUserGroup();
        try {
            String sql = "DELETE FROM " + pstObj.getTableName() +
                         " WHERE " + PstUserGroup.fieldNames[PstUserGroup.FLD_USER_ID] +
                         " = '" + oid +"'";
            stmt.executeUpdate(sql);
            return 999;
        }catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public static int deleteByGroup(long oid) throws DBException, SQLException {
        Connection conn = DBHandler.getConnection();
        Statement stmt = conn.createStatement();
        PstUserGroup pstObj = new PstUserGroup();
        try {
            String sql = "DELETE FROM " + pstObj.getTableName() +
                         " WHERE " + PstUserGroup.fieldNames[PstUserGroup.FLD_GROUP_ID] +
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
            String sql = "SELECT * FROM " + TBL_USER_GROUP;
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
                UserGroup usergroup = new UserGroup();
                resultToObject(rs, usergroup);
                lists.add(usergroup);
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

    private static void resultToObject(ResultSet rs, UserGroup usergroup){
	try{
            usergroup.setIdUser(rs.getLong(PstUserGroup.fieldNames[PstUserGroup.FLD_USER_ID]));
            usergroup.setIdGroup(rs.getLong(PstUserGroup.fieldNames[PstUserGroup.FLD_GROUP_ID]));
	}catch(Exception e){ }
    }
    
    public static boolean setAllUserGroup(long oid, Vector vector) throws DBException, SQLException {
        if(PstUserGroup.deleteByUser(oid) == 0)
            return false;
        if(vector == null || vector.size() == 0)
            return true;
        for(int i = 0; i < vector.size(); i++) {
            UserGroup usergroup = (UserGroup)vector.get(i);
            if(PstUserGroup.insertExc(usergroup) == 0)
                return false;
        }
        return true;
    }
    
    public static boolean checkGroup(long groupOID){
	DBResultSet dbrs = null;
	boolean result = false;
	try{
            String sql = "SELECT * FROM " + TBL_USER_GROUP + " WHERE " +
                PstUserGroup.fieldNames[PstUserGroup.FLD_GROUP_ID] + " = '" + groupOID +"'";
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