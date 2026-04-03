package com.lifeline.safety.utils;

public class CooldownManager {
    private static final long COOLDOWN_MS = 15000; // 15 seconds
    private static long lastTriggerTime = 0;

    public static boolean isCoolingDown() {
        return System.currentTimeMillis() - lastTriggerTime < COOLDOWN_MS;
    }

    public static void markTriggered() {
        lastTriggerTime = System.currentTimeMillis();
    }
}