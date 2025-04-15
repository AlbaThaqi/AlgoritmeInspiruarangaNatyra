package src.models;

import java.util.*;

public class CacheServer {
    private final int id;
    private final int capacity;
    private final Set<Integer> videos;

    public CacheServer(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.videos = new HashSet<>();
    }

/*    public boolean canStoreVideo(Video video) {
        return (capacity >= video.getSize());
    }

    public void storeVideo(Video video) {
        if (canStoreVideo(video)) {
            videos.add(video.getId());
            capacity -= video.getSize();
        }
    }*/

    public void addVideo(int videoId) {
        videos.add(videoId);
    }

    public int getId() {
        return id;
    }

    public int getCapacity(){
        return capacity;
    }

    public Set<Integer> getVideos() {
        return videos;
    }

    public String getVideosAsString() {
        return String.join(" ", videos.stream().map(String::valueOf).toArray(String[]::new));
    }
}
