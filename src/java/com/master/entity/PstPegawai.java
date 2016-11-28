/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.master.entity;

import java.util.*;
import java.sql.*;

import com.dimata.qdep.entity.*;
import com.dimata.qdep.db.*;
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;

public class PstPegawai extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_NAME = "pegawai";

    public static final int FLD_NIP = 0;
    public static final int FLD_NAMA = 1;
    public static final int FLD_JENIS_KELAMIN = 2;
    public static final int FLD_TEMPAT_LAHIR = 3;
    public static final int FLD_TANGGAL_LAHIR = 4;
    public static final int FLD_ALAMAT = 5;
    public static final int FLD_PENDIDIKAN = 6;
    public static final int FLD_EMAIL = 7;
    public static final int FLD_JABATAN = 8;

    public static final String[] fieldNames = {
        "NIP",
        "NAMA_PEGAWAI",
        "JENIS_KELAMIN",
        "TEMPAT_LAHIR",
        "TGL_LAHIR",
        "ALAMAT",
        "PENDIDIKAN",
        "EMAIL",
        "JABATAN"
    };

    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING
    };

    public PstPegawai() {
    }

    public PstPegawai(int i) throws DBException {
        super(new PstPegawai());
    }

    public PstPegawai(String sOid) throws DBException {
        super(new PstPegawai(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstPegawai(long lOid) throws DBException {
        super(new PstPegawai(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
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
        return new PstPegawai().getClass().getName();
    }

    @Override
    public long fetchExc(Entity entity) throws Exception {
        Pegawai entObj = PstPegawai.fetchExc(entity.getOID());
        entity = (Entity) entObj;
        return entObj.getOID();
    }

    public static Pegawai fetchExc(long oid) throws DBException {
        Pegawai entObj = new Pegawai();
        try {
            PstPegawai pstObj = new PstPegawai(oid);
            entObj.setOID(oid);
            entObj.setNamaPegawai(pstObj.getString(FLD_NAMA));
            entObj.setJenisKelamin(pstObj.getString(FLD_JENIS_KELAMIN));
            entObj.setTempatLahir(pstObj.getString(FLD_TEMPAT_LAHIR));
            entObj.setTanggalLahir(pstObj.getDate(FLD_TANGGAL_LAHIR));
            entObj.setAlamat(pstObj.getString(FLD_ALAMAT));
            entObj.setPendidikan(pstObj.getString(FLD_PENDIDIKAN));
            entObj.setEmail(pstObj.getString(FLD_EMAIL));
            entObj.setJabatan(pstObj.getString(FLD_JABATAN));
            return entObj;
        } catch (DBException e) {
            System.out.println(e);
        }
        return entObj;
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Pegawai) ent);
    }

    public static long updateExc(Pegawai entObj) throws DBException {
        if ((entObj != null) && (entObj.getOID() != 0)) {
            try {
                PstPegawai pstObj = new PstPegawai(entObj.getOID());
                pstObj.setString(FLD_NAMA, entObj.getNamaPegawai());
                pstObj.setString(FLD_JENIS_KELAMIN, entObj.getJenisKelamin());
                pstObj.setString(FLD_TEMPAT_LAHIR, entObj.getTempatLahir());
                pstObj.setDate(FLD_TANGGAL_LAHIR, entObj.getTanggalLahir());
                pstObj.setString(FLD_ALAMAT, entObj.getAlamat());
                pstObj.setString(FLD_PENDIDIKAN, entObj.getPendidikan());
                pstObj.setString(FLD_EMAIL, entObj.getEmail());
                pstObj.setString(FLD_JABATAN, entObj.getJabatan());
                pstObj.update();
                return entObj.getOID();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return 0;
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstPegawai pstObj = new PstPegawai(oid);
            pstObj.delete();
            return 1;
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public long insertExc(Entity ent) {
        try {
            return PstPegawai.insertExc((Pegawai) ent);
        } catch (Exception e) {
            System.out.println(" EXC " + e);
            return 0;
        }
    }

    public static long insertExc(Pegawai entObj) throws DBException {
        PstPegawai pstObj = new PstPegawai(0);
        pstObj.setString(FLD_NAMA, entObj.getNamaPegawai());
        pstObj.setString(FLD_JENIS_KELAMIN, entObj.getJenisKelamin());
        pstObj.setString(FLD_TEMPAT_LAHIR, entObj.getTempatLahir());
        pstObj.setDate(FLD_TANGGAL_LAHIR, entObj.getTanggalLahir());
        pstObj.setString(FLD_ALAMAT, entObj.getAlamat());
        pstObj.setString(FLD_PENDIDIKAN, entObj.getPendidikan());
        pstObj.setString(FLD_EMAIL, entObj.getEmail());
        pstObj.setString(FLD_JABATAN, entObj.getJabatan());
        pstObj.insert();
        entObj.setOID(pstObj.getlong(FLD_NIP));
        return entObj.getOID();
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_NAME;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Pegawai ent = new Pegawai();
                resultToObject(rs, ent);
                lists.add(ent);
            }
            rs.close();
            return lists;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    private static void resultToObject(ResultSet rs, Pegawai ent) {
        try {
            ent.setOID(rs.getLong(PstPegawai.fieldNames[PstPegawai.FLD_NIP]));
            ent.setNip(rs.getString(PstPegawai.fieldNames[PstPegawai.FLD_NIP]));
            ent.setNamaPegawai(rs.getString(PstPegawai.fieldNames[PstPegawai.FLD_NAMA]));
            ent.setJenisKelamin(rs.getString(PstPegawai.fieldNames[FLD_JENIS_KELAMIN]));
            ent.setTempatLahir(rs.getString(PstPegawai.fieldNames[FLD_TEMPAT_LAHIR]));
            ent.setTanggalLahir(rs.getDate(PstPegawai.fieldNames[PstPegawai.FLD_TANGGAL_LAHIR]));
            ent.setAlamat(rs.getString(PstPegawai.fieldNames[PstPegawai.FLD_ALAMAT]));
            ent.setPendidikan(rs.getString(PstPegawai.fieldNames[FLD_PENDIDIKAN]));
            ent.setEmail(rs.getString(PstPegawai.fieldNames[FLD_EMAIL]));
            ent.setJabatan(rs.getString(PstPegawai.fieldNames[PstPegawai.FLD_JABATAN]));
        } catch (Exception e) {
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstPegawai.fieldNames[PstPegawai.FLD_NIP] + ") FROM " + TBL_NAME;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    Pegawai ent = (Pegawai) list.get(ls);
                    if (oid == ent.getOID()) {
                        found = true;
                    }
                }
            }
        }
        if ((start >= size) && (size > 0)) {
            start = start - recordToGet;
        }
        return start;
    }

    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + mdl;
        if (start == 0) {
            cmd = Command.FIRST;
        } else {
            if (start == (vectSize - recordToGet)) {
                cmd = Command.LAST;
            } else {
                start = start + recordToGet;
                if (start <= (vectSize - recordToGet)) {
                    cmd = Command.NEXT;
                    System.out.println("next.......................");
                } else {
                    start = start - recordToGet;
                    if (start > 0) {
                        cmd = Command.PREV;
                        System.out.println("prev.......................");
                    }
                }
            }
        }
        return cmd;
    }

    public static boolean checkNIPPegawai(String Kode) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_NAME + " WHERE "
                    + fieldNames[FLD_NIP] + " = '" + Kode + "'";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

}
