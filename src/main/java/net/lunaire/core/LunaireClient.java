package net.lunaire.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.lunaire.features.ModuleManager;
import net.lunaire.ui.ClickGuiScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class LunaireClient implements ClientModInitializer {
    private static final List<Integer> PRESSED = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        ModuleManager.init();
        Config.load();

        ClientSendMessageEvents.ALLOW_CHAT.register(message -> {
            if (message.startsWith(".bind")) {
                String[] args = message.split(" ");
                if (args.length >= 3) {
                    Module m = ModuleManager.getModule(args[1]);
                    if (m != null) {
                        m.setKey(args[2].toUpperCase().charAt(0));
                        Config.save();
                        MinecraftClient.getInstance().player.sendMessage(Text.of("§b[Lunaire] §fКлавиша сохранена!"), false);
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
                    boolean down = InputUtil.isKeyPressed(win, m.getKey());
                    if (down && !PRESSED.contains(m.getKey())) {
                        m.toggle();
                        PRESSED.add(m.getKey());
                    } else if (!down) PRESSED.remove(Integer.valueOf(m.getKey()));
                }
                if (m.isEnabled()) m.onTick();
            }
        });
    }
}
