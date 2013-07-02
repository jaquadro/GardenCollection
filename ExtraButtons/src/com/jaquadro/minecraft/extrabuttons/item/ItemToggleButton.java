package com.jaquadro.minecraft.extrabuttons.item;

import com.jaquadro.minecraft.extrabuttons.ExtraButtons;
import com.jaquadro.minecraft.extrabuttons.block.ToggleButton;
import com.jaquadro.minecraft.extrabuttons.tileentity.TileEntityButton;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import static net.minecraftforge.common.ForgeDirection.*;

public class ItemToggleButton extends ItemBlock
{
    public ItemToggleButton (int id)
    {
        super(id);
        setHasSubtypes(true);
        setItemName("illuminatedButton");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getIconFromDamage (int data)
    {
        ToggleButton block = (ToggleButton) ExtraButtons.illuminatedButton;
        return block.getBlockTextureFromIndex(ToggleButton.getBlockFromDye(data));
    }

    @Override
    public int getMetadata (int damageValue)
    {
        return damageValue;
    }

    @Override
    public String getItemNameIS (ItemStack itemStack)
    {
        return getItemName() + "." + ItemDye.dyeColorNames[ToggleButton.getBlockFromDye(itemStack.getItemDamage())];
    }

    @Override
    public boolean placeBlockAt (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        if (!super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata))
            return false;

        ForgeDirection dir = ForgeDirection.getOrientation(side);

        TileEntityButton te = (TileEntityButton) world.getBlockTileEntity(x, y, z);

        if (te != null) {
            if (dir == NORTH && world.isBlockSolidOnSide(x, y, z + 1, NORTH))
                te.setDirection(4);
            else if (dir == SOUTH && world.isBlockSolidOnSide(x, y, z - 1, SOUTH))
                te.setDirection(3);
            else if (dir == WEST && world.isBlockSolidOnSide(x + 1, y, z, WEST))
                te.setDirection(2);
            else if (dir == EAST && world.isBlockSolidOnSide(x - 1, y, z, EAST))
                te.setDirection(1);
        }

        return true;
    }
}
