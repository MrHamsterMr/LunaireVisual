package net.lunaire.core;

import net.lunaire.features.ModuleManager;
import net.minecraft.client.MinecraftClient;
import java.io.*;
import java.util.Scanner;

public class Config {
    // Файл будет создан в папке с игрой
    private static final File file = new File(MinecraftClient.getInstance().runDirectory, "lunaire_config.txt");

    public static void save() {
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (LunaireModule m : ModuleManager.getModules()) {
                // Формат: Имя:Включен:Клавиша:ЭтоМышь
                out.println(m.name + ":" + m.enabled + ":" + m.key + ":" + m.isMouse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        if (!file.exists()) return;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty() || !line.contains(":")) continue;
                
                String[] parts = line.split(":");
                if (parts.length < 4) continue;

                LunaireModule m = ModuleManager.getModule(parts[0]);
                if (m != null) {
                    boolean shouldBeEnabled = Boolean.parseBoolean(parts[1]);
                    int savedKey = Integer.parseInt(parts[2]);
                    boolean savedIsMouse = Boolean.parseBoolean(parts[3]);

                    // Загружаем бинд
                    m.key = savedKey;
                    m.isMouse = savedIsMouse;

                    // Загружаем состояние (включаем, если было включено)
                    if (shouldBeEnabled && !m.isEnabled()) {
                        m.toggle();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
