package net.lunaire.features;

import net.lunaire.core.Module;
import net.lunaire.core.Category;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.clear();
        // Добавляем пустые модули для теста
        modules.add(new Module("Zoom", Category.VISUAL) {});
        modules.add(new Module("FastExp", Category.COMBAT) {});
        modules.add(new Module("ArmorHUD", Category.HUD) {});
    }

    public static List<Module> getModules() {
        return modules;
    }
}
