package com.example.followme.interfaces;

public interface ILocationListener {
	
	public void onOffTrack(boolean onTrack, float distance);
	
	public void noGpsSignal();

}
