package io.github.racoondog.debugutils.mixin;

import io.github.racoondog.debugutils.ConfigHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.SubtitlesHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SubtitlesHud.class)
public abstract class SubtitlesHudMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void graphsHideSubtitles(MatrixStack matrices, CallbackInfo ci) {
        if (ConfigHandler.graphsEnabled) ci.cancel();
    }
}
