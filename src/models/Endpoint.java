package models;

import java.util.HashMap;
import java.util.Map;

public class Endpoint {
    int id, dataCenterLatency;
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
}
