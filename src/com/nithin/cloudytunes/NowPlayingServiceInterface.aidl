package com.nithin.cloudytunes;

interface NowPlayingServiceInterface
{

	void playMedia(in String uri);
	void pause();
	void stop();
	
}