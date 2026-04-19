package net.lunaire.features;

import net.lunaire.core.*;
import net.lunaire.mixin.IMinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<LunaireModule> modules = new ArrayList<>();

    public static void init() {
        modules.clear();

        // --- COMBAT ---
        
        // №7 FastSwap - Смена предмета на зажатую кнопку
        LunaireModule fastSwap = new LunaireModule("FastSwap", Category.COMBAT, 0) {
            @Override
            public void onTick() {
                Setting s = getSetting("Slot");
                if (mc.player != null) {
                    mc.player.getInventory().selectedSlot = (int)s.dVal - 1;
                }
            }
        };
        fastSwap.addSetting(new Setting("Slot", 1.0)); // Настройка слота 1-9
        modules.add(fastSwap);

        // №6 HitColor - Цвет удара
        LunaireModule hitColor = new LunaireModule("HitColor", Category.COMBAT, 0) {};
        hitColor.addSetting(new Setting("Red", 255.0));
        hitColor.addSetting(new Setting("Green", 0.0));
        hitColor.addSetting(new Setting("Blue", 0.0));
        modules.add(hitColor);

        modules.add(new LunaireModule("FastExp", Category.COMBAT, 0) {
            @Override public void onTick() {
                if (mc.options.useKey.isPressed() && mc.player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
                    ((IMinecraftClient)mc).setItemUseCooldown(0);
                }
            }
        });

        // --- VISUAL ---

        modules.add(new LunaireModule("Zoom", Category.VISUAL, GLFW.GLFW_KEY_C) {});

        modules.add(new LunaireModule("FullBright", Category.VISUAL, 0) {
            @Override public void onTick() {
                mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1000, 0, false, false));
            }
            @Override public void onDisable() { mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION); }
        });

        // №4 NoRender (Много настроек)
        LunaireModule noRender = new LunaireModule("NoRender", Category.VISUAL, 0) {};
        noRender.addSetting(new Setting("NoFire", true));
        noRender.addSetting(new Setting("NoShake", true));
        noRender.addSetting(new Setting("NoCrystals", false));
        modules.add(noRender);

        // №13 Продвинутый TargetHUD
        modules.add(new LunaireModule("TargetHUD", Category.VISUAL, 0) {
            @Override public void onRenderHud(DrawContext context) {
                if (mc.targetedEntity instanceof LivingEntity target) {
                    int x = context.getScaledWindowWidth() / 2 + 10, y = context.getScaledWindowHeight() / 2 + 10;
                    context.fill(x, y, x + 120, y + 55, 0x90101010);
                    context.fill(x, y, x + 120, y + 1, 0xFF00FBFF);
                    context.drawText(mc.textRenderer, target.getName().getString(), x + 5, y + 5, -1, true);
                    context.drawText(mc.textRenderer, (int)target.getHealth() + " HP", x + 5, y + 15, 0xFF00FBFF, false);
                    // Прочность брони врага
                    int ax = 5;
                    for (ItemStack s : target.getArmorItems()) {
                        if (!s.isEmpty()) {
                            context.drawItem(s, x + ax, y + 25);
                            int dur = s.getMaxDamage() - s.getDamage();
                            context.drawText(mc.textRenderer, dur + "", x + ax, y + 42, 0xAAAAAA, false);
                            ax += 25;
                        }
                    }
                }
            }
        });

        // --- HUD ---
        
        // №5 ArmorStatus - Прочность цифрами
        modules.add(new LunaireModule("ArmorHUD", Category.HUD, 0) {
            @Override public void onRenderHud(DrawContext context) {
                int y = 70;
                for (int i = 3; i >= 0; i--) {
                    ItemStack s = mc.player.getInventory().getArmorStack(i);
                    if (!s.isEmpty()) {
                        context.drawItem(s, 10, y);
                        int dur = s.getMaxDamage() - s.getDamage();
                        context.drawText(mc.textRenderer, dur + "", 32, y + 5, dur < 50 ? 0xFFFF5555 : -1, true);
                        y += 20;
                    }
                }
            }
        });

        // Заглушки для биндов остальных функций
        String[] misc = {"FreeLook", "Waypoints", "Friends", "Macros", "ItemScroller", "Optimization", "TotemPop"};
        for (String s : misc) modules.add(new LunaireModule(s, Category.MISC, 0) {});
    }

    public static List<LunaireModule> getModules() { return modules; }
    public static LunaireModule getModule(String name) {
        for (LunaireModule m : modules) if (m.name.equalsIgnoreCase(name)) return m;
        return null;
    }
}
