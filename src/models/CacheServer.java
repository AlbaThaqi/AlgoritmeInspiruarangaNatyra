package models;

import java.util.HashSet;
import java.util.Set;

public class CacheServer {
    private int id, capacity;
    Set<Integer> storedVideos;

    public CacheServer(int id, int capacity){
        this.id = id;
        this.capacity = capacity;
        this.storedVideos = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public Set<Integer> getStoredVideos() {
        return storedVideos;
    }
}
