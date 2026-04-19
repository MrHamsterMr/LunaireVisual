package net.lunaire.features;

import net.lunaire.core.Category;
import net.lunaire.core.Module;
import net.lunaire.mixin.IMinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.clear();

        // --- COMBAT ---
        modules.add(new Module("FastExp", Category.COMBAT, 0) {
            @Override public void onTick() {
                if (mc.options.useKey.isPressed() && mc.player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
                    ((IMinecraftClient)mc).setItemUseCooldown(0);
                }
            }
        });
        modules.add(new Module("FastSwap", Category.COMBAT, 0) {});
        modules.add(new Module("TotemPop", Category.COMBAT, 0) {});
        modules.add(new Module("HitColor", Category.COMBAT, 0) {});

        // --- VISUAL ---
        modules.add(new Module("Zoom", Category.VISUAL, GLFW.GLFW_KEY_C) {});
        modules.add(new Module("FullBright", Category.VISUAL, 0) {
            @Override public void onTick() { mc.options.getGamma().setValue(100.0); }
            @Override public void onDisable() { mc.options.getGamma().setValue(1.0); }
        });
        modules.add(new Module("NoRender", Category.VISUAL, 0) {});
        modules.add(new Module("TargetHUD", Category.VISUAL, 0) {});
        modules.add(new Module("BlockOverlay", Category.VISUAL, 0) {});
        modules.add(new Module("Crosshair", Category.VISUAL, 0) {});
        modules.add(new Module("CustomHand", Category.VISUAL, 0) {});
        modules.add(new Module("ShulkerView", Category.VISUAL, 0) {});
        modules.add(new Module("ItemGlow", Category.VISUAL, 0) {});
        modules.add(new Module("HitboxColor", Category.VISUAL, 0) {});

        // --- MISC ---
        modules.add(new Module("FreeLook", Category.MISC, 0) {});
        modules.add(new Module("ItemScroller", Category.MISC, 0) {});
        modules.add(new Module("Waypoints", Category.MISC, 0) {});
        modules.add(new Module("Friends", Category.MISC, 0) {});
        modules.add(new Module("Optimization", Category.MISC, 0) {});
        modules.add(new Module("ChunkOpt", Category.MISC, 0) {});
        modules.add(new Module("Macros", Category.MISC, 0) {});

        // --- HUD ---
        modules.add(new Module("ArmorHUD", Category.HUD, 0) {});
        modules.add(new Module("InfoHUD", Category.HUD, 0) {});
    }

    public static List<Module> getModules() { return modules; }
    public static Module getModule(String name) {
        for (Module m : modules) if (m.getName().equalsIgnoreCase(name)) return m;
        return null;
    }
}
