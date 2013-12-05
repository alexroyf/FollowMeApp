package com.example.followme;

public class ApplicationParameters {

	public static final long LOCATION_TIMEOUT = 10 * 60 * 1000; // 10 min
	public static final float OFF_COURSE_DISTANCE = 80; // in meters

	// Actions
	public static final String ACTION_START_MONITORING = "com.example.followme.START_LOCATION_MONITOR";
	public static final String ACTION_STOP_MONITORING = "com.example.followme.STOP_LOCATION_MONITOR";
	
	//Keys
	public static final String POLYLINE_KEY = "polyline_key";
	public static final String LOCATION_LISTENER_KEY = "location_listener";
}
