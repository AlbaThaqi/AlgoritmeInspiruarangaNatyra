package src.models;

import src.Problem;
import src.Solver;
import src.Validator;

import java.util.*;

public class HarmonySearch {

    private final List<Video> videos;
    private final int cacheCapacity;
    private final Problem problem;
    private final Random rand = new Random();

    private final int HM_SIZE = 10;
    private final double HMCR = 0.9;  // Harmony Memory Consideration Rate
    private final double PAR = 0.3;   // Pitch Adjustment Rate
    private final int MAX_ITER = 5000;

    private final List<Solution> harmonyMemory = new ArrayList<>();

    public HarmonySearch(Problem problem) {
        this.problem = problem;
        this.videos = problem.getVideos();
        this.cacheCapacity = problem.getCacheCapacity();
    }

    public Solution run() {
        initializeMemory();

        for (int iter = 0; iter < MAX_ITER; iter++) {
            Solution newHarmony = improviseNewHarmony();
            insertIfBetter(newHarmony);
        }

        return getBestSolution();
    }

    private void initializeMemory() {
        Solver solver = new Solver(
                videos,
                problem.getCacheServers(),
                problem.getEndpoints(),
                problem.getRequests()
        );

        harmonyMemory.add(solver.solveGreedy());

        while (harmonyMemory.size() < HM_SIZE) {
            Solution mutated = GAOperators.mutateSolution(
                    harmonyMemory.get(rand.nextInt(harmonyMemory.size())),
                    videos,
                    cacheCapacity
            );
            harmonyMemory.add(mutated);
        }
    }

    private Solution improviseNewHarmony() {
        Solution base = harmonyMemory.get(rand.nextInt(HM_SIZE)).deepCopy();
        Map<Integer, Set<Integer>> newCacheMap = new HashMap<>();

        for (Map.Entry<Integer, Set<Integer>> entry : base.getCacheMap().entrySet()) {
            int cacheId = entry.getKey();
            Set<Integer> newSet = new HashSet<>();

            for (Integer vid : entry.getValue()) {
                if (rand.nextDouble() < HMCR) {
                    if (rand.nextDouble() < PAR) {
                        Video candidate = videos.get(rand.nextInt(videos.size()));
                        if (candidate.getSize() <= cacheCapacity) {
                            newSet.add(candidate.getId());
                        }
                    } else {
                        newSet.add(vid);
                    }
                } else {
                    Video randomVideo = videos.get(rand.nextInt(videos.size()));
                    if (randomVideo.getSize() <= cacheCapacity) {
                        newSet.add(randomVideo.getId());
                    }
                }
            }

            Set<Integer> filtered = enforceCapacity(newSet);
            newCacheMap.put(cacheId, filtered);
        }

        return new Solution(newCacheMap);
    }

    private Set<Integer> enforceCapacity(Set<Integer> videoIds) {
        Set<Integer> finalSet = new HashSet<>();
        int used = 0;
        for (int vid : videoIds) {
            int size = videos.get(vid).getSize();
            if (used + size <= cacheCapacity) {
                finalSet.add(vid);
                used += size;
            }
        }
        return finalSet;
    }


    private void insertIfBetter(Solution candidate) {
        double candidateScore = Validator.getFitnessScore(problem, candidate.getCacheMap());

        Solution worst = getWorstSolution();
        double worstScore = Validator.getFitnessScore(problem, worst.getCacheMap());

        if (candidateScore > worstScore) {
            harmonyMemory.remove(worst);
            harmonyMemory.add(candidate);
        }
    }

    private Solution getBestSolution() {
        return harmonyMemory.stream()
                .max(Comparator.comparingDouble(s -> Validator.getFitnessScore(problem, s.getCacheMap())))
                .orElse(harmonyMemory.get(0));
    }

    private Solution getWorstSolution() {
        return harmonyMemory.stream()
                .min(Comparator.comparingDouble(s -> Validator.getFitnessScore(problem, s.getCacheMap())))
                .orElse(harmonyMemory.get(0));
    }
}
