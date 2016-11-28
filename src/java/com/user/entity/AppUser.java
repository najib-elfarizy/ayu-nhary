/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.user.entity;

import com.dimata.qdep.entity.Entity;
import java.util.Vector;

public class AppUser extends Entity {

    public final static int STATUS_NEW = 0;
    public final static int STATUS_LOGOUT = 1;
    public final static int STATUS_LOGIN = 2;

    public final static String[] statusTxt = {"New", "Logged out", "Logged In"};

    private String LoginId = "";
    private String Password = "";
    private String ConfirmPassword = "";
    private String FullName = "";
    private String Email = "";
    private int UserStatus;

    /**
     * @return the LoginId
     */
    public String getLoginId() {
        return LoginId;
    }

    /**
     * @param LoginId the LoginId to set
     */
    public void setLoginId(String LoginId) {
        this.LoginId = LoginId;
    }

    /**
     * @return the Password
     */
    public String getPassword() {
        return Password;
    }

    /**
     * @param Password the Password to set
     */
    public void setPassword(String Password) {
        this.Password = Password;
    }

    /**
     * @return the FullName
     */
    public String getFullName() {
        return FullName;
    }

    /**
     * @param FullName the FullName to set
     */
    public void setFullName(String FullName) {
        this.FullName = FullName;
    }

    /**
     * @return the Email
     */
    public String getEmail() {
        return Email;
    }

    /**
     * @param Email the Email to set
     */
    public void setEmail(String Email) {
        this.Email = Email;
    }

    /**
     * @return the UserStatus
     */
    public int getUserStatus() {
        return UserStatus;
    }

    /**
     * @param UserStatus the UserStatus to set
     */
    public void setUserStatus(int UserStatus) {
        this.UserStatus = UserStatus;
    }

    public static Vector getStatusTxts() {
        Vector vct = new Vector(1, 1);
        for (int i = 0; i < statusTxt.length; i++) {
            vct.add(statusTxt[i]);
        }
        return vct;
    }

    public static String getStatus(int userStatus) {
        return statusTxt[userStatus];
    }

    public static Vector getStatusVals() {
        Vector vct = new Vector(1, 1);
        for (int i = 0; i < statusTxt.length; i++) {
            vct.add(Integer.toString(i));
        }
        return vct;
    }

    /**
     * @return the ConfirmPassword
     */
    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    /**
     * @param ConfirmPassword the ConfirmPassword to set
     */
    public void setConfirmPassword(String ConfirmPassword) {
        this.ConfirmPassword = ConfirmPassword;
    }

}
