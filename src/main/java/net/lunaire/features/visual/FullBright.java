package net.lunaire.features.visual;

import net.lunaire.core.Category;
import net.lunaire.core.LunaireModule;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;

public class FullBright extends LunaireModule {
    public FullBright() {
        super("FullBright", Category.VISUAL, 0);
    }

    @Override
    public void onTick() {
        if (mc.player != null) {
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1000, 0, false, false));
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }
}
