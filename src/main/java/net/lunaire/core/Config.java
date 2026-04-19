package net.lunaire.core;

import net.lunaire.features.ModuleManager;
import java.io.*;
import java.util.Scanner;

public class Config {
    private static final File file = new File(MinecraftClient.getInstance().runDirectory, "lunaire_config.txt");

    public static void save() {
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (Module m : ModuleManager.getModules()) {
                out.println(m.getName() + ":" + m.isEnabled() + ":" + m.getKey());
            }
        } catch (IOException ignored) {}
    }

    public static void load() {
        if (!file.exists()) return;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(":");
                Module m = ModuleManager.getModule(parts[0]);
                if (m != null) {
                    if (Boolean.parseBoolean(parts[1]) != m.isEnabled()) m.toggle();
                    m.setKey(Integer.parseInt(parts[2]));
                }
            }
        } catch (Exception ignored) {}
    }
}
