package com.jgonfer.pwned.model.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by jgonfer on 13/6/16.
 */
public class History extends RealmObject {
    @PrimaryKey
    @Required
    private String emailServiceId = "";
    private String email = "";
    private String searchDate = "";
    private String titleBreachedService = "";
    private boolean isFixed = false;

    public History () {

    }

    public String getEmailServiceId() {
        return emailServiceId;
    }

    public void setEmailServiceId(String emailServiceId) {
        this.emailServiceId = emailServiceId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }

    public String getTitleBreachedService() {
        return titleBreachedService;
    }

    public void setTitleBreachedService(String titleBreachedService) {
        this.titleBreachedService = titleBreachedService;
    }

    public boolean isFixed() {
        return isFixed;
    }

    public void setFixed(boolean fixed) {
        isFixed = fixed;
    }
}
