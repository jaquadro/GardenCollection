package com.jaquadro.minecraft.gardenstuff.item;

import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityCandelabra;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemCandelabra extends ItemBlock
{
    public ItemCandelabra (Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata (int meta) {
        return meta;
    }

    @Override
    public boolean placeBlockAt (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (!super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata))
            return false;

        TileEntityCandelabra tile = (TileEntityCandelabra) world.getTileEntity(x, y, z);
        if (tile != null) {
            if (side != 0 && side != 1) {
                tile.setDirection((side % 2 == 0) ? side + 1 : side - 1);
                tile.setSconce(true);
            }

            tile.setLevel(MathHelper.clamp_int(metadata & 0x3, 0, 2));
        }

        return true;
    }
}
