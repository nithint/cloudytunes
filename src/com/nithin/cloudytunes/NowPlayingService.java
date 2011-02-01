package com.nithin.cloudytunes;

import java.io.IOException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class NowPlayingService extends Service
{
	private MediaPlayer player = new MediaPlayer();
	private NotificationManager nm = null;
	private String currentMediaUri = "";
	
	private static final int NOTIFY_ID = R.layout.view;
	@Override
	public IBinder onBind(Intent arg0)
	{
		return mBinder;
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate()
	{
		super.onCreate();
		nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		player.setOnErrorListener(new MediaPlayerErrorListener());
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if(player != null)
			player.release();
		nm.cancel(NOTIFY_ID);
	}
	
	public String getCurrentMediaUri()
	{
		return currentMediaUri;
	}

	private void showToast(String msg, int duration)
	{
		Toast toast = Toast.makeText(this, msg, duration);
		toast.show();
	}
	
	protected void playSong(String uri)
	{
		// get full url
		String path = String.format(MediaRestAdapter.GET_MEDIA_URL,
				Uri.encode(uri));
		try
		{
			Notification notification = new Notification(
					R.drawable.playbackstart, uri, System.currentTimeMillis());
			nm.notify(NOTIFY_ID, notification);
			
			player.reset();
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.setDataSource(path);
		    player.prepare();
		    player.start();
			
		} catch (IOException e) {
			showToast(getString(R.string.media_play_error), Toast.LENGTH_LONG);
			Log.e(getString(R.string.app_name), e.getMessage());
		}
	}
	
	private class MediaPlayerErrorListener implements MediaPlayer.OnErrorListener
	{
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra)
		{
			showToast(getString(R.string.media_play_error), Toast.LENGTH_LONG);
			return false;
		}
	}
	
	private final NowPlayingServiceInterface.Stub mBinder = new NowPlayingServiceInterface.Stub()
	{

		@Override
		public void playMedia(String uri) throws RemoteException
		{
			playSong(uri);
		}

		@Override
		public void pause() throws RemoteException
		{
			Notification notification = new Notification(
					R.drawable.playbackpause, getCurrentMediaUri(),System.currentTimeMillis());
			nm.notify(NOTIFY_ID, notification);
			player.pause();
		}

		@Override
		public void stop() throws RemoteException
		{
			nm.cancel(NOTIFY_ID);
			player.stop();
		}
		
	};
}
