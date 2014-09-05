/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fabmo.supervisor.glassedition;

import java.util.ArrayList;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Service owning the LiveCard living in the timeline.
 */
public class FabMoLaunchService extends Service {

    private static final String LIVE_CARD_TAG = "FabMo Linker";
	public ArrayList<DeviceCard> devcards = new ArrayList<DeviceCard>();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	
    	try {Device.detection_service.socket.close();}catch(Exception ex) {}
    	ArrayList<Device> dev_list = Device.ScanWifiNetwork(getApplicationContext());
    	
    	for(Device dev : dev_list)
    	{
    		if(dev!=null && !have_a_card(dev))
    		{
    			DeviceCard mDevCard = new DeviceCard(dev, new LiveCard(this, LIVE_CARD_TAG),new DeviceDrawer(this,dev));
    			devcards.add(mDevCard);
    			mDevCard.mLiveCard.setDirectRenderingEnabled(true).getSurfaceHolder().addCallback(mDevCard.mCallback);
	            Intent menuIntent = new Intent(this, MenuActivity.class);
	            menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	            mDevCard.mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));
	            mDevCard.mLiveCard.attach(this);
	            mDevCard.mLiveCard.publish(PublishMode.REVEAL);
	        } else {
	            //Doing nothing
	        }
    	}

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
    	for(DeviceCard mDevCard : devcards) {
	        if (mDevCard.mLiveCard != null && mDevCard.mLiveCard.isPublished()) {
	        	mDevCard.mLiveCard.unpublish();
	        	mDevCard.mLiveCard = null;
	        }
    	}
        super.onDestroy();
    }
    
    public boolean have_a_card(Device dev) {
		for(DeviceCard devcard : devcards)
		{
			if(devcard.is_this_device(dev))
				return true;
		}	
		return false;
    }

}
