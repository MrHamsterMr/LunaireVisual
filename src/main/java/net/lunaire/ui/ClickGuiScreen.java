package net.lunaire.ui;

import net.lunaire.core.*;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ClickGuiScreen extends Screen {
    public ClickGuiScreen() { super(Text.of("Lunaire GUI")); }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x60000000);
        int x = 40;
        for (Category cat : Category.values()) {
            int y = 40;
            context.fill(x, y, x + 110, y + 14, 0xFF00FBFF);
            context.drawText(textRenderer, cat.name(), x + 5, y + 3, 0x0, false);
            y += 18;

            for (LunaireModule m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    int bgColor = m.isEnabled() ? 0xBF00FBFF : 0x90151515;
                    if (m.binding) bgColor = 0xFFFFAA00;
                    context.fill(x, y, x + 90, y + 14, bgColor);
                    context.drawText(textRenderer, m.name, x + 5, y + 3, -1, false);

                    context.fill(x + 92, y, x + 110, y + 14, 0x90303030);
                    context.drawText(textRenderer, ">", x + 98, y + 3, -1, false);

                    if (m.showSettings) renderSettings(context, m, x + 115, y);
                    y += 16;
                }
            }
            x += 130;
        }
    }

    private void renderSettings(DrawContext context, LunaireModule m, int x, int y) {
        context.fill(x, y, x + 100, y + (m.settings.size() * 15) + 5, 0xBF101010);
        int sy = y + 5;
        for (Setting s : m.settings) {
            int color = s.bVal ? 0xFF00FBFF : -1;
            context.drawText(textRenderer, s.name, x + 5, sy, color, false);
            sy += 15;
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
            x += 100;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (LunaireModule m : ModuleManager.getModules()) {
            if (m.binding) { m.setKey(keyCode, false); m.binding = false; Config.save(); return true; }
        }
        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) { this.close(); return true; }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
