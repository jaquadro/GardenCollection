package com.jaquadro.minecraft.gardencore.api.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;

@Cancelable
@Event.HasResult
public class UseTrowelEvent extends PlayerEvent
{
    public final ItemStack current;
    public final World world;
    public final int x;
    public final int y;
    public final int z;

    public UseTrowelEvent (EntityPlayer player, ItemStack current, World world, int x, int y, int z) {
        super(player);
        this.current = current;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
