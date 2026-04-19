package net.lunaire.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    private String name;
    private Category category;
    private boolean enabled = false;

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public String getName() { return name; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public void toggle() { this.enabled = !this.enabled; }

    public void onTick() {}
    public void onRenderHud(DrawContext context) {}
}
