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
        context.fill(0, 0, width, height, 0x80000000);
        int x = 50;
        for (Category cat : Category.values()) {
            int y = 50;
            context.fill(x, y - 16, x + 95, y, 0xFF00FBFF);
            context.drawCenteredTextWithShadow(textRenderer, cat.name(), x + 47, y - 12, -1);
            for (Module m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    int color = m.isEnabled() ? 0xFF00FBFF : 0xFFFFFFFF;
                    if (m.binding) color = 0xFFFFAA00;
                    context.fill(x, y, x + 95, y + 14, 0x90101010);
                    context.drawText(textRenderer, m.getName(), x + 5, y + 3, color, false);
                    y += 16;
                }
            }
            x += 105;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = 50;
        for (Category cat : Category.values()) {
            int y = 50;
            for (Module m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    if (mouseX >= x && mouseX <= x + 95 && mouseY >= y && mouseY <= y + 14) {
                        if (button == 0) m.toggle();
                        if (button == 1) m.binding = !m.binding;
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
                if (keyCode == GLFW.GLFW_KEY_ESCAPE) m.setKey(0);
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
