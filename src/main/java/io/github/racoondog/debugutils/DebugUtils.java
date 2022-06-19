package io.github.racoondog.debugutils;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.network.PlayerListEntry;
import org.slf4j.Logger;

import java.util.List;
import java.util.Random;

@Environment(EnvType.CLIENT)
public class DebugUtils implements ClientModInitializer {
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final GraphHud GRAPH_HUD = new GraphHud();
    public static final List<ModMetricsData> metrics = Lists.newArrayList();

    private static final Random RANDOM = new Random();

    private static int ticks = 0;

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            if (ConfigHandler.graphsEnabled) {
                GRAPH_HUD.render(matrixStack);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ticks == 20) {
                ticks = 0;
                if (client.getNetworkHandler() != null && client.player != null) {
                    PlayerListEntry playerListEntry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
                    if (playerListEntry == null) return;
                    int ping = playerListEntry.getLatency();

                    if (ping > 0) ModMetricsData.pingMetricsData.metricsData.pushSample(ping * 1000000L);
                }
            }
            ticks++;
        });
    }
}
