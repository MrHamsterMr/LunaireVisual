package net.lunaire.ui;

import net.lunaire.core.Category;
import net.lunaire.core.Module;
import net.lunaire.core.ModuleManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ClickGuiScreen extends Screen {
    public ClickGuiScreen() { super(Text.of("Lunaire GUI")); }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x80000000); 

        int x = 40;
        for (Category cat : Category.values()) {
            int y = 40;
            context.drawText(textRenderer, "§b" + cat.name(), x, y - 15, -1, true);

            for (Module m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    int color = m.isEnabled() ? 0x00FBFF : 0xFFFFFF;
                    context.fill(x, y, x + 80, y + 14, 0x40FFFFFF);
                    context.drawText(textRenderer, m.getName(), x + 5, y + 3, color, false);
                    y += 16;
                }
            }
            x += 100;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = 40;
        for (Category cat : Category.values()) {
            int y = 40;
            for (Module m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    if (mouseX >= x && mouseX <= x + 80 && mouseY >= y && mouseY <= y + 14) {
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
