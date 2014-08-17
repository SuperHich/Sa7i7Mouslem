package com.sa7i7mouslem.mediaplayer;

/**
 * AlMoufasserAlSaghir
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

public interface IMediaPlayerNotifier {

	void onCompletion();
	void onConfigProgress(int totalTime);
	void onProgressPlayer(int progress);
}
