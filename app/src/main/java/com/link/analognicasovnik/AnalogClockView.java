package com.link.analognicasovnik;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

public class AnalogClockView extends View {

    private Paint paint;
    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;

    private boolean isDragging = false;
    private double dragAngle = 0;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            updateTime();
            invalidate();
            handler.postDelayed(this, 1000);
        }
    };

    public AnalogClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        startUpdatingTime();
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float touchX = event.getX();
                float touchY = event.getY();

                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isDragging = true;
                        dragAngle = Math.atan2(touchY - centerY, touchX - centerX) - Math.toRadians(hours % 12 * 30 - 90);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isDragging) {
                            double newAngle = Math.atan2(touchY - centerY, touchX - centerX);
                            double angleDiff = newAngle - dragAngle;
                            int newHours = (int) ((Math.toDegrees(angleDiff) + 90 + 360) % 360 / 30);
                            setTime(newHours, minutes, seconds);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isDragging = false;
                        break;
                }
                return true;
            }
        });
    }

    private void startUpdatingTime() {
        handler.removeCallbacks(updateRunnable);
        handler.post(updateRunnable);
    }

    private void updateTime() {
        Calendar calendar = Calendar.getInstance();
        hours = calendar.get(Calendar.HOUR);
        minutes = calendar.get(Calendar.MINUTE);
        seconds = calendar.get(Calendar.SECOND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 20;


        paint.setColor(Color.WHITE);
        canvas.drawCircle(centerX, centerY, radius, paint);


        double hourAngle = Math.toRadians((hours % 12) * 30 - 90);
        int hourX = (int) (centerX + radius * 0.5 * Math.cos(hourAngle));
        int hourY = (int) (centerY + radius * 0.5 * Math.sin(hourAngle));
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(8);
        canvas.drawLine(centerX, centerY, hourX, hourY, paint);


        double minuteAngle = Math.toRadians(minutes * 6 - 90);
        int minuteX = (int) (centerX + radius * 0.7 * Math.cos(minuteAngle));
        int minuteY = (int) (centerY + radius * 0.7 * Math.sin(minuteAngle));
        paint.setStrokeWidth(5);
        canvas.drawLine(centerX, centerY, minuteX, minuteY, paint);


        double secondAngle = Math.toRadians(seconds * 6 - 90);
        int secondX = (int) (centerX + radius * 0.8 * Math.cos(secondAngle));
        int secondY = (int) (centerY + radius * 0.8 * Math.sin(secondAngle));
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        canvas.drawLine(centerX, centerY, secondX, secondY, paint);
    }

    public void setTime(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        invalidate();
    }
}


