package com.slipkprojects.sockshttp;

import com.magicvpn.net.*;

import android.app.*;
import android.content.*;
import android.support.v7.app.*;
import android.os.*;
import android.media.*;
import android.view.animation.*;
import android.widget.*;

public class SplashActivity extends Activity {

	private ProgressBar mProgress;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Show the splash screen
		setContentView(R.layout.splash);
		mProgress = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);

		// Start lengthy operation in a background thread
		new Thread(new Runnable() {
				public void run() {
					doWork();
					startApp();
					finish();
				}
			}).start();
	}


	@Override
	protected void onStart()
	{

		super.onStart();
	}

	private void doWork() {
		for (int progress=0; progress<1000; progress+=100) {
			try {
				Thread.sleep(10);
				mProgress.setProgress(progress);
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	private void startApp() {
		Intent intent = new Intent(getBaseContext(),com.slipkprojects.sockshttp.SocksHttpMainActivity.class);
		startActivity(intent);
	}
}
