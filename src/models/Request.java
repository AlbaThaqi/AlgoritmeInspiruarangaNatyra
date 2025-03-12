package models;

public class Request {
    int videoId, endpointId, numRequests;
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
