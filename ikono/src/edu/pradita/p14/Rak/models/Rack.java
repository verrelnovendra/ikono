package models;

public class Rack {
    private int rackId;
    private String rackName;
    private String location;

    // Constructor
    public Rack(int rackId, String rackName, String location) {
        this.rackId = rackId;
        this.rackName = rackName;
        this.location = location;
    }

    // Getters and Setters
    public int getRackId() {
        return rackId;
    }

    public void setRackId(int rackId) {
        this.rackId = rackId;
    }

    public String getRackName() {
        return rackName;
    }

    public void setRackName(String rackName) {
        this.rackName = rackName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Rack [rackId=" + rackId + ", rackName=" + rackName + ", location=" + location + "]";
    }
}
