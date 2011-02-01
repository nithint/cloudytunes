package com.nithin.cloudytunes;

import java.io.InputStream;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

import com.nithin.cloudytunes.datamodel.MediaFile;
import com.nithin.cloudytunes.datamodel.ResourceFolder;

public class SAXParser
{
	public static final String NS = "http://schemas.datacontract.org/2004/07/MediaGallery.Web.DataModel";
	public ResourceFolder parse(InputStream xml)
	{
		final ResourceFolder folder = new ResourceFolder();
		RootElement root = new RootElement(NS,
				"ResourceFolder");
		final ResourceFolder currentSubFolder = new ResourceFolder();
		final MediaFile currentFile = new MediaFile();
		
		Element folders = root.getChild(NS,"Folders");
		Element subfolderNode = folders.getChild(NS,"ResourceFolder");
		
		Element files = root.getChild(NS,"Files");
		Element mediaFileNode = files.getChild(NS,"MediaFile");
		
		root.getChild(NS, MediaRestAdapter.NAME).setEndTextElementListener(new EndTextElementListener()
		{
			
			@Override
			public void end(String body)
			{
				folder.setName(body);
			}
		});
		root.getChild(NS, MediaRestAdapter.V_PATH).setEndTextElementListener(new EndTextElementListener()
		{
			
			@Override
			public void end(String body)
			{
				folder.setVirtualPath(body);
			}
		});
		subfolderNode.setEndElementListener(new EndElementListener()
		{
			
			@Override
			public void end()
			{
				folder.getFolders().add(new ResourceFolder(currentSubFolder));
			}
		});
		subfolderNode.getChild(NS,MediaRestAdapter.NAME).setEndTextElementListener(new EndTextElementListener()
		{
			
			@Override
			public void end(String body)
			{
				currentSubFolder.setName(body);
				
			}
		});
		subfolderNode.getChild(NS,MediaRestAdapter.V_PATH).setEndTextElementListener(new EndTextElementListener()
		{
			
			@Override
			public void end(String body)
			{
				currentSubFolder.setVirtualPath(body);
			}
		});
		
		mediaFileNode.setEndElementListener(new EndElementListener()
		{
			
			@Override
			public void end()
			{
				folder.getFiles().add(new MediaFile(currentFile));
			}
		});
		mediaFileNode.getChild(NS,MediaRestAdapter.NAME).setEndTextElementListener(new EndTextElementListener()
		{
			
			@Override
			public void end(String body)
			{
				currentFile.setName(body);
			}
		});
		mediaFileNode.getChild(NS,MediaRestAdapter.V_PATH).setEndTextElementListener(new EndTextElementListener()
		{
			
			@Override
			public void end(String body)
			{
				currentFile.setVirtualPath(body);
			}
		});
		
		try
		{
			Xml.parse(xml, Xml.Encoding.UTF_8, root.getContentHandler());
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		return folder;
	}
}
