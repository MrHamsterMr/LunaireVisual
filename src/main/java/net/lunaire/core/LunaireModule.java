package net.lunaire.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import java.util.ArrayList;
import java.util.List;

public abstract class LunaireModule {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    public String name;
    public Category category;
    public boolean enabled = false;
    public int key;
    public boolean isMouse = false;
    public boolean binding = false;
    public boolean showSettings = false;
    public List<Setting> settings = new ArrayList<>();

    public LunaireModule(String name, Category category, int key) {
        this.name = name;
        this.category = category;
        this.key = key;
    }

    public void addSetting(Setting s) { settings.add(s); }
    public Setting getSetting(String name) {
        for (Setting s : settings) if (s.name.equalsIgnoreCase(name)) return s;
        return null;
    }

    public void toggle() {
        this.enabled = !this.enabled;
        if (enabled) onEnable(); else onDisable();
        Config.save();
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
    public void onRenderHud(DrawContext context) {}
}
