package com.example.followme.locationutils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.followme.ApplicationParameters;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

public class OLFLocationManager implements ConnectionCallbacks,
		OnConnectionFailedListener,
		com.google.android.gms.location.LocationListener {

	// private static final String DEBUG_TAG = "OLFLocationManager";
	private static final long HIGHFREQ_UPDATE_INTERVAL = 2000;
	private static final long HIGHFREQ_FASTEST_INTERVAL = 1000;
	private static final long LOWFREQ_UPDATE_INTERVAL = 60000;
	private static volatile OLFLocationManager instance = null;
	private static LocationManager mLocationManager;

	// Singelton functions
	public static OLFLocationManager getInstance() {
		return getInstance(null);
	}

	public static OLFLocationManager getInstance(Context ctx) {
		if (instance == null) {
			synchronized (OLFLocationManager.class) {
				if (instance == null) {
					instance = new OLFLocationManager(ctx);
				}
			}
		}

		return instance;
	}

	// Fields
	private Context mContext;
	private LocationClient mLocationClient = null;
	private Location mLastLocation = null;
	private boolean bHighFreqSearch = false;
	private int nConnections = 0;
	private Handler mContinueHandler;
	private String mLocationTimestamp;

	// C'Tor
	private OLFLocationManager(Context ctx) {
		// Application Context
		mContext = ctx;

		// Create local LocationClient and set it to HighAccuracy since it's the
		// first time we are trying to get a location
		mLocationClient = new LocationClient(mContext, this, this);

		// Location Manager - as a fallback
		mLocationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
	}

	// Connect
	public void StartLocationManager() {

		if (!mLocationClient.isConnected()) {
			// Look for my location in high freq
			mLastLocation = null;
			mLocationTimestamp = "";
			mLocationClient.connect();

		}
	}

	// Connect
	public void StopLocationManager() {
		StopListener();
		mLocationClient.disconnect();

	}

	public void StopListener() {
		try {

			mLocationClient.removeLocationUpdates(this);
		} catch (Exception e) {

		}

	}

	// Connection/Failure Callback
	public void LookForMyLocationHighFrequency() {
		bHighFreqSearch = true;
		mLastLocation = null;
		mLocationTimestamp = "";

		if (nConnections > 0)
			StopListener();

		LocationRequest lRequest = new LocationRequest();
		lRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		lRequest.setFastestInterval(HIGHFREQ_FASTEST_INTERVAL);
		try {
			mLocationClient.requestLocationUpdates(lRequest, this);
		} catch (IllegalStateException e) {
			// We are not connected
			StartLocationManager();
		}
	}

	// Connection/Failure Callback
	public void LookForMyLocationLowFrequency() {
		bHighFreqSearch = false;

		StopListener();

		// For now don't use the low freq option

		// LocationRequest lRequest = new LocationRequest();
		// lRequest.setPriority(LocationRequest.PRIORITY_NO_POWER);
		// lRequest.setFastestInterval(LOWFREQ_UPDATE_INTERVAL);
		// lRequest.setInterval(LOWFREQ_UPDATE_INTERVAL);
		// lRequest.setSmallestDisplacement(50);
		//
		// mLocationClient.requestLocationUpdates(lRequest, this);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		LookForMyLocationHighFrequency();
	}

	@Override
	public void onDisconnected() {
	}

	public Location GetLastKnownLocation() {
		return mLastLocation;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		Location mNewLocation = mLocationClient.getLastLocation();

		// Any is better than nothing
		if (mLastLocation == null) {
			mLastLocation = mNewLocation;
			mLocationTimestamp = (new Date()).toString();
		}

		// HIGH FREQ MODE - We are looking for the current location with the
		// best accuracy
		if ((mNewLocation.getAccuracy() < mLastLocation.getAccuracy())
				&& (bHighFreqSearch)) {
			mLastLocation = mNewLocation;
			mLocationTimestamp = (new Date()).toString();
		}

		// LOW FERQ MODE - UNDER 30 METERS IS GOOD FOR US, UPDATE AS WILL..
		if ((!bHighFreqSearch) && (mNewLocation.getAccuracy() < 30)) {
			mLastLocation = mNewLocation;
			mLocationTimestamp = (new Date()).toString();
		}

		// If our total accuracy is below 10, go back to low frequency
		if ((mLastLocation.getAccuracy() < 10) && (bHighFreqSearch)) {
			LookForMyLocationLowFrequency();
		}

	}

	public void StopLocationsManager() {
		// Don't stop it all toghether but change the interval
		try {
			mLocationClient.disconnect();
			StopListener();
		} catch (Exception e) {

		}

	}

	public static Location getLastKnownLocationToPhoneLocationManager() {
		List<String> providers = mLocationManager.getProviders(true);
		Location bestLocation = null;
		for (String provider : providers) {
			Location l = mLocationManager.getLastKnownLocation(provider);

			if (l == null) {
				continue;
			}
			if (l.getTime() >= (System.currentTimeMillis() - ApplicationParameters.LOCATION_TIMEOUT)) {
				if ((bestLocation == null)
						|| (bestLocation.getAccuracy() > l.getAccuracy())) {
					bestLocation = l;
				}
			}
		}

		return bestLocation;
	}

	public static boolean isProviderEnabled(String locProvider) {
		return mLocationManager.isProviderEnabled(locProvider);
	}

	public void SetManualLocation(Location result) {
		StopListener();
		mLastLocation = result;
		mLocationTimestamp = "No Location, Using Last Known";
	}

	public String GetLastLocationTimestamp() {
		return mLocationTimestamp;
	}

	public void deleteLastKnownLocation() {
		mLastLocation = null;
	}
}
