package com.sixtyseven.mixin;

import com.sixtyseven.client.module.ModuleManager;
import com.sixtyseven.client.module.base.SusChunkFinder;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.MinecraftClient;

@Mixin(ClientPlayNetworkHandler.class)
public class ChunkPacketMixin {
    @Inject(method = "onChunkData", at = @At("TAIL"))
    private void onChunkData(ChunkDataS2CPacket packet, CallbackInfo ci) {
        var client = MinecraftClient.getInstance();
        if (client.world == null) return;
        WorldChunk chunk = client.world.getChunk(packet.getX(), packet.getZ());
        if (chunk == null) return;
        if (ModuleManager.susFinder != null) ModuleManager.susFinder.onChunkLoad(chunk);
        if (ModuleManager.stashFinder != null) ModuleManager.stashFinder.onChunkLoad(chunk);
    }
}
