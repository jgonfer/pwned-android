package com.jgonfer.pwned.model.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jgonfer on 11/6/16.
 */
public class BreachedService extends RealmObject {
    @PrimaryKey
    private String Title = "";
    private String Name = "";
    private String Domain = "";
    private String BreachDate = "";
    private String AddedDate = "";
    private int PwnCount = 0;
    private String Description = "";
    private RealmList<RealmString> DataClasses;
    private boolean IsVerified = false;
    private boolean IsSensitive = false;
    private boolean IsActive = false;
    private boolean IsRetired = false;

    private String LogoType = "";

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDomain() {
        return Domain;
    }

    public void setDomain(String domain) {
        Domain = domain;
    }

    public String getBreachDate() {
        return BreachDate;
    }

    public void setBreachDate(String breachDate) {
        BreachDate = breachDate;
    }

    public String getAddedDate() {
        return AddedDate;
    }

    public void setAddedDate(String addedDate) {
        AddedDate = addedDate;
    }

    public int getPwnCount() {
        return PwnCount;
    }

    public void setPwnCount(int pwnCount) {
        PwnCount = pwnCount;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public RealmList<RealmString> getDataClasses() {
        return DataClasses;
    }

    public void setDataClasses(RealmList<RealmString> dataClasses) {
        DataClasses = dataClasses;
    }

    public boolean getIsVerified() {
        return IsVerified;
    }

    public void setIsVerified(boolean isVerified) {
        IsVerified = isVerified;
    }

    public boolean getIsSensitive() {
        return IsSensitive;
    }

    public void setIsSensitive(boolean isSensitive) {
        IsSensitive = isSensitive;
    }

    public boolean getIsActive() {
        return IsActive;
    }

    public void setIsActive(boolean isActive) {
        IsActive = isActive;
    }

    public boolean getIsRetired() {
        return IsRetired;
    }

    public void setIsRetired(boolean isRetired) {
        IsRetired = isRetired;
    }

    public String getLogoType() {
        return LogoType;
    }

    public void setLogoType(String logoType) {
        LogoType = logoType;
    }
}
