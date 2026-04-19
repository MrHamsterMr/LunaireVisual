package net.lunaire.features.visual;
import net.lunaire.core.Category;
import net.lunaire.core.Module;
import net.minecraft.client.gui.DrawContext;

public class ArmorHUD extends Module {
    public ArmorHUD() { super("ArmorHUD", Category.HUD, 0); }
    @Override
    public void onRenderHud(DrawContext context) {
        // Твой код отрисовки брони
    }
}
