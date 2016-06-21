package com.jgonfer.pwned.model.realm;

import com.jgonfer.pwned.R;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by jgonfer on 19/7/15.
 */
public class UserInfo extends RealmObject {
    @PrimaryKey
    @Required
    private String userId = "0";
    private String name = "";
    private int initialSection = 2;
    private double lastDownloadBikeServices = 0.0;
    private double lastDownloadBikeStations = 0.0;
    private boolean tutorial_completed = false;
    private String lastVersionInstalled = "";
    private int counterExecutions = 0;
    private boolean openedGooglePlay = false;
    private boolean shouldSayThanksRate = false;
    private int displayedView = R.id.nav_manual;
    private String lastEmailSearched = "";
    private String lastEmailWritten = "";

    public UserInfo () {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInitialSection() {
        return initialSection;
    }

    public void setInitialSection(int initialSection) {
        this.initialSection = initialSection;
    }

    public double getLastDownloadBikeServices() {
        return lastDownloadBikeServices;
    }

    public void setLastDownloadBikeServices(double lastDownloadBikeServices) {
        this.lastDownloadBikeServices = lastDownloadBikeServices;
    }

    public double getLastDownloadBikeStations() {
        return lastDownloadBikeStations;
    }

    public void setLastDownloadBikeStations(double lastDownloadBikeStations) {
        this.lastDownloadBikeStations = lastDownloadBikeStations;
    }

    public boolean isTutorial_completed() {
        return tutorial_completed;
    }

    public void setTutorial_completed(boolean tutorial_completed) {
        this.tutorial_completed = tutorial_completed;
    }

    public String getLastVersionInstalled() {
        return lastVersionInstalled;
    }

    public void setLastVersionInstalled(String lastVersionInstalled) {
        this.lastVersionInstalled = lastVersionInstalled;
    }

    public int getCounterExecutions() {
        return counterExecutions;
    }

    public void setCounterExecutions(int counterExecutions) {
        this.counterExecutions = counterExecutions;
    }

    public boolean isOpenedGooglePlay() {
        return openedGooglePlay;
    }

    public void setOpenedGooglePlay(boolean openedGooglePlay) {
        this.openedGooglePlay = openedGooglePlay;
    }

    public boolean isShouldSayThanksRate() {
        return shouldSayThanksRate;
    }

    public void setShouldSayThanksRate(boolean shouldSayThanksRate) {
        this.shouldSayThanksRate = shouldSayThanksRate;
    }

    public int getDisplayedView() {
        return displayedView;
    }

    public void setDisplayedView(int displayedView) {
        this.displayedView = displayedView;
    }

    public String getLastEmailSearched() {
        return lastEmailSearched;
    }

    public void setLastEmailSearched(String lastEmailSearched) {
        this.lastEmailSearched = lastEmailSearched;
    }

    public String getLastEmailWritten() {
        return lastEmailWritten;
    }

    public void setLastEmailWritten(String lastEmailWritten) {
        this.lastEmailWritten = lastEmailWritten;
    }
}
