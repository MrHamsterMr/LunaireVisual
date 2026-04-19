package net.lunaire.mixin;

import net.lunaire.core.Module;
import net.lunaire.core.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderFire(DrawContext context, CallbackInfo ci) {
        Module m = ModuleManager.getModule("NoRender");
        if (m != null && m.isEnabled()) {
            ci.cancel();
        }
    }
}
