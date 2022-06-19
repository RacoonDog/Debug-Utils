package io.github.racoondog.debugutils;

import org.lwjgl.glfw.GLFW;

public class ConfigHandler {
    public static boolean graphsEnabled = false;
    public static boolean uuidDisplay = false;

    public static void processF4(int key) {
        if (key == GLFW.GLFW_KEY_U) {
            uuidDisplay = !uuidDisplay;
            ChatUtils.debugLog("UUID display: " + (uuidDisplay ? "shown" : "hidden"));
        }
    }

    public static void toggleGraphs() {
        graphsEnabled = !graphsEnabled;
        ChatUtils.debugLog("Debug metric graphs: " + (graphsEnabled ? "shown" : "hidden"));

        if (graphsEnabled) {
            ModMetricsData.getMetrics(DebugUtils.metrics);
        }
    }

    public static void resetSettings() {
        graphsEnabled = false;
        uuidDisplay = false;
        DebugUtils.metrics.clear();
    }
}
