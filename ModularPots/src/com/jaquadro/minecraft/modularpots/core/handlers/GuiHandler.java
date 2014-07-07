package com.jaquadro.minecraft.modularpots.core.handlers;

import com.jaquadro.minecraft.modularpots.client.gui.inventory.GuiGardenLayout;
import com.jaquadro.minecraft.modularpots.client.gui.inventory.GuiPotteryTable;
import com.jaquadro.minecraft.modularpots.inventory.ContainerGarden;
import com.jaquadro.minecraft.modularpots.inventory.ContainerPotteryTable;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityGarden;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityPotteryTable;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityPotteryTable)
            return new ContainerPotteryTable(player.inventory, (TileEntityPotteryTable) tileEntity);
        if (tileEntity instanceof TileEntityGarden)
            return new ContainerGarden(player.inventory, (TileEntityGarden) tileEntity);

        return null;
    }

    @Override
    public Object getClientGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityPotteryTable)
            return new GuiPotteryTable(player.inventory, (TileEntityPotteryTable) tileEntity);
        if (tileEntity instanceof TileEntityGarden)
            return new GuiGardenLayout(player.inventory, (TileEntityGarden) tileEntity);

        return null;
    }
}
