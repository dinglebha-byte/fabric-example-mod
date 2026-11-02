package com.sixtyseven.client.module.base;

import com.sixtyseven.client.module.Module;
import net.minecraft.client.MinecraftClient;

public class Disabler extends Module {
    public Disabler() {
        super("Disabler", "Delays transaction packets to desync Grim/Vulcan movement checks", Category.MISC);
    }

    public boolean shouldDelay() { return enabled; }
}
