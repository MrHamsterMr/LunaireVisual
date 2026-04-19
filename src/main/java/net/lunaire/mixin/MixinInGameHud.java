package net.lunaire.mixin;

import net.lunaire.core.Module;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    // 1.21.4 требует RenderTickCounter (tickCounter)
    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderLunaire(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        for (Module m : ModuleManager.getModules()) {
            if (m != null && m.isEnabled()) {
                m.onRenderHud(context);
            }
        }
    }

    // Убираем огонь (используем более точный дескриптор для 1.21.4)
    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderFire(DrawContext context, CallbackInfo ci) {
        Module m = ModuleManager.getModule("NoRender");
        if (m != null && m.isEnabled()) {
            ci.cancel();
        }
    }
}
