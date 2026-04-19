package net.lunaire.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    private String name;
    private Category category;
    private boolean enabled = false;
    private int key;
    
    // ВАЖНО: Этого поля не хватало для ClickGUI
    public boolean binding = false; 

    public Module(String name, Category category, int key) {
        this.name = name;
        this.category = category;
        this.key = key;
    }

    public void toggle() {
        this.enabled = !this.enabled;
        if (enabled) onEnable(); else onDisable();
    }

    public void onEnable() {}
    
    // ВАЖНО: Этого метода не хватало для FullBright
    public void onDisable() {} 

    public void onTick() {}
    public void onRenderHud(DrawContext context) {}

    public String getName() { return name; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public int getKey() { return key; }
    public void setKey(int key) { this.key = key; }
}
