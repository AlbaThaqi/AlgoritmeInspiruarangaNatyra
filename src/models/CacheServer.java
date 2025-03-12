package src.models;

import java.util.*;

public class CacheServer {
    private int id;
    private int capacity;
    private Set<Integer> videos;

    public CacheServer(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.videos = new HashSet<>();
    }

    public boolean canStoreVideo(Video video) {
        return (capacity >= video.getSize());
    }

    public void storeVideo(Video video) {
        if (canStoreVideo(video)) {
            videos.add(video.getId());
            capacity -= video.getSize();
        }
    }

    public int getId() {
        return id;
    }

    public Set<Integer> getVideos() {
        return videos;
    }

    public String getVideosAsString() {
        return String.join(" ", videos.stream().map(String::valueOf).toArray(String[]::new));
    }
}
