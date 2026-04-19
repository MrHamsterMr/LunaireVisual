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
    public static final String NAME = "Lunaire";
    public static final String VERSION = "1.0.0";
    
    // Список для отслеживания клавиш, которые уже нажаты (чтобы не было спама переключений)
    private static final List<Integer> KNOWN_KEYS = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        // 1. Инициализация всех модулей
        ModuleManager.init();

        // 2. Основной цикл обработки (Тики)
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.getWindow() == null) return;

            long window = client.getWindow().getHandle();

            // Проверка открытия ClickGUI (Right Shift)
            if (InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                if (!(client.currentScreen instanceof ClickGuiScreen)) {
                    client.setScreen(new ClickGuiScreen());
                }
            }

            // Проверка биндов для каждого модуля
            for (Module m : ModuleManager.getModules()) {
                int key = m.getKey();
                if (key == 0) continue; // Пропускаем модули без бинда

                boolean isPressed = InputUtil.isKeyPressed(window, key);

                if (isPressed && !KNOWN_KEYS.contains(key)) {
                    // Если кнопка нажата СЕЙЧАС, но не была нажата в прошлом тике
                    m.toggle();
                    KNOWN_KEYS.add(key); // Запоминаем, что кнопка зажата
                } else if (!isPressed && KNOWN_KEYS.contains(key)) {
                    // Если кнопку отпустили
                    KNOWN_KEYS.remove(Integer.valueOf(key));
                }

                // Вызываем метод onTick для всех включенных модулей (например, для FastExp)
                if (m.isEnabled()) {
                    m.onTick();
                }
            }
        });

        // 3. Отрисовка HUD элементов
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null || mc.options.hudHidden) return;

            // Название клиента в верхнем левом углу
            context.drawText(mc.textRenderer, "§b" + NAME + " §f" + VERSION, 10, 10, -1, true);

            // Отрисовка визуальных модулей (например, ArmorHUD)
            for (Module m : ModuleManager.getModules()) {
                if (m.isEnabled()) {
                    m.onRenderHud(context);
                }
            }
        });
    }
}
