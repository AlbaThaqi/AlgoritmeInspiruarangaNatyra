package src.models;

import src.models.*;

import java.util.*;

public class GAOperators {

    public static Solution mutateSolution(Solution original, List<Video> videos, int cacheCapacity) {
        Solution mutated = original.deepCopy();
        Random rand = new Random();

        List<Integer> nonEmptyCaches = new ArrayList<>();
        for (Map.Entry<Integer, Set<Integer>> entry : mutated.getCacheMap().entrySet()) {
            if (!entry.getValue().isEmpty()) {
                nonEmptyCaches.add(entry.getKey());
            }
        }

        if (nonEmptyCaches.isEmpty()) return mutated; // nothing to tweak

        int cacheId = nonEmptyCaches.get(rand.nextInt(nonEmptyCaches.size()));
        Set<Integer> videoSet = mutated.getVideosForCache(cacheId);
        List<Integer> videoList = new ArrayList<>(videoSet);

        int removedVid = videoList.get(rand.nextInt(videoList.size()));
        videoSet.remove(removedVid);

        int used = videoSet.stream()
                .mapToInt(v -> videos.get(v).getSize())
                .sum();
        int spaceLeft = cacheCapacity - used;

        Collections.shuffle(videos);
        for (Video v : videos) {
            int vid = v.getId();
            if (!videoSet.contains(vid) && v.getSize() <= spaceLeft) {
                videoSet.add(vid); // mutation complet
                break;
            }
        }

        return mutated;
    }
}
