package net.lunaire.core;

import net.lunaire.features.combat.FastExp;
import net.lunaire.features.visual.ArmorHUD;
import net.lunaire.features.visual.Zoom;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.clear();
        // Добавляем конкретные классы
        modules.add(new Zoom());
        modules.add(new FastExp());
        modules.add(new ArmorHUD());
        
        // Добавляем заглушки для других функций (теперь с 3 аргументами)
        modules.add(new Module("NoRender", Category.VISUAL, 0) {});
        modules.add(new Module("TotemPop", Category.COMBAT, 0) {});
        modules.add(new Module("FastSwap", Category.COMBAT, 0) {});
    }

    public static List<Module> getModules() {
        return modules;
    }

    // Тот самый метод, который требовали миксины
    public static Module getModule(String name) {
        return modules.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
