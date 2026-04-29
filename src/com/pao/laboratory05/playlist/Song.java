package com.pao.laboratory05.playlist;

public record Song(String title, String artist, int durationSeconds) implements Comparable<Song> {
    // compareTo: sortare după titlu (alfabetic)
    // Hint: String are deja compareTo — folosește-l
    
    @Override
    public int compareTo(Song o){
      return title.compareTo(o.title);
    }

}
