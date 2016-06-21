package com.jgonfer.pwned.utils;

import android.app.Application;

import com.jgonfer.pwned.R;
import com.jgonfer.pwned.model.Migration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by jgonzalez on 17/6/16.
 */
public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		initSingletons();

		Realm realm;
		try {
			realm = Realm.getDefaultInstance();
		} catch (Exception ex) {
			RealmConfiguration config = new RealmConfiguration.Builder(this)
					.schemaVersion(1) // Must be bumped when the schema changes
					.migration(new Migration()) // Migration to run instead of throwing an exception
					.build();
			Realm.setDefaultConfiguration(config);
			realm = Realm.getDefaultInstance();
		}
		realm.close();

		RealmHelper.setDisplayedView(R.id.nav_manual);
		RealmHelper.setLastEmailSearched("");
		RealmHelper.setLastEmailWritten("");
	}

	protected void initSingletons() {
		UserServiceSingleton.initInstance();
	}
}