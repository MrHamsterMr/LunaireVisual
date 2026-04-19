package net.lunaire.mixin;

import net.lunaire.core.LunaireModule;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    // Внутреннее имя метода getFov для 1.21.4
    @Inject(method = "method_3196", at = @At("RETURN"), cancellable = true)
    private void onGetFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Float> info) {
        LunaireModule m = ModuleManager.getModule("Zoom");
        if (m != null && m.isEnabled()) {
            float fov = info.getReturnValue();
            info.setReturnValue(fov / 4.0f);
        }
    }
}
