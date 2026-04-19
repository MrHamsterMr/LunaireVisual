package net.lunaire.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    private String name;
    private Category category;
    private boolean enabled = false;
    private int key;

    // Конструктор с ТРЕМЯ аргументами
    public Module(String name, Category category, int key) {
        this.name = name;
        this.category = category;
        this.key = key;
    }

    public String getName() { return name; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public void toggle() { this.enabled = !this.enabled; }
    public int getKey() { return key; }
    public void setKey(int key) { this.key = key; }

    public void onTick() {}
    public void onRenderHud(DrawContext context) {}
}
