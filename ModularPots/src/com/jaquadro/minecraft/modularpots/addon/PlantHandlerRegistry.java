package com.jaquadro.minecraft.modularpots.addon;

import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PlantHandlerRegistry
{
    private static final List<IPlantHandler> handlers = new ArrayList<IPlantHandler>();

    public static void init () {
        if (!register(new ModPlantMegaPackHandler()))
            register(new ModPlantMegaPackLegacyHandler());
    }

    public static boolean register (IPlantHandler handler) {
        if (handler.init()) {
            handlers.add(handler);
            return true;
        }

        return false;
    }

    public static boolean applyBonemeal (World world, int x, int y, int z) {
        for (IPlantHandler handler : handlers) {
            if (handler.applyBonemeal(world, x, y, z))
                return true;
        }

        return false;
    }
}
