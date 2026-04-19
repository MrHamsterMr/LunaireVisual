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

        // --- COMBAT ---
        modules.add(new Module("FastExp", Category.COMBAT, 0) {
            @Override public void onTick() {
                if (mc.options.useKey.isPressed() && mc.player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
                    ((IMinecraftClient)mc).setItemUseCooldown(0);
                }
            }
        });
        
        modules.add(new Module("FastSwap", Category.COMBAT, 0) {
            @Override public void onTick() {
                // Если ХП меньше 10 и в левой руке пусто - ищем тотем
                if (mc.player.getHealth() < 10f && mc.player.getOffHandStack().isEmpty()) {
                    for (int i = 0; i < 9; i++) {
                        if (mc.player.getInventory().getStack(i).isOf(Items.TOTEM_OF_UNDYING)) {
                            mc.player.getInventory().selectedSlot = i;
                            break;
                        }
                    }
                }
            }
        });

        // --- VISUAL ---
        modules.add(new Module("Zoom", Category.VISUAL, GLFW.GLFW_KEY_C) {});
        
        modules.add(new Module("TargetHUD", Category.VISUAL, 0) {
            @Override public void onRenderHud(DrawContext context) {
                if (mc.targetedEntity instanceof LivingEntity target) {
                    int x = context.getScaledWindowWidth() / 2 + 10;
                    int y = context.getScaledWindowHeight() / 2 + 10;
                    context.fill(x, y, x + 120, y + 45, 0x90000000);
                    context.fill(x, y, x + 120, y + 1, 0xFF00FBFF);
                    context.drawText(mc.textRenderer, target.getName().getString(), x + 5, y + 5, -1, true);
                    context.drawText(mc.textRenderer, (int)target.getHealth() + " HP", x + 5, y + 15, 0xFF00FBFF, false);
                    // Броня цели
                    int ax = 5;
                    for (ItemStack s : target.getArmorItems()) {
                        if (!s.isEmpty()) {
                            context.drawItem(s, x + ax, y + 25);
                            ax += 20;
                        }
                    }
                }
            }
        });

        // --- HUD ---
        modules.add(new Module("ArmorHUD", Category.HUD, 0) {
            @Override public void onRenderHud(DrawContext context) {
                int y = context.getScaledWindowHeight() - 100;
                for (int i = 3; i >= 0; i--) {
                    ItemStack stack = mc.player.getInventory().getArmorStack(i);
                    if (!stack.isEmpty()) {
                        context.drawItem(stack, 10, y);
                        int dur = stack.getMaxDamage() - stack.getDamage();
                        context.drawText(mc.textRenderer, dur + "", 30, y + 5, dur < 50 ? 0xFFFF5555 : -1, true);
                        y += 20;
                    }
                }
            }
        });

        // Заглушки для биндов (остальные из твоего списка)
        String[] misc = {"Waypoints", "FullBright", "NoRender", "FreeLook", "HitColor", "Macros", "Friends", "Optimization"};
        for (String s : misc) modules.add(new Module(s, Category.MISC, 0) {});
    }

    public static List<Module> getModules() { return modules; }
    public static Module getModule(String name) {
        for (Module m : modules) if (m.name.equalsIgnoreCase(name)) return m;
        return null;
    }
}
