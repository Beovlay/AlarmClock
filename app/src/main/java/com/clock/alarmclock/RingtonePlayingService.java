package com.clock.alarmclock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class RingtonePlayingService extends Service {
    MediaPlayer media_song;
    int startId;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public  int onStartCommand(Intent intent, int flags, int startId){

        Log.i("LocalService", "Received start id " + startId + ":" + intent);

        String state = intent.getExtras().getString("extra");

        Log.e("Ringtone extra is", state);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.alarm_clock)
                        .setContentTitle("Будильник")
                        .setContentText("Вставай");

        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, mBuilder.build());

        assert state != null;
        switch (state){
            case "on":
                startId = 1;
                break;
            case "off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        if(!this.isRunning && startId == 1){

            Log.e("music", "end");

            media_song = MediaPlayer.create(this, R.raw.budilnik);
            media_song.start();

            this.isRunning = true;
            this.startId = 0;

        }

        else if(this.isRunning && startId == 0){
            Log.e("no music", "end");

            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0;
        }

        else  if(this.isRunning && startId == 0){
            Log.e("no music", "end");

            this.isRunning = false;
            this.startId = 0;
        }

        else if(this.isRunning && startId == 1){
            Log.e("music", "end");

            this.isRunning = true;
            this.startId = 1;
        }

        else {
            Log.e("else", "reached this");
        }

       return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){

        super.onDestroy();
        this.isRunning = false;
    }


}
