package com.zjut.cy;
import android.text.format.Time;



public class TimeThread extends Thread {
	Time time=null;
	ClockActivity ca;
	int minute,hour;
	public TimeThread (ClockActivity clockactivity){
		time=new Time();
		this.ca=clockactivity;
	}
	
	public void run(){
		while(true){
		time.setToNow();
		minute=time.minute;
		hour=time.hour;
		if (minute==ca.cv.getminute()&&hour==ca.cv.gethour()&&ca.cv.check){
			ca.myhandler.sendEmptyMessage(1);
		}
		if (!ca.cv.check)
			ca.myhandler.sendEmptyMessage(2);
			
		}
		
		
	}
}
