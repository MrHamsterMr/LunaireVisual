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
        context.fill(0, 0, width, height, 0x50000000); // Мягкое затемнение

        int x = 40;
        for (Category cat : Category.values()) {
            int y = 40;
            // Стильная шапка категории
            context.fill(x - 2, y - 2, x + 92, y + 15, 0xFF00FBFF);
            context.drawCenteredTextWithShadow(textRenderer, cat.name(), x + 45, y + 3, 0xFF000000);
            y += 20;

            for (Module m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    boolean hovered = mouseX >= x && mouseX <= x + 90 && mouseY >= y && mouseY <= y + 14;
                    int bgColor = m.isEnabled() ? 0xBF00FBFF : (hovered ? 0x90303030 : 0x90151515);
                    if (m.binding) bgColor = 0xFFFFAA00;

                    context.fill(x, y, x + 90, y + 14, bgColor);
                    context.drawText(textRenderer, m.getName(), x + 5, y + 3, m.isEnabled() ? 0xFF000000 : -1, false);
                    y += 16;
                }
            }
            x += 105;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = 40;
        for (Category cat : Category.values()) {
            int y = 60;
            for (Module m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    if (mouseX >= x && mouseX <= x + 90 && mouseY >= y && mouseY <= y + 14) {
                        if (m.binding) {
                            m.setKey(button, true); // Бинд на кнопку мыши!
                            m.binding = false;
                            Config.save();
                        } else {
                            if (button == 0) m.toggle();
                            if (button == 1) m.binding = true;
                        }
                        return true;
                    }
                    y += 16;
                }
            }
            x += 105;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Module m : ModuleManager.getModules()) {
            if (m.binding) {
                m.setKey(keyCode, false);
                m.binding = false;
                Config.save();
                return true;
            }
        }
        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) { this.close(); return true; }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
