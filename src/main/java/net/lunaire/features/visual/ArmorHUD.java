package net.lunaire.features.visual;

import net.lunaire.core.Category;
import net.lunaire.core.LunaireModule;
import net.minecraft.client.gui.DrawContext;

public class ArmorHUD extends LunaireModule {
    public ArmorHUD() { 
        super("ArmorHUD", Category.HUD, 0); 
    }

    @Override
    public void onRenderHud(DrawContext context) {
        // Логика отрисовки уже есть в ModuleManager, но для чистоты оставляем метод здесь
    }
}
