package net.lunaire.ui;

import net.lunaire.core.*;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

public class ClickGuiScreen extends Screen {
    public ClickGuiScreen() { super(net.minecraft.text.Text.of("Lunaire GUI")); }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x70000000);
        int x = 40;
        for (Category cat : Category.values()) {
            int y = 40;
            context.fill(x, y, x + 110, y + 15, 0xFF00FBFF);
            context.drawText(textRenderer, cat.name(), x + 5, y + 4, 0, false);
            y += 18;

            for (LunaireModule m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    int bgColor = m.isEnabled() ? 0xBF00FBFF : 0x90151515;
                    context.fill(x, y, x + 90, y + 14, bgColor);
                    context.drawText(textRenderer, m.name, x + 4, y + 3, -1, false);

                    context.fill(x + 92, y, x + 110, y + 14, 0x90303030);
                    context.drawText(textRenderer, ">", x + 98, y + 3, -1, false);

                    if (m.showSettings) {
                        int sy = y;
                        context.fill(x + 115, sy, x + 215, sy + (m.settings.size() * 15) + 5, 0xDF101010);
                        for (Setting s : m.settings) {
                            context.drawText(textRenderer, s.name, x + 120, sy + 5, s.bVal ? 0xFF00FBFF : -1, false);
                            sy += 15;
                        }
                    }
                    y += 16;
                }
            }
            x += 135;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = 40;
        for (Category cat : Category.values()) {
            int y = 58;
            for (LunaireModule m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    if (mouseX >= x && mouseX <= x + 90 && mouseY >= y && mouseY <= y + 14) {
                        if (m.binding) { m.setKey(button, true); m.binding = false; }
                        else { if (button == 0) m.toggle(); if (button == 1) m.binding = true; }
                        return true;
                    }
                    if (mouseX >= x + 92 && mouseX <= x + 110 && mouseY >= y && mouseY <= y + 14) {
                        m.showSettings = !m.showSettings;
                        return true;
                    }
                    y += 16;
                }
            }
            x += 135;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
