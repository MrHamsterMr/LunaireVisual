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
        
        modules.add(new LunaireModule("Zoom", Category.VISUAL, GLFW.GLFW_KEY_C) {});
        
        LunaireModule noRender = new LunaireModule("NoRender", Category.VISUAL, 0) {};
        noRender.addSetting(new Setting("NoFire", true));
        noRender.addSetting(new Setting("NoShake", true));
        modules.add(noRender);

        modules.add(new LunaireModule("ArmorHUD", Category.HUD, 0) {
            @Override
            public void onRenderHud(DrawContext context) {
                int y = 60;
                for (int i = 3; i >= 0; i--) {
                    ItemStack s = mc.player.getInventory().getArmorStack(i);
                    if (!s.isEmpty()) {
                        context.drawItem(s, 10, y);
                        y += 20;
                    }
                }
            }
        });

        // Заглушки для всех 23 функций
        String[] rest = {"FastSwap", "FastExp", "TotemPop", "HitColor", "TargetHUD", "Waypoints", "Friends", "FreeLook", "Macros", "FullBright", "ItemScroller", "Optimization", "ChunkOpt", "BlockOverlay", "Crosshair", "CustomHand", "ShulkerView", "ItemGlow", "InfoHUD", "PingFix"};
        for (String s : rest) modules.add(new LunaireModule(s, Category.MISC, 0) {});
    }

    public static List<LunaireModule> getModules() { return modules; }
    public static LunaireModule getModule(String name) {
        for (LunaireModule m : modules) if (m.name.equalsIgnoreCase(name)) return m;
        return null;
    }
}
