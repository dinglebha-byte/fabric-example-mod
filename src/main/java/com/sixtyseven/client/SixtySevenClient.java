package com.sixtyseven.client;

import com.sixtyseven.client.module.*;
import com.sixtyseven.client.ui.ClickGUI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class SixtySevenClient implements ClientModInitializer {
    public static final String MOD_ID = "67client";
    public static ClickGUI gui;
    public static KeyBinding openGui;

    @Override
    public void onInitializeClient() {
        gui = new ClickGUI();
        openGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.67client.gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "category.67client"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGui.wasPressed()) {
                client.setScreen(gui);
            }
            ModuleManager.tick();
        });

        // HUD: suspend chunk finder overlay + watermark
        HudElementRegistry.attachElementBefore(
            VanillaHudElements.CHAT,
            Identifier.of(MOD_ID, "sus_hud"),
            (context, tickCounter) -> {
                if (ModuleManager.susFinder.enabled) {
                    ModuleManager.susFinder.renderHud(context);
                }
            }
        );

        ModuleManager.init();
    }
}
