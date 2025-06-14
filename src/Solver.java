package src;

import src.models.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Solver {
    private final List<Video> videos;
    private final List<CacheServer> caches;
    private final List<Endpoint> endpoints;
    private final List<Request> requests;

    public Solver(List<Video> videos, List<CacheServer> caches, List<Endpoint> endpoints, List<Request> requests) {
        this.videos = videos;
        this.caches = caches;
        this.endpoints = endpoints;
        this.requests = requests;
    }

    public Solution solveGreedy() {
        Map<Integer, Integer> remainingSpace = new HashMap<>();
        for (CacheServer cache : caches) {
            remainingSpace.put(cache.getId(), cache.getCapacity());
        }

        Map<String, Long> scoreMap = new HashMap<>();
        for (Request request : requests) {
            int videoId = request.getVideoId();
            int endpointId = request.getEndpointId();
            int numRequests = request.getNumRequests();
            Endpoint endpoint = endpoints.get(endpointId);
            int dcLatency = endpoint.getDataCenterLatency();

            for (Map.Entry<Integer, Integer> entry : endpoint.getCacheLatencies().entrySet()) {
                int cacheId = entry.getKey();
                int latency = entry.getValue();
                long timeSaved = (long)(dcLatency - latency) * numRequests;
                String key = videoId + ":" + cacheId;
                scoreMap.put(key, scoreMap.getOrDefault(key, 0L) + timeSaved);
            }
        }

        List<Map.Entry<String, Long>> sortedPairs = new ArrayList<>(scoreMap.entrySet());
        sortedPairs.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));

        Solution solution = new Solution();

        for (Map.Entry<String, Long> entry : sortedPairs) {
            String[] parts = entry.getKey().split(":");
            int videoId = Integer.parseInt(parts[0]);
            int cacheId = Integer.parseInt(parts[1]);
            Video video = videos.get(videoId);

            if (!solution.getVideosForCache(cacheId).contains(videoId)) {
                int usedSpace = solution.getVideosForCache(cacheId).stream()
                        .mapToInt(v -> videos.get(v).getSize()).sum();
                int spaceLeft = caches.get(cacheId).getCapacity() - usedSpace;

                if (video.getSize() <= spaceLeft) {
                    solution.addVideoToCache(cacheId, videoId);
                }
            }
        }

        return solution;
    }


    public void writeOutput(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        List<CacheServer> usedCaches = new ArrayList<>();
        for (CacheServer cache : caches) {
            if (!cache.getVideos().isEmpty()) {
                usedCaches.add(cache);
            }
        }

        writer.write(String.valueOf(usedCaches.size()));
        writer.newLine();
        for (CacheServer cache : usedCaches) {
            StringBuilder line = new StringBuilder();
            line.append(cache.getId());
            for (int videoId : cache.getVideos()) {
                line.append(" ").append(videoId);
            }
            writer.write(line.toString());
            writer.newLine();
        }

        writer.close();
    }
}
