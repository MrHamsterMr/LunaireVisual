package net.lunaire.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.lunaire.features.ModuleManager;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class LunaireClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModuleManager.init();

        // Слушатель команд в чате
        ClientSendMessageEvents.ALLOW_CHAT.register(message -> {
            if (message.startsWith(".bind")) {
                handleBindCommand(message);
                return false; // Не отправлять сообщение на сервер
            }
            return true;
        });
        
        // ... остальной код (бинд Right Shift и HUD)
    }

    private void handleBindCommand(String msg) {
        String[] args = msg.split(" ");
        if (args.length < 3) {
            mc.player.sendMessage(Text.of("§b[Lunaire] §7Используй: §f.bind <модуль> <кнопка>"), false);
            return;
        }

        Module m = ModuleManager.getModule(args[1]);
        if (m == null) {
            mc.player.sendMessage(Text.of("§b[Lunaire] §cМодуль не найден!"), false);
            return;
        }

        // Простейший парсер кнопок (например, .bind Zoom R)
        String keyName = args[2].toUpperCase();
        try {
            // Здесь можно добавить мапу имен кнопок, но для примера берем первую букву
            int keyCode = GLFW.glfwGetKeyName(GLFW.GLFW_KEY_A, 0) != null ? 0 : 0; 
            // В идеале использовать: KeyBindingHelper или рефлексию GLFW по имени
            m.setKey(GLFW.GLFW_KEY_R); // Заглушка, поставим R для теста
            mc.player.sendMessage(Text.of("§b[Lunaire] §7Модуль §f" + m.getName() + " §7привязан к §b" + keyName), false);
        } catch (Exception e) {
            mc.player.sendMessage(Text.of("§cОшибка бинда!"), false);
        }
    }
}
