/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.master.entity;

import java.sql.*;

import com.dimata.qdep.entity.*;
import com.dimata.qdep.db.*;
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;
import java.util.Vector;

public class PstProyek extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_NAME                = "proyek";
    
    public static final int FLD_ID_PROYEK              = 0;
    public static final int FLD_NAMA_PROYEK            = 1;
    public static final int FLD_WAKTU_MULAI            = 2;
    public static final int FLD_WAKTU_SELESAI          = 3;
    public static final int FLD_TOTAL                  = 4;
      
    public static final String[] fieldNames = {
        "ID_PROYEK",
        "NAMA_PROYEK",
        "WAKTU_MULAI",
        "WAKTU_SELESAI",
        "TOTAL"
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_FLOAT
    };
   
    public PstProyek() {
    }

    public PstProyek(int i) throws DBException {
        super(new PstProyek());
    }
    public PstProyek(String sOid) throws DBException {
        super(new PstProyek(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstProyek(long lOid) throws DBException {
        super(new PstProyek(0));
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
        return new PstProyek().getClass().getName();
    }
    
    @Override
    public long fetchExc(Entity entity) throws Exception {
        Proyek entObj = PstProyek.fetchExc(entity.getOID());
        entity = (Entity)entObj;
        return entObj.getOID();
    }
    
    public static Proyek fetchExc (long oid) throws DBException{
        Proyek entObj = new Proyek();
         try {
            PstProyek pstObj = new PstProyek(oid);
            entObj.setOID(oid);
            entObj.setNamaProyek(pstObj.getString(FLD_NAMA_PROYEK));
            entObj.setWaktuMulai(pstObj.getDate(FLD_WAKTU_MULAI));
            entObj.setWaktuSelesai(pstObj.getDate(FLD_WAKTU_SELESAI));
            entObj.setTotal(pstObj.getfloat(FLD_TOTAL));
            return entObj;
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return entObj;
    }

    public long updateExc(Entity ent) throws Exception{
        return updateExc((Proyek) ent);
    }
   
    public static long updateExc(Proyek entObj) throws DBException{
        if( (entObj!=null) && (entObj.getOID() != 0)) {
            try {
                PstProyek pstObj = new PstProyek(entObj.getOID());           
                pstObj.setString(FLD_NAMA_PROYEK, entObj.getNamaProyek());
                pstObj.setDate(FLD_WAKTU_MULAI, entObj.getWaktuMulai());
                 pstObj.setDate(FLD_WAKTU_SELESAI, entObj.getWaktuSelesai());
                 pstObj.setFloat(FLD_TOTAL, entObj.getTotal());
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
            PstProyek pstObj = new PstProyek(oid);
            pstObj.delete();
            return 1;
        }catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }
    
    public long insertExc(Entity ent) {
        try{
            return PstProyek.insertExc((Proyek) ent);
        } catch (Exception e){
            System.out.println(" EXC " + e);
            return 0;
        }
    }
    
    public static long insertExc(Proyek entObj) throws DBException {
        PstProyek pstObj = new PstProyek(0);        
        pstObj.setString(FLD_NAMA_PROYEK, entObj.getNamaProyek());
        pstObj.setDate(FLD_WAKTU_MULAI, entObj.getWaktuMulai());
                 pstObj.setDate(FLD_WAKTU_SELESAI, entObj.getWaktuSelesai());
                 pstObj.setFloat(FLD_TOTAL, entObj.getTotal());
        pstObj.insert();
        entObj.setOID(pstObj.getlong(FLD_ID_PROYEK));
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
                Proyek ent = new Proyek();
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
    
    private static void resultToObject(ResultSet rs, Proyek ent){
	try{
            ent.setOID(rs.getLong(PstProyek.fieldNames[PstProyek.FLD_ID_PROYEK]));
            ent.setNamaProyek(rs.getString(PstProyek.fieldNames[PstProyek.FLD_NAMA_PROYEK]));
            Timestamp timestampmulai = rs.getTimestamp(PstProyek.fieldNames[PstProyek.FLD_WAKTU_MULAI]);
            ent.setWaktuMulai(new java.util.Date(timestampmulai.getTime()));
            Timestamp timestampselesai = rs.getTimestamp(PstProyek.fieldNames[PstProyek.FLD_WAKTU_SELESAI]);
            ent.setWaktuSelesai(new java.util.Date(timestampselesai.getTime()));
            ent.setTotal(rs.getFloat(PstProyek.fieldNames[PstProyek.FLD_TOTAL]));
	}catch(Exception e){ }
    }
    
    public static int getCount(String whereClause){
	DBResultSet dbrs = null;
	try {
            String sql = "SELECT COUNT("+ PstProyek.fieldNames[PstProyek.FLD_ID_PROYEK] + ") FROM " + TBL_NAME;
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
                    Proyek ent = (Proyek)list.get(ls);
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