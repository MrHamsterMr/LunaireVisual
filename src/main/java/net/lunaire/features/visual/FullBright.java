package net.lunaire.features.visual;

import net.lunaire.core.Category;
import net.lunaire.core.Module;

public class FullBright extends Module {
    public FullBright() {
        super("FullBright", Category.VISUAL, 0);
    }

    @Override
    public void onTick() {
        mc.options.getGamma().setValue(100.0);
    }

    @Override
    public void onDisable() {
        mc.options.getGamma().setValue(1.0);
    }
}
