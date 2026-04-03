package com.lifeline.safety.models;

import java.util.List;

public class SafetyCategory {
    private final String title;
    private final String subtitle;
    private final int iconRes;
    private final int backgroundRes;
    private final int iconTint;
    private final List<String> steps;
    private final boolean offlineAvailable;
    private boolean isExpanded;

    public SafetyCategory(String title, String subtitle, int iconRes, int backgroundRes,
                          int iconTint, List<String> steps, boolean offlineAvailable) {
        this.title = title;
        this.subtitle = subtitle;
        this.iconRes = iconRes;
        this.backgroundRes = backgroundRes;
        this.iconTint = iconTint;
        this.steps = steps;
        this.offlineAvailable = offlineAvailable;
        this.isExpanded = false;
    }

    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public int getIconRes() { return iconRes; }
    public int getBackgroundRes() { return backgroundRes; }
    public int getIconTint() { return iconTint; }
    public List<String> getSteps() { return steps; }
    public boolean isOfflineAvailable() { return offlineAvailable; }
    public boolean isExpanded() { return isExpanded; }

    public void setExpanded(boolean expanded) { isExpanded = expanded; }
    public void toggleExpanded() { isExpanded = !isExpanded; }
}