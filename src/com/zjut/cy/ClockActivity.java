package com.zjut.cy;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;


public class ClockActivity extends Activity {
	public Time time;
	MediaPlayer mediaplayer;				//铃声播放
	TimeThread tt;							//时钟的线程
	ClockView cv;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cv=new ClockView(this);
        setContentView(cv);
        initSound();
        time=new Time();
        tt=new TimeThread(this);
        tt.start();
        time.setToNow();
        /*TextView tv=(TextView)findViewById(R.id.Time);
        tv.setText("time = "+time.hour+"\n"+time.minute);*/
       // mediaplayer.start();
    }
    public Handler myhandler=new Handler(){
    	public void handleMessage(Message msg){
    		switch(msg.what){
    		case 1:
    			mediaplayer.start();
    			break;
    		case 2:
    			//mediaplayer.pause();
    			break;
    		}
    	}
    };
    public int getHour(){
    	return time.hour;
    }
    private void initSound(){
		mediaplayer=MediaPlayer.create(this, R.raw.oldworld);
	}
    
}