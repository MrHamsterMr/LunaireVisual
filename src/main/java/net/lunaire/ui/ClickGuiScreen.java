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
            // Шапка категории
            context.fill(x - 2, y - 2, x + 112, y + 15, 0xFF00FBFF);
            context.drawCenteredTextWithShadow(textRenderer, cat.name(), x + 55, y + 3, 0x000000);
            y += 20;

            for (LunaireModule m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    // Кнопка модуля
                    int bgColor = m.isEnabled() ? 0xBF00FBFF : 0x90151515;
                    if (m.binding) bgColor = 0xFFFFAA00;
                    
                    context.fill(x, y, x + 90, y + 14, bgColor);
                    context.drawText(textRenderer, m.name, x + 5, y + 3, m.isEnabled() ? 0 : -1, false);

                    // Кнопка настройки [>]
                    context.fill(x + 92, y, x + 110, y + 14, 0x90303030);
                    context.drawText(textRenderer, ">", x + 98, y + 3, -1, false);

                    // Панель настроек справа от модуля
                    if (m.showSettings) {
                        int sy = y;
                        context.fill(x + 115, sy, x + 215, sy + (m.settings.size() * 15) + 5, 0xDF101010);
                        for (Setting s : m.settings) {
                            int sColor = s.bVal ? 0xFF00FBFF : -1;
                            String val = s.type.equals("num") ? ": " + (int)s.dVal : "";
                            context.drawText(textRenderer, s.name + val, x + 120, sy + 5, sColor, false);
                            sy += 15;
                        }
                    }
                    y += 16;
                }
            }
            x += 135; // Сдвиг для следующей колонки
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = 40;
        for (Category cat : Category.values()) {
            int y = 60;
            for (LunaireModule m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    // Включение модуля
                    if (mouseX >= x && mouseX <= x + 90 && mouseY >= y && mouseY <= y + 14) {
                        if (m.binding) { m.setKey(button, true); m.binding = false; Config.save(); }
                        else { if (button == 0) m.toggle(); if (button == 1) m.binding = true; }
                        return true;
                    }
                    // Открытие настроек [>]
                    if (mouseX >= x + 92 && mouseX <= x + 110 && mouseY >= y && mouseY <= y + 14) {
                        m.showSettings = !m.showSettings;
                        return true;
                    }
                    // Клик по параметрам внутри настроек
                    if (m.showSettings && mouseX >= x + 115 && mouseX <= x + 215) {
                        int sy = y + 5;
                        for (Setting s : m.settings) {
                            if (mouseY >= sy && mouseY <= sy + 12) {
                                if (s.type.equals("bool")) s.bVal = !s.bVal;
                                if (s.type.equals("num")) { s.dVal = (s.dVal >= 9) ? 1 : s.dVal + 1; }
                                Config.save();
                                return true;
                            }
                            sy += 15;
                        }
                    }
                    y += 16;
                }
            }
            x += 135;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (LunaireModule m : ModuleManager.getModules()) {
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
