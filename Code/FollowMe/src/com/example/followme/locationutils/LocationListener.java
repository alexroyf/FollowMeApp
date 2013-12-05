package com.example.followme.locationutils;

import java.util.ArrayList;

import com.example.followme.ApplicationParameters;
import com.example.followme.interfaces.ILocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;

public class LocationListener implements android.location.LocationListener {

	private static final int LOCATION_TIMEOUT = 60 * 1000; // 1 min
	private static final int ACCURACY_LIMIT = 50;

	private ArrayList<LatLng> mPoints;
	
	private ILocationListener mListener;

	public LocationListener(ArrayList<LatLng> points) {
		mPoints = points;
	}

	@Override
	public void onLocationChanged(Location location) {
		float nAccuracy = location.getAccuracy();
		if (location.getTime() + LOCATION_TIMEOUT >= System.currentTimeMillis()
				&& nAccuracy < ACCURACY_LIMIT) {
			
			float shortestDistance = -1;
			for (LatLng point : mPoints) {
				float[] result = new float[1];
				Location.distanceBetween(location.getLatitude(),
						location.getLongitude(), point.latitude,
						point.longitude, result);
				
				if (shortestDistance == -1) {
					shortestDistance = result[0];
				} else if (result[0] < shortestDistance) {
					shortestDistance = result[0];
				}
			}
			
			if (shortestDistance > ApplicationParameters.OFF_COURSE_DISTANCE) {
				mListener.onOffTrack(shortestDistance);
			}
			
		} else {
			mListener.noGpsSignal();
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	
	public void setListener(ILocationListener listener) {
		mListener = listener;
	}

}
