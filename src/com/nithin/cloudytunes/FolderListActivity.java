package com.nithin.cloudytunes;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import com.nithin.cloudytunes.datamodel.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nithin.cloudytunes.datamodel.Nameable;
import com.nithin.cloudytunes.datamodel.ResourceFolder;

public class FolderListActivity extends ListActivity
{

	private List<Nameable> listItems = new ArrayList<Nameable>();
	private MediaRestAdapter dataAdapter = new MediaRestAdapter();
	private ResourceFolder currentFolder = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Bundle extras = getIntent().getExtras();
		String virtualPath = MediaRestAdapter.ROOT_PATH;
		if (extras != null)
		{
			virtualPath = extras.getString(MediaRestAdapter.V_PATH);
		}
		new DownloadMediaTask().execute(virtualPath);
	}

	private void showToast(String msg, int duration)
	{
		Toast toast = Toast.makeText(this, msg, duration);
		toast.show();
	}
	
	private void fillData() 
	{
		if (currentFolder == null)
			currentFolder = new ResourceFolder();

		listItems.addAll(currentFolder.getFolders());
		listItems.addAll(currentFolder.getFiles());

		ArrayAdapter<Nameable> adapter = new ArrayAdapter<Nameable>(this,
				R.layout.row, R.id.text1, listItems);
		setListAdapter(adapter);
	}
	
	private class DownloadMediaTask extends AsyncTask<String, String, ResourceFolder>
	{
		@Override
		protected ResourceFolder doInBackground(String... urls)
		{
			publishProgress("Loading media, Please wait...");
			return dataAdapter.getFolder(urls[0]);
		}
		
		@Override
		protected void onProgressUpdate(String... values)
		{
			showToast(values[0], Toast.LENGTH_SHORT);
		}
		
		@Override
		protected void onPostExecute(ResourceFolder result)
		{
			currentFolder = result;
			fillData();
		}
		
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	Nameable n = this.listItems.get(position);
    	if(n instanceof ResourceFolder)
    	{
    		ResourceFolder next = (ResourceFolder)n;
    		Intent i = new Intent(this, FolderListActivity.class);
    		i.putExtra(MediaRestAdapter.V_PATH, next.getVirtualPath());
    		startActivity(i);
    	}
    	else
    	{
    		MediaFile file = (MediaFile)n;
    		Intent i = new Intent(this, MediaPlayerActivity.class);
    		i.putExtra(MediaRestAdapter.V_PATH, file.getVirtualPath());
    		startActivity(i);
    	}
    }
	
}