package net.lunaire.features;

import net.lunaire.core.*;
import net.lunaire.mixin.IMinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<LunaireModule> modules = new ArrayList<>();

    public static void init() {
        modules.clear();

        // --- COMBAT ---
        LunaireModule fastSwap = new LunaireModule("FastSwap", Category.COMBAT, 0) {
            @Override public void onTick() {
                if (mc.player != null) {
                    Setting s = getSetting("Slot");
                    mc.player.getInventory().selectedSlot = (int)s.dVal - 1;
                }
            }
        };
        fastSwap.addSetting(new Setting("Slot", 1.0));
        modules.add(fastSwap);

        modules.add(new LunaireModule("FastExp", Category.COMBAT, 0) {
            @Override public void onTick() {
                if (mc.options.useKey.isPressed() && mc.player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
                    ((IMinecraftClient)mc).setItemUseCooldown(0);
                }
            }
        });

        // --- VISUAL ---
        modules.add(new LunaireModule("Zoom", Category.VISUAL, GLFW.GLFW_KEY_C) {});
        
        LunaireModule noRender = new LunaireModule("NoRender", Category.VISUAL, 0) {};
        noRender.addSetting(new Setting("NoShake", true));
        noRender.addSetting(new Setting("NoFire", true));
        modules.add(noRender);

        modules.add(new LunaireModule("TargetHUD", Category.VISUAL, 0) {
            @Override public void onRenderHud(DrawContext context) {
                if (mc.targetedEntity instanceof LivingEntity target) {
                    int x = 100, y = 100;
                    context.fill(x, y, x + 120, y + 40, 0x90000000);
                    context.drawText(mc.textRenderer, target.getName().getString(), x + 5, y + 5, -1, true);
                    context.drawText(mc.textRenderer, (int)target.getHealth() + " HP", x + 5, y + 15, 0xFF00FBFF, true);
                }
            }
        });

        // Заглушки для остальных из списка 23
        String[] rest = {"Waypoints", "Friends", "FreeLook", "HitColor", "ArmorHUD", "Macros", "ItemScroller", "Optimization", "FullBright"};
        for (String s : rest) modules.add(new LunaireModule(s, Category.MISC, 0) {});
    }

    public static List<LunaireModule> getModules() { return modules; }
    public static LunaireModule getModule(String name) {
        for (LunaireModule m : modules) if (m.name.equalsIgnoreCase(name)) return m;
        return null;
    }
}
