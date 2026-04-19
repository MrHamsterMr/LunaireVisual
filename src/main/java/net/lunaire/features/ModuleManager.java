package net.lunaire.features;

import net.lunaire.core.Category;
import net.lunaire.core.Module;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.clear();
        modules.add(new Module("Zoom", Category.VISUAL, GLFW.GLFW_KEY_C) {});
        modules.add(new Module("FullBright", Category.VISUAL, 0) {});
        modules.add(new Module("NoRender", Category.VISUAL, 0) {});
        modules.add(new Module("ArmorHUD", Category.HUD, 0) {
            @Override
            public void onRenderHud(DrawContext context) {
                context.drawText(mc.textRenderer, "Armor: OK", 10, 50, -1, true);
            }
        });
        
        // Заглушки для всех остальных 23 пунктов
        String[] other = {"Waypoints", "Friends", "ItemScroller", "Optimization", "FreeLook", "FastSwap", "FastExp", "TotemPop", "HitColor", "HitboxColor"};
        for (String s : other) modules.add(new Module(s, Category.MISC, 0) {});
    }

    public static List<Module> getModules() { return modules; }
    public static Module getModule(String name) {
        for (Module m : modules) if (m.name.equalsIgnoreCase(name)) return m;
        return null;
    }
}
