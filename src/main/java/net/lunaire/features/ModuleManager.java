package net.lunaire.features;

import net.lunaire.core.*;
import net.lunaire.mixin.IMinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<LunaireModule> modules = new ArrayList<>();

    public static void init() {
        modules.clear();

        // Zoom (№9)
        modules.add(new LunaireModule("Zoom", Category.VISUAL, GLFW.GLFW_KEY_C) {});

        // NoRender (№20)
        LunaireModule noRender = new LunaireModule("NoRender", Category.VISUAL, 0) {};
        noRender.addSetting(new Setting("NoFire", true));
        noRender.addSetting(new Setting("NoShake", true));
        modules.add(noRender);

        // ArmorHUD (№5)
        modules.add(new LunaireModule("ArmorHUD", Category.HUD, 0) {
            @Override
            public void onRenderHud(DrawContext context) {
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

        // Заглушки для всех остальных (чтобы меню было полным)
        String[] rest = {"FastSwap", "FastExp", "TotemPop", "HitColor", "TargetHUD", "Waypoints", "Friends", "FreeLook", "Macros", "FullBright"};
        for (String s : rest) modules.add(new LunaireModule(s, Category.MISC, 0) {});
    }

    public static List<LunaireModule> getModules() { return modules; }
    public static LunaireModule getModule(String name) {
        for (LunaireModule m : modules) if (m.name.equalsIgnoreCase(name)) return m;
        return null;
    }
}
