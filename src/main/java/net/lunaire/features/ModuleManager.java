package net.lunaire.core;

import net.lunaire.features.visual.ArmorHUD; // ВАЖНО: Добавлен импорт
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.clear();
        
        // Теперь компилятор найдет этот класс, так как мы его импортировали выше
        modules.add(new ArmorHUD());
        
        // Если хочешь добавить пустые модули для теста без создания файлов:
        modules.add(new Module("Zoom", Category.VISUAL) {});
        modules.add(new Module("FastExp", Category.COMBAT) {});
    }

    public static List<Module> getModules() {
        return modules;
    }
}
