package net.lunaire.core;

import net.lunaire.mixin.IMinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.clear();

        // --- COMBAT ---
        modules.add(new Module("FastExp", Category.COMBAT, 0) {
            @Override
            public void onTick() {
                if (mc.options.useKey.isPressed() && mc.player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
                    ((IMinecraftClient)mc).setItemUseCooldown(0);
                }
            }
        });
        modules.add(new Module("TotemPop", Category.COMBAT, 0) {});
        modules.add(new Module("FastSwap", Category.COMBAT, 0) {});

        // --- VISUAL ---
        modules.add(new Module("Zoom", Category.VISUAL, 67) {}); // Кнопка C
        modules.add(new Module("FullBright", Category.VISUAL, 0) {
            @Override
            public void onTick() { mc.options.getGamma().setValue(100.0); }
            @Override
            public void onDisable() { mc.options.getGamma().setValue(1.0); }
        });
        modules.add(new Module("NoRender", Category.VISUAL, 0) {});
        modules.add(new Module("TargetHUD", Category.VISUAL, 0) {
            @Override
            public void onRenderHud(DrawContext context) {
                if (mc.targetedEntity instanceof LivingEntity target) {
                    int x = context.getScaledWindowWidth() / 2 + 10;
                    int y = context.getScaledWindowHeight() / 2 + 10;
                    context.fill(x, y, x + 100, y + 30, 0x90000000);
                    context.drawText(mc.textRenderer, target.getName().getString(), x + 5, y + 5, -1, true);
                    context.fill(x + 5, y + 20, x + 95, y + 24, 0xFF444444);
                    context.fill(x + 5, y + 20, x + 5 + (int)(90 * (target.getHealth()/target.getMaxHealth())), y + 24, 0xFF00FBFF);
                }
            }
        });

        // --- HUD ---
        modules.add(new Module("ArmorStatus", Category.HUD, 0) {
            @Override
            public void onRenderHud(DrawContext context) {
                int y = context.getScaledWindowHeight() - 100;
                for (int i = 3; i >= 0; i--) {
                    ItemStack stack = mc.player.getInventory().getArmorStack(i);
                    if (!stack.isEmpty()) {
                        context.drawItem(stack, 10, y);
                        context.drawText(mc.textRenderer, (stack.getMaxDamage() - stack.getDamage()) + "", 30, y + 5, -1, true);
                        y += 20;
                    }
                }
            }
        });
    }

    public static List<Module> getModules() { return modules; }
    public static Module getModule(String name) {
        return modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
