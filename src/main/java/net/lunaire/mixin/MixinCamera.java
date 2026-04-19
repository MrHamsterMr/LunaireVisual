package net.lunaire.mixin;

import net.lunaire.core.Module;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class MixinCamera {
    @Shadow private float yaw;
    @Shadow private float pitch;

    private float freeYaw, freePitch;

    @Inject(method = "update", at = @At("RETURN"))
    private void onUpdate(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        Module freeLook = ModuleManager.getModule("FreeLook");
        if (freeLook != null && freeLook.isEnabled()) {
            // Камера застывает или вращается отдельно (базовая логика)
            this.setRotation(this.yaw, this.pitch); 
        }
    }

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);
}
