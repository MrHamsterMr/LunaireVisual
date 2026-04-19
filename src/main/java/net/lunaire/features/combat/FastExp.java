package net.lunaire.features.combat;

import net.lunaire.core.Category;
import net.lunaire.core.Module;
import net.lunaire.mixin.IMinecraftClient; // Импортируем наш миксин
import net.minecraft.item.Items;

public class FastExp extends Module {
    public FastExp() { super("FastExp", Category.COMBAT); }

    @Override
    public void onTick() {
        if (mc.options.useKey.isPressed()) {
            if (mc.player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
                // Магия: превращаем обычный клиент в наш IMinecraftClient и обнуляем КД
                ((IMinecraftClient)mc).setItemUseCooldown(0);
            }
        }
    }
}
