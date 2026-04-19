package net.lunaire.core;

import net.lunaire.features.combat.*;
import net.lunaire.features.visual.*;
import java.util.*;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        // Добавляем модули из разных папок
        modules.add(new FastExp());
        modules.add(new Zoom());
        modules.add(new ArmorHUD());
        // Сюда будешь дописывать новые классы
    }

    public static List<Module> getModules() { return modules; }
}
