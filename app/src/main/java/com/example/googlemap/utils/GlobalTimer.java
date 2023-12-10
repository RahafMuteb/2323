package com.example.googlemap.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.TimeUnit;

public class GlobalTimer {

    private static final int TIMER_INTERVAL = 1000; // 1 second interval
    private static Handler timerHandler = new Handler(Looper.getMainLooper());
    private static Runnable timerRunnable;

    // Callback interface for timer events
    public interface TimerCallback {
        void onTimerTick(long remainingMinutes, long remainingSeconds);
    }

    public static TimerCallback callback;
    // Start the global timer
    public static void startTimer(long startTimestamp) {
        long durationMillis = TimeUnit.MINUTES.toMillis(30); // 30 minutes duration
        final long totalDurationMillis = startTimestamp + durationMillis;

        timerRunnable = new Runnable() {
            private long startTime = System.currentTimeMillis();

            @Override
            public void run() {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long remainingTimeMillis = Math.max(0, totalDurationMillis - elapsedTime);

                long remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(remainingTimeMillis);
                long remainingSeconds = TimeUnit.MILLISECONDS.toSeconds(remainingTimeMillis) -
                        TimeUnit.MINUTES.toSeconds(remainingMinutes);

                // Notify the callback with the remaining time
                if (callback != null) {
                    callback.onTimerTick(remainingMinutes, remainingSeconds);
                }

                // Schedule the next iteration if time is remaining
                if (remainingTimeMillis > 0) {
                    timerHandler.postDelayed(this, TIMER_INTERVAL);
                }
            }
        };

        // Start the initial iteration
        timerHandler.post(timerRunnable);
    }

    // Stop the global timer
    public static void stopTimer() {
        if (timerHandler != null && timerRunnable != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }
    }
}
