package com.fabmo.supervisor.glassedition;


import com.google.android.glass.timeline.LiveCard;

public class DeviceCard {
	public Device mdev;
	public LiveCard  mLiveCard;
    public DeviceDrawer mCallback;
    
    
	public DeviceCard(Device dev, LiveCard mLiveCards,DeviceDrawer mCallback) {
		this.mdev = dev;
		this.mLiveCard = mLiveCards;
		this.mCallback = mCallback;
	}
    
	public boolean is_this_device(Device dev) {
		if (dev.hostname.compareTo(mdev.hostname)==0)
			return true;
		else
			return false;
	}
    
}
