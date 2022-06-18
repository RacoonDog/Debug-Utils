package io.github.racoondog.debugutils.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public interface AbstractClientPlayerEntityInvoker {
    @Invoker
    PlayerListEntry invokeGetPlayerListEntry();
}
