package net.lunaire.ui;

import net.lunaire.core.Module;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ClickGuiScreen extends Screen {
    public ClickGuiScreen() { super(Text.of("Lunaire GUI")); }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x70000000); // Размытие фона

        int x = 50;
        for (Module.Category cat : Module.Category.values()) {
            int y = 50;
            // Заголовок категории
            context.fill(x, y - 15, x + 90, y, 0xFF00FBFF);
            context.drawCenteredTextWithShadow(textRenderer, cat.name(), x + 45, y - 12, -1);

            for (Module m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    int color = m.isEnabled() ? 0xFF00FBFF : 0xFFFFFFFF;
                    context.fill(x, y, x + 90, y + 14, 0x90101010);
                    context.drawText(textRenderer, m.getName(), x + 5, y + 3, color, false);
                    y += 16;
                }
            }
            x += 100;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = 50;
        for (Module.Category cat : Module.Category.values()) {
            int y = 50;
            for (Module m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    if (mouseX >= x && mouseX <= x + 90 && mouseY >= y && mouseY <= y + 14) {
                        m.toggle();
                        return true;
                    }
                    y += 16;
                }
            }
            x += 100;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
