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

        // NoRender (Тряска, Огонь)
        LunaireModule noRender = new LunaireModule("NoRender", Category.VISUAL, 0) {};
        noRender.addSetting(new Setting("NoShake", true));
        noRender.addSetting(new Setting("NoFire", true));
        modules.add(noRender);

        // FastSwap (Выбор слота)
        LunaireModule fastSwap = new LunaireModule("FastSwap", Category.COMBAT, 0) {
            @Override public void onTick() {
                Setting s = getSetting("Slot");
                if (mc.player != null && s != null) mc.player.getInventory().selectedSlot = (int)s.dVal - 1;
            }
        };
        fastSwap.addSetting(new Setting("Slot", 1.0));
        modules.add(fastSwap);

        // ArmorHUD (Прочность цифрами)
        modules.add(new LunaireModule("ArmorHUD", Category.HUD, 0) {
            @Override public void onRenderHud(DrawContext context) {
                int y = 70;
                for (int i = 3; i >= 0; i--) {
                    ItemStack s = mc.player.getInventory().getArmorStack(i);
                    if (!s.isEmpty()) {
                        context.drawItem(s, 10, y);
                        int dur = s.getMaxDamage() - s.getDamage();
                        context.drawText(mc.textRenderer, String.valueOf(dur), 32, y + 5, -1, true);
                        y += 20;
                    }
                }
            }
        });

        // Заглушки для всех 23 функций
        String[] rest = {"Zoom", "FastExp", "TotemPop", "HitColor", "TargetHUD", "Waypoints", "Friends", "FreeLook", "Macros", "FullBright", "Optimization", "BlockOverlay"};
        for (String name : rest) modules.add(new LunaireModule(name, Category.MISC, 0) {});
    }

    public static List<LunaireModule> getModules() { return modules; }
    public static LunaireModule getModule(String name) {
        for (LunaireModule m : modules) if (m.name.equalsIgnoreCase(name)) return m;
        return null;
    }
}
