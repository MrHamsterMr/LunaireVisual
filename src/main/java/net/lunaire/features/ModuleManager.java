package net.lunaire.features;

import net.lunaire.core.Category;
import net.lunaire.core.Module;
import net.lunaire.mixin.IMinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.clear();
        
        // COMBAT
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

        // VISUAL
        modules.add(new Module("Zoom", Category.VISUAL, GLFW.GLFW_KEY_C) {});
        modules.add(new Module("NoRender", Category.VISUAL, 0) {});
        modules.add(new Module("FullBright", Category.VISUAL, 0) {
            @Override public void onTick() { mc.options.getGamma().setValue(100.0); }
            @Override public void onDisable() { mc.options.getGamma().setValue(1.0); }
        });
        modules.add(new Module("TargetHUD", Category.VISUAL, 0) {
            @Override public void onRenderHud(DrawContext context) {
                if (mc.targetedEntity instanceof net.minecraft.entity.LivingEntity target) {
                    context.fill(10, 10, 110, 40, 0x90000000);
                    context.drawText(mc.textRenderer, target.getName().getString(), 15, 15, -1, true);
                }
            }
        });

        // HUD & MISC (все остальное из твоего списка)
        String[] other = {"Waypoints", "Friends", "ItemScroller", "Optimization", "FreeLook", "ChunkOpt", "BlockOverlay", "Crosshair", "CustomHand", "Macros", "ShulkerView", "ItemGlow", "InfoHUD", "ArmorHUD", "PingFix"};
        for (String s : other) modules.add(new Module(s, Category.MISC, 0) {});
    }

    public static List<Module> getModules() { return modules; }
    public static Module getModule(String name) {
        for (Module m : modules) if (m.name.equalsIgnoreCase(name)) return m;
        return null;
    }
}
