package net.lunaire.ui;

import net.lunaire.core.Module;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ClickGuiScreen extends Screen {
    public ClickGuiScreen() { super(Text.of("Lunaire Menu")); }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x90000000);
        int y = 50;
        for (Module m : ModuleManager.getModules()) {
            int color = m.isEnabled() ? 0x00FBFF : 0xFFFFFF;
            context.drawText(textRenderer, m.getName(), 50, y, color, true);
            y += 15;
        }
        super.render(context, mouseX, mouseY, delta);
    }
}
