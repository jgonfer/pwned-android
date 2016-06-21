package com.jgonfer.pwned.connection.requests;

import com.jgonfer.pwned.connection.PwnedAPIEndpointInterface;
import com.jgonfer.pwned.connection.ServiceGenerator;
import com.jgonfer.pwned.fragment.BaseFragment;
import com.jgonfer.pwned.model.realm.BreachedService;
import com.jgonfer.pwned.utils.UserServiceSingleton;
import com.jgonfer.pwned.utils.RealmHelper;
import com.jgonfer.pwned.utils.Utils;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BreachedServicesListRequest extends com.jgonfer.pwned.connection.requests.AbstractRequest {
	private OnLoginResponseListener mBreachedServiceListRequestCallback;

	// Parameters
	private BaseFragment baseFragment;
	private String mEmail;

	// Login Activity must implement this interface
	public interface OnLoginResponseListener {
		void onBreachedServicesListResponse();

		void onBreachedServicesListErrorResponse(String errorMessage);
	}

	public BreachedServicesListRequest(OnLoginResponseListener loginRequestCallback) {
		mBreachedServiceListRequestCallback = loginRequestCallback;
	}

	public void getBreachedServices(BaseFragment base, final String email) {
		// Set params
		baseFragment = base;
		mEmail = email;
		String url = ServiceGenerator.API_BASE_URL + "breachedaccount/" + email;

		// Start request
		PwnedAPIEndpointInterface userService = UserServiceSingleton.getUserService();

		Observable<List<BreachedService>> observable = userService.getBreachedServicesList(url);
		baseFragment.addSubscription(
				observable.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.unsubscribeOn(Schedulers.io())
						.subscribe(new Subscriber<List<BreachedService>>() {
							@Override
							public void onCompleted() {
								mBreachedServiceListRequestCallback.onBreachedServicesListResponse();
							}

							@Override
							public void onError(Throwable e)
							{
								mBreachedServiceListRequestCallback.onBreachedServicesListErrorResponse(e.getMessage());
							}

							@Override
							public void onNext(List<BreachedService> breachedServices) {
								String nowDateString = Utils.getNowDateString();
								RealmHelper.setFixedAllHistoryEntryByEmail(email);
								for (BreachedService breachedService : breachedServices) {
									RealmHelper.createOrUpdateBreachedService(breachedService);
									RealmHelper.createOrUpdateHistoryEntry(email, nowDateString, breachedService.getName());
								}
							}
						})
		);
	}
}
