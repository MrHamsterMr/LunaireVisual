package net.lunaire.features;

import net.lunaire.core.Category;
import net.lunaire.core.LunaireModule;
import net.lunaire.core.Setting;
import net.lunaire.mixin.IMinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<LunaireModule> modules = new ArrayList<>();

    public static void init() {
        modules.clear();

        // ==========================
        //         COMBAT (Бой)
        // ==========================

        // №2 FastExp - моментальный опыт
        modules.add(new LunaireModule("FastExp", Category.COMBAT, 0) {
            @Override
            public void onTick() {
                if (mc.options.useKey.isPressed() && mc.player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
                    ((IMinecraftClient)mc).setItemUseCooldown(0);
                }
            }
        });

        // №14 FastSwap - быстрая смена на выбранный слот
        LunaireModule fastSwap = new LunaireModule("FastSwap", Category.COMBAT, 0) {
            @Override
            public void onTick() {
                Setting s = getSetting("Slot");
                if (mc.player != null && s != null) {
                    mc.player.getInventory().selectedSlot = (int)s.dVal - 1;
                }
            }
        };
        fastSwap.addSetting(new Setting("Slot", 1.0)); // Настройка слота (1-9)
        modules.add(fastSwap);

        // №23 TotemPop - уведомление (Логика в MixinClientPlayNetworkHandler)
        modules.add(new LunaireModule("TotemPop", Category.COMBAT, 0) {});

        // №11 HitColor - кастомный цвет удара
        LunaireModule hitColor = new LunaireModule("HitColor", Category.COMBAT, 0) {};
        hitColor.addSetting(new Setting("Red", 255.0));
        hitColor.addSetting(new Setting("Green", 0.0));
        hitColor.addSetting(new Setting("Blue", 0.0));
        modules.add(hitColor);


        // ==========================
        //         VISUAL (Визуал)
        // ==========================

        // №9 Zoom - приближение
        modules.add(new LunaireModule("Zoom", Category.VISUAL, GLFW.GLFW_KEY_C) {});

        // №11 FullBright - ночное зрение
        modules.add(new LunaireModule("FullBright", Category.VISUAL, GLFW.GLFW_KEY_B) {
            @Override
            public void onTick() {
                mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1000, 0, false, false));
            }
            @Override
            public void onDisable() {
                mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
            }
        });

        // №20 NoRender - отключение лишнего
        LunaireModule noRender = new LunaireModule("NoRender", Category.VISUAL, 0) {};
        noRender.addSetting(new Setting("NoFire", true));
        noRender.addSetting(new Setting("NoShake", true));
        noRender.addSetting(new Setting("NoCrystals", false));
        modules.add(noRender);

        // №13 TargetHUD - ХП и Броня врага
        modules.add(new LunaireModule("TargetHUD", Category.VISUAL, 0) {
            @Override
            public void onRenderHud(DrawContext context) {
                if (mc.targetedEntity instanceof LivingEntity target) {
                    int x = context.getScaledWindowWidth() / 2 + 10;
                    int y = context.getScaledWindowHeight() / 2 + 10;
                    context.fill(x, y, x + 120, y + 50, 0x90101010);
                    context.fill(x, y, x + 120, y + 1, 0xFF00FBFF);
                    context.drawText(mc.textRenderer, target.getName().getString(), x + 5, y + 5, -1, true);
                    context.drawText(mc.textRenderer, (int)target.getHealth() + " HP", x + 5, y + 15, 0xFF00FBFF, false);
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

        // №18 CustomHand - маленькие руки
        modules.add(new LunaireModule("CustomHand", Category.VISUAL, 0) {});


        // ==========================
        //         MISC (Разное)
        // ==========================

        // №15 FreeLook - свободный обзор
        modules.add(new LunaireModule("FreeLook", Category.MISC, GLFW.GLFW_KEY_LEFT_ALT) {});

        // №3 Waypoints - метки
        modules.add(new LunaireModule("Waypoints", Category.MISC, 0) {});

        // №10 Friends - настройка друзей
        LunaireModule friends = new LunaireModule("Friends", Category.MISC, 0) {};
        friends.addSetting(new Setting("FriendRGB", 0x00FF00));
        modules.add(friends);

        // №21 Macros - макросы
        modules.add(new LunaireModule("Macros", Category.MISC, 0) {});


        // ==========================
        //         HUD (Интерфейс)
        // ==========================

        // №5 ArmorHUD - прочность цифрами
        modules.add(new LunaireModule("ArmorHUD", Category.HUD, 0) {
            @Override
            public void onRenderHud(DrawContext context) {
                int y = context.getScaledWindowHeight() - 100;
                for (int i = 3; i >= 0; i--) {
                    ItemStack s = mc.player.getInventory().getArmorStack(i);
                    if (!s.isEmpty()) {
                        context.drawItem(s, 10, y);
                        int dur = s.getMaxDamage() - s.getDamage();
                        int color = dur < 50 ? 0xFFFF5555 : -1;
                        context.drawText(mc.textRenderer, String.valueOf(dur), 32, y + 5, color, true);
                        y += 18;
                    }
                }
            }
        });

        // Добавляем остальные как заглушки для меню
        String[] rest = {"ItemScroller", "Optimization", "ChunkOpt", "BlockOverlay", "Crosshair", "ShulkerView", "InfoHUD"};
        for (String s : rest) modules.add(new LunaireModule(s, Category.MISC, 0) {});
    }

    public static List<LunaireModule> getModules() {
        return modules;
    }

    public static LunaireModule getModule(String name) {
        for (LunaireModule m : modules) {
            if (m.name.equalsIgnoreCase(name)) return m;
        }
        return null;
    }
}
