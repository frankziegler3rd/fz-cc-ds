/*
 * Frank Ziegler - DS Final Exam
 */ 
package com.fzds.FZFinalExamServer;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Machine {

    @JsonProperty("machineID")
    private String id;
    @JsonProperty("timestamp")
    private long timestamp;
    @JsonProperty("resourcesHeld")
    private List<String> resourcesHeld;
    @JsonProperty("waitingOn")
    private String waitingOn;

    public Machine(String id, long timestamp, List<String> resourcesHeld, String waitingOn) {
        this.id = id;
        this.timestamp = timestamp;
        this.resourcesHeld = resourcesHeld;
        this.waitingOn = waitingOn;
    }

    public Machine() {}

    public String getMachineID() { return id; }

    public long getTimestamp() { return timestamp; }

    public List<String> getResourcesHeld() { return resourcesHeld; }

    public String getWaitingOn() { return waitingOn; }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: " + id + "\n");
        sb.append("timestamp: " + timestamp + "\n");
        sb.append("resources held: \n");
        for (String res : resourcesHeld) {
            sb.append("        " + res + "\n");
        }
        sb.append("waiting on: " + waitingOn);
        return sb.toString();
    }
}