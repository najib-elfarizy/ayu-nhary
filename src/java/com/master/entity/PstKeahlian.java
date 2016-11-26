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

public class PstKeahlian extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_NAME                = "keahlian";
    
    public static final int FLD_KODE_KEAHLIAN          = 0;
    public static final int FLD_NAMA_KEAHLIAN          = 1;
      
    public static final String[] fieldNames = {
        "KODE_KEAHLIAN",
        "NAMA_KEAHLIAN",    
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_STRING   
    };
   
    public PstKeahlian() {
    }

    public PstKeahlian(int i) throws DBException {
        super(new PstKeahlian());
    }
    
    public PstKeahlian(String sOid) throws DBException {
        super(new PstKeahlian(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstKeahlian(long lOid) throws DBException {
        super(new PstKeahlian(0));
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
        return new PstKeahlian().getClass().getName();
    }
    
    @Override
    public long fetchExc(Entity entity) throws Exception {
        Keahlian entObj = PstKeahlian.fetchExc(entity.getOID());
        entity = (Entity)entObj;
        return entObj.getOID();
    }
    
    public static Keahlian fetchExc (long oid) throws DBException{
        Keahlian entObj = new Keahlian();
         try {
            PstKeahlian pstObj = new PstKeahlian(oid);
            entObj.setOID(oid);
            entObj.setNamaKeahlian(pstObj.getString(FLD_NAMA_KEAHLIAN));
            return entObj;
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return entObj;
    }

    public long updateExc(Entity ent) throws Exception{
        return updateExc((Keahlian) ent);
    }
   
    public static long updateExc(Keahlian entObj) throws DBException{
        if( (entObj!=null) && (entObj.getOID()!=0) ) {
            try {
                PstKeahlian pstObj = new PstKeahlian(entObj.getOID());
                pstObj.setString(FLD_NAMA_KEAHLIAN, entObj.getNamaKeahlian());                
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
            PstKeahlian pstObj = new PstKeahlian(oid);
            pstObj.delete();
            return 1;
        }catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }
    
    public long insertExc(Entity ent) {
        try{
            return PstKeahlian.insertExc((Keahlian) ent);
        } catch (Exception e){
            System.out.println(" EXC " + e);
            return 0;
        }
    }
    
    public static long insertExc(Keahlian entObj) throws DBException {
        PstKeahlian pstObj = new PstKeahlian(0);
        pstObj.setString(FLD_NAMA_KEAHLIAN, entObj.getNamaKeahlian());       
        pstObj.insert();
        entObj.setOID(pstObj.getLong(FLD_KODE_KEAHLIAN));
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
                Keahlian ent = new Keahlian();
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
    
    private static void resultToObject(ResultSet rs, Keahlian ent){
	try{           
            ent.setOID(rs.getLong(PstKeahlian.fieldNames[PstKeahlian.FLD_KODE_KEAHLIAN]));
            ent.setNamaKeahlian(rs.getString(PstKeahlian.fieldNames[PstKeahlian.FLD_NAMA_KEAHLIAN]));
	}catch(Exception e){ }
    }
    
    public static int getCount(String whereClause){
	DBResultSet dbrs = null;
	try {
            String sql = "SELECT COUNT("+ PstKeahlian.fieldNames[PstKeahlian.FLD_KODE_KEAHLIAN] + ") FROM " + TBL_NAME;
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
                    Keahlian ent = (Keahlian)list.get(ls);
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
    
    public static boolean checkKodeKeahlian(String Kode){
	DBResultSet dbrs = null;
	boolean result = false;
	try{
            String sql = "SELECT * FROM " + TBL_NAME + " WHERE " +
                         fieldNames[FLD_KODE_KEAHLIAN] + " = '" + Kode +"'";
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