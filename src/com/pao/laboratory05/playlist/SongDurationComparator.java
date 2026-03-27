package com.pao.laboratory05.playlist;

import java.util.Comparator;

public class SongDurationComparator implements Comparator<Song> {
    @Override
    public int compare(Song first, Song second) {
        return Integer.compare(first.durationSeconds(), second.durationSeconds());
    }
}
