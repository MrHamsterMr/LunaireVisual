package net.lunaire.features.combat;

import net.lunaire.core.Category;
import net.lunaire.core.Module;
import net.minecraft.item.Items;

public class FastExp extends Module {
    public FastExp() { super("FastExp", Category.COMBAT); }

    @Override
    public void onTick() {
        if (mc.options.useKey.isPressed() && mc.player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
            // Логика обнуления задержки (через Mixin)
        }
    }
}
