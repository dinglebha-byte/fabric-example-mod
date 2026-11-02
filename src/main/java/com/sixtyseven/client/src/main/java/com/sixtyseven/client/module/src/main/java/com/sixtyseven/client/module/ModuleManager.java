package com.sixtyseven.client.module;

import com.sixtyseven.client.module.base.*;
import com.sixtyseven.client.module.render.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static final List<Module> modules = new ArrayList<>();
    public static SusChunkFinder susFinder;
    public static StashFinder stashFinder;
    public static BaseFinder baseFinder;
    public static Disabler disabler;

    public static void init() {
        susFinder = new SusChunkFinder();
        stashFinder = new StashFinder();
        baseFinder = new BaseFinder();
        disabler = new Disabler();
        modules.add(susFinder);
        modules.add(stashFinder);
        modules.add(baseFinder);
        modules.add(disabler);
    }

    public static void tick() {
        for (Module m : modules) if (m.enabled) m.tick();
    }
}
