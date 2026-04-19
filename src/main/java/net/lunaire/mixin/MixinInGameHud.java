package net.lunaire.mixin;

import net.lunaire.core.Module;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    // Этот метод отрисовывает все текстуры на экране (огонь, портал и т.д.)
    @Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderOverlay(DrawContext context, Identifier texture, float opacity, CallbackInfo ci) {
        Module m = ModuleManager.getModule("NoRender");
        if (m != null && m.isEnabled()) {
            String path = texture.getPath();
            // Если игра пытается нарисовать огонь или портал — отменяем
            if (path.contains("fire") || path.contains("portal")) {
                ci.cancel();
            }
        }
    }
}
