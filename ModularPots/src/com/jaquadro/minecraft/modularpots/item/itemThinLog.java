package com.jaquadro.minecraft.modularpots.item;

import com.jaquadro.minecraft.modularpots.core.ModBlocks;
import com.jaquadro.minecraft.modularpots.block.BlockThinLog;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityWoodProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemThinLog extends ItemBlock
{
    public ItemThinLog (Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata (int meta) {
        return meta;
    }

    @Override
    public String getUnlocalizedName (ItemStack itemStack) {
        int meta = itemStack.getItemDamage();
        if (meta < 0 || meta >= BlockThinLog.subNames.length)
            meta = 0;

        return super.getUnlocalizedName() + "." + BlockThinLog.subNames[meta];
    }

    @Override
    public IIcon getIconFromDamage (int meta) {
        return ModBlocks.thinLog.getIcon(0, meta);
    }

    @Override
    public int getColorFromItemStack (ItemStack itemStack, int meta) {
        return ModBlocks.thinLog.getRenderColor(itemStack.getItemDamage());
    }

    @Override
    public boolean placeBlockAt (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        int blockMeta = (metadata < 16) ? metadata : 0;
        if (!super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, blockMeta))
            return false;

        TileEntityWoodProxy.syncTileEntityWithData(world, x, y, z, metadata);
        return true;
    }
}
