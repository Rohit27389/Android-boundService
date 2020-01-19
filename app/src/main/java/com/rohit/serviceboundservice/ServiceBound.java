package com.rohit.serviceboundservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by Rohit Kumar on 02-01-2020.
 */

public class ServiceBound extends Service {

    public static final String TAG = "Service Bound";
    public IBinder mIbinder = new MyServiceBinder();
    public MediaPlayer mMediaPlayer;
    public static final String MUSIC_COMPLETION = "Music_Complettion ";


    public class MyServiceBinder extends Binder {
        public ServiceBound getService() {
            return ServiceBound.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind: ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return mIbinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        mMediaPlayer = MediaPlayer.create(this, R.raw.meraaapkikripaseroyaljattcomringto);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent intent = new Intent(MUSIC_COMPLETION);
                intent.putExtra(MainActivity.MESSAE_KEY, "download complete");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                stopSelf();
            }
        });
    }

    public boolean isPlay() {
        return mMediaPlayer.isPlaying();
    }

    public void play() {
        mMediaPlayer.start();
    }

    public void pause() {
        mMediaPlayer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        mMediaPlayer.release();
    }
}
