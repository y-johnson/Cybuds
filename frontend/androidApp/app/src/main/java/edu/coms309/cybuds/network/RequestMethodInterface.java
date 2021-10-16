package edu.coms309.cybuds.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Locale;

/**
 * {@code RequestMethodInterface} is a simple lambda helper interface for {@code HTTPDriver}. It has one method that must be overridden, that being
 * {@code executeAction()}. It provides a default implementation of error handling ({@code handleError()}) that prints the stack trace and displays a
 * {@code Toast} with the error response code.
 */
public interface RequestMethodInterface {
	void executeAction(JSONObject response, Context context);

	default void handleError(VolleyError error, Context context) {
		Toast.makeText(
				context,
				String.format(Locale.getDefault(), "%s: %3d", "Error", error.networkResponse.statusCode),
				Toast.LENGTH_LONG
		).show();
		error.printStackTrace();
	}
}
