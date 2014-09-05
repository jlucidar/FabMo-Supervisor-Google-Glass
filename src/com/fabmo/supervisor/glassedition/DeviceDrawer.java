package com.fabmo.supervisor.glassedition;

import java.util.Timer;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import com.google.android.glass.timeline.DirectRenderingCallback;

public class DeviceDrawer implements DirectRenderingCallback {

    private static final String TAG = DeviceDrawer.class.getSimpleName();	
    private final ToolView mToolView;
	private boolean mRenderingPaused;
	private SurfaceHolder mHolder;
	private Device mDev;
    Timer statusTimer;

    public DeviceDrawer(Context mContext,Device dev) {
    	this(new ToolView(mContext));
    	mDev = dev;
    }


    public DeviceDrawer(ToolView mToolView) {
    	super();
    	this.mToolView = mToolView;
    	mToolView.mChangeListener = mToolViewListener;
    }    
    
    private final ToolView.Listener mToolViewListener = new ToolView.Listener() {

        @Override
        public void onChange() {
            if (mHolder != null) {
                draw(mToolView);
            }
        }
    };
    
@Override
public void surfaceCreated(SurfaceHolder holder) {
    // The creation of a new Surface implicitly resumes the rendering.
    mRenderingPaused = false;
    mHolder = holder;
    updateRenderingState();
	
}

@Override
public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    // Measure and layout the view with the canvas dimensions.
    int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
    int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

    mToolView.measure(measuredWidth, measuredHeight);
    mToolView.layout(
            0, 0, mToolView.getMeasuredWidth(), mToolView.getMeasuredHeight());
	
}

@Override
public void surfaceDestroyed(SurfaceHolder holder) {
    mHolder = null;
    updateRenderingState();
}

@Override
public void renderingPaused(SurfaceHolder holder, boolean paused) {
    mRenderingPaused = paused;
    updateRenderingState();
}


private void updateRenderingState() {
    if (mHolder != null && !mRenderingPaused) {
    	updateStatus();
        mToolView.start();
		statusTimer = new Timer();

    } else {
        mToolView.stop();
        statusTimer.cancel();
    }
}

protected void updateStatus() {
	try {
	mDev.getStatus();
	mToolView.hostname = mDev.hostname;
	//mToolView.ip_address = mDev.network.toString();
	mToolView.state = mDev.status.state;
	mToolView.posx = mDev.status.posx;
	mToolView.posy = mDev.status.posy;
	mToolView.posz = mDev.status.posz;
	mToolView.current_file =mDev.status.current_file;
	mToolView.line = mDev.status.line;
	mToolView.nb_lines = mDev.status.nb_lines;
	}catch(Exception ex) {}
}


private void draw(View view) {
    Canvas canvas;
    try {
    	updateStatus();
        canvas = mHolder.lockCanvas();
    } catch (Exception e) {
        Log.e(TAG, "Unable to lock canvas: " + e);
        return;
    }
    if (canvas != null) {
        view.draw(canvas);
        mHolder.unlockCanvasAndPost(canvas);
    }
}


}
