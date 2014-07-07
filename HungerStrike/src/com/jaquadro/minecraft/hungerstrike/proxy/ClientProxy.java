package com.jaquadro.minecraft.hungerstrike.proxy;

import com.jaquadro.minecraft.hungerstrike.proxy.CommonProxy;
import com.jaquadro.minecraft.hungerstrike.HungerStrike;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public void renderGameOverlay (RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.FOOD) {
            if (!HungerStrike.config.isHungerBarHidden())
                return;

            switch (HungerStrike.config.getMode()) {
                case NONE:
                    break;
                case ALL:
                    event.setCanceled(true);
                    break;
                case LIST:
                    if (playerHandler.isOnHungerStrike(Minecraft.getMinecraft().thePlayer))
                        event.setCanceled(true);
                    break;
            }
        }
    }
}
