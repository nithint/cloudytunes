package com.nithin.cloudytunes;

import android.net.Uri;

import com.nithin.cloudytunes.SAXParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.nithin.cloudytunes.datamodel.ResourceFolder;

public class MediaRestAdapter
{
	public static final String ROOT_PATH = "/";
	public static final String V_PATH = "VirtualPath";
	public static final String NAME = "Name";
	public static final String MEDIA_FILE = "MediaFile";
	public static final String GET_FOLDER_URL = "http://nithinthomas.com/media/MediaRepositoryService.svc/GetFolder/?filepath=%s";
	public static final String GET_MEDIA_URL = "http://nithinthomas.com/media/StreamingService.svc/GetFileStream/?filepath=%s";
	//public static final String GET_MEDIA_URL = "http://localhost:81/HotNCold.mp3";
	
	public ResourceFolder getFolder(String virtualPath)
	{
		if (virtualPath == null )
			virtualPath = ROOT_PATH;

		virtualPath = Uri.encode(virtualPath);
		// encode the virtual path
		
		HttpGet getRequest = new HttpGet(String.format(GET_FOLDER_URL,
				virtualPath));
		HttpClient client = new DefaultHttpClient();
		
		try
		{
			HttpResponse response = client.execute(getRequest);
			HttpEntity entity = response.getEntity();
			if (entity == null)
				return new ResourceFolder();
			
			
			return new SAXParser().parse(entity.getContent());

		} catch (Exception e) 
		{
			return new ResourceFolder();
		}

	}

	public ResourceFolder getRootFolder()
	{
		return getFolder(ROOT_PATH);
	}
}
