package com.rohit.serviceboundservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    public static final String MESSAE_KEY = "SentToService";
    private TextView mText;
    private Button mButton1, mButton2, mButton3;
    private ServiceBound mServiceBound;
    public boolean mBound = false;

    public static final String TAG = "Log Message";


    public ServiceConnection mServiceCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder Ibinder) {
            ServiceBound.MyServiceBinder myServiceBinder = (ServiceBound.MyServiceBinder) Ibinder;
            mServiceBound = myServiceBinder.getService();
            mBound = true;
            Log.d(TAG, "onServiceConnected: Service Connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String value=intent.getStringExtra(MESSAE_KEY);
            if(value=="download complete"){
                mButton3.setText("play");
            }

            Log.d(TAG, "onReceive: " + Thread.currentThread().getName());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText = findViewById(R.id.Ready);
        mButton1 = findViewById(R.id.btnRun);
        mButton2 = findViewById(R.id.btnClear);
        mButton3 = findViewById(R.id.btnPlay);
        player();
        clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(MainActivity.this, ServiceBound.class);
        bindService(intent, mServiceCon, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,new IntentFilter(ServiceBound.MUSIC_COMPLETION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mServiceCon);
            mBound = false;

        }
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    public void player() {
        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) {
                    if (mServiceBound.isPlay()) {
                        mServiceBound.pause();
                        mButton3.setText("Play");
                    } else {

                        Intent intent=new Intent(MainActivity.this,ServiceBound.class);
                        startService(intent);

                        mServiceBound.play();
                        mButton3.setText("Pause");
                    }
                }
            }
        });
    }

    public void clear() {
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void log(String message) {
        Log.i(TAG, message);
        mText.append(message + "\n");

    }

}
