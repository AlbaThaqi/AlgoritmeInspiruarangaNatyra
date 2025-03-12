package src;

import src.models.*;
import java.io.*;
import java.util.*;

public class Problem {
    private List<Video> videos;
    private List<Endpoint> endpoints;
    private List<Request> requests;
    private List<CacheServer> cacheServers;
    private int cacheCapacity;

    public Problem(String inputFile) throws IOException {
        parseInput(inputFile);
    }

    private void parseInput(String inputFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int V = Integer.parseInt(st.nextToken());
        int E = Integer.parseInt(st.nextToken());
        int R = Integer.parseInt(st.nextToken()); 
        int C = Integer.parseInt(st.nextToken());
        this.cacheCapacity = Integer.parseInt(st.nextToken()); 

        videos = new ArrayList<>();
        endpoints = new ArrayList<>();
        requests = new ArrayList<>();
        cacheServers = new ArrayList<>();

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < V; i++) {
            videos.add(new Video(i, Integer.parseInt(st.nextToken())));
        }

        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(br.readLine());
            int dataCenterLatency = Integer.parseInt(st.nextToken());
            int K = Integer.parseInt(st.nextToken()); // Number of connected caches

            Endpoint endpoint = new Endpoint(i, dataCenterLatency);
            for (int j = 0; j < K; j++) {
                st = new StringTokenizer(br.readLine());
                int cacheId = Integer.parseInt(st.nextToken());
                int latency = Integer.parseInt(st.nextToken());
                endpoint.addCacheLatency(cacheId, latency);
            }
            endpoints.add(endpoint);
        }

        for (int i = 0; i < R; i++) {
            st = new StringTokenizer(br.readLine());
            int videoId = Integer.parseInt(st.nextToken());
            int endpointId = Integer.parseInt(st.nextToken());
            int numRequests = Integer.parseInt(st.nextToken());

            requests.add(new Request(videoId, endpointId, numRequests));
        }

        for (int i = 0; i < C; i++) {
            cacheServers.add(new CacheServer(i, cacheCapacity));
        }

        br.close();
    }

    public void solve() {

        requests.sort((a, b) -> Integer.compare(b.getNumRequests(), a.getNumRequests()));

        for (Request request : requests) {
            Video video = videos.get(request.getVideoId());
            Endpoint endpoint = endpoints.get(request.getEndpointId());

            int bestCache = -1;
            int bestLatencySave = 0;

            for (Map.Entry<Integer, Integer> entry : endpoint.getCacheLatencies().entrySet()) {
                int cacheId = entry.getKey();
                int latency = entry.getValue();

                CacheServer cache = cacheServers.get(cacheId);
                int latencySaved = endpoint.getDataCenterLatency() - latency;

                if (latencySaved > bestLatencySave && cache.canStoreVideo(video)) {
                    bestCache = cacheId;
                    bestLatencySave = latencySaved;
                }
            }

            if (bestCache != -1) {
                cacheServers.get(bestCache).storeVideo(video);
            }
        }
    }

    public void writeOutput(String outputFile) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));

        long usedCaches = cacheServers.stream().filter(c -> !c.getVideos().isEmpty()).count();
        bw.write(usedCaches + "\n");

        for (CacheServer cache : cacheServers) {
            if (!cache.getVideos().isEmpty()) {
                bw.write(cache.getId() + " " + cache.getVideosAsString() + "\n");
            }
        }
        bw.close();
    }
}
