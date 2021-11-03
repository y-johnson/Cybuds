package edu.coms309.cybuds.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import edu.coms309.cybuds.model.User;

/**
 * HTTPDriver is a wrapper class for HTTP interactions with the CyBuds server. The purpose of this class is to provide ease of development to the
 * activity classes by reducing the amount of repeated code and functionality in them. Methods from this class may require a {@code
 * RequestMethodInterface} implementation that dictates what the caller desires once the server responds.
 */
public class HTTPDriver {
	//final String BASE_URL = "http://10.48.49.41:8080";
	//final String BASE_URL = "http://192.168.1.101:8080";
	final String BASE_URL = "http://coms-309-028.cs.iastate.edu:8080";
	final String USER_MAP = "/users";
	final String LOGIN_MAP = "/login";
	final String REGISTER_MAP = "/register";

	// TODO: These methods would ideally me modified to implement authentication
	public void requestLogin(Context context, User user, RequestMethodInterface onResponse) {
		RequestQueue requestQueue = Volley.newRequestQueue(context);

		JsonObjectRequest jsonObjectRequest;
		try {
			jsonObjectRequest = new JsonObjectRequest(
					Request.Method.POST,
					BASE_URL /*+ USER_MAP*/ + LOGIN_MAP,
					user.toJSONObject(),
					response -> onResponse.executeAction(response, context),
					error -> onResponse.handleError(error, context)
			);
			requestQueue.add(jsonObjectRequest);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void requestSignUp(Context context, User newUser, RequestMethodInterface onResponse) {
		RequestQueue requestQueue = Volley.newRequestQueue(context);

		JsonObjectRequest jsonObjectRequest;
		try {
			jsonObjectRequest = new JsonObjectRequest(
					Request.Method.POST,
					BASE_URL + /*USER_MAP*/REGISTER_MAP,
					newUser.toJSONObject(),
					response -> onResponse.executeAction(response, context),
					error -> onResponse.handleError(error, context)
			);
			requestQueue.add(jsonObjectRequest);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
