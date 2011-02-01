package com.nithin.cloudytunes.datamodel;

public class MediaFile implements Nameable
{
	
	private String name;
	private String virtualPath;

	public MediaFile(){}
	/**
	 * copy constructor - create identical media file to given argument
	 * @param currentFile
	 */
	public MediaFile(MediaFile f)
	{
		this.setName(f.getName());
		this.setVirtualPath(f.getVirtualPath());
	}

	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @param virtualPath the virtualPath to set
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
	
	public String toString()
	{
		return this.getName();
	}

	public boolean isValid()
	{
		return (getName() != null && getName().length() != 0 && getVirtualPath() != null && getVirtualPath().length() != 0);
	}

}
