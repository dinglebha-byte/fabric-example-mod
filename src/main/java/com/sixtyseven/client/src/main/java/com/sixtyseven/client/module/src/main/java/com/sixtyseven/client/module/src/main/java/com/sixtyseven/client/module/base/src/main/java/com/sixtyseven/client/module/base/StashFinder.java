package com.sixtyseven.client.module.base;

import com.sixtyseven.client.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.block.entity.*;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import java.util.*;

public class StashFinder extends Module {
    private final Map<ChunkPos, Integer> chestCounts = new HashMap<>();

    public StashFinder() {
        super("Stash Finder", "Counts chests/barrels per chunk, prints coords above threshold", Category.BASE_FINDING);
    }

    public void onChunkLoad(WorldChunk chunk) {
        if (!enabled) return;
        int count = 0;
        for (BlockEntity be : chunk.getBlockEntities().values()) {
            if (be instanceof ChestBlockEntity || be instanceof BarrelBlockEntity
                || be instanceof ShulkerBoxBlockEntity) count++;
        }
        if (count >= 3) {
            chestCounts.put(chunk.getPos(), count);
        }
    }

    public void renderHud(net.minecraft.client.gui.DrawContext ctx) {
        var client = MinecraftClient.getInstance();
        int y = 100;
        ctx.drawText(client.textRenderer, "§cStashes", 10, y, 0xFFFFFF, true);
        y += 12;
        for (var e : chestCounts.entrySet()) {
            ctx.drawText(client.textRenderer,
                "§c" + e.getKey().x + ", " + e.getKey().z + " §7x" + e.getValue(),
                10, y, 0xFFFFFF, true);
            y += 10;
            if (y > 300) break;
        }
    }
}
