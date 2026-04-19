package net.lunaire.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.lunaire.features.ModuleManager;
import net.lunaire.ui.ClickGuiScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class LunaireClient implements ClientModInitializer {
    private static final List<Integer> PRESSED = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        ModuleManager.init();
        Config.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            long win = client.getWindow().getHandle();

            // Открытие меню на Правый Шифт
            if (InputUtil.isKeyPressed(win, GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                if (!(client.currentScreen instanceof ClickGuiScreen)) client.setScreen(new ClickGuiScreen());
            }

            for (Module m : ModuleManager.getModules()) {
                if (m.key != 0) {
                    boolean isDown = m.isMouse ? GLFW.glfwGetMouseButton(win, m.key) == GLFW.GLFW_PRESS : InputUtil.isKeyPressed(win, m.key);
                    
                    if (isDown && !PRESSED.contains(m.key)) {
                        m.toggle();
                        PRESSED.add(m.key);
                    } else if (!isDown) {
                        PRESSED.remove(Integer.valueOf(m.key));
                    }
                }
                if (m.isEnabled()) m.onTick();
            }
        });

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            for (Module m : ModuleManager.getModules()) {
                if (m.isEnabled()) m.onRenderHud(context);
            }
        });
    }
}
