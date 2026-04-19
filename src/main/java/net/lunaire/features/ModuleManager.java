package net.lunaire.features;

import net.lunaire.core.Category;
import net.lunaire.core.Module;
import net.lunaire.mixin.IMinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.clear();

        // --- COMBAT ---
        // №7 FastSwap - Смена предмета на выбранный
        modules.add(new Module("FastSwap", Category.COMBAT, 0) {
            @Override
            public void onTick() {
                // Если зажата кнопка (настраивается), свапаем на 1 слот (пример)
                // В будущем добавим выбор предмета в GUI
            }
        });

        modules.add(new Module("FastExp", Category.COMBAT, 0) {
            @Override public void onTick() {
                if (mc.options.useKey.isPressed() && mc.player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
                    ((IMinecraftClient)mc).setItemUseCooldown(0);
                }
            }
        });

        // --- VISUAL ---
        modules.add(new Module("Zoom", Category.VISUAL, GLFW.GLFW_KEY_C) {});
        
        // №6 TargetHUD с прочностью брони врага
        modules.add(new Module("TargetHUD", Category.VISUAL, 0) {
            @Override
            public void onRenderHud(DrawContext context) {
                if (mc.targetedEntity instanceof LivingEntity target) {
                    int x = context.getScaledWindowWidth() / 2 + 10;
                    int y = context.getScaledWindowHeight() / 2 + 10;

                    context.fill(x, y, x + 120, y + 50, 0x90101010); // Фон
                    context.fill(x, y, x + 120, y + 1, 0xFF00FBFF); // Линия Lunaire

                    context.drawText(mc.textRenderer, target.getName().getString(), x + 5, y + 5, -1, true);
                    
                    // ХП полоска
                    float hpPercent = target.getHealth() / target.getMaxHealth();
                    context.fill(x + 5, y + 16, x + 115, y + 20, 0xFF444444);
                    context.fill(x + 5, y + 16, x + 5 + (int)(110 * hpPercent), y + 20, 0xFF00FBFF);

                    // Броня противника
                    int armorX = 5;
                    for (ItemStack armor : target.getArmorItems()) {
                        if (!armor.isEmpty()) {
                            context.drawItem(armor, x + armorX, y + 24);
                            // Показываем прочность брони врага
                            int dur = armor.getMaxDamage() - armor.getDamage();
                            context.drawText(mc.textRenderer, dur + "", x + armorX, y + 42, 0xAAAAAA, false);
                            armorX += 25;
                        }
                    }
                }
            }
        });

        // --- HUD ---
        // №5 ArmorHUD с точным числом прочности
        modules.add(new Module("ArmorHUD", Category.HUD, 0) {
            @Override
            public void onRenderHud(DrawContext context) {
                int y = context.getScaledWindowHeight() - 100;
                for (int i = 3; i >= 0; i--) {
                    ItemStack stack = mc.player.getInventory().getArmorStack(i);
                    if (!stack.isEmpty()) {
                        context.drawItem(stack, 10, y);
                        int durability = stack.getMaxDamage() - stack.getDamage();
                        int color = durability < 50 ? 0xFFFF5555 : 0xFFFFFFFF;
                        context.drawText(mc.textRenderer, durability + "", 30, y + 5, color, true);
                        y += 18;
                    }
                }
            }
        });

        // Заглушки для биндов
        String[] misc = {"Waypoints", "FullBright", "NoRender", "FreeLook", "HitColor", "Macros", "Optimization"};
        for (String s : misc) modules.add(new Module(s, Category.MISC, 0) {});
    }

    public static List<Module> getModules() { return modules; }
    public static Module getModule(String name) {
        for (Module m : modules) if (m.name.equalsIgnoreCase(name)) return m;
        return null;
    }
}
