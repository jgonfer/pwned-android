package com.jgonfer.pwned.utils;

import android.content.Context;

import com.jgonfer.pwned.model.realm.BreachedService;
import com.jgonfer.pwned.model.realm.History;
import com.jgonfer.pwned.model.realm.RealmString;
import com.jgonfer.pwned.model.realm.UserInfo;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by jgonfer on 6/6/15.
 */
public class RealmHelper {

    //region User Info

    public static UserInfo getUserInfo() {
        Realm realm = Realm.getDefaultInstance();

        UserInfo object = realm.where(UserInfo.class).equalTo("userId", "0").findFirst();
        if (object != null) {
            return object;
        } else {
            setInitialUserInfo(new UserInfo());
            return getUserInfo();
        }
    }

    public static int getInitialSection(Context context) {
        if (context == null) return 2;

        UserInfo userInfo = getUserInfo();
        return userInfo.getInitialSection();
    }

    public static void setInitialUserInfo(UserInfo userInfo) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(userInfo);
        realm.commitTransaction();
    }

    public static double getLastDownloadBikeServices(Context context) {
        if (context == null) return 0.0;

        return getUserInfo().getLastDownloadBikeServices();
    }

    public static void updateLastDownloadBikeServices(Context context) {
        if (context == null) return;

        Realm realm = Realm.getDefaultInstance();
        UserInfo userInfo = getUserInfo();

        realm.beginTransaction();
        userInfo.setLastDownloadBikeServices(System.currentTimeMillis()/1000);
        realm.copyToRealmOrUpdate(userInfo);
        realm.commitTransaction();
    }

    public static void restartLastDownloadBikeServices(Context context) {
        if (context == null) return;

        Realm realm = Realm.getDefaultInstance();
        UserInfo userInfo = getUserInfo();

        realm.beginTransaction();
        userInfo.setLastDownloadBikeServices(0.0);
        realm.copyToRealmOrUpdate(userInfo);
        realm.commitTransaction();
    }

    public static double getLastDownloadBikeStations(Context context) {
        if (context == null) return 0.0;

        return getUserInfo().getLastDownloadBikeStations();
    }

    public static void updateLastDownloadBikeStations(Context context) {
        if (context == null) return;

        Realm realm = Realm.getDefaultInstance();
        UserInfo userInfo = getUserInfo();

        realm.beginTransaction();
        double timestamp = System.currentTimeMillis()/1000;
        userInfo.setLastDownloadBikeStations(timestamp);
        realm.copyToRealmOrUpdate(userInfo);
        realm.commitTransaction();
    }

    public static void restartLastDownloadBikeStations(Context context) {
        if (context == null) return;

        Realm realm = Realm.getDefaultInstance();
        UserInfo userInfo = getUserInfo();

        realm.beginTransaction();
        userInfo.setLastDownloadBikeStations(0.0);
        realm.copyToRealmOrUpdate(userInfo);
        realm.commitTransaction();
    }

    public static void setTutorialCompleted(Context context, boolean isCompleted) {
        if (context == null) return;

        Realm realm = Realm.getDefaultInstance();

        UserInfo userInfo = getUserInfo();
        realm.beginTransaction();
        userInfo.setTutorial_completed(isCompleted);
        realm.copyToRealmOrUpdate(userInfo);
        realm.commitTransaction();
    }

    public static String getLastVersionInstalled(Context context) {
        if (context == null) return "";

        return getUserInfo().getLastVersionInstalled();
    }

    public static void setLastVersionInstalled(Context context, String lastVersion) {
        if (context == null) return;

        Realm realm = Realm.getDefaultInstance();

        UserInfo userInfo = getUserInfo();
        realm.beginTransaction();
        userInfo.setLastVersionInstalled(lastVersion);
        realm.copyToRealmOrUpdate(userInfo);
        realm.commitTransaction();
    }

    public static int getCounterExecutions(Context context) {
        if (context == null) return 0;

        return getUserInfo().getCounterExecutions();
    }

    public static void setCounterExecutions(Context context, int executions) {
        if (context == null) return;

        Realm realm = Realm.getDefaultInstance();

        UserInfo userInfo = getUserInfo();
        realm.beginTransaction();
        userInfo.setCounterExecutions(executions);
        realm.copyToRealmOrUpdate(userInfo);
        realm.commitTransaction();
    }

    public static boolean isOpenedGooglePlay(Context context) {
        if (context == null) return false;

        return getUserInfo().isOpenedGooglePlay();
    }

    public static void setOpenedGooglePlay(Context context, boolean opened) {
        if (context == null) return;

        Realm realm = Realm.getDefaultInstance();

        UserInfo userInfo = getUserInfo();
        realm.beginTransaction();
        userInfo.setOpenedGooglePlay(opened);
        realm.copyToRealmOrUpdate(userInfo);
        realm.commitTransaction();

        setShouldSayThanks(context, true);
    }

    public static boolean shouldSayThanks(Context context) {
        if (context == null) return false;

        return getUserInfo().isShouldSayThanksRate();
    }

    public static void setShouldSayThanks(Context context, boolean sayThanks) {
        if (context == null) return;

        Realm realm = Realm.getDefaultInstance();

        UserInfo userInfo = getUserInfo();
        realm.beginTransaction();
        userInfo.setShouldSayThanksRate(sayThanks);
        realm.copyToRealmOrUpdate(userInfo);
        realm.commitTransaction();
    }

    public static int getDisplayedView() {
        return getUserInfo().getDisplayedView();
    }

    public static void setDisplayedView(int displayedView) {
        Realm realm = Realm.getDefaultInstance();

        UserInfo userInfo = getUserInfo();
        realm.beginTransaction();
        userInfo.setDisplayedView(displayedView);
        realm.copyToRealmOrUpdate(userInfo);
        realm.commitTransaction();
    }

    public static String getLastEmailSearched() {
        return getUserInfo().getLastEmailSearched();
    }

    public static void setLastEmailSearched(String lastEmail) {
        Realm realm = Realm.getDefaultInstance();

        UserInfo userInfo = getUserInfo();
        realm.beginTransaction();
        userInfo.setLastEmailSearched(lastEmail);
        realm.copyToRealmOrUpdate(userInfo);
        realm.commitTransaction();
    }

    public static String getLastEmailWritten() {
        return getUserInfo().getLastEmailWritten();
    }

    public static void setLastEmailWritten(String lastEmail) {
        Realm realm = Realm.getDefaultInstance();

        UserInfo userInfo = getUserInfo();
        realm.beginTransaction();
        userInfo.setLastEmailWritten(lastEmail);
        realm.copyToRealmOrUpdate(userInfo);
        realm.commitTransaction();
    }

    //endregion


    //region BreachedService

    public static void createOrUpdateBreachedService(BreachedService bs) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(bs);
        realm.commitTransaction();
    }

    public static BreachedService getBreachedServiceByTitle(String title) {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(BreachedService.class).equalTo("Title", title).findFirst();
    }

    public static BreachedService[] getBreachedServices() {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<BreachedService> results = realm.where(BreachedService.class).findAll();

        ArrayList<BreachedService> breachedServices = new ArrayList<>();
        for (BreachedService s : results) {
            breachedServices.add(s);
        }

        return breachedServices.toArray(new BreachedService[breachedServices.size()]);
    }

    public static BreachedService[] getBreachedServicesOrdered() {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<BreachedService> results = realm.where(BreachedService.class).findAll().sort("PwnCount", Sort.DESCENDING);

        ArrayList<BreachedService> breachedServices = new ArrayList<>();
        for (BreachedService s : results) {
            breachedServices.add(s);
        }

        return breachedServices.toArray(new BreachedService[breachedServices.size()]);
    }

    public static BreachedService[] getBreachedServicesByEmail(String email) {
        ArrayList<BreachedService> breachedServices = new ArrayList<>();
        List<History> entries = getHistoryEntriesByEmail(email);
        for (History entry : entries) {
            BreachedService breachedService = getBreachedServiceByTitle(entry.getTitleBreachedService());
            breachedServices.add(breachedService);
        }
        return breachedServices.toArray(new BreachedService[breachedServices.size()]);
    }

    public static BreachedService[] getBreachedServicesBySpecialType(boolean isSensitive) {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<BreachedService> results;

        if (isSensitive) {
            results = realm.where(BreachedService.class).equalTo("IsSensitive", true).findAll();
        } else {
            results = realm.where(BreachedService.class).equalTo("IsRetired", true).findAll();
        }

        ArrayList<BreachedService> breachedServices = new ArrayList<>();
        for (BreachedService s : results) {
            breachedServices.add(s);
        }

        return breachedServices.toArray(new BreachedService[breachedServices.size()]);
    }

    public static RealmString createOrUpdateDataClass(String title) {
        Realm realm = Realm.getDefaultInstance();

        RealmString dataClass = getDataClassByTitle(title);
        if (dataClass == null) {
            dataClass = new RealmString();
        }
        realm.beginTransaction();
        dataClass.setValue(title);
        realm.copyToRealmOrUpdate(dataClass);
        realm.commitTransaction();

        return dataClass;
    }

    public static RealmString getDataClassByTitle(String title) {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(RealmString.class).equalTo("value", title).findFirst();
    }

    //endregion


    //region History

    public static History createOrUpdateHistoryEntry(String email, String date, String breachedService) {
        Realm realm = Realm.getDefaultInstance();

        History entry = getHistoryEntryByEmailAndBreachedService(email, breachedService);
        if (entry == null) {
            entry = new History();
            entry.setEmailServiceId(email + "-" + breachedService);
        }
        realm.beginTransaction();
        entry.setEmail(email);
        entry.setSearchDate(date);
        entry.setTitleBreachedService(breachedService);
        entry.setFixed(false);
        realm.copyToRealmOrUpdate(entry);
        realm.commitTransaction();

        return entry;
    }

    public static void setFixedAllHistoryEntryByEmail(String email) {
        Realm realm = Realm.getDefaultInstance();

        List<History> entries = getHistoryEntriesByEmail(email);
        realm.beginTransaction();
        for (History entry : entries) {
            entry.setFixed(true);
            realm.copyToRealmOrUpdate(entry);
        }
        realm.commitTransaction();
    }

    public static History getHistoryEntryByEmailAndBreachedService(String email, String breachedService) {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(History.class).equalTo("email", email).equalTo("titleBreachedService", breachedService).findFirst();
    }

    public static boolean isFixedHistoryEntryByEmailAndBreachedService(String email, String breachedService) {
        History entry = getHistoryEntryByEmailAndBreachedService(email, breachedService);
        return entry != null && entry.isFixed();
    }

    public static List<History> getHistoryEntriesByEmail(String email) {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(History.class).equalTo("email", email).findAll();
    }

    //endregion
}
