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
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void onInitializeClient() {
        ModuleManager.init();
        Config.load(); // Загружаем конфиг

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // Открытие меню
            if (InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                if (!(client.currentScreen instanceof ClickGuiScreen)) {
                    client.setScreen(new ClickGuiScreen());
                }
            }

            // Работа функций
            for (Module m : ModuleManager.getModules()) {
                if (m.isEnabled()) {
                    m.onTick();
                }
            }
        });

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (mc.options.hudHidden) return;
            for (Module m : ModuleManager.getModules()) {
                if (m.isEnabled()) m.onRenderHud(context);
            }
        });
    }
}
