package net.lunaire.mixin;

import net.lunaire.core.LunaireModule;
import net.lunaire.features.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.text.Text;
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

        if (packet.getStatus() == 35) { // 35 = Totem Pop
            LunaireModule m = ModuleManager.getModule("TotemPop");
            if (m != null && m.isEnabled()) {
                Entity entity = packet.getEntity(mc.world);
                if (entity instanceof PlayerEntity player) {
                    mc.player.sendMessage(Text.of("§b[Lunaire] §e" + player.getName().getString() + " §fпотерял тотем!"), false);
                }
            }
        }
    }
}
