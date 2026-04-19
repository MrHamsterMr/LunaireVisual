package net.lunaire.mixin;

import net.lunaire.core.Module;
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

    /**
     * Логика Зума (№9)
     * Используем Float, так как Legacy Launcher выдает ClassCastException при использовании Double
     */
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void onGetFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Float> info) {
        Module m = ModuleManager.getModule("Zoom");
        if (m != null && m.isEnabled()) {
            float originalFov = info.getReturnValue();
            // Делим FOV на 4 (приближаем), суффикс 'f' обязателен для float
            info.setReturnValue(originalFov / 4.0f);
        }
    }

    /**
     * Отключение тряски камеры при получении урона (Часть №20 NoRender)
     * Настройка "NoShake" в модуле NoRender
     */
    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void onHurtTilt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        Module m = ModuleManager.getModule("NoRender");
        if (m != null && m.isEnabled()) {
            // Ищем настройку NoShake внутри модуля NoRender
            Setting s = m.getSetting("NoShake");
            if (s != null && s.bVal) {
                // Если галочка стоит - отменяем тряску
                ci.cancel();
            }
        }
    }
}
