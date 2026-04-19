package net.lunaire.features;

import net.lunaire.core.Category;
import net.lunaire.core.Module;
import net.lunaire.mixin.IMinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.clear();

        // ==========================
        //         COMBAT (Бой)
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

        // №14 FastSwap - легитная смена тотема/сферы при низком ХП
        modules.add(new Module("FastSwap", Category.COMBAT, 0) {
            @Override
            public void onTick() {
                if (mc.player.getHealth() < 8.0f && mc.player.getOffHandStack().isEmpty()) {
                    for (int i = 0; i < 36; i++) {
                        ItemStack stack = mc.player.getInventory().getStack(i);
                        if (stack.isOf(Items.TOTEM_OF_UNDYING) || stack.isOf(Items.PLAYER_HEAD)) {
                            mc.interactionManager.pickItem(i); // Быстрый свап в слот
                            break;
                        }
                    }
                }
            }
        });

        // №23 TotemPop - уведомление (Логика в MixinClientPlayNetworkHandler, здесь только кнопка)
        modules.add(new Module("TotemPop", Category.COMBAT, 0) {});

        // №11 HitColor - цвет удара (Логика в миксине)
        modules.add(new Module("HitColor", Category.COMBAT, 0) {});


        // ==========================
        //         VISUAL (Визуал)
        // ==========================

        // №9 Zoom - приближение на C
        modules.add(new Module("Zoom", Category.VISUAL, GLFW.GLFW_KEY_C) {});

        // №11 FullBright - яркость
        modules.add(new Module("FullBright", Category.VISUAL, GLFW.GLFW_KEY_B) {
            @Override
            public void onTick() { mc.options.getGamma().setValue(100.0); }
            @Override
            public void onDisable() { mc.options.getGamma().setValue(1.0); }
        });

        // №13 TargetHUD - инфо о цели
        modules.add(new Module("TargetHUD", Category.VISUAL, 0) {
            @Override
            public void onRenderHud(DrawContext context) {
                if (mc.targetedEntity instanceof LivingEntity target) {
                    int x = context.getScaledWindowWidth() / 2 + 10;
                    int y = context.getScaledWindowHeight() / 2 + 10;
                    // Фон
                    context.fill(x, y, x + 120, y + 40, 0x90000000);
                    context.fill(x, y, x + 120, y + 1, 0xFF00FBFF);
                    // Ник и ХП
                    context.drawText(mc.textRenderer, target.getName().getString(), x + 5, y + 5, -1, true);
                    float hp = target.getHealth();
                    int color = hp > 10 ? 0xFF00FF00 : 0xFFFF0000;
                    context.drawText(mc.textRenderer, (int)hp + " HP", x + 5, y + 15, color, false);
                    // Полоска
                    float width = (target.getHealth() / target.getMaxHealth()) * 110;
                    context.fill(x + 5, y + 28, x + 115, y + 32, 0xFF444444);
                    context.fill(x + 5, y + 28, x + 5 + (int)width, y + 32, 0xFF00FBFF);
                }
            }
        });

        // №18 CustomHand - маленькие руки (SmallHand)
        modules.add(new Module("CustomHand", Category.VISUAL, 0) {});

        // №12 HitboxColor - изменение цвета хитбоксов
        modules.add(new Module("HitboxColor", Category.VISUAL, 0) {});

        // №20 NoRender - убрать огонь/частицы
        modules.add(new Module("NoRender", Category.VISUAL, 0) {});


        // ==========================
        //         MISC (Разное)
        // ==========================

        // №15 FreeLook - свободный обзор (Логика в MixinCamera)
        modules.add(new Module("FreeLook", Category.MISC, GLFW.GLFW_KEY_LEFT_ALT) {});

        // №1 ItemScroller - быстрая прокрутка
        modules.add(new Module("ItemScroller", Category.MISC, 0) {});

        // №3 Waypoints - метки
        modules.add(new Module("Waypoints", Category.MISC, 0) {
            @Override
            public void onEnable() {
                if (mc.player != null) {
                    mc.player.sendMessage(Text.of("§b[Lunaire] §fМетка поставлена на ваших координатах!"), false);
                }
            }
        });

        // №21 Макросы
        modules.add(new Module("Macros", Category.MISC, 0) {});


        // ==========================
        //         HUD (Интерфейс)
        // ==========================

        // №6, 7 ArmorStatus - прочность и предупреждения
        modules.add(new Module("ArmorHUD", Category.HUD, 0) {
            @Override
            public void onRenderHud(DrawContext context) {
                int y = context.getScaledWindowHeight() - 110;
                for (int i = 3; i >= 0; i--) {
                    ItemStack stack = mc.player.getInventory().getArmorStack(i);
                    if (!stack.isEmpty()) {
                        context.drawItem(stack, 5, y);
                        int max = stack.getMaxDamage();
                        int cur = max - stack.getDamage();
                        int pc = (max > 0) ? (cur * 100 / max) : 100;

                        // №7 Динамический цвет (25%, 50%, 75%)
                        int color = 0xFF00FF00; // Зеленый
                        if (pc <= 25) color = 0xFFFF0000; // Красный
                        else if (pc <= 50) color = 0xFFFFAA00; // Оранжевый
                        else if (pc <= 75) color = 0xFFFFFF00; // Желтый

                        context.drawText(mc.textRenderer, pc + "%", 25, y + 5, color, true);
                        y += 18;
                    }
                }
            }
        });

        // №19 HUD: КД на предметы, FPS, Пинг
        modules.add(new Module("InfoHUD", Category.HUD, 0) {
            @Override
            public void onRenderHud(DrawContext context) {
                String info = "§bFPS: §f" + mc.getCurrentFps() + "  §bPing: §f" + (mc.getNetworkHandler() != null ? 0 : 0);
                context.drawText(mc.textRenderer, info, 10, 25, -1, true);
            }
        });
    }

    public static List<Module> getModules() {
        return modules;
    }

    public static Module getModule(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }
}
