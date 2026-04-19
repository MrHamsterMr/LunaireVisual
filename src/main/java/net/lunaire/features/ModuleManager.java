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

        // 1. FastSwap (Настройка слота)
        LunaireModule fastSwap = new LunaireModule("FastSwap", Category.COMBAT, 0) {
            @Override public void onTick() {
                if (mc.player.getHealth() < 10.0f) {
                    Setting s = getSetting("Slot");
                    int slot = (s != null) ? (int)s.dVal - 1 : 0;
                    mc.player.getInventory().selectedSlot = slot;
                }
            }
        };
        fastSwap.addSetting(new Setting("Slot", 1.0));
        modules.add(fastSwap);

        // 2. FullBright (Зелье стиля)
        modules.add(new LunaireModule("FullBright", Category.VISUAL, GLFW.GLFW_KEY_B) {
            @Override public void onTick() {
                mc.options.getGamma().setValue(100.0);
            }
        });

        // 3. NoRender (Тряска, Огонь и т.д.)
        LunaireModule noRender = new LunaireModule("NoRender", Category.VISUAL, 0) {};
        noRender.addSetting(new Setting("NoShake", true));
        noRender.addSetting(new Setting("NoFire", true));
        modules.add(noRender);

        // 4. TargetHUD (ХП и Броня врага)
        modules.add(new LunaireModule("TargetHUD", Category.VISUAL, 0) {
            @Override public void onRenderHud(DrawContext context) {
                if (mc.targetedEntity instanceof LivingEntity target) {
                    int x = context.getScaledWindowWidth() / 2 + 10, y = context.getScaledWindowHeight() / 2 + 10;
                    context.fill(x, y, x + 120, y + 45, 0x90101010);
                    context.drawText(mc.textRenderer, target.getName().getString(), x + 5, y + 5, -1, true);
                    context.drawText(mc.textRenderer, (int)target.getHealth() + " HP", x + 5, y + 15, 0xFF00FBFF, false);
                    int ax = 5;
                    for (ItemStack s : target.getArmorItems()) {
                        if (!s.isEmpty()) { context.drawItem(s, x + ax, y + 25); ax += 20; }
                    }
                }
            }
        });

        // 5. ArmorHUD (Прочность цифрами)
        modules.add(new LunaireModule("ArmorHUD", Category.HUD, 0) {
            @Override public void onRenderHud(DrawContext context) {
                int y = 60;
                for (int i = 3; i >= 0; i--) {
                    ItemStack s = mc.player.getInventory().getArmorStack(i);
                    if (!s.isEmpty()) {
                        context.drawItem(s, 10, y);
                        context.drawText(mc.textRenderer, (s.getMaxDamage() - s.getDamage()) + "", 30, y + 5, -1, true);
                        y += 20;
                    }
                }
            }
        });

        String[] misc = {"Zoom", "Waypoints", "Friends", "FreeLook", "HitColor", "Macros", "FastExp"};
        for (String s : misc) modules.add(new LunaireModule(s, Category.MISC, 0) {});
    }

    public static List<LunaireModule> getModules() { return modules; }
    public static LunaireModule getModule(String name) {
        for (LunaireModule m : modules) if (m.name.equalsIgnoreCase(name)) return m;
        return null;
    }
}
