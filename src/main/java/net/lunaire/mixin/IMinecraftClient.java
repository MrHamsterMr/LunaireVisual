package net.lunaire.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface IMinecraftClient {
    // Этот метод позволяет нам изменять скрытую переменную itemUseCooldown
    @Accessor("itemUseCooldown")
    void setItemUseCooldown(int cooldown);

    @Accessor("itemUseCooldown")
    int getItemUseCooldown();
}
