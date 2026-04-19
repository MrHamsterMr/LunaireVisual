package net.lunaire.mixin;

import net.lunaire.features.ModuleManager;
import net.lunaire.core.Module;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void onGetFov(float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> info) {
        // Ищем модуль Zoom через наш менеджер
        for (Module m : ModuleManager.getModules()) {
            if (m.getName().equals("Zoom") && m.isEnabled()) {
                // Если зум включен, делим текущий FOV на 4 (приближаем)
                info.setReturnValue(info.getReturnValue() / 4.0);
            }
        }
    }
}
