package net.lunaire.mixin;

import net.lunaire.core.Module;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    // 1. Убираем/опускаем огонь на экране (Часть №20 NoRender)
    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderFire(DrawContext context, CallbackInfo ci) {
        if (isModuleEnabled("NoRender")) {
            // Если включен NoRender, мы просто отменяем отрисовку огня
            ci.cancel();
        } else if (isModuleEnabled("LowFire")) {
            // Если включен LowFire, мы опускаем его пониже
            context.getMatrices().push();
            context.getMatrices().translate(0, -0.3f, 0); // Опускаем на 30% вниз
            // Отрисовка продолжится, но со смещением
            context.getMatrices().pop();
        }
    }

    // 2. Убираем эффект тошноты от портала (№20 NoRender)
    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderPortal(DrawContext context, float nauseaStrength, CallbackInfo ci) {
        if (isModuleEnabled("NoRender")) {
            ci.cancel();
        }
    }

    // 3. Убираем рамку подзорной трубы (Помогает для №9 Zoom)
    @Inject(method = "renderSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderSpyglass(DrawContext context, float scale, CallbackInfo ci) {
        if (isModuleEnabled("Zoom") || isModuleEnabled("NoRender")) {
            ci.cancel();
        }
