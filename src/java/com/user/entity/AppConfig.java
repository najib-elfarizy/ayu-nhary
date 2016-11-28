/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.user.entity;

import com.dimata.qdep.entity.Entity;

public class AppConfig extends Entity {

    private String ConfigName = "";
    private String ConfigValue = "";

    public String getConfigName() {
        return ConfigName;
    }

    public void setConfigName(String ConfigName) {
        this.ConfigName = ConfigName;
    }

    public String getConfigValue() {
        return ConfigValue;
    }

    public void setConfigValue(String ConfigValue) {
        this.ConfigValue = ConfigValue;
    }

}
