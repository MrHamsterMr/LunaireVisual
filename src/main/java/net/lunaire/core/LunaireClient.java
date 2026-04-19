package net.lunaire.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.lunaire.features.ModuleManager; // Вот этот импорт вызывал ошибку
import net.lunaire.ui.ClickGuiScreen;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class LunaireClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModuleManager.init();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            
            // Открытие меню на Правый Шифт
            if (InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                if (!(client.currentScreen instanceof ClickGuiScreen)) {
                    client.setScreen(new ClickGuiScreen());
                }
            }
        });

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            context.drawText(net.minecraft.client.MinecraftClient.getInstance().textRenderer, "Lunaire Loaded", 10, 10, -1, true);
        });
    }
}
