package net.lunaire.features;

import net.lunaire.core.Module;
import net.lunaire.core.Category;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.clear();
        // Добавляем заглушки с ТРЕМЯ аргументами (имя, категория, кнопка)
        modules.add(new Module("Zoom", Category.VISUAL, 0) {});
        modules.add(new Module("FastExp", Category.COMBAT, 0) {});
        modules.add(new Module("ArmorHUD", Category.HUD, 0) {});
        modules.add(new Module("NoRender", Category.VISUAL, 0) {});
        modules.add(new Module("TotemPop", Category.COMBAT, 0) {});
    }

    public static List<Module> getModules() {
        return modules;
    }

    // Метод, который искал компилятор
    public static Module getModule(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }
}
