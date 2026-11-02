package com.sixtyseven.client.module;

public abstract class Module {
    public final String name;
    public final String description;
    public boolean enabled;
    public final Category category;

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public void toggle() { enabled = !enabled; onToggle(); }
    public void onToggle() {}
    public void tick() {}

    public enum Category {
        RENDER, COMBAT, MOVEMENT, PLAYER, BASE_FINDING, MISC
    }
}
