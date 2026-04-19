package net.lunaire.features;

import net.lunaire.core.*;
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

        // 1. FastSwap
        LunaireModule fastSwap = new LunaireModule("FastSwap", Category.COMBAT, 0) {};
        fastSwap.addSetting(new Setting("Slot", 1.0));
        modules.add(fastSwap);

        // 2. FullBright
        modules.add(new LunaireModule("FullBright", Category.VISUAL, GLFW.GLFW_KEY_B) {
            @Override public void onTick() { mc.options.getGamma().setValue(100.0); }
            @Override public void onDisable() { mc.options.getGamma().setValue(1.0); }
        });

        // 3. NoRender (Настройки NoShake и т.д.)
        LunaireModule noRender = new LunaireModule("NoRender", Category.VISUAL, 0) {};
        noRender.addSetting(new Setting("NoShake", true));
        noRender.addSetting(new Setting("NoFire", true));
        modules.add(noRender);

        // 4. TargetHUD
        modules.add(new LunaireModule("TargetHUD", Category.VISUAL, 0) {
            @Override public void onRenderHud(DrawContext context) {
                if (mc.targetedEntity instanceof LivingEntity target) {
                    int x = 10, y = 10;
                    context.fill(x, y, x + 100, y + 30, 0x90000000);
                    context.drawText(mc.textRenderer, target.getName().getString(), x + 5, y + 5, -1, true);
                    context.drawText(mc.textRenderer, (int)target.getHealth() + " HP", x + 5, y + 15, 0xFF00FBFF, true);
                }
            }
        });

        // Базовые заглушки
        String[] misc = {"Zoom", "Waypoints", "Friends", "FreeLook", "HitColor", "Macros", "FastExp", "ArmorHUD"};
        for (String s : misc) modules.add(new LunaireModule(s, Category.MISC, 0) {});
    }

    public static List<LunaireModule> getModules() { return modules; }
    public static LunaireModule getModule(String name) {
        for (LunaireModule m : modules) if (m.name.equalsIgnoreCase(name)) return m;
        return null;
    }
}
