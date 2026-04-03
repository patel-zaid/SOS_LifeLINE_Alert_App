package com.lifeline.safety.models;

public class AlertHistory {
    private final int id;
    private final String date;
    private final String time;
    private final String location;

    public AlertHistory(int id, String date, String time, String location) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public int getId() { return id; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getLocation() { return location; }

    // Helper method to check if alert was successful
    public boolean isSuccessful() {
        return location != null &&
                location.startsWith("http") &&  // Valid Google Maps link
                !location.equals("Unavailable");
    }

    // Helper method to get error message
    public String getErrorMessage() {
        if (location != null && location.startsWith("FAILED: ")) {
            // Remove "FAILED: " prefix and return the actual error
            return location.substring(8);
        } else if (location == null || location.equals("Unavailable")) {
            return "Location unavailable";
        }
        return "Unknown error occurred";
    }
}