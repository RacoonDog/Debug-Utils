package io.github.racoondog.debugutils.mixin;

import io.github.racoondog.debugutils.ModMetricsData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {
    @Shadow private int packetsReceivedCounter;

    @Shadow private int packetsSentCounter;

    @Inject(method = "updateStats", at = @At("HEAD"))
    private void updateStatsInject(CallbackInfo ci) {
        ModMetricsData.packetsInMetricsData.metricsData.pushSample(this.packetsReceivedCounter * 1000000L);
        ModMetricsData.packetsOutMetricsData.metricsData.pushSample(this.packetsSentCounter * 1000000L);
    }
}
