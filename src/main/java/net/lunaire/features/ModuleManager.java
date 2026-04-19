package net.lunaire.features;

import net.lunaire.core.Category;
import net.lunaire.core.Module;
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

        // ==========================
        //         COMBAT (ПвП)
        // ==========================

        // №2 FastExp - моментальное кидание опыта
        modules.add(new Module("FastExp", Category.COMBAT, 0) {
            @Override
            public void onTick() {
                if (mc.options.useKey.isPressed() && mc.player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
                    ((IMinecraftClient)mc).setItemUseCooldown(0);
                }
            }
        });

        // №14 FastSwap - база для быстрой смены (логика будет дополняться)
        modules.add(new Module("FastSwap", Category.COMBAT, 0) {});

        // №23 TotemPop - уведомления о тотемах (логика в MixinClientPlayNetworkHandler)
        modules.add(new Module("TotemPop", Category.COMBAT, 0) {});


        // ==========================
        //         VISUAL (Визуал)
        // ==========================

        // №9 Zoom - приближение на клавишу C (67 - код клавиши C)
        modules.add(new Module("Zoom", Category.VISUAL, 67) {});

        // №11 FullBright - яркое освещение везде
        modules.add(new Module("FullBright", Category.VISUAL, 0) {
            @Override
            public void onTick() {
                mc.options.getGamma().setValue(100.0);
            }
            @Override
            public void onDisable() {
                mc.options.getGamma().setValue(1.0);
            }
        });

        // №20 NoRender - убирает огонь и эффекты
        modules.add(new Module("NoRender", Category.VISUAL, 0) {});

        // №13 TargetHUD - ХП и ник врага в рамке
        modules.add(new Module("TargetHUD", Category.VISUAL, 0) {
            @Override
            public void onRenderHud(DrawContext context) {
                if (mc.targetedEntity instanceof LivingEntity target) {
                    int x = context.getScaledWindowWidth() / 2 + 15;
                    int y = context.getScaledWindowHeight() / 2 + 15;

                    // 1. Рисуем стеклянный фон Lunaire
                    context.fill(x, y, x + 130, y + 42, 0x90101010); // Черный полупрозрачный
                    context.fill(x, y, x + 130, y + 1, 0xFF00FBFF);  // Верхняя голубая линия

                    // 2. Имя цели
                    context.drawText(mc.textRenderer, target.getName().getString(), x + 5, y + 6, -1, true);

                    // 3. Полоска здоровья
                    float healthPercent = Math.max(0, Math.min(1, target.getHealth() / target.getMaxHealth()));
                    int barWidth = 120;
                    
                    // Фон полоски (серый)
                    context.fill(x + 5, y + 18, x + 5 + barWidth, y + 26, 0xFF444444);
                    // Сама полоска (голубая Lunaire)
                    context.fill(x + 5, y + 18, x + 5 + (int)(barWidth * healthPercent), y + 26, 0xFF00FBFF);

                    // 4. Текстовое значение HP
                    String hpText = String.format("%.1f HP", target.getHealth());
                    context.drawText(mc.textRenderer, hpText, x + 5, y + 29, 0xAAAAAA, false);
                }
            }
        });


        // ==========================
        //         HUD (Интерфейс)
        // ==========================

        // №6, 7 ArmorStatus - прочность брони на экране
        modules.add(new Module("ArmorHUD", Category.HUD, 0) {
            @Override
            public void onRenderHud(DrawContext context) {
                int y = context.getScaledWindowHeight() - 110;
                for (int i = 3; i >= 0; i--) {
                    ItemStack stack = mc.player.getInventory().getArmorStack(i);
                    if (!stack.isEmpty()) {
                        // Рисуем предмет
                        context.drawItem(stack, 10, y);
                        
                        // Считаем прочность в %
                        int maxDmg = stack.getMaxDamage();
                        int curDmg = maxDmg - stack.getDamage();
                        int percent = (maxDmg > 0) ? (curDmg * 100 / maxDmg) : 100;
                        
                        // Цвет в зависимости от поломки
                        int color = 0xFFFFFFFF; // Белый
                        if (percent < 25) color = 0xFFFF5555; // Красный
                        else if (percent < 50) color = 0xFFFFAA00; // Оранжевый

                        context.drawText(mc.textRenderer, curDmg + " (" + percent + "%)", 32, y + 5, color, true);
                        y += 20;
                    }
                }
            }
        });

        // ==========================
        //         MISC (Прочее)
        // ==========================
        
        // №1 ItemScroller - быстрая прокрутка предметов (база)
        modules.add(new Module("ItemScroller", Category.MISC, 0) {});
        
        // №15 FreeLook - свободный обзор (база)
        modules.add(new Module("FreeLook", Category.MISC, 0) {});
    }

    public static List<Module> getModules() {
        return modules;
    }

    public static Module getModule(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }
}
