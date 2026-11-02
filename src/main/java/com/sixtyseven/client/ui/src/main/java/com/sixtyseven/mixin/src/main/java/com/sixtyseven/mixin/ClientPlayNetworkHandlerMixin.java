package com.sixtyseven.mixin;

import com.sixtyseven.client.module.ModuleManager;
import com.sixtyseven.client.module.base.SusChunkFinder;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onBlockUpdate", at = @At("TAIL"))
    private void onBlockUpdate(BlockUpdateS2CPacket packet, CallbackInfo ci) {
        ChunkPos pos = new ChunkPos(packet.getPos());
        SusChunkFinder.ChunkStats.onBlockUpdate(pos);
    }
}
