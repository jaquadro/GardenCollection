package com.jaquadro.minecraft.gardencontainers.core.handlers;

import com.jaquadro.minecraft.gardencontainers.block.tile.TileEntityPotteryTable;
import com.jaquadro.minecraft.gardencontainers.client.gui.GuiPotteryTable;
import com.jaquadro.minecraft.gardencontainers.inventory.ContainerPotteryTable;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler
{
    public static int potteryTableGuiID = 0;

    @Override
    public Object getServerGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityPotteryTable)
            return new ContainerPotteryTable(player.inventory, (TileEntityPotteryTable) tileEntity);

        return null;
    }

    @Override
    public Object getClientGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityPotteryTable)
            return new GuiPotteryTable(player.inventory, (TileEntityPotteryTable) tileEntity);

        return null;
    }
}