package src;

import src.models.*;

import java.io.*;
import java.util.*;

public class Validator {

    public static boolean validate(String inputFilePath, String outputFilePath) {
        try {
            Problem problem = new Problem(inputFilePath);
            Map<Integer, Set<Integer>> cacheToVideos = new HashMap<>();

            if (!readAndValidateOutput(outputFilePath, problem, cacheToVideos)) {
                System.err.println("❌ Output format or constraints are invalid.");
                return false;
            }

            if (!validateCacheSizes(problem.getVideos(), problem.getCacheCapacity(), cacheToVideos)) {
                System.err.println("❌ Output is invalid: Cache capacity exceeded.");
                return false;
            }

            System.out.println("✅ Valid output");
            return true;

        } catch (IOException e) {
            System.err.println("❌ Error during validation: " + e.getMessage());
            return false;
        }
    }

    public static boolean validateCacheSizes(List<Video> videos, int maxCapacity, Map<Integer, Set<Integer>> cacheToVideos) {
        Map<Integer, Integer> videoSizes = new HashMap<>();
        for (Video v : videos) {
            videoSizes.put(v.getId(), v.getSize());
        }

        for (Map.Entry<Integer, Set<Integer>> entry : cacheToVideos.entrySet()) {
            int usedCapacity = 0;
            for (int videoId : entry.getValue()) {
                usedCapacity += videoSizes.getOrDefault(videoId, 0);
            }
            if (usedCapacity > maxCapacity) {
                System.err.println("❌ Cache " + entry.getKey() + " exceeds capacity: " + usedCapacity + " > " + maxCapacity);
                return false;
            }
        }

        return true;
    }

    public static boolean readAndValidateOutput(String outputFile, Problem problem, Map<Integer, Set<Integer>> map) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(outputFile));
        int declaredLines;
        try {
            declaredLines = Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
            System.err.println("❌ First line must be a number indicating number of cache entries.");
            return false;
        }

        Set<Integer> seenCaches = new HashSet<>();

        for (int i = 0; i < declaredLines; i++) {
            String line = reader.readLine();
            if (line == null || line.trim().isEmpty()) {
                System.err.println("❌ Missing or empty cache line.");
                return false;
            }

            String[] parts = line.trim().split("\\s+");

            try {
                int cacheId = Integer.parseInt(parts[0]);

                if (cacheId < 0 || cacheId >= problem.getNumCacheServers()) {
                    System.err.println("❌ Invalid cache ID: " + cacheId);
                    return false;
                }

                if (seenCaches.contains(cacheId)) {
                    System.err.println("❌ Cache ID " + cacheId + " is described more than once.");
                    return false;
                }

                seenCaches.add(cacheId);

                Set<Integer> videos = new HashSet<>();
                for (int j = 1; j < parts.length; j++) {
                    int videoId = Integer.parseInt(parts[j]);
                    if (videoId < 0 || videoId >= problem.getNumVideos()) {
                        System.err.println("❌ Invalid video ID: " + videoId);
                        return false;
                    }
                    if (!videos.add(videoId)) {
                        System.err.println("❌ Duplicate video ID " + videoId + " in cache " + cacheId);
                        return false;
                    }
                }
                map.put(cacheId, videos);
            } catch (NumberFormatException e) {
                System.err.println("❌ Invalid number format in line: " + line);
                return false;
            }
        }

        reader.close();
        return true;
    }
}
