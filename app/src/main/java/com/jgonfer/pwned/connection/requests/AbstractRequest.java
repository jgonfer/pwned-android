package com.jgonfer.pwned.connection.requests;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author pbassiner
 */
public class AbstractRequest {

	protected void logResponse(JSONObject response) {
		/*if (Constants.isDebugLogsEnabled()) {
			try {
				String sResponse = response.toString(4);

				logResponse(sResponse);
			} catch (JSONException e) {
				mLogger.log(Level.SEVERE, getClass(), "logResponse", e.getMessage(), e);
			}
		}*/
	}

	protected void logResponse(JSONArray response) {
		/*if (Constants.isDebugLogsEnabled()) {
			try {
				String sResponse = response.toString(4);

				logResponse(sResponse);
			} catch (JSONException e) {
				mLogger.log(Level.SEVERE, getClass(), "logResponse", e.getMessage(), e);
			}
		}*/
	}

	private void logResponse(String response) {
		/*VolleyLog.v("Response:%n %s", response);
		mLogger.log(Level.FINE, getClass(), "onResponse", response);*/
	}
}
