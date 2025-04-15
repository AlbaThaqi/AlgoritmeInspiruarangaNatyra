package src.models;

public class Request {
    private final int videoId;
    private final int endpointId;
    private final int numRequests;
    public Request(int videoId, int endpointId, int numRequests) {
        this.videoId = videoId;
        this.endpointId = endpointId;
        this.numRequests = numRequests;
    }

    public int getVideoId() {
        return videoId;
    }

    public int getEndpointId() {
        return endpointId;
    }

    public int getNumRequests() {
        return numRequests;
    }
}
