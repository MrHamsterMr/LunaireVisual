package net.lunaire.features.visual;

import net.lunaire.core.Category;
import net.lunaire.core.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

public class ArmorHUD extends Module {
    public ArmorHUD() {
        super("ArmorHUD", Category.HUD);
    }

    @Override
    public void onRenderHud(DrawContext context) {
        int y = 50;
        for (int i = 3; i >= 0; i--) {
            ItemStack stack = mc.player.getInventory().getArmorStack(i);
            if (!stack.isEmpty()) {
                context.drawItem(stack, 10, y);
                // Рисуем прочность
                int durability = stack.getMaxDamage() - stack.getDamage();
                context.drawText(mc.textRenderer, String.valueOf(durability), 30, y + 5, -1, true);
                y += 20;
            }
        }
    }
}
