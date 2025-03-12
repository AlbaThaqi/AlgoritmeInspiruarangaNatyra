import models.CacheServer;
import models.Endpoint;
import models.Request;
import models.Video;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Problem {
    int V, E, R, C, X;
    List<Video> videos = new ArrayList<>();
    List<CacheServer> cacheServers = new ArrayList<>();
    List<Endpoint> endpoints = new ArrayList<>();
    List<Request> requests = new ArrayList<>();

    public void parseInput(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String[] firstLine = br.readLine().split(" ");
        V = Integer.parseInt(firstLine[0]);
        E = Integer.parseInt(firstLine[1]);
        R = Integer.parseInt(firstLine[2]);
        C = Integer.parseInt(firstLine[3]);
        X = Integer.parseInt(firstLine[4]);

        String[] videoSizes = br.readLine().split(" ");
        for (int i = 0; i < V; i++) {
            videos.add(new Video(i, Integer.parseInt(videoSizes[i])));
        }

        for (int i = 0; i < C; i++) {
            cacheServers.add(new CacheServer(i, X));
        }
    }
}
