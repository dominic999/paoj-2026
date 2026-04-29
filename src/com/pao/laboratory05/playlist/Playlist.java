package com.pao.laboratory05.playlist;
import java.util.Arrays;

import com.pao.laboratory05.playlist.Song;

public class Playlist{
  private String name;
  private Song songs[];

  public Playlist(String name){
    this.name = name;
    this.songs = new Song[0];
  }

  public void addSong(Song a){
    Song[] newSongs = new Song[songs.length + 1];
    System.arraycopy(songs, 0, newSongs, 0, songs.length);
    newSongs[songs.length] = a;
    songs = newSongs;
  }

  public void printSortedByTitle(){
    Song[] sorted = new Song[songs.length];
    System.arraycopy(songs, 0, sorted, 0, songs.length);
    Arrays.sort(sorted);
    System.out.println(sorted);
  }

  public int getTotalDuration(){

  }

}
