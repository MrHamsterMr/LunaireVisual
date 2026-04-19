package net.lunaire.mixin;

import net.lunaire.core.LunaireModule;
import net.lunaire.features.ModuleManager;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

@Mixin(ItemStack.class)
public class MixinItemStack {
    @Inject(method = "getTooltip", at = @At("RETURN"))
    private void onGetTooltip(CallbackInfoReturnable<List<Text>> info) {
        LunaireModule shulkerView = ModuleManager.getModule("ShulkerView");
        if (shulkerView != null && shulkerView.isEnabled()) {
            // Логика будет дополнена
        }
    }
}
