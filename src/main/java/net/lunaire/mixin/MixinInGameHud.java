package net.lunaire.mixin;

import net.lunaire.core.LunaireModule;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderLunaire(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        for (LunaireModule m : ModuleManager.getModules()) {
            if (m.isEnabled()) {
                m.onRenderHud(context);
            }
        }
    }
}
