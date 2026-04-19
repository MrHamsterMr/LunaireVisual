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
        context.fill(0, 0, width, height, 0x70000000);
        int x = 40;
        for (Category cat : Category.values()) {
            int y = 40;
            context.fill(x, y, x + 100, y + 15, 0xFF00FBFF);
            context.drawCenteredTextWithShadow(textRenderer, cat.name(), x + 50, y + 4, -1);
            y += 18;

            for (Module m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    int color = m.isEnabled() ? 0xFF00FBFF : 0xFFFFFFFF;
                    if (m.binding) color = 0xFFFFAA00;
                    context.fill(x, y, x + 100, y + 14, 0x90101010);
                    context.drawText(textRenderer, m.getName(), x + 5, y + 3, color, false);
                    y += 16;
                }
            }
            x += 110;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = 40;
        for (Category cat : Category.values()) {
            int y = 58; 
            for (Module m : ModuleManager.getModules()) {
                if (m.getCategory() == cat) {
                    if (mouseX >= x && mouseX <= x + 100 && mouseY >= y && mouseY <= y + 14) {
                        if (button == 0) m.toggle();
                        if (button == 1) m.binding = !m.binding;
                        return true;
                    }
                    y += 16;
                }
            }
            x += 110;
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
                Config.save();
                return true;
            }
        }
        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) { this.close(); return true; }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
