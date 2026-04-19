package net.lunaire.ui;

import net.lunaire.core.Category;
import net.lunaire.core.Module;
import net.lunaire.core.ModuleManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ClickGuiScreen extends Screen {
    public ClickGuiScreen() { super(Text.of("Lunaire GUI")); }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x60000000); // Размытие фона

        int xOffset = 50;
        for (Category cat : Category.values()) {
            int yOffset = 50;
            // Шапка категории
            context.fill(xOffset, yOffset - 18, xOffset + 95, yOffset, 0xFF00FBFF);
            context.drawCenteredTextWithShadow(textRenderer, cat.name(), xOffset + 47, yOffset - 13, -1);

            for (Module m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    // Стиль модуля
                    int bgColor = m.isEnabled() ? 0x9000FBFF : 0x90101010;
                    if (m.binding) bgColor = 0xFFFFAA00; // Желтый при бинде

                    context.fill(xOffset, yOffset, xOffset + 95, yOffset + 16, bgColor);
                    context.drawText(textRenderer, m.getName(), xOffset + 5, yOffset + 4, -1, false);
                    
                    // Отображение текущего бинда
                    String keyName = m.getKey() == 0 ? "NONE" : GLFW.glfwGetKeyName(m.getKey(), 0);
                    if (keyName == null) keyName = "KEY " + m.getKey();
                    context.drawText(textRenderer, keyName, xOffset + 70, yOffset + 4, 0xAAAAAA, false);

                    yOffset += 18;
                }
            }
            xOffset += 110;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int xOffset = 50;
        for (Category cat : Category.values()) {
            int yOffset = 50;
            for (Module m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    if (mouseX >= xOffset && mouseX <= xOffset + 95 && mouseY >= yOffset && mouseY <= yOffset + 16) {
                        if (button == 0) m.toggle(); // Левая кнопка - вкл/выкл
                        if (button == 1) m.binding = !m.binding; // Правая кнопка - режим бинда
                        return true;
                    }
                    yOffset += 18;
                }
            }
            xOffset += 110;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Module m : ModuleManager.getModules()) {
            if (m.binding) {
                if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_BACKSPACE) m.setKey(0);
                else m.setKey(keyCode);
                m.binding = false;
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() { return false; }
}
