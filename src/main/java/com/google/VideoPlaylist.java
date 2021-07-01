package com.google;

import java.util.ArrayList;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist implements Comparable<VideoPlaylist>{

    //Name of the Playlist
    private final String title;
    //Playlist Videos
    private final List<Video> videos = new ArrayList<>();

    //Constructor
    VideoPlaylist(String playlistName){
        this.title = playlistName;
    }

    //Return videos from Playlist
    public List<Video> getVideos() {
        return videos;
    }

    // Return title of playlist
    public String getTitle() {
        return title;
    }

    // Add Video into Playlist
    public void addVideo(Video video){
        videos.add(video);
    }

    //Remove video from Playlist
    public void removeVideo(String video_id){
        for(int index = 0; index < videos.size(); index++)
            if (videos.get(index).getVideoId().equals(video_id)) {
                videos.remove(index);
            }
    }

    // Total Numbers of Videos in playlist
    public int numberofVideos(){
        return videos.size()+1;
    }

    //Remove All videos from Playlist
    public void removeAllVideos(){
        videos.clear();
    }

    //Abstract Method of Comparable
    @Override
    public int compareTo(VideoPlaylist o) {
        return title.compareTo(o.title);
    }
}
