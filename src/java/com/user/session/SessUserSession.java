/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.user.session;

import com.dimata.qdep.db.DBException;
import java.util.*;
import com.user.entity.*;

public class SessUserSession {

    public final static String HTTP_SESSION_NAME = "USER_SESSION";
    public final static int DO_LOGIN_OK = 0;
    public final static int DO_LOGIN_ALREADY_LOGIN = 1;
    public final static int DO_LOGIN_NOT_VALID = 2;
    public final static int DO_LOGIN_SYSTEM_FAIL = 3;
    public final static int DO_LOGIN_GET_PRIV_ERROR = 4;
    public final static int DO_LOGIN_NO_PRIV_ASIGNED = 5;

    public final static String[] soLoginTxt = {
        "Login berhasil",
        "User saat ini sedang online",
        "Username atau Password tidak benar, silahkan diulang kembali!",
        "Username atau Password tidak boleh kosong!",
        "Privilege tidak ditemukan",
        "Tidak bisa mengakses sistem, silahkan menghubungi administrator"
    };

    private Vector privObj = new Vector();
    private AppUser appUser = new AppUser();

    public SessUserSession() {

        appUser.setUserStatus(AppUser.STATUS_LOGOUT);

    }

    public SessUserSession(String hostIP) {

        appUser.setUserStatus(AppUser.STATUS_LOGOUT);

    }

    public boolean isUserLoggedIn() {

        if ((this.appUser.getUserStatus() == AppUser.STATUS_LOGIN)) {
            return true;
        } else {
            return false;
        }

    }

    public int doLogin(String loginID, String password) throws DBException {

        AppUser user = PstAppUser.getByLoginIDAndPassword(loginID, password);

        if (user == null) {
            return DO_LOGIN_SYSTEM_FAIL;
        }

        if (user.getOID() == 0) {
            return DO_LOGIN_NOT_VALID;
        }

//        if((user.getUserStatus()==AppUser.STATUS_LOGIN))
//
//            return DO_LOGIN_ALREADY_LOGIN;
        user.setUserStatus(AppUser.STATUS_LOGIN);

        if (PstAppUser.updateExc(user) == 0) {
            return DO_LOGIN_SYSTEM_FAIL;
        }

        this.appUser = user;

        if (privObj == null) {

            privObj = new Vector(1, 1);

            return DO_LOGIN_GET_PRIV_ERROR;

        }

        System.out.println(" User login OK status ->" + appUser.getUserStatus());

        return DO_LOGIN_OK;

    }

    public void doLogout() {

        PstAppUser.updateUserStatus(appUser.getOID(), AppUser.STATUS_LOGOUT);

    }

    public void printAppUser() {

        System.out.println(" ==== AppUser ====");
        System.out.println(" User ID = " + this.appUser.getOID());
        System.out.println(" Login ID = " + this.appUser.getLoginId());
        System.out.println(" Status = " + this.appUser.getUserStatus());

    }

    public AppUser getAppUser() {

        return this.appUser;

    }

}
