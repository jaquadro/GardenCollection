package com.jaquadro.minecraft.gardencore.core.handlers;

import com.jaquadro.minecraft.gardencore.block.BlockGardenProxy;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class ForgeEventHandler
{
    @SubscribeEvent
    public void applyBonemeal (BonemealEvent event) {
        if (event.block instanceof BlockGardenProxy) {
            BlockGardenProxy proxy = (BlockGardenProxy) event.block;
            if (proxy.applyBonemeal(event.world, event.x, event.y, event.z))
                event.setResult(Event.Result.ALLOW);
        }
    }
}
