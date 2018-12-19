package com.eukalyptus.eukalyptusplayer.interfaces;

import android.os.RemoteException;

public interface SongListOnClick {
    void playNewSong() throws RemoteException;

    boolean playingCheck();
}
