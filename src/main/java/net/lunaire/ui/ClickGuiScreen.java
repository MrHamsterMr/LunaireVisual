package net.lunaire.ui;

import net.lunaire.core.Category;
import net.lunaire.core.Module;
import net.lunaire.features.ModuleManager;
import net.lunaire.core.Config;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ClickGuiScreen extends Screen {
    public ClickGuiScreen() { super(Text.of("Lunaire GUI")); }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x60000000); // Размытие фона
        int x = 40;
        for (Category cat : Category.values()) {
            int y = 40;
            context.fill(x, y, x + 90, y + 14, 0xFF00FBFF);
            context.drawText(textRenderer, cat.name(), x + 5, y + 3, 0xFF000000, false);
            y += 18;
            for (Module m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    int bgColor = m.isEnabled() ? 0x9000FBFF : 0x90202020;
                    if (m.binding) bgColor = 0xFFFFAA00;
                    context.fill(x, y, x + 90, y + 14, bgColor);
                    context.drawText(textRenderer, m.getName(), x + 5, y + 3, -1, false);
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
            int y = 58;
            for (Module m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    if (mouseX >= x && mouseX <= x + 90 && mouseY >= y && mouseY <= y + 14) {
                        if (m.binding) {
                            m.setKey(button, true);
                            m.binding = false;
                        } else {
                            if (button == 0) m.toggle();
                            if (button == 1) m.binding = true;
                        }
                        return true;
                    }
                    y += 16;
                }
            }
            x += 100;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Module m : ModuleManager.getModules()) {
            if (m.binding) {
                if (keyCode == GLFW.GLFW_KEY_ESCAPE) m.setKey(0, false);
                else m.setKey(keyCode, false);
                m.binding = false;
                Config.save();
                return true;
            }
        }
        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) { this.close(); return true; }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
