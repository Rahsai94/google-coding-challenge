package com.google;

import java.util.Collections;
import java.util.List;

/** A class used to represent a video. */
class Video implements Comparable<Video>{

  private final String title;
  private final String videoId;
  private final List<String> tags;

  // Class Variable for video playing status
  private boolean isPlaying;
  private boolean isPause;
  private boolean FLAG;
  private String flagReason;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
    this.isPlaying = false;
    this.isPause = false;
    this.FLAG = false;
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  //Getter and Setter Methods for the Class members
  public boolean isPlaying() {
    return this.isPlaying;
  }

  public void setPlaying(boolean playing) {
    isPlaying = playing;
  }

  public boolean isPause() {
    return isPause;
  }

  public void setPause(boolean pause) {
    isPause = pause;
  }

  public boolean isFLAG() {
    return FLAG;
  }

  public void setFLAG(boolean FLAG) {
    this.FLAG = FLAG;
  }

  public String getFlagReason() {
    return this.flagReason;
  }

  public void setFlagReason(String flagReason) {
    this.flagReason = flagReason;
  }

  //Abstract Method of interface Comparable
  @Override
  public int compareTo(Video o) {
    if(o != null){
      return title.compareTo(o.title);
    }else {
      return -1;
    }
  }
}
