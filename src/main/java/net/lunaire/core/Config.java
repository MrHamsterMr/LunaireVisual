package net.lunaire.core;

import net.lunaire.features.ModuleManager;
import net.minecraft.client.MinecraftClient;
import java.io.*;
import java.util.Scanner;

public class Config {
    private static final File file = new File(MinecraftClient.getInstance().runDirectory, "lunaire_config.txt");

    public static void save() {
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (Module m : ModuleManager.getModules()) {
                out.println(m.name + ":" + m.enabled + ":" + m.key + ":" + m.isMouse);
            }
        } catch (Exception e) {}
    }

    public static void load() {
        if (!file.exists()) return;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                if (parts.length < 4) continue;
                Module m = ModuleManager.getModule(parts[0]);
                if (m != null) {
                    if (Boolean.parseBoolean(parts[1])) m.enabled = true;
                    m.key = Integer.parseInt(parts[2]);
                    m.isMouse = Boolean.parseBoolean(parts[3]);
                }
            }
        } catch (Exception e) {}
    }
}
