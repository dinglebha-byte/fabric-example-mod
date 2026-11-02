package com.sixtyseven.client.module.base;

import com.sixtyseven.client.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SusChunkFinder extends Module {
    public int sensitivity = 5; // 1-10, higher = more flags
    public boolean highlightOnly = false;
    public int scanRadius = 8; // chunks

    private final Map<ChunkPos, Integer> susScores = new ConcurrentHashMap<>();
    private final Map<ChunkPos, Long> lastSeen = new ConcurrentHashMap<>();

    public SusChunkFinder() {
        super("Sus Chunk Finder", "Heuristic new-chunk / base detection via palette and block updates", Category.BASE_FINDING);
    }

    public void onChunkLoad(WorldChunk chunk) {
        if (!enabled) return;
        ChunkPos pos = chunk.getPos();
        int score = 0;

        // Heuristic 1: palette order deviation (simplified new-chunk signal)
        var sections = chunk.getSectionArray();
        for (var section : sections) {
            if (section == null || section.isEmpty()) continue;
            var palette = section.getBlockStateContainer();
            if (palette == null) continue;
            int nonAir = 0;
            for (int i = 0; i < 4096; i++) {
                if (!palette.get(i).isAir()) nonAir++;
            }
            if (nonAir > 0 && nonAir < 50) score += 2; // sparse unnatural blocks
            if (nonAir > 4000) score += 1; // heavy terrain
        }

        // Heuristic 2: block update density (server side edits)
        // Mixin feeds update counts per chunk
        score += ChunkStats.updateCounts.getOrDefault(pos, 0) / 4;

        // Heuristic 3: distance from known loaded areas
        if (lastSeen.containsKey(pos)) {
            long dt = System.currentTimeMillis() - lastSeen.get(pos);
            if (dt < 5000) score += 2; // re-loaded quickly, likely edited
        }

        lastSeen.put(pos, System.currentTimeMillis());
        susScores.put(pos, score);

        // Threshold scaled by sensitivity (1-10)
        int threshold = Math.max(1, 12 - sensitivity);
        if (score >= threshold) {
            // mark for render; HUD shows coords
        }
    }

    public void renderHud(DrawContext ctx) {
        var client = MinecraftClient.getInstance();
        if (client.player == null) return;
        int y = 10;
        ctx.drawText(client.textRenderer, "§c67client §7| Sus Chunks", 10, y, 0xFFFFFF, true);
        y += 12;
        for (var e : susScores.entrySet()) {
            if (e.getValue() >= Math.max(1, 12 - sensitivity)) {
                ctx.drawText(client.textRenderer,
                    "§c" + e.getKey().x + ", " + e.getKey().z + " §7(" + e.getValue() + ")",
                    10, y, 0xFFFFFF, true);
                y += 10;
                if (y > 200) break;
            }
        }
    }

    public int getScore(ChunkPos pos) { return susScores.getOrDefault(pos, 0); }

    // Called from mixin
    public static class ChunkStats {
        public static final Map<ChunkPos, Integer> updateCounts = new ConcurrentHashMap<>();
        public static void onBlockUpdate(ChunkPos pos) {
            updateCounts.merge(pos, 1, Integer::sum);
        }
    }
}
