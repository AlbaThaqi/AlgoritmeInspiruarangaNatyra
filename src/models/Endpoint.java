package src.models;

import java.util.HashMap;
import java.util.Map;

public class Endpoint {
    private final int id;
    private final int dataCenterLatency;
    Map<Integer, Integer> cacheLatencies;
    public Endpoint(int id, int dataCenterLatency) {
        this.id = id;
        this.dataCenterLatency = dataCenterLatency;
        this.cacheLatencies = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public int getDataCenterLatency() {
        return dataCenterLatency;
    }

    public Map<Integer, Integer> getCacheLatencies() {
        return cacheLatencies;
    }

    public void addCacheLatency(int cacheId, int latency){
        cacheLatencies.put(cacheId, latency);
    }
}
