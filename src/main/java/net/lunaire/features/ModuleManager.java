package net.lunaire.features;

import net.lunaire.core.Category;
import net.lunaire.core.Module;
import net.lunaire.mixin.IMinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
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
        //         COMBAT
        // ==========================

        // №2 FastExp
        modules.add(new Module("FastExp", Category.COMBAT, 0) {
            @Override
            public void onTick() {
                if (mc.options.useKey.isPressed() && mc.player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
                    ((IMinecraftClient)mc).setItemUseCooldown(0);
                }
            }
        });

        // №14 FastSwap (Исправлено для 1.21.4)
        modules.add(new Module("FastSwap", Category.COMBAT, 0) {
            @Override
            public void onTick() {
                if (mc.player != null && mc.player.getHealth() < 10.0f) {
                    if (mc.player.getOffHandStack().isEmpty()) {
                        for (int i = 0; i < 9; i++) { // Проверяем хотбар
                            if (mc.player.getInventory().getStack(i).isOf(Items.TOTEM_OF_UNDYING)) {
                                mc.player.getInventory().selectedSlot = i;
                                break;
                            }
                        }
                    }
                }
            }
        });

        // №23 TotemPop (Логика в MixinClientPlayNetworkHandler)
        modules.add(new Module("TotemPop", Category.COMBAT, 0) {});

        // №11 HitColor
        modules.add(new Module("HitColor", Category.COMBAT, 0) {});

        // ==========================
        //         VISUAL
        // ==========================

        // №9 Zoom
        modules.add(new Module("Zoom", Category.VISUAL, GLFW.GLFW_KEY_C) {});

        // №11 FullBright
        modules.add(new Module("FullBright", Category.VISUAL, GLFW.GLFW_KEY_B) {
            @Override
            public void onTick() { mc.options.getGamma().setValue(100.0); }
            @Override
            public void onDisable() { mc.options.getGamma().setValue(1.0); }
        });

        // №13 TargetHUD
        modules.add(new Module("TargetHUD", Category.VISUAL, 0) {
            @Override
            public void onRenderHud(DrawContext context) {
                if (mc.targetedEntity instanceof LivingEntity target) {
                    int x = context.getScaledWindowWidth() / 2 + 10;
                    int y = context.getScaledWindowHeight() / 2 + 10;
                    context.fill(x, y, x + 120, y + 40, 0x90000000);
                    context.fill(x, y, x + 120, y + 1, 0xFF00FBFF);
                    context.drawText(mc.textRenderer, target.getName().getString(), x + 5, y + 5, -1, true);
                    String hp = String.format("%.1f HP", target.getHealth());
                    context.drawText(mc.textRenderer, hp, x + 5, y + 16, 0xFF00FBFF, false);
                    float progress = target.getHealth() / target.getMaxHealth();
                    context.fill(x + 5, y + 28, x + 115, y + 32, 0xFF444444);
                    context.fill(x + 5, y + 28, x + 5 + (int)(110 * progress), y + 32, 0xFF00FBFF);
                }
            }
        });

        // №20 NoRender
        modules.add(new Module("NoRender", Category.VISUAL, 0) {});

        // №12 HitboxColor
        modules.add(new Module("HitboxColor", Category.VISUAL, 0) {});

        // №16 BlockOverlay
        modules.add(new Module("BlockOverlay", Category.VISUAL, 0) {});

        // №17 Crosshair
        modules.add(new Module("Crosshair", Category.VISUAL, 0) {});

        // №18 CustomHand
        modules.add(new Module("CustomHand", Category.VISUAL, 0) {});

        // №22 ShulkerView
        modules.add(new Module("ShulkerView", Category.VISUAL, 0) {});

        // ==========================
        //         MISC
        // ==========================

        // №15 FreeLook
        modules.add(new Module("FreeLook", Category.MISC, GLFW.GLFW_KEY_LEFT_ALT) {});

        // №1 ItemScroller
        modules.add(new Module("ItemScroller", Category.MISC, 0) {});

        // №3 Waypoints
        modules.add(new Module("Waypoints", Category.MISC, 0) {});

        // №10 Friends
        modules.add(new Module("Friends", Category.MISC, 0) {});

        // №21 Macros
        modules.add(new Module("Macros", Category.MISC, 0) {});

        // №4, 5 Optimization
        modules.add(new Module("Optimization", Category.MISC, 0) {});

        // ==========================
        //         HUD
        // ==========================

        // №6, 7 ArmorHUD
        modules.add(new Module("ArmorHUD", Category.HUD, 0) {
            @Override
            public void onRenderHud(DrawContext context) {
                int y = context.getScaledWindowHeight() - 110;
                for (int i = 3; i >= 0; i--) {
                    ItemStack stack = mc.player.getInventory().getArmorStack(i);
                    if (!stack.isEmpty()) {
                        context.drawItem(stack, 10, y);
                        int max = stack.getMaxDamage();
                        int cur = max - stack.getDamage();
                        int pc = (max > 0) ? (cur * 100 / max) : 100;
                        int color = pc <= 25 ? 0xFFFF5555 : (pc <= 50 ? 0xFFFFAA00 : 0xFFFFFFFF);
                        context.drawText(mc.textRenderer, pc + "%", 30, y + 5, color, true);
                        y += 20;
                    }
                }
            }
        });

        // №19 InfoHUD
        modules.add(new Module("InfoHUD", Category.HUD, 0) {
            @Override
            public void onRenderHud(DrawContext context) {
                String fps = "FPS: " + mc.getCurrentFps();
                context.drawText(mc.textRenderer, fps, 10, 10, 0xFF00FBFF, true);
            }
        });
    }

    public static List<Module> getModules() { return modules; }
    public static Module getModule(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }
}
