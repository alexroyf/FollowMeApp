package com.example.followme.services;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.followme.ApplicationParameters;
import com.example.followme.R;
import com.example.followme.interfaces.ILocationListener;
import com.example.followme.locationutils.LocationListener;
import com.google.android.gms.maps.model.LatLng;

public class LocationService extends Service implements Handler.Callback,
		ILocationListener {

	private static final String DEBUG_TAG = LocationService.class.getName();

	private static final int NOTIFACTION_ID = 895;
	private static final int INACTIVITY_TIMEOUT = 60 * 5; // 5 min in sec

	// Location
	private LocationListener mLocListner;

	private Looper mLooper;
	private Handler mHandler;

	private Notification mNotification;
	private NotificationManager mNotificationManager;

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

		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mHandler.sendMessage(mHandler.obtainMessage(startId, intent));

		return START_STICKY;
	}

	@Override
	public boolean handleMessage(Message msg) {
		Log.i(DEBUG_TAG, "Start Handleing Message");
		
		Intent intent = (Intent) msg.obj;
		if (intent != null) {
			String action = intent.getAction();

			if (action.equals(ApplicationParameters.ACTION_START_MONITORING)) {
				startFourground();
				ArrayList<LatLng> points = intent
						.getParcelableArrayListExtra(ApplicationParameters.POLYLINE_KEY);
				startTracking(points);
			} else if (action
					.equals(ApplicationParameters.ACTION_STOP_MONITORING)) {
				doStopTracking();
				
				stopFourgrond();

				ScheduledExecutorService shutdownService = Executors
						.newSingleThreadScheduledExecutor();
				Runnable shutDownRunnable = new shutDownRunnable(msg.what, this);

				shutdownService.schedule(shutDownRunnable, INACTIVITY_TIMEOUT,
						TimeUnit.SECONDS);
			}
			return true;
		} else
			return false;

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
		mLocListner.setListener(this);
		
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				mLocListner, mLooper);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
				mLocListner, mLooper);
	}

	private void doStopTracking() {
		if (mLocListner != null) {
			LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
			lm.removeUpdates(mLocListner);
		}
	}

	@SuppressWarnings("deprecation")
	private void startFourground() {

		String tickerText = getResources().getString(R.string.start_service);
		String titleText = getResources().getString(R.string.app_name);
		String secodaryText = getResources().getString(R.string.start_service);

		Intent intent = new Intent(ApplicationParameters.ACTION_STOP_MONITORING);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
				0);

		mNotification = new Notification(R.drawable.ic_notifaction, tickerText,
				System.currentTimeMillis());

		mNotification.setLatestEventInfo(this, titleText, secodaryText,
				pendingIntent);
		mNotification.flags = Notification.FLAG_NO_CLEAR;

		startForeground(NOTIFACTION_ID, mNotification);
	}
	
	private void stopFourgrond() {
		stopForeground(true);
	}

	@SuppressWarnings("deprecation")
	private void updateNotifaction(String text) {
		String titleText = getResources().getString(R.string.app_name);

		Intent intent = new Intent(ApplicationParameters.ACTION_STOP_MONITORING);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
				0);

		mNotification.setLatestEventInfo(this, titleText, text, pendingIntent);

		mNotificationManager.notify(NOTIFACTION_ID, mNotification);
	}

	@Override
	public void onOffTrack(boolean onTrack, float distance) {
		if (onTrack) {
			String sOnTrack = getResources().getString(R.string.on_track);
			updateNotifaction(sOnTrack);
		} else {
			String offTrack = getResources().getString(R.string.off_track);
			updateNotifaction(offTrack + "By: " + distance);
		}

	}

	@Override
	public void noGpsSignal() {
		String noGps = getResources().getString(R.string.no_gps_signal);
		updateNotifaction(noGps);
	}

	class shutDownRunnable implements Runnable {
		private int mRequastCode;
		private Service mShutDownService;

		public shutDownRunnable(int requastCode, Service shutDownService) {
			mRequastCode = requastCode;
			mShutDownService = shutDownService;
		}

		@Override
		public void run() {
			if (mShutDownService != null) {
				mShutDownService.stopSelfResult(mRequastCode);
			}
		}

	}
}
