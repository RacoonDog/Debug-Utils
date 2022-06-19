package io.github.racoondog.debugutils.mixin;

import io.github.racoondog.debugutils.ConfigHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Inject(method = "isChatHidden", at = @At("RETURN"), cancellable = true)
    private void graphsHideChat(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || ConfigHandler.graphsEnabled);
    }
}
