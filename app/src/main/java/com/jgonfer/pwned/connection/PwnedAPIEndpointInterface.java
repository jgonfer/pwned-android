package com.jgonfer.pwned.connection;

import com.jgonfer.pwned.model.realm.BreachedService;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by jgonfer on 20/3/16.
 */
public interface PwnedAPIEndpointInterface {
	@GET
	Observable<List<BreachedService>> getBreachedServicesList(@Url String url);

	@GET
	Observable<List<BreachedService>> geAlltBreachedServicesList(@Url String url);
}
