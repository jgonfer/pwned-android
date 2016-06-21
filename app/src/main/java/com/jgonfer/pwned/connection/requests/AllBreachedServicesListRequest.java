package com.jgonfer.pwned.connection.requests;

import com.jgonfer.pwned.activity.BaseActivity;
import com.jgonfer.pwned.connection.PwnedAPIEndpointInterface;
import com.jgonfer.pwned.connection.ServiceGenerator;
import com.jgonfer.pwned.fragment.BaseFragment;
import com.jgonfer.pwned.model.realm.BreachedService;
import com.jgonfer.pwned.utils.RealmHelper;
import com.jgonfer.pwned.utils.UserServiceSingleton;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AllBreachedServicesListRequest extends AbstractRequest {
	private OnLoginResponseListener mBreachedServiceListRequestCallback;

	// Parameters
	private BaseActivity baseActivity;
	private BaseFragment baseFragment;

	// Login Activity must implement this interface
	public interface OnLoginResponseListener {
		void onAllBreachedServicesListResponse();

		void onAllBreachedServicesListErrorResponse(String errorMessage);
	}

	public AllBreachedServicesListRequest(OnLoginResponseListener loginRequestCallback) {
		mBreachedServiceListRequestCallback = loginRequestCallback;
	}

	public OnLoginResponseListener getBreachedServiceListRequestCallback() {
		return mBreachedServiceListRequestCallback;
	}

	public void setBreachedServiceListRequestCallback(OnLoginResponseListener mBreachedServiceListRequestCallback) {
		this.mBreachedServiceListRequestCallback = mBreachedServiceListRequestCallback;
	}

	public void getAllBreachedServices(BaseFragment base) {
		// Set params
		baseFragment = base;
		String url = ServiceGenerator.API_BASE_URL + "breaches";

		// Start request
		PwnedAPIEndpointInterface userService = UserServiceSingleton.getUserService();

		Observable<List<BreachedService>> observable = userService.geAlltBreachedServicesList(url);
		baseFragment.addSubscription(
				observable.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.unsubscribeOn(Schedulers.io())
						.subscribe(new Subscriber<List<BreachedService>>() {
							@Override
							public void onCompleted() {
								mBreachedServiceListRequestCallback.onAllBreachedServicesListResponse();
							}

							@Override
							public void onError(Throwable e)
							{
								mBreachedServiceListRequestCallback.onAllBreachedServicesListErrorResponse(e.getMessage());
							}

							@Override
							public void onNext(List<BreachedService> breachedServices) {
								for (BreachedService breachedService : breachedServices) {
									RealmHelper.createOrUpdateBreachedService(breachedService);
								}
							}
						})
		);
	}

	public void getAllBreachedServices(BaseActivity base) {
		// Set params
		baseActivity = base;
		String url = ServiceGenerator.API_BASE_URL + "breaches";

		// Start request
		PwnedAPIEndpointInterface userService = UserServiceSingleton.getUserService();

		Observable<List<BreachedService>> observable = userService.geAlltBreachedServicesList(url);
		baseActivity.addSubscription(
				observable.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.unsubscribeOn(Schedulers.io())
						.subscribe(new Subscriber<List<BreachedService>>() {
							@Override
							public void onCompleted() {
								mBreachedServiceListRequestCallback.onAllBreachedServicesListResponse();
							}

							@Override
							public void onError(Throwable e) {
								mBreachedServiceListRequestCallback.onAllBreachedServicesListErrorResponse(e.getMessage());
							}

							@Override
							public void onNext(List<BreachedService> breachedServices) {
								for (BreachedService breachedService : breachedServices) {
									RealmHelper.createOrUpdateBreachedService(breachedService);
								}
							}
						})
		);
	}
}
