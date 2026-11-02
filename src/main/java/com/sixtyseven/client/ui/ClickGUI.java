package com.sixtyseven.client.ui;

import com.sixtyseven.client.module.Module;
import com.sixtyseven.client.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class ClickGUI extends Screen {
    private Module.Category selected = Module.Category.BASE_FINDING;
    private int scroll = 0;

    public ClickGUI() { super(Text.literal("67client")); }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        int w = this.width, h = this.height;
        // Red translucent backdrop
        ctx.fill(0, 0, w, h, 0x88000000);

        // Sidebar
        int sw = 160;
        ctx.fill(0, 0, sw, h, 0xCC1A0000);
        ctx.drawText(textRenderer, "§c67client", 20, 20, 0xFFFFFF, true);
        int y = 60;
        for (Module.Category cat : Module.Category.values()) {
            boolean sel = cat == selected;
            ctx.fill(0, y - 4, sw, y + 14, sel ? 0xFFAA0000 : 0x00000000);
            ctx.drawText(textRenderer, (sel ? "§f" : "§7") + cat.name().replace("_", " "),
                        20, y, 0xFFFFFF, false);
            y += 24;
        }

        // Module list
        int lx = sw + 20;
        int ly = 40;
        ctx.drawText(textRenderer, "§c" + selected.name().replace("_", " "), lx, ly, 0xFFFFFF, true);
        ly += 30;
        int startY = ly;
        for (Module m : ModuleManager.modules) {
            if (m.category != selected) continue;
            if (ly - startY > h - 100) break;
            boolean hover = mouseX > lx && mouseX < w - 40 && mouseY > ly && mouseY < ly + 40;
            ctx.fill(lx, ly, w - 40, ly + 40, hover ? 0x55AA0000 : 0x33220000);
            ctx.drawText(textRenderer, (m.enabled ? "§c" : "§7") + m.name, lx + 10, ly + 6, 0xFFFFFF, true);
            ctx.drawText(textRenderer, "§8" + m.description, lx + 10, ly + 20, 0xAAAAAA, false);
            // toggle pill
            int px = w - 80, py = ly + 10;
            ctx.fill(px, py, px + 40, py + 20, m.enabled ? 0xFFCC0000 : 0xFF333333);
            ctx.fill(px + (m.enabled ? 22 : 2), py + 2, px + (m.enabled ? 38 : 18), py + 18, 0xFFFFFFFF);
            ly += 48;
        }
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        int sw = 160, lx = sw + 20;
        int y = 60;
        for (Module.Category cat : Module.Category.values()) {
            if (mx > 0 && mx < sw && my > y - 4 && my < y + 14) {
                selected = cat; return true;
            }
            y += 24;
        }
        int ly = 70;
        for (Module m : ModuleManager.modules) {
            if (m.category != selected) continue;
            if (mx > lx && mx < this.width - 40 && my > ly && my < ly + 40) {
                m.toggle(); return true;
            }
            ly += 48;
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override public boolean shouldPause() { return false; }
    @Override public boolean isPauseScreen() { return false; }
}
