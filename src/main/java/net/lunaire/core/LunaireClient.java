package net.lunaire.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.lunaire.features.ModuleManager;
import net.lunaire.ui.ClickGuiScreen;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class LunaireClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModuleManager.init();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            ModuleManager.getModules().forEach(m -> { if (m.isEnabled()) m.onTick(); });

            if (InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                if (!(client.currentScreen instanceof ClickGuiScreen)) {
                    client.setScreen(new ClickGuiScreen());
                }
            }
        });

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            ModuleManager.getModules().forEach(m -> { if (m.isEnabled()) m.onRenderHud(context); });
        });
    }
}
