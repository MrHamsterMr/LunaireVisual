package net.lunaire.mixin;

import net.lunaire.core.Module;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    // Используем более общий метод отрисовки оверлеев, чтобы NoRender работал 100%
    @Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderOverlay(DrawContext context, Identifier texture, float opacity, CallbackInfo ci) {
        Module m = ModuleManager.getModule("NoRender");
        if (m != null && m.isEnabled()) {
            // Если текстура содержит "fire" (огонь) или "portal" (портал), отменяем отрисовку
            if (texture.getPath().contains("fire") || texture.getPath().contains("portal")) {
                ci.cancel();
            }
        }
    }
    
    // Дополнительно для LowFire (опускаем огонь вниз)
    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderFire(DrawContext context, CallbackInfo ci) {
        Module m = ModuleManager.getModule("NoRender");
        if (m != null && m.isEnabled()) {
            ci.cancel();
        }
    }
}
