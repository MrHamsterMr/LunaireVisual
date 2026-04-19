package net.lunaire.mixin;

import net.lunaire.core.LunaireModule;
import net.lunaire.core.Setting;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    // Фикс Зума через системное имя метода getFov
    @Inject(method = "method_3196", at = @At("RETURN"), cancellable = true)
    private void onGetFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Float> info) {
        LunaireModule m = ModuleManager.getModule("Zoom");
        if (m != null && m.isEnabled()) {
            float fov = info.getReturnValue();
            info.setReturnValue(fov / 4.0f);
        }
    }

    // Фикс Тряски (NoShake) через системное имя метода bobViewWhenHurt
    @Inject(method = "method_3190", at = @At("HEAD"), cancellable = true)
    private void onHurtTilt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        LunaireModule m = ModuleManager.getModule("NoRender");
        if (m != null && m.isEnabled()) {
            Setting s = m.getSetting("NoShake");
            if (s != null && s.bVal) {
                ci.cancel();
            }
        }
    }
}
