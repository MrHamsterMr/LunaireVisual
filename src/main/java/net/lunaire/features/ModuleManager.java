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
        modules.add(new Module("Zoom", Category.VISUAL, GLFW.GLFW_KEY_C) {});
        modules.add(new Module("NoRender", Category.VISUAL, 0) {});
        modules.add(new Module("FullBright", Category.VISUAL, GLFW.GLFW_KEY_B) {
            @Override public void onTick() { mc.options.getGamma().setValue(100.0); }
            @Override public void onDisable() { mc.options.getGamma().setValue(1.0); }
        });
        modules.add(new Module("FastExp", Category.COMBAT, 0) {
            @Override public void onTick() {
                if (mc.options.useKey.isPressed() && mc.player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
                    ((IMinecraftClient)mc).setItemUseCooldown(0);
                }
            }
        });
        modules.add(new Module("TargetHUD", Category.VISUAL, 0) {
            @Override public void onRenderHud(DrawContext context) {
                if (mc.targetedEntity instanceof net.minecraft.entity.LivingEntity target) {
                    context.fill(10, 10, 110, 40, 0x90000000);
                    context.drawText(mc.textRenderer, target.getName().getString(), 15, 15, -1, true);
                    context.drawText(mc.textRenderer, (int)target.getHealth() + " HP", 15, 25, 0xFF00FBFF, true);
                }
            }
        });
        modules.add(new Module("ArmorHUD", Category.HUD, 0) {
            @Override public void onRenderHud(DrawContext context) {
                int y = 50;
                for (int i = 3; i >= 0; i--) {
                    ItemStack stack = mc.player.getInventory().getArmorStack(i);
                    if (!stack.isEmpty()) {
                        context.drawItem(stack, 10, y);
                        y += 20;
                    }
                }
            }
        });
        // Заглушки для остальных
        String[] misc = {"FreeLook", "Waypoints", "FastSwap", "TotemPop", "HitColor", "HitboxColor", "ItemScroller", "Friends", "Optimization", "ChunkOpt", "BlockOverlay", "Crosshair", "CustomHand", "Macros", "ShulkerView", "ItemGlow", "InfoHUD"};
        for (String s : misc) modules.add(new Module(s, Category.MISC, 0) {});
    }

    public static List<Module> getModules() { return modules; }
    public static Module getModule(String name) {
        for (Module m : modules) if (m.getName().equalsIgnoreCase(name)) return m;
        return null;
    }
}
