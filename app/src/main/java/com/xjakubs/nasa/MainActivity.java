package com.xjakubs.nasa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xjakubs.nasa.meteorite.Meteorite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	private ProgressBar mProgressBar;

	private RequestQueue mRequestQueue;
	private StringRequest mStringRequest;

	private String mHttpResult = "";

	private List<Meteorite> mMeteoriteList = null;
	private String lastDate = "";


	public static final String JSON_DATA = "meteorite.json";
	public static final String PREFERENCES = "preferences";

	public static final String TAG_REQUEST = "nasa";

	public static final int YEAR_FILTER = 2011;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mProgressBar = (ProgressBar) findViewById(R.id.loadingBar);
		mProgressBar.setVisibility(View.GONE);

		mRequestQueue = Volley.newRequestQueue(this);
		String url = getResources().getString(R.string.app_url) + getResources().getString(R.string.app_json) + "?$$app_token=" + getResources().getString(R.string.app_token);

		mProgressBar.setVisibility(View.VISIBLE);


		SharedPreferences sharedPref = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
		lastDate = sharedPref.getString(getString(R.string.last_update), "");

		String currentDate = getCurrentDate();
		if (!currentDate.equals(lastDate)) {
			mStringRequest = new StringRequest(Request.Method.GET, url,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							mHttpResult = response;

							if (response != null && !response.isEmpty()) {
								Gson gson = new Gson();
								Type type = new TypeToken<List<Meteorite>>() {
								}.getType();
								List<Meteorite> meteoriteList = gson.fromJson(response, type);
								if (meteoriteList != null) {
									saveData(response);
									saveLastUpdate();
									showMeteorite(meteoriteList);
								}
								mProgressBar.setVisibility(View.GONE);
							} else {
								List<Meteorite> meteoriteList = readData();
								if (meteoriteList != null) {
									showMeteorite(meteoriteList);
								}
							}

						}
					}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					mProgressBar.setVisibility(View.GONE);
					Toast.makeText(getApplicationContext(), R.string.connection_problem, Toast.LENGTH_SHORT).show();

					List<Meteorite> meteoriteList = readData();
					if (meteoriteList != null) {
						showMeteorite(meteoriteList);
					}
				}
			});
			mRequestQueue.add(mStringRequest);
		} else {
			// The data were downloaded this day! Use them from memory!
			List<Meteorite> meteoriteList = readData();
			if (meteoriteList != null) {
				showMeteorite(meteoriteList);
			}
		}
	}

	private String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}

	private void saveLastUpdate() {
		String currentDate = getCurrentDate();
		if (!currentDate.equals(lastDate)) {
			lastDate = currentDate;
		}

		SharedPreferences sharedPref = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(getString(R.string.last_update), currentDate);
		editor.apply();
	}

	private void saveData(String data) {
		File dataFile = new File(getFilesDir() + "/" + JSON_DATA);

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(dataFile);
			fos.write(data.getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<Meteorite> readData() {
		String jsonData = null;
		File dataFile = new File(getFilesDir() + "/" + JSON_DATA);

		try {
			InputStream is = new FileInputStream(dataFile);
			StringBuilder sb = new StringBuilder();
			if (is != null) {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String receiveText = "";
				while ((receiveText = br.readLine()) != null) {
					sb.append(receiveText);
				}
				is.close();
				jsonData = sb.toString();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Gson gson = new Gson();
		Type type = new TypeToken<List<Meteorite>>() {
		}.getType();
		List<Meteorite> meteoriteList = gson.fromJson(jsonData, type);
		return meteoriteList;
	}

	private List<Meteorite> readMockData() {
		String jsonData = null;
		try {
			jsonData = inputStreamToString(getAssets().open("y77d-th95.json"));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		Gson gson = new Gson();
		Type type = new TypeToken<List<Meteorite>>() {
		}.getType();
		List<Meteorite> meteoriteList = gson.fromJson(jsonData, type);
		return meteoriteList;
	}

	private String inputStreamToString(InputStream inputStream) throws IOException {
		String jsonData = "";
		int size = inputStream.available();
		byte[] buffer = new byte[size];
		inputStream.read(buffer);
		inputStream.close();
		jsonData = new String(buffer);
		return jsonData;
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(TAG_REQUEST);
			mProgressBar.setVisibility(View.GONE);
		}

	}

	private void showMeteorite(List<Meteorite> list) {
		if (!list.isEmpty()) {
			List<Meteorite> filteredList = filterYears(list, 2011);
			Collections.sort(filteredList);

			Intent i = new Intent(this, MateoriteListActivity.class);
			i.putExtra("data", (Serializable) filteredList);
			startActivity(i);
			finish();
		}
	}

	private List<Meteorite> filterYears(List<Meteorite> list, int since) {
		List<Meteorite> resList = new ArrayList<Meteorite>();
		for (Meteorite meteorite : list) {
			if (meteorite.getYear() >= YEAR_FILTER) {
				resList.add(meteorite);
			}
		}
		return resList;
	}


}


