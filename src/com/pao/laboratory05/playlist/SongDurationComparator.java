package com.pao.laboratory05.playlist;
import java.util.Comparator;

public class SongDurationComparator implements Comparator<Song> {
    // compare: sortare după durationSeconds crescător
    @Override
    public int compare(Song a, Song b){
      return a.durationSeconds() < b.durationSeconds() ? 1 : -1;
    }

}
