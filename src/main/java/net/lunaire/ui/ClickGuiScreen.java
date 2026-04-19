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
        int x = 30;
        for (Category cat : Category.values()) {
            int y = 30;
            context.fill(x, y, x + 110, y + 15, 0xFF00FBFF);
            context.drawText(textRenderer, cat.name(), x + 5, y + 4, 0x0, false);
            y += 18;

            for (Module m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    int bgColor = m.isEnabled() ? 0xBF00FBFF : 0x90151515;
                    context.fill(x, y, x + 90, y + 14, bgColor);
                    context.drawText(textRenderer, m.getName(), x + 4, y + 3, -1, false);

                    // Кнопка настроек [>]
                    context.fill(x + 92, y, x + 110, y + 14, 0x90303030);
                    context.drawText(textRenderer, ">", x + 98, y + 3, -1, false);

                    // Если настройки открыты, рисуем их сбоку
                    if (m.showSettings) renderSettings(context, m, mouseX, mouseY, x + 115, y);

                    y += 16;
                }
            }
            x += 130;
        }
    }

    private void renderSettings(DrawContext context, Module m, int mx, int my, int x, int y) {
        context.fill(x, y, x + 100, y + (m.settings.size() * 15) + 5, 0xBF101010);
        int sy = y + 5;
        for (Setting s : m.settings) {
            int color = s.bVal ? 0xFF00FBFF : -1;
            context.drawText(textRenderer, s.name + (s.type.equals("num") ? ": " + (int)s.dVal : ""), x + 5, sy, color, false);
            sy += 15;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = 30;
        for (Category cat : Category.values()) {
            int y = 48;
            for (Module m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    // Клик по модулю
                    if (mouseX >= x && mouseX <= x + 90 && mouseY >= y && mouseY <= y + 14) {
                        if (button == 0) m.toggle();
                        return true;
                    }
                    // Клик по кнопке настроек [>]
                    if (mouseX >= x + 92 && mouseX <= x + 110 && mouseY >= y && mouseY <= y + 14) {
                        m.showSettings = !m.showSettings;
                        return true;
                    }
                    // Клик по самим настройкам
                    if (m.showSettings && mouseX >= x + 115 && mouseX <= x + 215) {
                        int sy = y + 5;
                        for (Setting s : m.settings) {
                            if (mouseY >= sy && mouseY <= sy + 12) {
                                if (s.type.equals("bool")) s.bVal = !s.bVal;
                                if (s.type.equals("num")) s.dVal = (s.dVal >= 9) ? 1 : s.dVal + 1;
                                Config.save();
                                return true;
                            }
                            sy += 15;
                        }
                    }
                    y += 16;
                }
            }
            x += 130;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
