package net.lunaire.mixin;

import net.lunaire.core.LunaireModule;
import net.lunaire.core.Setting;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    /**
     * Отключение тряски камеры при получении урона (NoHurtTilt)
     * Это часть функции NoRender.
     */
    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void onHurtTilt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        // Ищем наш новый класс LunaireModule
        LunaireModule m = ModuleManager.getModule("NoRender");
        
        if (m != null && m.isEnabled()) {
            // Ищем настройку NoShake внутри модуля NoRender
            Setting s = m.getSetting("NoShake");
            if (s != null && s.bVal) {
                // Если галочка включена, отменяем (cancel) тряску камеры
                ci.cancel();
            }
        }
    }
}
