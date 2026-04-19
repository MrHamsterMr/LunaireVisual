package net.lunaire.features.visual;

import net.lunaire.core.Category;
import net.lunaire.core.Module;

public class Zoom extends Module {
    public Zoom() { super("Zoom", Category.VISUAL); }
    // Логика зума работает через MixinGameRenderer, проверяя этот модуль
}
