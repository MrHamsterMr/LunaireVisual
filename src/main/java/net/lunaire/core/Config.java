package net.lunaire.core;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.MinecraftClient;
import java.io.*;
import java.util.Scanner;

public class Config {
    private static final File file = new File(MinecraftClient.getInstance().runDirectory, "lunaire_config.txt");

    public static void save() {
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (LunaireModule m : ModuleManager.getModules()) {
                out.println(m.name + ":" + m.enabled + ":" + m.key + ":" + m.isMouse);
            }
        } catch (Exception e) {}
    }

    public static void load() {
        if (!file.exists()) return;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] p = scanner.nextLine().split(":");
                if (p.length < 4) continue;
                LunaireModule m = ModuleManager.getModule(p[0]);
                if (m != null) {
                    if (Boolean.parseBoolean(p[1]) != m.enabled) m.toggle();
                    m.key = Integer.parseInt(p[2]);
                    m.isMouse = Boolean.parseBoolean(p[3]);
                }
            }
        } catch (Exception e) {}
    }
}
