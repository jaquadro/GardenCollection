package com.jaquadro.minecraft.extrabuttons.item;

import com.jaquadro.minecraft.extrabuttons.ExtraButtons;
import com.jaquadro.minecraft.extrabuttons.block.DelayButton;
import com.jaquadro.minecraft.extrabuttons.tileentity.TileEntityDelayButton;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static net.minecraftforge.common.util.ForgeDirection.*;

public class ItemDelayButton extends ItemBlock
{
    public ItemDelayButton (Block block)
    {
        super(block);
        setUnlocalizedName("delayButton");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage (int data)
    {
        DelayButton block = (DelayButton) ExtraButtons.delayButton;
        return block.getBlockTextureFromSide(0);
    }

    @Override
    public int getMetadata (int damageValue)
    {
        return damageValue;
    }

    @Override
    public boolean placeBlockAt (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        if (!super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata))
            return false;

        ForgeDirection dir = ForgeDirection.getOrientation(side);

        TileEntityDelayButton te = (TileEntityDelayButton) world.getTileEntity(x, y, z);

        if (te != null) {
            if (dir == NORTH && world.isSideSolid(x, y, z + 1, NORTH))
                te.setDirection(4);
            else if (dir == SOUTH && world.isSideSolid(x, y, z - 1, SOUTH))
                te.setDirection(3);
            else if (dir == WEST && world.isSideSolid(x + 1, y, z, WEST))
                te.setDirection(2);
            else if (dir == EAST && world.isSideSolid(x - 1, y, z, EAST))
                te.setDirection(1);

            te.setDelay(3);
        }

        return true;
    }
}
