package net.lunaire.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.lunaire.features.ModuleManager;
import net.lunaire.ui.ClickGuiScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class LunaireClient implements ClientModInitializer {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static final List<Integer> PRESSED_KEYS = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        ModuleManager.init();

        // Система команд (.bind)
        ClientSendMessageEvents.ALLOW_CHAT.register(message -> {
            if (message.startsWith(".bind")) {
                String[] args = message.split(" ");
                if (args.length < 3) {
                    mc.player.sendMessage(Text.of("§b[Lunaire] §7Используй: §f.bind <модуль> <кнопка>"), false);
                } else {
                    Module m = ModuleManager.getModule(args[1]);
                    if (m != null) {
                        // Упрощенный бинд (берет первую букву)
                        int key = GLFW.glfwGetKeyName(GLFW.GLFW_KEY_A, 0) != null ? args[2].toUpperCase().charAt(0) : 0;
                        m.setKey(key); 
                        mc.player.sendMessage(Text.of("§b[Lunaire] §7Бинд установлен!"), false);
                    } else {
                        mc.player.sendMessage(Text.of("§b[Lunaire] §cМодуль не найден!"), false);
                    }
                }
                return false; 
            }
            return true;
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            long win = client.getWindow().getHandle();

            if (InputUtil.isKeyPressed(win, GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                if (!(client.currentScreen instanceof ClickGuiScreen)) client.setScreen(new ClickGuiScreen());
            }

            for (Module m : ModuleManager.getModules()) {
                if (m.getKey() != 0) {
                    boolean isPressed = InputUtil.isKeyPressed(win, m.getKey());
                    if (isPressed && !PRESSED_KEYS.contains(m.getKey())) {
                        m.toggle();
                        PRESSED_KEYS.add(m.getKey());
                    } else if (!isPressed) {
                        PRESSED_KEYS.remove(Integer.valueOf(m.getKey()));
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
