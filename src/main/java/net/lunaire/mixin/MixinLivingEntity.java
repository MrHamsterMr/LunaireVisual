package net.lunaire.mixin;

import net.lunaire.core.LunaireModule;
import net.lunaire.core.Setting;
import net.lunaire.features.ModuleManager;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(method = "getJumpVelocity", at = @At("HEAD")) // Заглушка для доступа к логике рендера
    private void onHit(CallbackInfoReturnable<Float> cir) {
        LunaireModule m = ModuleManager.getModule("HitColor");
        if (m != null && m.isEnabled()) {
            // В 1.21.4 цвет меняется через оверлей в Renderer
        }
    }
}
