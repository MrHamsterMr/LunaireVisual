package net.lunaire.mixin;
import net.lunaire.core.LunaireModule;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class MixinCamera {
    @Shadow private float yaw;
    @Shadow private float pitch;
    @Inject(method = "update", at = @At("RETURN"))
    private void onUpdate(net.minecraft.world.BlockView area, net.minecraft.entity.Entity entity, boolean tp, boolean inv, float tick, CallbackInfo ci) {
        LunaireModule m = ModuleManager.getModule("FreeLook");
        if (m != null && m.enabled) this.setRotation(this.yaw, this.pitch);
    }
    @Shadow protected abstract void setRotation(float yaw, float pitch);
}
