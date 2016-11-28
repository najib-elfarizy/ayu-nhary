/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.user.entity;

import java.util.*;
import java.sql.*;

import com.dimata.qdep.entity.*;
import com.dimata.qdep.db.*;
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;

public class PstAppConfig extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_APP_CONFIG = "app_config";

    public static final int FLD_CONFIG_ID = 0;
    public static final int FLD_CONFIG_NAME = 1;
    public static final int FLD_CONFIG_VALUE = 2;

    public static final String[] fieldNames = {
        "CONFIG_ID",
        "CONFIG_NAME",
        "CONFIG_VALUE"
    };

    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING
    };

    public PstAppConfig() {
    }

    public PstAppConfig(int i) throws DBException {
        super(new PstAppConfig());
    }

    public PstAppConfig(String sOid) throws DBException {
        super(new PstAppConfig(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstAppConfig(long lOid) throws DBException {
        super(new PstAppConfig(0));
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
        return TBL_APP_CONFIG;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstAppConfig().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        AppConfig entObj = PstAppConfig.fetchExc(ent.getOID());
        ent = (Entity) entObj;
        return entObj.getOID();
    }

    public static AppConfig fetchExc(long oid) throws DBException {
        AppConfig entObj = new AppConfig();
        try {
            PstAppConfig pstObj = new PstAppConfig(oid);
            entObj.setOID(oid);
            entObj.setConfigName(pstObj.getString(FLD_CONFIG_NAME));
            entObj.setConfigValue(pstObj.getString(FLD_CONFIG_VALUE));
            return entObj;
        } catch (DBException e) {
            System.out.println(e);
        }
        return entObj;
    }

    public long insertExc(Entity ent) {
        try {
            return PstAppConfig.insertExc((AppConfig) ent);
        } catch (Exception e) {
            System.out.println(" EXC " + e);
            return 0;
        }
    }

    public static long insertExc(AppConfig entObj) throws DBException {
        PstAppConfig pstObj = new PstAppConfig(0);

        pstObj.setString(FLD_CONFIG_NAME, entObj.getConfigName());
        pstObj.setString(FLD_CONFIG_VALUE, entObj.getConfigValue());

        pstObj.insert();
        return entObj.getOID();
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((AppConfig) ent);
    }

    public static long updateExc(AppConfig entObj) throws DBException {
        if ((entObj != null) && (entObj.getOID() != 0)) {
            try {
                PstAppConfig pstObj = new PstAppConfig(entObj.getOID());

                pstObj.setString(FLD_CONFIG_NAME, entObj.getConfigName());
                pstObj.setString(FLD_CONFIG_VALUE, entObj.getConfigValue());

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
            PstAppConfig pstObj = new PstAppConfig(oid);
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
            String sql = "SELECT * FROM " + TBL_APP_CONFIG;
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
                AppConfig appconfig = new AppConfig();
                resultToObject(rs, appconfig);
                lists.add(appconfig);
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

    private static void resultToObject(ResultSet rs, AppConfig appconfig) {
        try {
            appconfig.setOID(rs.getLong(PstAppConfig.fieldNames[PstAppConfig.FLD_CONFIG_ID]));
            appconfig.setConfigName(rs.getString(PstAppConfig.fieldNames[PstAppConfig.FLD_CONFIG_NAME]));
            appconfig.setConfigValue(rs.getString(PstAppConfig.fieldNames[PstAppConfig.FLD_CONFIG_VALUE]));
        } catch (Exception e) {
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(*) FROM " + TBL_APP_CONFIG;
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
                    AppGroup appgroup = (AppGroup) list.get(ls);
                    if (oid == appgroup.getOID()) {
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

    public static boolean checkConfigName(String ConfiCode) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_APP_CONFIG + " WHERE "
                    + PstAppConfig.fieldNames[PstAppConfig.FLD_CONFIG_ID] + " = '" + ConfiCode + "'";
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

    public static AppConfig getByName(String configName) {
        DBResultSet dbrs = null;
        AppConfig appconfig = new AppConfig();

        try {
            String sql = "SELECT * FROM " + TBL_APP_CONFIG + " WHERE "
                    + PstAppConfig.fieldNames[PstAppConfig.FLD_CONFIG_NAME] + " = '" + configName + "'";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                resultToObject(rs, appconfig);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return appconfig;
    }
}
