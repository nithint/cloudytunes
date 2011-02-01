package com.nithin.cloudytunes.datamodel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class ResourceFolder implements Nameable
{

	private String name;
	private String virtualPath;
	private List<ResourceFolder> folders = new ArrayList<ResourceFolder>();
	private List<MediaFile> files = new ArrayList<MediaFile>();

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param virtualPath
	 *            the virtualPath to set
	 */
	public void setVirtualPath(String virtualPath)
	{
		this.virtualPath = virtualPath;
	}

	/**
	 * @return the virtualPath
	 */
	public String getVirtualPath()
	{
		return virtualPath;
	}

	/**
	 * @param folders
	 *            the folders to set
	 */
	public void setFolders(List<ResourceFolder> folders)
	{
		this.folders = folders;
	}

	/**
	 * @return the folders
	 */
	public List<ResourceFolder> getFolders()
	{
		return folders;
	}

	/**
	 * @param files
	 *            the files to set
	 */
	public void setFiles(List<MediaFile> files)
	{
		this.files = files;
	}

	/**
	 * @return the files
	 */
	public List<MediaFile> getFiles()
	{
		return files;
	}

	public String toString()
	{
		return this.getName();
	}

	public boolean isValid()
	{
		return (getName() != null && getName().length() != 0
				&& getVirtualPath() != null && getVirtualPath().length() != 0);
	}

	/**
	 * copy constructor. creates identical resource folder as given
	 * 
	 * @param r
	 *            - the resource folder from which the copy is to be made
	 */
	public ResourceFolder(ResourceFolder r)
	{
		this.setName(r.getName());
		this.setVirtualPath(r.getVirtualPath());
		if (r.getFiles() != null)
			this.getFiles().addAll(r.getFiles());
		if (r.getFolders() != null)
			this.getFolders().addAll(r.getFolders());
	}

	public ResourceFolder()
	{
	}

	/** FACTORY METHODS **/

	public static ResourceFolder createFromXml(InputStream xml)
	{
		if (xml == null)
			return new ResourceFolder();
		ResourceFolder folder = new ResourceFolder();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(xml);

			Element root = dom.getDocumentElement();
			NodeList names = root.getElementsByTagName("Name");
			if (names != null && names.getLength() > 0)
			{
				folder.setName(names.item(0).getFirstChild().getNodeValue());
			}
			NodeList paths = root.getElementsByTagName("VirtualPath");
			if (paths != null && paths.getLength() > 0)
			{
				folder.setVirtualPath(paths.item(0).getFirstChild()
						.getNodeValue());
			}

			NodeList folders = root.getElementsByTagName("Folders");
			if (folders != null && folders.getLength() > 0)
			{
				for (int i = 0; i < folders.getLength(); i++)
				{
					Element subf = (Element) folders.item(i);
					NodeList subfnames = subf.getElementsByTagName("Name");

					ResourceFolder resf = new ResourceFolder();

					if (subfnames != null && subfnames.getLength() > 0)
					{
						resf.setName(subfnames.item(0).getFirstChild()
								.getNodeValue());
					}
					NodeList subfpaths = subf
							.getElementsByTagName("VirtualPath");
					if (subfpaths != null && subfpaths.getLength() > 0)
					{
						resf.setVirtualPath(subfpaths.item(0).getFirstChild()
								.getNodeValue());
					}
					if (resf.isValid())
						folder.getFolders().add(resf);
				}
			}

			NodeList files = root.getElementsByTagName("Files");
			if (files != null && files.getLength() > 0)
			{
				for (int i = 0; i < files.getLength(); i++)
				{
					Element file = (Element) files.item(i);
					MediaFile mediaFile = new MediaFile();
					NodeList filenames = file.getElementsByTagName("Name");
					if (filenames != null && filenames.getLength() > 0)
					{
						mediaFile.setName(filenames.item(0).getNodeValue());
					}
					NodeList filepaths = file
							.getElementsByTagName("VirtualPath");
					if (filepaths != null && filepaths.getLength() > 0)
					{
						mediaFile.setVirtualPath(filepaths.item(0)
								.getNodeValue());
					}
					if (mediaFile.isValid())
						folder.getFiles().add(mediaFile);
				}
			}
		} catch (Exception e)
		{
			int i = 0;
			// log somewhere
		}
		return folder;
	}

}
