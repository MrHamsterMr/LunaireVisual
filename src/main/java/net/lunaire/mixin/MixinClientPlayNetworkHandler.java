package net.lunaire.mixin;

import net.lunaire.core.Module;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @Inject(method = "onEntityStatus", at = @At("HEAD"))
    private void onEntityStatus(EntityStatusS2CPacket packet, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null || mc.player == null) return;

        // Проверяем включен ли модуль в меню
        boolean isEnabled = false;
        for(Module m : ModuleManager.getModules()) {
            if(m.getName().equals("TotemPop") && m.isEnabled()) isEnabled = true;
        }

        if (isEnabled && packet.getStatus() == 35) { // 35 = использование тотема
            Entity entity = packet.getEntity(mc.world);
            if (entity instanceof PlayerEntity player) {
                // Проверка на зачарование (блеск)
                boolean enchanted = player.getMainHandStack().hasGlint() || player.getOffHandStack().hasGlint();
                String rarity = enchanted ? "§6Зачарованный" : "§7Обычный";
                
                mc.player.sendMessage(Text.of("§b[Lunaire] §e" + player.getName().getString() + " §fпотерял " + rarity + " §fтотем!"), false);
            }
        }
    }
}
