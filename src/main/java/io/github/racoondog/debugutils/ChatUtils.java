package io.github.racoondog.debugutils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChatUtils {
    public static void addDebugMessage(Formatting formatting, Text text) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.empty().append(Text.translatable("debug.prefix").formatted(formatting, Formatting.BOLD)).append(" ").append(text));
    }

    public static void debugLog(Text text) {
        addDebugMessage(Formatting.YELLOW, text);
    }

    public static void debugLog(String key, Object... args) {
        debugLog(Text.translatable(key, args));
    }
}
