package net.lunaire.features;

import net.lunaire.core.Module;
import net.minecraft.item.Items;
import java.util.*;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        // COMBAT
        modules.add(new Module("FastExp", Module.Category.COMBAT) { // №2
            @Override
            public void onTick() {
                if (mc.options.useKey.isPressed() && mc.player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
                    // Используем миксин для обнуления задержки
                    ((net.lunaire.mixin.IMinecraftClient)mc).setItemUseCooldown(0);
                }
            }
        });
        modules.add(new Module("FastSwap", Module.Category.COMBAT)); // №14
        modules.add(new Module("TotemPop", Module.Category.COMBAT)); // №23

        // VISUAL
        modules.add(new Module("Zoom", Module.Category.VISUAL)); // №9
        modules.add(new Module("TargetHUD", Module.Category.VISUAL)); // №13
        modules.add(new Module("HitColor", Module.Category.VISUAL)); // №11
        modules.add(new Module("NoRender", Module.Category.VISUAL)); // №20
        
        // HUD
        modules.add(new Module("ArmorHUD", Module.Category.HUD) { // №6, 7
            @Override
            public void onRenderHud(net.minecraft.client.gui.DrawContext context) {
                int y = 50;
                for (int i = 3; i >= 0; i--) {
                    var stack = mc.player.getInventory().getArmorStack(i);
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
}
