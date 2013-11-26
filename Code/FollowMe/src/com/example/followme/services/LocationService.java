package com.example.followme.services;

import com.example.followme.locationutils.OLFLocationManager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;

public class LocationService extends Service {

	private OLFLocationManager mLocManager;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mLocManager = mLocManager.getInstance(this);
		mLocManager.StartLocationManager();
	}

}
