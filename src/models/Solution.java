package src.models;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Solution {
    private final Map<Integer, Set<Integer>> cacheToVideos;

    public Solution() {
        this.cacheToVideos = new HashMap<>();
    }

    public Solution(Map<Integer, Set<Integer>> cacheToVideos) {
        this.cacheToVideos = new HashMap<>();
        for (Map.Entry<Integer, Set<Integer>> entry : cacheToVideos.entrySet()) {
            this.cacheToVideos.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
    }

    public void addVideoToCache(int cacheId, int videoId) {
        cacheToVideos.computeIfAbsent(cacheId, k -> new HashSet<>()).add(videoId);
    }

    public Set<Integer> getVideosForCache(int cacheId) {
        return cacheToVideos.getOrDefault(cacheId, Collections.emptySet());
    }

    public Map<Integer, Set<Integer>> getCacheMap() {
        return cacheToVideos;
    }

    public Solution deepCopy() {
        return new Solution(this.cacheToVideos);
    }

    public void writeToFile(String outputPath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));
        long usedCaches = cacheToVideos.values().stream().filter(v -> !v.isEmpty()).count();
        bw.write(String.valueOf(usedCaches));
        bw.newLine();

        for (Map.Entry<Integer, Set<Integer>> entry : cacheToVideos.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                StringBuilder line = new StringBuilder();
                line.append(entry.getKey());
                for (int vid : entry.getValue()) {
                    line.append(" ").append(vid);
                }
                bw.write(line.toString());
                bw.newLine();
            }
        }
        bw.close();
    }
}