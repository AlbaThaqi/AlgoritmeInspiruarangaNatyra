package src;

import src.models.*;
import java.io.*;
import java.util.*;

public class Problem {
    private int numVideos, numEndpoints, numRequests, numCacheServers, cacheCapacity;
    private List<Video> videos;
    private List<Endpoint> endpoints;
    private List<Request> requests;
    private List<CacheServer> cacheServers;

    public Problem(String inputFile) throws IOException {
        parseInput(inputFile);
    }

    private void parseInput(String inputFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        StringTokenizer st = new StringTokenizer(br.readLine());

        this.numVideos = Integer.parseInt(st.nextToken());
        this.numEndpoints = Integer.parseInt(st.nextToken());
        this.numRequests = Integer.parseInt(st.nextToken());
        this.numCacheServers = Integer.parseInt(st.nextToken());
        this.cacheCapacity = Integer.parseInt(st.nextToken()); 

        videos = new ArrayList<>();
        endpoints = new ArrayList<>();
        requests = new ArrayList<>();
        cacheServers = new ArrayList<>();

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < this.numVideos; i++) {
            videos.add(new Video(i, Integer.parseInt(st.nextToken())));
        }

        for (int i = 0; i < this.numCacheServers; i++){
            cacheServers.add(new CacheServer(i, cacheCapacity));
        }

        for (int i = 0; i < this.numEndpoints; i++) {
            st = new StringTokenizer(br.readLine());
            int dataCenterLatency = Integer.parseInt(st.nextToken());
            int numConnectedCaches = Integer.parseInt(st.nextToken()); // Number of connected caches
            Endpoint endpoint = new Endpoint(i, dataCenterLatency);

            for (int j = 0; j < numConnectedCaches; j++) {
                st = new StringTokenizer(br.readLine());
                int cacheId = Integer.parseInt(st.nextToken());
                int latency = Integer.parseInt(st.nextToken());
                endpoint.addCacheLatency(cacheId, latency);
            }
            endpoints.add(endpoint);
        }

        for (int i = 0; i < this.numRequests; i++) {
            st = new StringTokenizer(br.readLine());
            int videoId = Integer.parseInt(st.nextToken());
            int endpointId = Integer.parseInt(st.nextToken());
            int numRequests = Integer.parseInt(st.nextToken());

            requests.add(new Request(videoId, endpointId, numRequests));
        }

        br.close();
    }

    public void printParsedData(){
        System.out.println("Videos:");
        for (Video v : videos) {
            System.out.println(v.getId() + " " + v.getSize());
        }

        System.out.println("\nEndpoints:");
        for (Endpoint e : endpoints) {
            System.out.println("Endpoint " + e.getId() + " (DC Latency: " + e.getDataCenterLatency() + ")");
            for (Map.Entry<Integer, Integer> entry : e.getCacheLatencies().entrySet()) {
                System.out.println("  Cache " + entry.getKey() + " Latency: " + entry.getValue());
            }
        }
    }

    /*public void solve() {

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
    }*/

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

    public int getNumVideos() {
        return numVideos;
    }

    public int getNumEndpoints() {
        return numEndpoints;
    }

    public int getNumRequests() {
        return numRequests;
    }

    public int getNumCacheServers() {
        return numCacheServers;
    }

    public int getCacheCapacity() {
        return cacheCapacity;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public List<CacheServer> getCacheServers() {
        return cacheServers;
    }
}
