package com.fabmo.supervisor.glassedition;


import java.text.DecimalFormat;
import java.util.Timer;


import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ToolView extends FrameLayout {
	
    public interface Listener {
        /** Notified of a change in the view. */
        public void onChange();
    }



	final long DELAY_MILLIS = 41;//for the FPS
	
	Context mContext;
	TextView hostname_text_view;
	TextView network_text_view ;
	TextView state_text_view ;
	TextView posx_text_view ;
	TextView posy_text_view ;
	TextView posz_text_view ;
	
	public String state = "disconnected" ;
	public double posx = 0 ;
	public double posy = 0 ;
	public double posz = 0 ;
	/* not implemented yet
	public double posa = (Float) null ;
	public double posb = (Float) null ;
	public double posc = (Float) null ;
	*/
    public String current_file = null ;
    public int nb_lines = 0;
    public int line = 0;
    //public String ip_address="0.0.0.0";
    public String hostname;
    
	static Timer statusTimer;
	static Thread update_status_thread;

	Listener mChangeListener;
    private boolean mRunning;
    private final Handler mHandler = new Handler();
    private final Runnable mUpdateTextRunnable = new Runnable() {

        @Override
        public void run() {
            if (mRunning) {
                //tool_to_display.getStatus();
            	updateUI();
                mHandler.postDelayed(mUpdateTextRunnable, DELAY_MILLIS);
            }
        }
    };

	private TextView current_file_text_view;

	private ProgressBar file_progress_bar_view;

	private LinearLayout file_display_view;



	
	public ToolView(Context mContext) {
		super(mContext);
    	//android.os.Debug.waitForDebugger();
		this.mContext=mContext;
		LayoutInflater.from(mContext).inflate(R.layout.tool_view, this);
		hostname_text_view = (TextView) this.findViewById(R.id.device_hostname);
		//network_text_view = (TextView) this.findViewById(R.id.network);
		
		state_text_view = (TextView) this.findViewById(R.id.state);
		posx_text_view = (TextView) this.findViewById(R.id.posx);
		posy_text_view = (TextView) this.findViewById(R.id.posy);
		posz_text_view = (TextView) this.findViewById(R.id.posz);
		
		file_display_view = (LinearLayout) this.findViewById(R.id.file_display);
		current_file_text_view = (TextView) this.findViewById(R.id.current_file);
		file_progress_bar_view = (ProgressBar) this.findViewById(R.id.file_progressBar);

	}
	
	protected void updateUI() {
		hostname_text_view.setText(hostname);
		//network_text_view.setText(ip_address);
				
		
		state_text_view.setText(state);
		posx_text_view.setText(new DecimalFormat("##0.000;-##0.000").format(posx));
		posy_text_view.setText(new DecimalFormat("##0.000;-##0.000").format(posy));
		posz_text_view.setText(new DecimalFormat("##0.000;-##0.000").format(posz));
		
		if(current_file == null) {
			file_display_view.layout(0, 0, 0, 0);

			
		}
		else {
			file_display_view.layout(0, ((LinearLayout)file_display_view.getParent()).getBottom()-100,((LinearLayout)file_display_view.getParent()).getWidth() ,((LinearLayout)file_display_view.getParent()).getBottom());
			current_file_text_view.setText(current_file);
			file_progress_bar_view.setMax(nb_lines);
			file_progress_bar_view.setProgress(line);
		}
		/*
		if(current_file != null)
		{
			// display the file status view
			View fileView = LayoutInflater.from(mContext).inflate(R.layout.file_report_view,this);
			TextView current_file_text_view = (TextView) fileView.findViewById(R.id.current_file);
			ProgressBar file_progress_bar_view = (ProgressBar) fileView.findViewById(R.id.file_progressBar);
			// fill in any details dynamically here
			current_file_text_view.setText(current_file);
			file_progress_bar_view.setMax(nb_lines);
			file_progress_bar_view.setProgress(line);

			// insert into main view
			((ViewGroup) this).addView(fileView, 2, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		}*/
			
		
        if (mChangeListener != null) {
            mChangeListener.onChange();
        }
	}

	public void start() {
        if (!mRunning) {
            mHandler.postDelayed(mUpdateTextRunnable, DELAY_MILLIS);
        }
        mRunning = true;
	}

	public void stop() {
        if (mRunning) {
        	mHandler.removeCallbacks(mUpdateTextRunnable);
        }
        mRunning = false;
	}
	
	
}
