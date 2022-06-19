package io.github.racoondog.debugutils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.MetricsData;

import java.util.List;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class ModMetricsData {
    public static final ModMetricsData fpsMetricsData = new ModMetricsData(MinecraftClient.getInstance().getMetricsData(), "60 FPS", "ms", 30, 60, 100);
    public static final ModMetricsData pingMetricsData = new ModMetricsData("Ping", "ms", 100, 4, 60);
    public static final ModMetricsData tpsMetricsData = new ModMetricsData(null, "20 TPS", "ms", 60, 20, 60);
    public static final ModMetricsData packetsInMetricsData = new ModMetricsData("Packets Received / Second", "pkt/s", 6, 15, 0) {
        @Override
        public int getLineHeight(long value) {
            if (MinecraftClient.getInstance().getNetworkHandler() == null || value == 0) return 0;
            int x = (int)Math.sqrt(MinecraftClient.getInstance().getNetworkHandler().getPlayerList().size()) >> 2;
            return this.getLineHeight(value, x);
        }
    }.color(-16711936);
    public static final ModMetricsData packetsOutMetricsData = new ModMetricsData("Packets Sent / Second", "pkt/s", 40, 100, 0) {
        @Override
        public int getLineHeight(long value) {
            if (MinecraftClient.getInstance().getNetworkHandler() == null || value == 0) return 0;
            int x = (int)Math.sqrt(MinecraftClient.getInstance().getNetworkHandler().getPlayerList().size()) >> 2;
            return this.getLineHeight(value, x);
        }
    }.color(-16711936);

    public MetricsData metricsData;
    public final String text;
    public final String unit;
    protected final int i;
    protected final int j;
    public final int red;
    public int staticColor = 0;

    public ModMetricsData(String text, String unit, int i, int j, int red) {
        this(new MetricsData(), text, unit, i, j, red);
    }

    public ModMetricsData(MetricsData metricsData, String text, String unit, int i, int j, int red) {
        this.metricsData = metricsData;
        this.text = text;
        this.unit = unit;
        this.i = i;
        this.j = j;
        this.red = red;
    }

    public int getLineHeight(long value) {
        return this.getLineHeight(value, this.j);
    }

    public int getLineHeight(long value, int j) {
        if (value == 0) return 0;
        double d = (double)value / (double)(1000000000L / (long)j);
        return (int)(d * (double)this.i);
    }

    public ModMetricsData color(int color) {
        this.staticColor = color;
        return this;
    }

    public ModMetricsData init(Supplier<MetricsData> metrics) {
        this.metricsData = metrics.get();
        return this;
    }

    public static void getMetrics(List<ModMetricsData> toReturn) {
        IntegratedServer integratedServer = MinecraftClient.getInstance().getServer();

        toReturn.add(fpsMetricsData);
        if (integratedServer == null) {
            toReturn.add(ModMetricsData.pingMetricsData);
            toReturn.add(ModMetricsData.packetsInMetricsData);
            toReturn.add(ModMetricsData.packetsOutMetricsData);
        } else {
            toReturn.add(ModMetricsData.tpsMetricsData.init(integratedServer::getMetricsData));
        }
    }
}
