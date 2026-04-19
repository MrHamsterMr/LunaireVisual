package net.lunaire.core;

import net.lunaire.features.ModuleManager;
import net.minecraft.client.MinecraftClient;
import java.io.*;
import java.util.Scanner;

public class Config {
    // Создаем файл в корневой папке майнкрафта
    private static final File file = new File(MinecraftClient.getInstance().runDirectory, "lunaire_config.txt");

    public static void save() {
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (Module m : ModuleManager.getModules()) {
                // Формат записи: Имя:Включен:Кнопка
                out.println(m.getName() + ":" + m.isEnabled() + ":" + m.getKey());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        if (!file.exists()) return;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) continue;
                String[] parts = line.split(":");
                if (parts.length < 3) continue;

                Module m = ModuleManager.getModule(parts[0]);
                if (m != null) {
                    boolean shouldBeEnabled = Boolean.parseBoolean(parts[1]);
                    // Устанавливаем клавишу
                    m.setKey(Integer.parseInt(parts[2]));
                    // Устанавливаем состояние (вкл/выкл)
                    if (shouldBeEnabled != m.isEnabled()) {
                        m.toggle();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
