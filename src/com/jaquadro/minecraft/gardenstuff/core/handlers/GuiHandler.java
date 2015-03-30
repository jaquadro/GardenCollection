package com.jaquadro.minecraft.gardenstuff.core.handlers;

import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityBloomeryFurnace;
import com.jaquadro.minecraft.gardenstuff.client.gui.GuiBloomeryFurnace;
import com.jaquadro.minecraft.gardenstuff.container.ContainerBloomeryFurnace;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler
{
    public static int bloomeryFurnaceGuiID = 0;

    @Override
    public Object getServerGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityBloomeryFurnace)
            return new ContainerBloomeryFurnace(player.inventory, (TileEntityBloomeryFurnace)tile);

        return null;
    }

    @Override
    public Object getClientGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityBloomeryFurnace)
            return new GuiBloomeryFurnace(player.inventory, (TileEntityBloomeryFurnace)tile);

        return null;
    }
}
