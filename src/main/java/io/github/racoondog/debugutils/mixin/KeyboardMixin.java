package io.github.racoondog.debugutils.mixin;

import io.github.racoondog.debugutils.ConfigHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onKey", at = @At("TAIL"))
    private void graphKeyCombo(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (window == this.client.getWindow().getHandle() && (this.client.currentScreen == null || this.client.currentScreen.passEvents)) {
            if (action == GLFW.GLFW_RELEASE) {
                if (key == GLFW.GLFW_KEY_F4 && Screen.hasShiftDown()) {
                    ConfigHandler.toggleGraphs();
                } else if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_F4)) {
                    ConfigHandler.processF4(key);
                }
            }
        }
    }
}
