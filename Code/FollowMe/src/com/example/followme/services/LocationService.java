package com.example.followme.services;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.followme.ApplicationParameters;
import com.example.followme.locationutils.LocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class LocationService extends Service implements Handler.Callback {

	private static final String DEBUG_TAG = LocationService.class.getName();

	// Location
	private LocationListener mLocListner;

	private Looper mLooper;
	private Handler mHandler;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		HandlerThread thread = new HandlerThread("Location Thread");
		thread.start();
		mLooper = thread.getLooper();

		mHandler = new Handler(mLooper, this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mHandler.sendMessage(mHandler.obtainMessage(0, intent));

		return START_STICKY;
	}

	@Override
	public boolean handleMessage(Message msg) {
		Log.i(DEBUG_TAG, "Start Handleing Message");
		Intent intent = (Intent) msg.obj;

		String action = intent.getAction();

		if (action.equals(ApplicationParameters.ACTION_START_MONITORING)) {
			ArrayList<LatLng> points = intent
					.getParcelableArrayListExtra(ApplicationParameters.POLYLINE_KEY);
			startTracking(points);
		} else if (action.equals(ApplicationParameters.ACTION_STOP_MONITORING)) {
			doStopTracking();
			stopSelf();
		}

		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		doStopTracking();

		if (mLooper != null) {
			mLooper.quit();
			mLooper = null;
		}
	}

	private void startTracking(ArrayList<LatLng> points) {
		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		mLocListner = new LocationListener(points);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				mLocListner, mLooper);
	}

	private void doStopTracking() {
		if (mLocListner != null) {
			LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
			lm.removeUpdates(mLocListner);
		}
	}

}
