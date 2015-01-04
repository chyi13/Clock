package com.zjut.cy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ClockView extends View {

	public enum needle{
		hour,minute,none;
	}
	
	ClockActivity clock;
	int viewwidth,viewheight;
	Bitmap srcbitmap,resizebitmap;
	Bitmap minutebmp,hourbmp;
	
	int bitmapwidth,bitmapheight;
	
	Time time;
	boolean ampm=false;
	boolean check=false;								//设置确定或取消
	
	int movepointX,movepointY;
	
	int pointcenterX=0,pointcenterY=0;					//时钟中心点位置
	float minuteangle=0.0f,hourangle=0.0f;				//分钟,小时的角度
	Matrix minuterotate,hourrotate;						//时针，分针的矩阵
	needle hand=needle.none;							//标志移动的对象
	
	public ClockView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		clock=(ClockActivity)context;
		time=new Time();
		time.setToNow();
		Log.d("timehour", ""+time.hour);
		Log.d("timeminute", ""+time.minute);
		initBitmap();
		initMinute();
		initVar();
		setFocusable(true);
		
	}
	private void initVar(){
		movepointX=20;
		movepointY=20;
	}
	
	private void initMinute(){	
		pointcenterX=viewwidth/2;
		pointcenterY=viewheight/2;
		
		minuteangle=(float)time.minute*6;
		if (time.hour>12){
			hourangle=(float)(time.hour-12)/12*360;
			ampm=true;
		}
		else{
			hourangle=(float)time.hour/12*360;
			ampm=false;
		}
			
		
		minuterotate=new Matrix();
		minuterotate.setTranslate(pointcenterX,pointcenterY);
		minuterotate.preRotate(minuteangle-180);
		
		hourrotate=new Matrix();
		hourrotate.setTranslate(pointcenterX,pointcenterY);
		hourrotate.preRotate(hourangle-180);
	}
	
	private void initBitmap(){
		srcbitmap=BitmapFactory.decodeResource(getResources(), R.drawable.clock);
		
		minutebmp=BitmapFactory.decodeResource(getResources(), R.drawable.minute);
		hourbmp=BitmapFactory.decodeResource(getResources(), R.drawable.hour);
		
		resizebitmap=srcbitmap;
		
		bitmapwidth=srcbitmap.getWidth();
		bitmapheight=srcbitmap.getHeight();
	}
	
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		p=viewheight/7*5;
		q=viewwidth/2-150;
		pointcenterX=viewwidth/2;
		pointcenterY=viewwidth/2;
		
		
		Paint pClockBack=new Paint();
		pClockBack.setColor(Color.WHITE);
		canvas.drawRect(new Rect(0,0,viewwidth,viewheight), pClockBack);
		
	//	Rect src=new Rect(0,0,viewwidth,viewheight);
		
		Paint minute=new Paint();
		minute.setColor(Color.BLACK);
		minute.setAntiAlias(true);
		
		
		float scalewidth=(float)viewwidth/(float)bitmapwidth;//scaleheight=(float)viewheight/(float)bitmapheight;
		
		Matrix resize=new Matrix();
		
		
		resize.postScale(scalewidth, scalewidth);
		resizebitmap=Bitmap.createBitmap(srcbitmap, 0,0,bitmapwidth, bitmapheight,resize,true);
		
		

		minuterotate.reset();
		minuterotate.setTranslate(pointcenterX,pointcenterY);
		minuterotate.preRotate(minuteangle-180);
		
		hourrotate.reset();
		hourrotate.setTranslate(pointcenterX,pointcenterY);
		hourrotate.preRotate(hourangle-180);
		
		
		canvas.drawBitmap(resizebitmap, 0,0, pClockBack);
		canvas.drawBitmap(minutebmp,minuterotate, minute);
		canvas.drawBitmap(hourbmp, hourrotate, minute);
		
		//canvas.drawLine(viewwidth/2, viewwidth/2,movepointX, movepointY, minute);
		//minuteangle++;
		
		drawbutton(canvas);
		
		invalidate();
	}
	@Override
	protected void onSizeChanged(int w,int h,int oldw,int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
		viewwidth=w;
		viewheight=h;
		
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event){
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			float dx=event.getX();
			float dy=event.getY();
			needle temp=calculatewhich(dx,dy);
			switch(temp){
			case hour:
				hand=needle.hour;
				break;
			case minute:
				hand=needle.minute;
				break;
			case none:
				hand=needle.none;
				break;
			}
			
			if (dx>=q&&dx<=q+150&&dy>=p&&dy<=p+80){
				if (check){
					check=false;
				}
				else{
					check=true;
				}
			}
			if (dx>=q+160&&dx<=q+310&&dy>=p&&dy<=p+80){
				clock.finish();
				clock.mediaplayer.stop();
				clock.tt.stop();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float mx=event.getX();
			float my=event.getY();
			
			double angle;
			switch(hand){
			case hour:
				angle=calculateangle(mx,my);
				hourangle=(float)angle;
				break;
			case minute:
				angle=calculateangle(mx,my);
				minuteangle=(float)angle;
				break;
			case none:
				
				break;
			}
            
			
			invalidate();
			break;
			
		}
		return true;
	}
	int p,q;
	void drawbutton(Canvas canvas){						//绘制按钮
		Paint button=new Paint();
		button.setColor(Color.argb(90, 192, 192, 192));
		
		canvas.drawRect(new Rect(q,p,q+150,p+80), button);
		
		Paint foreground1 = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground1.setColor(Color.argb(120, 163, 148, 128));
		foreground1.setStyle(Style.FILL);
		foreground1.setTextSize(80*0.50f);
		foreground1.setTextScaleX(150/80);
		foreground1.setTextAlign(Paint.Align.CENTER);
		
		if (check){
			canvas.drawText("取 消", q+70, p+60,foreground1);
		}
		else{
			canvas.drawText("确 定", q+70, p+60,foreground1);
		}
		canvas.drawRect(new Rect(q+160,p,q+310,p+80), button);
		canvas.drawText("退 出", q+230, p+60,foreground1 );
	}
	needle calculatewhich(float mx,float my){			//计算与那个指针相交
		float a=mx-pointcenterX;
		float b=my-pointcenterY;
		
		double angle=calculateangle(mx,my);
		
		double distance=Math.sqrt(a*a+b*b);
		
		if (Math.abs(angle-minuteangle)<10||Math.abs((Math.abs(angle-360)-minuteangle))<10){
			Log.d("angle", ""+angle);
			return needle.minute;
		}
		if ((Math.abs(angle-hourangle)<10||Math.abs((Math.abs(angle-360)-hourangle))<10)
				&&distance<50){
			Log.d("minuteangle", ""+angle);
			return needle.hour;
		}
		return needle.none;
	}
	double calculateangle(float mx,float my){			//计算触电与中心点的角度
		float a=mx-pointcenterX;
		float b=my-pointcenterY;
		double angle=Math.atan(-a/b)*180.0f/3.141592653f;
		if (a<0){
			if (b<0){
				angle=angle+360;
			}
			else{
				angle=angle+180;
			}
		}
		else{
			if (b>0){
				angle=angle+180;
			}
		}
		return angle;
	}
	
	int gethour(){										//获取小时数
		int hour=(int)(hourangle/360*12);
		if (ampm){
			Log.d("gethour", ""+(hour+12));
			return hour+12;
		}
		else
			return hour;
	}
	int getminute(){									//获取分钟数
		return (int)(minuteangle/360*60);
	}
	
	
	
	
}
