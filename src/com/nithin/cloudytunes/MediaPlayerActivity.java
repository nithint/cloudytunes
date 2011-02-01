package com.nithin.cloudytunes;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

public class MediaPlayerActivity extends Activity
{
	private String vpath = null;
	private NowPlayingServiceInterface npsInterface = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view);

		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			vpath = extras.getString(MediaRestAdapter.V_PATH);
			
			this.bindService(new Intent(this,NowPlayingService.class), serviceConn, Context.BIND_AUTO_CREATE);
			
		}
		else
		{
			// nothing to play
			// show a toast first

		   	Toast toast = Toast.makeText(this, getString(R.string.no_file_selected), Toast.LENGTH_LONG);
		   	toast.show();
		   	
			this.finish();
		}
	}
	
	private void playMedia()
	{
		if(vpath != null && vpath.length() > 0)
		{
			try
			{
				npsInterface.playMedia(vpath);
			} catch (RemoteException e)
			{
				Toast toast = Toast.makeText(this, "Cannot connect to now playing service", Toast.LENGTH_LONG);
				toast.show();
				this.finish();
			}
		}
	}
	
	private ServiceConnection serviceConn = new ServiceConnection()
	{

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder service)
		{
			npsInterface = NowPlayingServiceInterface.Stub.asInterface(service);
			playMedia();
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0)
		{
			npsInterface = null;
		}
		
	};
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}
