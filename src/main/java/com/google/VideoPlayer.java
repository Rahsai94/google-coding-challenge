package com.google;

import java.util.*;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;

  //Create the List of Playlists
  private final List<VideoPlaylist> playlists = new ArrayList<>();

  private int currentVideoIndex = -1;


  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  /* shows how many videos are in the library */
  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  /* list all available videos in the format: “title (video_id) [tags]” */
  public void showAllVideos() {
    String flagged = "";
    List<Video> videos = videoLibrary.getVideos();
    Collections.sort(videos);
    System.out.println("Here's a list of all available videos:");
    //Display All the videos from the videoLibrary
    for (Video video : videos) {
      //Check if Video is Flagged or Not if Video is Flagged then Show stutus and Reason
      if (video.isFLAG()) {
        flagged = "- FLAGGED (reason: " + video.getFlagReason() + ")";
      }
      System.out.printf("%s (%s) %s %s%n", video.getTitle(), video.getVideoId(), video.getTags().toString().replaceAll(",", ""), flagged);
    }
  }

  /* Play the specified video. */
  public void playVideo(String videoId) {
    //Iterate the videos list to check videos
    for(int i = 0; i< videoLibrary.getVideos().size(); i++){
      //Compare the videoId with every videos in list
      if(videoLibrary.getVideos().get(i).getVideoId().equals(videoId)){
        if(videoLibrary.getVideos().get(i).isFLAG()){
          System.out.printf("Cannot play video: Video is currently flagged (reason: %s)%n",videoLibrary.getVideos().get(i).getFlagReason());
          return;
        }
        // Check the Current Video is Playing or Not if Playing then Stop First
        if(currentVideoIndex >= 0){
          if(videoLibrary.getVideos().get(currentVideoIndex).isPause()){
            videoLibrary.getVideos().get(currentVideoIndex).setPause(false);
          }
          // Stop Current Video
          stopVideo();
        }
        currentVideoIndex = i;
        videoLibrary.getVideos().get(i).setPlaying(true);
        System.out.printf("Playing video: %s %n",videoLibrary.getVideos().get(currentVideoIndex).getTitle());
        return;
      }
    }
    System.out.println("Cannot play video: Video does not exist");
  }

  /* Stop the current playing video */
  public void stopVideo() {
    //Check Video is Playing or Not First
    if(currentVideoIndex >= 0){
      //Change the Status of Video
      videoLibrary.getVideos().get(currentVideoIndex).setPause(false);
      videoLibrary.getVideos().get(currentVideoIndex).setPlaying(false);
      System.out.printf("Stopping video: %s %n",videoLibrary.getVideos().get(currentVideoIndex).getTitle());
      currentVideoIndex = -1;
    }else {
      System.out.println("Cannot stop video: No video is currently playing");
    }
  }

  /* Play a random video. */
  public void playRandomVideo() {
    Random ran = new Random();
    //Generate the Random index for selecting random Video
    int randomIndex = ran.nextInt(videoLibrary.getVideos().size()-1);
    if(videoLibrary.getVideos().get(randomIndex).isFLAG()){
      System.out.println("No videos available");
    }else {
      playVideo(videoLibrary.getVideos().get(randomIndex).getVideoId());
    }

  }

  /* Pause the current playing video */
  public void pauseVideo() {
    //Check Video is Playing or Not
    if(currentVideoIndex == -1){
      System.out.println("Cannot pause video: No video is currently playing");
      return;
    }
    //Check if Current Video is already Paused or Not
    if(!videoLibrary.getVideos().get(currentVideoIndex).isPause()){
      videoLibrary.getVideos().get(currentVideoIndex).setPause(true);
      System.out.printf("Pausing video: %s %n",videoLibrary.getVideos().get(currentVideoIndex).getTitle());
    }else{
      System.out.printf("Video already paused: %s %n",videoLibrary.getVideos().get(currentVideoIndex).getTitle());
    }
  }

  /* Continues a currently paused video. */
  public void continueVideo() {
    //Check Video is Currently Playing or Not
    if(currentVideoIndex == -1){
      System.out.println("Cannot continue video: No video is currently playing");
      return;
    }
    //Check Video is Paused or Not
    if(videoLibrary.getVideos().get(currentVideoIndex).isPause()){
      videoLibrary.getVideos().get(currentVideoIndex).setPause(false);
      System.out.printf("Continuing video: %s %n",videoLibrary.getVideos().get(currentVideoIndex).getTitle());
    }else{
      System.out.println("Cannot continue video: Video is not paused");
    }
  }

  /* Displays the title, video_id, video tags and paused status of the video that is currently playing */
  public void showPlaying() {
    String paused = "";
    //Check Video is Playing or Not
    if(currentVideoIndex == -1){
      System.out.println("No video is currently playing");
      return;
    }
    //Check Video is Flagged
    if(videoLibrary.getVideos().get(currentVideoIndex).isFLAG()){
      return;
    }
    //Show the Status of Playing video is Paused or Not
    if(videoLibrary.getVideos().get(currentVideoIndex).isPause()){
      paused = "- PAUSED";
    }

    System.out.printf("Currently playing: %s (%s) %s %s%n",
            videoLibrary.getVideos().get(currentVideoIndex).getTitle(),
            videoLibrary.getVideos().get(currentVideoIndex).getVideoId(),
            videoLibrary.getVideos().get(currentVideoIndex).getTags().toString().replaceAll(",",""),
            paused);
  }

  /* Create a new (empty) playlist with a unique name. */
  public void createPlaylist(String playlistName) {
    if(playlistExist(playlistName) >= 0){
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
    }else{
      //Create New Playlist
      playlists.add(new VideoPlaylist(playlistName));
      System.out.printf("Successfully created new playlist: %s%n",playlistName);
    }
  }

  /* Adds the specified video to a playlist. */
  public void addVideoToPlaylist(String playlistName, String videoId){
    //Check the Playlist Exist or Not by Receiveing Valid index
    int index = playlistExist(playlistName);
    //Check Index is Valid or Not
    if(index >= 0){
      if(videosExist(videoId)){ //Check Video is Exist or Not
        System.out.printf("Cannot add video to %s: Video does not exist%n",playlistName);
      }else if(playlistVideoExist(index, videoId)){ //Check Video Exist in Playlist or Not
        System.out.printf("Cannot add video to %s: Video already added%n",playlistName);
      }else if(videoLibrary.getVideo(videoId).isFLAG()){ //Check Video is Flagged or Not
        System.out.printf("Cannot add video to my_playlist: Video is currently flagged (reason: %s)",videoLibrary.getVideo(videoId).getFlagReason());
      }else { //Add Video to Playlist
        playlists.get(index).addVideo(videoLibrary.getVideo(videoId));
        System.out.printf("Added video to %s: %s%n",playlistName,videoLibrary.getVideo(videoId).getTitle());
      }
    }else{
      System.out.printf("Cannot add video to %s: Playlist does not exist%n",playlistName);
    }
  }

  /* Show all the available playlists (name only). */
  public void showAllPlaylists() {
    Collections.sort(playlists); //Sort the Playlist lexicographical order by title.
    //Check Playlist if there is no playlist then exist with message
    if(playlists.size() == 0){
      System.out.println("No playlists exist yet");
    }else {
      System.out.println("Showing all playlists:");
      playlists.forEach(playlist -> System.out.printf("%s%n", playlist.getTitle()));
    }
  }

  /* Show all the videos in the specified playlist in the following format: “title (video_id) [tags]”. */
  public void showPlaylist(String playlistName) {
    // Check Playlist is Exist or Not first
    if(playlistExist(playlistName) >= 0) for (VideoPlaylist playlist : playlists) {
      if (playlist.getTitle().equalsIgnoreCase(playlistName)) {
        System.out.printf("Showing playlist: %s%n", playlistName);
        showingPlaylistVideos(playlist);
      }
    }
    else{
      System.out.printf("Cannot show playlist %s: Playlist does not exist%n",playlistName);
    }
  }

  /* Remove the specified video from the specified playlist */
  public void removeFromPlaylist(String playlistName, String videoId) {
    //Check Playlist Exist or Not
    int index = playlistExist(playlistName);
    if(index < 0){
      System.out.printf("Cannot remove video from %s: Playlist does not exist%n",playlistName);
    }else if(videosExist(videoId)){ //Check Video Exist or Not
      System.out.printf("Cannot remove video from %s: Video does not exist%n",playlistName);
    }else {
      if(playlistVideoExist(index, videoId)){ //Check Video Exist in Playlist or Not
        playlists.get(index).removeVideo(videoId);
        System.out.printf("Removed video from %s: %s%n",playlistName,videoLibrary.getVideo(videoId).getTitle());
      }else{
        System.out.printf("Cannot remove video from %s: Video is not in playlist%n",playlistName);
      }
    }
  }

  /* Removes all the videos from the playlist, */
  public void clearPlaylist(String playlistName) {
    int index = playlistExist(playlistName);
    //Check Playlist Exist or Not by Comparing Index return by playlistExist(playlistName);
    if(index < 0){
      System.out.printf("Cannot clear playlist %s: Playlist does not exist%n",playlistName);
    }else {
      playlists.get(index).removeAllVideos();
      System.out.printf("Successfully removed all videos from %s%n",playlistName);
    }
  }

  /* Delete the specified playlist */
  public void deletePlaylist(String playlistName) {
    int index = playlistExist(playlistName);
    if(index >= 0){
      playlists.remove(index); // Remove playlist
      System.out.printf("Deleted playlist: %s%n",playlistName);
    }else {
      System.out.printf("Cannot delete playlist %s: Playlist does not exist%n",playlistName);
    }
  }

  /* Search all videos in the library whose titles contain the specified search term.*/
  public void searchVideos(String searchTerm) {
    //fiter serachterm in Video with Title
    filetrVideos(searchTerm, "TITLE");
  }

  /* Show all videos whose list of tags contains the specified hashtag. */
  public void searchVideosWithTag(String videoTag) {
    //Serach Video with TAG
    filetrVideos(videoTag, "TAG");
  }

  /* Mark a video as flagged with a (reason: Not supplied) reason. */
  public void flagVideo(String videoId) {
    List<Video> videos = videoLibrary.getVideos();
    //Check Video Exist or Not
    if(videosExist(videoId)){
      System.out.println("Cannot flag video: Video does not exist");
      return;
    }
    //Fiter the Video from All of Videos
    videos.stream().filter(video -> video.getVideoId().equals(videoId)).forEach(video -> {
      if (video.isFLAG()) { //Check is Already Flagged or Not
        System.out.println("Cannot flag video: Video is already flagged");
      } else {
        video.setFLAG(true); //Set Flagged Status
        video.setFlagReason("Not supplied"); //Set Default Reason of Flag
        System.out.printf("Successfully flagged video: %s (reason: %s)%n", video.getTitle(), videoLibrary.getVideo(videoId).getFlagReason());
      }
    });
  }

  /* Mark a video as flagged with a supplied reason. */
  public void flagVideo(String videoId, String reason) {
    List<Video> videos = videoLibrary.getVideos();
    // Check Video Exist or Not
    if(videosExist(videoId)){
      System.out.println("Cannot flag video: Video does not exist");
      return;
    }
    //Fiter the Videos
    videos.stream().filter(video -> video.getVideoId().equals(videoId)).forEach(video -> {
      //Check Video is Playing or Not if it is then Stop Video
      if (video.isPlaying()) {
        stopVideo();
      }
      if (video.isFLAG()) { // Check Flag is Already set or Not
        System.out.println("Cannot flag video: Video is already flagged");
      } else {
        video.setFLAG(true);
        video.setFlagReason(reason);
        System.out.printf("Successfully flagged video: %s (reason: %s)%n", video.getTitle(), video.getFlagReason());
      }
    });


  }

  /* Attempts to allow (un-flag) a video. */
  public void allowVideo(String videoId) {
    if(videosExist(videoId)){ //Check Video exist or Not
      System.out.println("Cannot remove flag from video: Video does not exist");
    }else if(!videoLibrary.getVideo(videoId).isFLAG()){ //Check is Flag set or Not
      System.out.println("Cannot remove flag from video: Video is not flagged");
    }else {
      videoLibrary.getVideo(videoId).setFLAG(false);
      videoLibrary.getVideo(videoId).setFlagReason("");
      System.out.printf("Successfully removed flag from video: %s%n",videoLibrary.getVideo(videoId).getTitle());
    }
  }

  // Check the Same Playlist Name exist or Not
  private int playlistExist(String playlistName){
    for(int i = 0; i < playlists.size(); i++){
      if(playlists.get(i).getTitle().equalsIgnoreCase(playlistName)){
        return i;
      }
    }
    return -1;
  }

  //Check Video inside the specific Playlist
  private boolean playlistVideoExist(int playlistIndex, String video_id){
    for(int index = 0; index < playlists.get(playlistIndex).getVideos().size(); index++){
          if(playlists.get(playlistIndex).getVideos().get(index).getVideoId().equals(video_id)){
            return true;
          }
    }
    return false;
  }


  //Check the Video is Exist or Not
  private boolean videosExist(String video_id){
    for(int index = 0; index < videoLibrary.getVideos().size(); index++){
      if(videoLibrary.getVideos().get(index).getVideoId().equals(video_id)){
        return false;
      }
    }
    return true;
  }

  //Showing Videos from Specific Playlist
  private void showingPlaylistVideos(VideoPlaylist videoPlaylist){
    if(videoPlaylist.getVideos().size() == 0){
      System.out.println("No videos here yet");
    }else {
      for(int index = 0; index < videoPlaylist.getVideos().size(); index++){
        videoDetails(videoPlaylist.getVideos().get(index));
      }
    }
  }

  //Show Video Details
  private void videoDetails(Video video){
    String flaged = "";
    if(video.isFLAG()){
      flaged = "- FLAGGED (reason: "+video.getFlagReason()+")";
    }
    System.out.printf("%s (%s) %s %s%n",video.getTitle(),video.getVideoId(),video.getTags().toString().replaceAll(",",""),flaged);
  }

  // Fiter Videos using Keyword in Title or Tags
  private void filetrVideos(String seachKey, String TERM){

    List<Video> serachResult = new ArrayList<>(); //Store the Search Result
    Scanner inputObj = new Scanner(System.in);
    int input; //Store the User Choice
    String masterString;
    for (int index = 0; index < videoLibrary.getVideos().size(); index++){
      //Check the Video Filter is Using TAG or TITLE
      if(TERM.equals("TAG")){
        masterString = videoLibrary.getVideos().get(index).getTags().toString().toLowerCase();
      }else {
        masterString = videoLibrary.getVideos().get(index).getTitle().toLowerCase();
      }
      String searchT = seachKey.toLowerCase();
      if(masterString.contains(searchT) && !videoLibrary.getVideos().get(index).isFLAG()){
        serachResult.add(new Video(videoLibrary.getVideos().get(index).getTitle(),videoLibrary.getVideos().get(index).getVideoId(),videoLibrary.getVideos().get(index).getTags()));
      }
    }
    Collections.sort(serachResult); //Sort Search Videos lexicographical order by title.
    //Check is SerchResult has any videos or Not
    if(serachResult.size() != 0){
      System.out.printf("Here are the results for %s:%n",seachKey);
      for(int index = 0; index < serachResult.size(); index++){
        System.out.printf("%d) %s (%s) %s%n",index+1,serachResult.get(index).getTitle(),serachResult.get(index).getVideoId(),serachResult.get(index).getTags().toString().replaceAll(",",""));
      }

      try{
        System.out.println("Would you like to play any of the above? If yes, specify the number of the video.\n" +
                "If your answer is not a valid number, we will assume it's a no.");
        input = inputObj.nextInt();
        if(input > 0 && input <= serachResult.size()){
          playVideo(serachResult.get(input-1).getVideoId());
        }else if(input > serachResult.size() && input <= videoLibrary.getVideos().size()){
          playVideo(videoLibrary.getVideos().get(input).getVideoId());
        }else{
          System.out.println("Nope!");
        }
      }catch (InputMismatchException | IndexOutOfBoundsException ignored){
          //Catch the user Invalid input
          //Catch the array index exception
      }
    }else{
      System.out.printf("No search results for %s%n",seachKey);
    }

  }
}