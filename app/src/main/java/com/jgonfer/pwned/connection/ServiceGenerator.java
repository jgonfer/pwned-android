package com.jgonfer.pwned.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jgonfer.pwned.adapter.RealmStringListTypeAdapter;
import com.jgonfer.pwned.model.realm.RealmString;

import java.io.IOException;

import io.realm.RealmList;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jgonfer on 12/6/16.
 */

public class ServiceGenerator {
	public static final String API_BASE_URL = "https://haveibeenpwned.com/api/v2/";

	private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
	private static Retrofit retrofit = null;

	private static Retrofit.Builder builder;

	public static <S> S createService(Class<S> serviceClass) {
		return createService(serviceClass, null);
	}

	public static <S> S createService(Class<S> serviceClass, final String authToken) {
		if (authToken != null) {
			httpClient.addInterceptor(new Interceptor() {
				@Override
				public Response intercept(Interceptor.Chain chain) throws IOException {
					Request original = chain.request();

					// Request customization: add request headers
					Request.Builder requestBuilder = original.newBuilder()
							//.header("Authorization", "Bearer " + authToken)
							.method(original.method(), original.body());

					Request request = requestBuilder.build();
					return chain.proceed(request);
				}
			});
		}

		// Add logging
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);

		// Add custom type adapter to Gson for REALM
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(new TypeToken<RealmList<RealmString>>() {
				}.getType(),
				RealmStringListTypeAdapter.INSTANCE);
		Gson gson = gsonBuilder.create();

		builder = new Retrofit.Builder()
				.baseUrl(API_BASE_URL)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create());

		httpClient.addInterceptor(logging);

		OkHttpClient client = httpClient.build();
		retrofit = builder.client(client).build();
		return retrofit.create(serviceClass);
	}

	public static Retrofit getService() {
		return retrofit;
	}
}
