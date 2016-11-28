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

public class PstAppUser extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_APP_USER = "app_user";

    public static final int FLD_USER_ID = 0;
    public static final int FLD_LOGIN_ID = 1;
    public static final int FLD_PASSWORD = 2;
    public static final int FLD_FULL_NAME = 3;
    public static final int FLD_EMAIL = 4;
    public static final int FLD_USER_STATUS = 5;

    public static final String[] fieldNames = {
        "USER_ID",
        "LOGIN_ID",
        "PASSWORD",
        "FULL_NAME",
        "EMAIL",
        "USER_STATUS"
    };

    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT
    };

    public PstAppUser() {
    }

    public PstAppUser(int i) throws DBException {
        super(new PstAppUser());
    }

    public PstAppUser(String sOid) throws DBException {
        super(new PstAppUser(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstAppUser(long lOid) throws DBException {
        super(new PstAppUser(0));
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
        return TBL_APP_USER;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstAppUser().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        AppUser entObj = PstAppUser.fetchExc(ent.getOID());
        ent = (Entity) entObj;
        return entObj.getOID();
    }

    public static AppUser fetchExc(long oid) throws DBException {
        AppUser entObj = new AppUser();
        try {
            PstAppUser pstObj = new PstAppUser(oid);
            entObj.setOID(oid);
            entObj.setLoginId(pstObj.getString(FLD_LOGIN_ID));
            entObj.setPassword(pstObj.getString(FLD_PASSWORD));
            entObj.setFullName(pstObj.getString(FLD_FULL_NAME));
            entObj.setEmail(pstObj.getString(FLD_EMAIL));
            entObj.setUserStatus(pstObj.getInt(FLD_USER_STATUS));
            return entObj;
        } catch (DBException e) {
            System.out.println(e);
        }
        return entObj;
    }

    public long insertExc(Entity ent) {
        try {
            return PstAppUser.insertExc((AppUser) ent);
        } catch (Exception e) {
            System.out.println(" EXC " + e);
            return 0;
        }
    }

    public static long insertExc(AppUser entObj) throws DBException {
        PstAppUser pstObj = new PstAppUser(0);

        pstObj.setString(FLD_LOGIN_ID, entObj.getLoginId());
        pstObj.setString(FLD_PASSWORD, entObj.getPassword());
        pstObj.setString(FLD_FULL_NAME, entObj.getFullName());
        pstObj.setString(FLD_EMAIL, entObj.getEmail());
        pstObj.setInt(FLD_USER_STATUS, entObj.getUserStatus());

        pstObj.insert();
        entObj.setOID(pstObj.getlong(FLD_USER_ID));
        return entObj.getOID();
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((AppUser) ent);
    }

    public static long updateExc(AppUser entObj) throws DBException {
        if ((entObj != null) && (entObj.getOID() != 0)) {
            try {
                PstAppUser pstObj = new PstAppUser(entObj.getOID());

                pstObj.setString(FLD_LOGIN_ID, entObj.getLoginId());
                pstObj.setString(FLD_PASSWORD, entObj.getPassword());
                pstObj.setString(FLD_FULL_NAME, entObj.getFullName());
                pstObj.setString(FLD_EMAIL, entObj.getEmail());
                pstObj.setInt(FLD_USER_STATUS, entObj.getUserStatus());

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

    public static int deleteExc(long oid) throws DBException {
        try {
            PstAppUser pstObj = new PstAppUser(oid);
            pstObj.delete();
            return 999;
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_APP_USER;
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
                AppUser appuser = new AppUser();
                resultToObject(rs, appuser);
                lists.add(appuser);
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

    private static void resultToObject(ResultSet rs, AppUser appuser) {
        try {
            appuser.setOID(rs.getLong(PstAppUser.fieldNames[PstAppUser.FLD_USER_ID]));
            appuser.setLoginId(rs.getString(PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID]));
            appuser.setPassword(rs.getString(PstAppUser.fieldNames[PstAppUser.FLD_PASSWORD]));
            appuser.setFullName(rs.getString(PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME]));
            appuser.setEmail(rs.getString(PstAppUser.fieldNames[PstAppUser.FLD_EMAIL]));
            appuser.setUserStatus(rs.getInt(PstAppUser.fieldNames[PstAppUser.FLD_USER_STATUS]));
        } catch (Exception e) {
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + ") FROM " + TBL_APP_USER;
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
                    AppUser appuser = (AppUser) list.get(ls);
                    if (oid == appuser.getOID()) {
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

    public static Vector listFullObj(int start, int recordToGet, String whereClause, String loginId, String password) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT " + fieldNames[FLD_USER_ID]
                    + " FROM " + TBL_APP_USER;

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            /* if(order != null && order.length() > 0)
             sql = sql + " ORDER BY " + order;
             */
            if ((start == 0) && (recordToGet == 0)) {
                sql = sql + "";  //nothing to do
            } else {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }

            System.out.println(sql);

            conn = DBHandler.getConnection();
            pst = DBHandler.getPreparedStatement(sql, conn);
            pst.setString(1, loginId);
            pst.setString(2, password);

            //dbrs=DBHandler.execQueryResult(sql);
            rs = pst.executeQuery();//dbrs.getResultSet();
            while (rs.next()) {
                AppUser appUser = new AppUser();
                appUser = PstAppUser.fetchExc(rs.getLong(fieldNames[FLD_USER_ID]));
                lists.add(appUser);
            }
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {

            try {
                DBHandler.closeConnection(conn);
                DBHandler.closeStatement(pst);
                rs.close();
            } catch (Exception e) {
            }
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    public static long updateUserStatus(long userOID, int status) {
        if (userOID == 0) {
            return 0;
        }

        DBResultSet dbrs = null;
        try {
            System.out.println("updateUserStatus ----- > lakukan update status --- ");

            if (userOID != 0) {
                AppUser auser = PstAppUser.fetchExc(userOID);
                auser.setUserStatus(status);

                if ((auser.getLoginId() != null && auser.getLoginId().length() > 0 && !(auser.getLoginId().equalsIgnoreCase("NULL")))
                        && (auser.getPassword() != null && auser.getPassword().length() > 0 && !(auser.getPassword().equalsIgnoreCase("NULL")))) {

                    PstAppUser.updateExc(auser);

                }
            }

            return userOID;

        } catch (Exception e) {
            System.out.println("updateUserStatus " + e);
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static AppUser getByLoginIDAndPassword(String loginID, String password) {
        if ((loginID == null) || (loginID.length() < 1) || (password == null) || (password.length() < 1)) {
            return null;
        }

        try {
            String whereClause = " " + fieldNames[FLD_LOGIN_ID] + "= ? AND "
                    + fieldNames[FLD_PASSWORD] + "= ? ";

            System.out.println("whereClause ........ : " + whereClause);

            Vector appUsers = listFullObj(0, 0, whereClause, loginID, password);

            if ((appUsers == null) || (appUsers.size() != 1)) {
                return new AppUser();
            }

            return (AppUser) appUsers.get(0);

        } catch (Exception e) {
            System.out.println("getByLoginIDAndPassword " + e);
            return null;
        }
    }

    public static String getOperatorName(long userOID) {
        try {
            if (userOID != 0) {
                AppUser appUser = PstAppUser.fetchExc(userOID);
                return appUser.getFullName();
            }
            return "";

        } catch (Exception e) {
        }
        return "";

    }

    public static boolean checkLoginID(String LoginID) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_APP_USER + " WHERE "
                    + PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID] + " = '" + LoginID + "'";
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
