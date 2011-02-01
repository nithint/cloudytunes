package com.nithin.cloudytunes;

interface NowPlayingServiceInterface
{

	boolean playMedia(in String uri);
	void pause();
	void stop();
	
}