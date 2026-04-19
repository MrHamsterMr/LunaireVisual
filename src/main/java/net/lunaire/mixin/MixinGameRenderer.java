package net.lunaire.mixin;

import net.lunaire.core.Module;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void onGetFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Float> info) {
        Module m = ModuleManager.getModule("Zoom");
        if (m != null && m.isEnabled()) {
            // В твоем Legacy лаунчере FOV — это Float (согласно твоему логу)
            float originalFov = info.getReturnValue();
            info.setReturnValue(originalFov / 4.0f);
        }
    }
}
