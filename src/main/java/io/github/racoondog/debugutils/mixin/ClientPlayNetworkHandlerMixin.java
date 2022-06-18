package io.github.racoondog.debugutils.mixin;

import io.github.racoondog.debugutils.DebugUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onGameJoin", at = @At("HEAD"))
    private void resetSettings(GameJoinS2CPacket packet, CallbackInfo ci) {
        DebugUtils.graphsEnabled = false;
        DebugUtils.metrics = null;
    }
}
