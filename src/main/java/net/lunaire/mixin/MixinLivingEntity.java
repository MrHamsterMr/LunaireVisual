package net.lunaire.mixin;
import net.lunaire.core.LunaireModule;
import net.lunaire.features.ModuleManager;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(method = "getJumpVelocity", at = @At("HEAD"))
    private void onHit(CallbackInfoReturnable<Float> cir) {
        LunaireModule m = ModuleManager.getModule("HitColor");
        if (m != null && m.enabled) { /* База */ }
    }
}
