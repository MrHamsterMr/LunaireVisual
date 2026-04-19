package net.lunaire.features;

import net.lunaire.core.Category;
import net.lunaire.core.Module;
import net.minecraft.client.gui.DrawContext;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.clear();

        // COMBAT
        modules.add(new Module("FastExp", Category.COMBAT, 0) {});
        modules.add(new Module("TotemPop", Category.COMBAT, 0) {});

        // VISUAL
        modules.add(new Module("Zoom", Category.VISUAL, 67) {}); // Клавиша C
        modules.add(new Module("FullBright", Category.VISUAL, 0) {});
        modules.add(new Module("TargetHUD", Category.VISUAL, 0) {});
        modules.add(new Module("NoRender", Category.VISUAL, 0) {});

        // MISC
        modules.add(new Module("FreeLook", Category.MISC, 76) {}); // Клавиша L (код 76)
        
        // HUD
        modules.add(new Module("ArmorHUD", Category.HUD, 0) {});
    }

    public static List<Module> getModules() { return modules; }
    public static Module getModule(String name) {
        return modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
