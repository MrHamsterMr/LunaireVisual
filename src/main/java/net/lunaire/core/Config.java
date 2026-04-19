package net.lunaire.core;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.MinecraftClient;
import java.io.*;
import java.util.Scanner;
public class Config {
    private static final File file = new File(MinecraftClient.getInstance().runDirectory, "lunaire_config.txt");
    public static void save() {
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (LunaireModule m : ModuleManager.getModules()) out.println(m.name + ":" + m.enabled + ":" + m.key);
        } catch (Exception e) {}
    }
    public static void load() {
        if (!file.exists()) return;
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String[] p = sc.nextLine().split(":");
                LunaireModule m = ModuleManager.getModule(p[0]);
                if (m != null) { if (Boolean.parseBoolean(p[1])) m.enabled = true; m.key = Integer.parseInt(p[2]); }
            }
        } catch (Exception e) {}
    }
}
