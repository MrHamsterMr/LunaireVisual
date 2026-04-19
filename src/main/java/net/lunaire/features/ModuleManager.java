package net.lunaire.features;

import net.lunaire.core.Category;
import net.lunaire.core.Module;
import net.lunaire.mixin.IMinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
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
        modules.add(new Module("TotemPop", Category.COMBAT, 0) {});
        modules.add(new Module("FastSwap", Category.COMBAT, 0) {});

        // VISUAL
        modules.add(new Module("Zoom", Category.VISUAL, GLFW.GLFW_KEY_C) {});
        modules.add(new Module("FullBright", Category.VISUAL, 0) {
            @Override public void onTick() { mc.options.getGamma().setValue(100.0); }
            @Override public void onDisable() { mc.options.getGamma().setValue(1.0); }
        });
        modules.add(new Module("NoRender", Category.VISUAL, 0) {});
        modules.add(new Module("TargetHUD", Category.VISUAL, 0) {
            @Override public void onRenderHud(DrawContext context) {
                if (mc.targetedEntity instanceof LivingEntity target) {
                    int x = context.getScaledWindowWidth() / 2 + 10;
                    int y = context.getScaledWindowHeight() / 2 + 10;
                    context.fill(x, y, x + 120, y + 35, 0x90000000);
                    context.drawText(mc.textRenderer, target.getName().getString(), x + 5, y + 5, -1, true);
                    context.fill(x + 5, y + 20, x + 115, y + 26, 0xFF444444);
                    context.fill(x + 5, y + 20, x + 5 + (int)(110 * (target.getHealth()/target.getMaxHealth())), y + 26, 0xFF00FBFF);
                }
            }
        });

        // MISC
        modules.add(new Module("FreeLook", Category.MISC, 0) {});
        modules.add(new Module("Waypoints", Category.MISC, 0) {});
        modules.add(new Module("ItemScroller", Category.MISC, 0) {});

        // HUD
        modules.add(new Module("ArmorHUD", Category.HUD, 0) {
            @Override public void onRenderHud(DrawContext context) {
                int y = context.getScaledWindowHeight() - 100;
                for (int i = 3; i >= 0; i--) {
                    ItemStack stack = mc.player.getInventory().getArmorStack(i);
                    if (!stack.isEmpty()) {
                        context.drawItem(stack, 10, y);
                        int pc = (int)((1.0 - (double)stack.getDamage() / stack.getMaxDamage()) * 100);
                        context.drawText(mc.textRenderer, pc + "%", 30, y + 5, pc < 25 ? 0xFFFF0000 : -1, true);
                        y += 20;
                    }
                }
            }
        });
    }

    public static List<Module> getModules() { return modules; }
    public static Module getModule(String name) {
        for (Module m : modules) if (m.getName().equalsIgnoreCase(name)) return m;
        return null;
    }
}
