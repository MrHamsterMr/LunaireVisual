package net.lunaire.core;

import net.lunaire.features.ModuleManager;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private static final File CONFIG_FILE = new File("lunaire_config.txt");

    public static void save() {
        try {
            List<String> lines = new ArrayList<>();
            for (Module m : ModuleManager.getModules()) {
                lines.add(m.getName() + ":" + m.isEnabled() + ":" + m.getKey());
            }
            PrintWriter writer = new PrintWriter(new FileWriter(CONFIG_FILE));
            for (String line : lines) writer.println(line);
            writer.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void load() {
        if (!CONFIG_FILE.exists()) return;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                Module m = ModuleManager.getModule(parts[0]);
                if (m != null) {
                    if (Boolean.parseBoolean(parts[1])) m.toggle();
                    m.setKey(Integer.parseInt(parts[2]));
                }
            }
            reader.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
