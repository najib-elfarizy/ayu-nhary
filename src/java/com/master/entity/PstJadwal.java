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

public class PstJadwal extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_NAME                = "jadwal";
    
    public static final int FLD_ID_JADWAL              = 0;
    public static final int FLD_NAMA_JADWAL            = 1;
    public static final int FLD_STATUS                 = 2;
    public static final int FLD_NILAI_FITNESS          = 3;
      
    public static final String[] fieldNames = {
        "ID_JADWAL",
        "NAMA_JADWAL",
        "STATUS",
        "NILAI_FITNESS"
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_STRING,
        TYPE_INT,
        TYPE_FLOAT
    };
   
    public PstJadwal() {
    }

    public PstJadwal(int i) throws DBException {
        super(new PstJadwal());
    }
    public PstJadwal(String sOid) throws DBException {
        super(new PstJadwal(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstJadwal(long lOid) throws DBException {
        super(new PstJadwal(0));
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
        return new PstJadwal().getClass().getName();
    }
    
    @Override
    public long fetchExc(Entity entity) throws Exception {
        Jadwal entObj = PstJadwal.fetchExc(entity.getOID());
        entity = (Entity)entObj;
        return entObj.getOID();
    }
    
    public static Jadwal fetchExc (long oid) throws DBException{
        Jadwal entObj = new Jadwal();
         try {
            PstJadwal pstObj = new PstJadwal(oid);
            entObj.setOID(oid);           
            entObj.setNamaJadwal(pstObj.getString(FLD_NAMA_JADWAL));
            entObj.setStatus(pstObj.getInt(FLD_STATUS));
            entObj.setNilaiFitness(pstObj.getfloat(FLD_NILAI_FITNESS));
            return entObj;
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return entObj;
    }

    public long updateExc(Entity ent) throws Exception{
        return updateExc((Jadwal) ent);
    }
   
    public static long updateExc(Jadwal entObj) throws DBException{
        if( (entObj!=null) && (entObj.getOID() != 0)) {
            try {
                PstJadwal pstObj = new PstJadwal(entObj.getOID());           
                pstObj.setString(FLD_NAMA_JADWAL, entObj.getNamaJadwal());
                pstObj.setInt(FLD_STATUS, entObj.getStatus());
                pstObj.setFloat(FLD_NILAI_FITNESS, entObj.getNilaiFitness());
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

    public static long deleteExc(long oid) throws DBException{
        try {
            PstJadwal pstObj = new PstJadwal(oid);
            pstObj.delete();
            return 1;
        }catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }
    
    public long insertExc(Entity ent) {
        try{
            return PstJadwal.insertExc((Jadwal) ent);
        } catch (Exception e){
            System.out.println(" EXC " + e);
            return 0;
        }
    }
    
    public static long insertExc(Jadwal entObj) throws DBException {
        PstJadwal pstObj = new PstJadwal(0);        
        pstObj.setString(FLD_NAMA_JADWAL, entObj.getNamaJadwal()); 
        pstObj.setInt(FLD_STATUS, entObj.getStatus());
        pstObj.setFloat(FLD_NILAI_FITNESS, entObj.getNilaiFitness());
        pstObj.insert();
        entObj.setOID(pstObj.getlong(FLD_ID_JADWAL));
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
                Jadwal ent = new Jadwal();
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
    
    private static void resultToObject(ResultSet rs, Jadwal ent){
	try{
            ent.setOID(rs.getLong(PstJadwal.fieldNames[PstJadwal.FLD_ID_JADWAL]));
            ent.setNamaJadwal(rs.getString(PstJadwal.fieldNames[PstJadwal.FLD_NAMA_JADWAL]));
            ent.setStatus(rs.getInt(PstJadwal.fieldNames[PstJadwal.FLD_STATUS]));
            ent.setNilaiFitness(rs.getFloat(PstJadwal.fieldNames[PstJadwal.FLD_NILAI_FITNESS]));
	}catch(Exception e){ }
    }
    
    public static int getCount(String whereClause){
	DBResultSet dbrs = null;
	try {
            String sql = "SELECT COUNT("+ PstJadwal.fieldNames[PstJadwal.FLD_ID_JADWAL] + ") FROM " + TBL_NAME;
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
                    Jadwal ent = (Jadwal)list.get(ls);
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
    
    public static boolean checkNamaJadwal(String Name){
	DBResultSet dbrs = null;
	boolean result = false;
	try{
            String sql = "SELECT * FROM " + TBL_NAME + " WHERE " +
                         fieldNames[FLD_NAMA_JADWAL] + " = '" + Name +"'";
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