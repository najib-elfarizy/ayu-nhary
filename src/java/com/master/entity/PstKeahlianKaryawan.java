/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.master.entity;

import java.util.*;
import java.sql.*;

import com.dimata.qdep.entity.*;
import com.dimata.qdep.db.*;
import com.dimata.util.lang.I_Language;

public class PstKeahlianKaryawan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    public static final String TBL_KARYAWAN_KEAHLIAN            = "keahlian_karyawan";
    
    public static final int FLD_KEAHLIAN_ID                     = 0;
    public static final int FLD_KARYAWAN_ID                     = 1;
    
    public static  final String[] fieldNames = {
        "KODE_KEAHLIAN",
        "NIK"
    };
    
    public static int[] fieldTypes = {
        TYPE_FK + TYPE_LONG,
        TYPE_FK + TYPE_LONG
    };

    public PstKeahlianKaryawan() {
    }

    public PstKeahlianKaryawan(int i) throws DBException {
        super(new PstKeahlianKaryawan());
    }

    public PstKeahlianKaryawan(String sOid) throws DBException {
        super(new PstKeahlianKaryawan(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstKeahlianKaryawan(long karyawanOID, long keahlianOID) throws DBException {
        super(new PstKeahlianKaryawan(0));
        if(!locate(karyawanOID, keahlianOID))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_KARYAWAN_KEAHLIAN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstKeahlianKaryawan().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception{
        KeahlianKaryawan entObj = PstKeahlianKaryawan.fetchExc(ent.getOID(0), ent.getOID(1));
        ent = (Entity)entObj;
        return entObj.getOID();
    }

    public static KeahlianKaryawan fetchExc(long karyawanOID, long keahlianOID) throws DBException{
        KeahlianKaryawan entObj = new KeahlianKaryawan();
        try {
            PstKeahlianKaryawan pstObj = new PstKeahlianKaryawan(karyawanOID, keahlianOID);
            entObj.setIdKaryawan(karyawanOID);
            entObj.setIdKeahlian(keahlianOID);
            return entObj;
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return entObj;
    }

    public long insertExc(Entity ent) {
        return PstKeahlianKaryawan.insertExc((KeahlianKaryawan) ent);
    }

    public static long insertExc(KeahlianKaryawan entObj) {
        try {
            PstKeahlianKaryawan pstObj = new PstKeahlianKaryawan(0);
            
            pstObj.setLong(FLD_KARYAWAN_ID, entObj.getIdKaryawan());            
            pstObj.setLong(FLD_KEAHLIAN_ID, entObj.getIdKeahlian());
            
            pstObj.insert();
            return entObj.getIdKaryawan();
        } catch(DBException e) {
            System.out.println(e);
        }
        return 0;
    }

    public long updateExc(Entity ent) throws Exception{
        return updateExc((KeahlianKaryawan) ent);
    }

    public static long updateExc(KeahlianKaryawan entObj) throws DBException{
        if( (entObj!=null) && (entObj.getOID() != 0)) {
            try {
                PstKeahlianKaryawan pstObj = new PstKeahlianKaryawan(entObj.getIdKaryawan(), entObj.getIdKeahlian());
                
                pstObj.setLong(FLD_KARYAWAN_ID, entObj.getIdKaryawan());            
                pstObj.setLong(FLD_KEAHLIAN_ID, entObj.getIdKeahlian());
                
                pstObj.update();
                return entObj.getIdKaryawan();
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

    public static long deleteExc(long karyawanOID, long keahlianOID) throws DBException {
        try {
            PstKeahlianKaryawan pstObj = new PstKeahlianKaryawan(karyawanOID, keahlianOID);
            pstObj.delete();
            return karyawanOID;
        }catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public static int deleteByKaryawan(long oid) throws DBException, SQLException {
        Connection conn = DBHandler.getConnection();
        Statement stmt = conn.createStatement();
        PstKeahlianKaryawan pstObj = new PstKeahlianKaryawan();
        try {
            String sql = "DELETE FROM " + pstObj.getTableName() +
                         " WHERE " + PstKeahlianKaryawan.fieldNames[PstKeahlianKaryawan.FLD_KARYAWAN_ID] +
                         " = '" + oid +"'";
            stmt.executeUpdate(sql);
            return 999;
        }catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public static int deleteByKeahlian(long oid) throws DBException, SQLException {
        Connection conn = DBHandler.getConnection();
        Statement stmt = conn.createStatement();
        PstKeahlianKaryawan pstObj = new PstKeahlianKaryawan();
        try {
            String sql = "DELETE FROM " + pstObj.getTableName() +
                         " WHERE " + PstKeahlianKaryawan.fieldNames[PstKeahlianKaryawan.FLD_KEAHLIAN_ID] +
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
            String sql = "SELECT * FROM " + TBL_KARYAWAN_KEAHLIAN;
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
                KeahlianKaryawan karyawankeahlian = new KeahlianKaryawan();
                resultToObject(rs, karyawankeahlian);
                lists.add(karyawankeahlian);
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

    private static void resultToObject(ResultSet rs, KeahlianKaryawan karyawankeahlian){
	try{
            karyawankeahlian.setIdKaryawan(rs.getLong(PstKeahlianKaryawan.fieldNames[PstKeahlianKaryawan.FLD_KARYAWAN_ID]));
            karyawankeahlian.setIdKeahlian(rs.getLong(PstKeahlianKaryawan.fieldNames[PstKeahlianKaryawan.FLD_KEAHLIAN_ID]));
	}catch(Exception e){ }
    }
    
    public static boolean setAllKaryawanKeahlian(long oid, Vector vector) throws DBException, SQLException {
        if(PstKeahlianKaryawan.deleteByKaryawan(oid) == 0)
            return false;
        if(vector == null || vector.size() == 0)
            return true;
        for(int i = 0; i < vector.size(); i++) {
            KeahlianKaryawan karyawankeahlian = (KeahlianKaryawan)vector.get(i);
            if(PstKeahlianKaryawan.insertExc(karyawankeahlian) == 0)
                return false;
        }
        return true;
    }
    
    public static boolean checkKeahlian(long keahlianOID){
	DBResultSet dbrs = null;
	boolean result = false;
	try{
            String sql = "SELECT * FROM " + TBL_KARYAWAN_KEAHLIAN + " WHERE " +
                PstKeahlianKaryawan.fieldNames[PstKeahlianKaryawan.FLD_KEAHLIAN_ID] + " = '" + keahlianOID +"'";
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
    
    public static Vector getKaryawanByKeahlian(long keahlianOID){
        return list(0,0,PstKeahlianKaryawan.fieldNames[PstKeahlianKaryawan.FLD_KEAHLIAN_ID]+" = '"+keahlianOID+"'","");
    }
    
}
