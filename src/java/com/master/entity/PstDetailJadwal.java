/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.master.entity;

import java.util.*;
import java.sql.*;
import java.io.*;

import com.dimata.qdep.entity.*;
import com.dimata.qdep.db.*;
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;

public class PstDetailJadwal extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_NAME                = "jadwal_detail";
    
    public static final int FLD_ID_DETAIL_JADWAL       = 0;
    public static final int FLD_ID_JADWAL              = 1;
    public static final int FLD_ID_PROYEK              = 2;
    public static final int FLD_NIP           = 3;
    public static final int FLD_JAM_MULAI              = 4;
    public static final int FLD_JAM_SELESAI             = 5;
      
    public static final String[] fieldNames = {
        "ID_DETAIL_JADWAL",
        "ID_JADWAL",
        "ID_PROYEK",
        "NIP",
        "JAM_MULAI",
        "JAM_SELESAI"
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_LONG + TYPE_FK,
        TYPE_LONG + TYPE_FK,
        TYPE_LONG + TYPE_FK,
        TYPE_DATE,
        TYPE_DATE
    };
   
    public PstDetailJadwal() {
    }

    public PstDetailJadwal(int i) throws DBException {
        super(new PstDetailJadwal());
    }
    public PstDetailJadwal(String sOid) throws DBException {
        super(new PstDetailJadwal(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstDetailJadwal(long lOid) throws DBException {
        super(new PstDetailJadwal(0));
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
        return TBL_NAME;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstDetailJadwal().getClass().getName();
    }
    
    @Override
    public long fetchExc(Entity entity) throws Exception {
        DetailJadwal entObj = PstDetailJadwal.fetchExc(entity.getOID());
        entity = (Entity)entObj;
        return entObj.getOID();
    }
    
    public static DetailJadwal fetchExc (long oid) throws DBException{
        DetailJadwal entObj = new DetailJadwal();
         try {
            PstDetailJadwal pstObj = new PstDetailJadwal(oid);
            entObj.setOID(oid);           
            entObj.setIdJadwal(pstObj.getlong(FLD_ID_JADWAL));
            entObj.setIdProyek(pstObj.getlong(FLD_ID_PROYEK));
            entObj.setNIP(pstObj.getlong(FLD_NIP));
            entObj.setJamMulai(pstObj.getDate(FLD_JAM_MULAI));
            entObj.setJamSelesai(pstObj.getDate(FLD_JAM_SELESAI));
            return entObj;
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return entObj;
    }

    public long updateExc(Entity ent) throws Exception{
        return updateExc((DetailJadwal) ent);
    }
   
    public static long updateExc(DetailJadwal entObj) throws DBException{
        if( (entObj!=null) && (entObj.getOID() != 0)) {
            try {
                PstDetailJadwal pstObj = new PstDetailJadwal(entObj.getOID());           
                pstObj.setLong(FLD_ID_JADWAL, entObj.getIdJadwal());
                pstObj.setLong(FLD_ID_PROYEK, entObj.getIdProyek());
                pstObj.setLong(FLD_NIP, entObj.getNIP());
                pstObj.setDate(FLD_JAM_MULAI, entObj.getJamMulai());
                pstObj.setDate(FLD_JAM_SELESAI, entObj.getJamSelesai());
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
	return deleteExc(((DetailJadwal)ent).getOID());
    }

    public static long deleteExc(long oid) throws DBException{
        try {
            PstDetailJadwal pstObj = new PstDetailJadwal(oid);
            pstObj.delete();
            return 1;
        }catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }
    
    public static int deleteByJadwal(long oid) throws DBException, SQLException {
        Connection conn = DBHandler.getConnection();
        Statement stmt = conn.createStatement();
        PstDetailJadwal pstObj = new PstDetailJadwal();
        try {
            String sql = "DELETE FROM " + pstObj.getTableName() +
                         " WHERE " + PstDetailJadwal.fieldNames[PstDetailJadwal.FLD_ID_JADWAL] +
                         " = '" + oid +"'";
            stmt.executeUpdate(sql);
            return 999;
        }catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }
    
    public long insertExc(Entity ent) {
        try{
            return PstDetailJadwal.insertExc((DetailJadwal) ent);
        } catch (Exception e){
            System.out.println(" EXC " + e);
            return 0;
        }
    }
    
    public static long insertExc(DetailJadwal entObj) throws DBException {
        PstDetailJadwal pstObj = new PstDetailJadwal(0);        
        pstObj.setLong(FLD_ID_JADWAL, entObj.getIdJadwal());
        pstObj.setLong(FLD_ID_PROYEK, entObj.getIdProyek());
        pstObj.setLong(FLD_NIP, entObj.getNIP());
       
                pstObj.setDate(FLD_JAM_MULAI, entObj.getJamMulai());
                pstObj.setDate(FLD_JAM_SELESAI, entObj.getJamSelesai());     
        pstObj.insert();
        entObj.setOID(pstObj.getlong(FLD_ID_DETAIL_JADWAL));
        return entObj.getOID();
    }
    
    public static Vector listAll(){
        return list(0, 500, "","");
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order){
	Vector lists = new Vector();
	DBResultSet dbrs = null;
	try {
            String sql = "SELECT * FROM " + TBL_NAME;
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
                DetailJadwal ent = new DetailJadwal();
                resultToObject(rs, ent);
                lists.add(ent);
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
    
    public static Vector listJoinJadwalNew(int limitStart, int recordToGet){
	Vector lists = new Vector();
	DBResultSet dbrs = null;
	try {
            String sql = "SELECT jadwal_detail.`ID_DETAIL_JADWAL`, jadwal_detail.`ID_JADWAL`, jadwal_detail.`ID_PROYEK`, jadwal_detail.`JAM_MULAI`, jadwal_detail.`JAM_SELESAI`, jadwal_detail.`NIP` " +
                         "FROM jadwal_detail INNER JOIN jadwal ON jadwal_detail.`ID_JADWAL` = jadwal.`ID_JADWAL` WHERE jadwal.`STATUS`= ' 0 ' ORDER BY `ID_PROYEK` DESC";   
            if(limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
            System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                DetailJadwal ent = new DetailJadwal();
                resultToObject(rs, ent);
                lists.add(ent);
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
    
    private static void resultToObject(ResultSet rs, DetailJadwal ent){
	try{
            ent.setOID(rs.getLong(PstDetailJadwal.fieldNames[PstDetailJadwal.FLD_ID_DETAIL_JADWAL]));
            ent.setIdJadwal(rs.getLong(PstDetailJadwal.fieldNames[PstDetailJadwal.FLD_ID_JADWAL]));
            ent.setIdProyek(rs.getLong(PstDetailJadwal.fieldNames[PstDetailJadwal.FLD_ID_PROYEK]));
            ent.setNIP(rs.getLong(PstDetailJadwal.fieldNames[PstDetailJadwal.FLD_NIP]));
            ent.setJamMulai(rs.getDate(PstDetailJadwal.fieldNames[PstDetailJadwal.FLD_JAM_MULAI]));
            ent.setJamSelesai(rs.getDate(PstDetailJadwal.fieldNames[PstDetailJadwal.FLD_JAM_SELESAI]));
	}catch(Exception e){ }
    }
    
    public static int getCount(String whereClause){
	DBResultSet dbrs = null;
	try {
            String sql = "SELECT COUNT("+ PstDetailJadwal.fieldNames[PstDetailJadwal.FLD_ID_DETAIL_JADWAL] + ") FROM " + TBL_NAME + " INNER JOIN jadwal ON jadwal_detail.`ID_JADWAL` = jadwal.`ID_JADWAL` WHERE jadwal.`STATUS`= ' 0 '";
            if(whereClause != null && whereClause.length() > 0)
        	sql = sql + " WHERE " + whereClause;
            System.out.println(sql);
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
                    DetailJadwal ent = (DetailJadwal)list.get(ls);
                    if(oid == ent.getOID())
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
     
}