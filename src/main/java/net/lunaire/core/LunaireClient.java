package net.lunaire.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.lunaire.features.ModuleManager;
import net.lunaire.ui.ClickGuiScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class LunaireClient implements ClientModInitializer {
    public static final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void onInitializeClient() {
        ModuleManager.init();
        Config.load(); // Авто-загрузка конфига при старте

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // 1. Открытие меню на Правый Шифт
            if (InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                if (!(client.currentScreen instanceof ClickGuiScreen)) {
                    client.setScreen(new ClickGuiScreen());
                }
            }

            // 2. Обработка всех функций и их биндов
            for (Module m : ModuleManager.getModules()) {
                // Если кнопка модуля нажата (и это не 0)
                if (m.getKey() != 0 && InputUtil.isKeyPressed(client.getWindow().getHandle(), m.getKey())) {
                    // Простая защита от "дребезга" кнопок
                    if (client.currentScreen == null) {
                        // Тут можно добавить задержку, но пока оставим логику тиков
                    }
                }

                // ВАЖНО: Вызываем логику работы функций
                if (m.isEnabled()) {
                    m.onTick();
                }
            }
        });

        // Отрисовка HUD элементов
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (mc.options.hudHidden) return;
            for (Module m : ModuleManager.getModules()) {
                if (m.isEnabled()) m.onRenderHud(context);
            }
        });
    }
}
