package com.jaquadro.minecraft.gardentrees.item;

import com.jaquadro.minecraft.gardentrees.block.BlockThinLogFence;
import com.jaquadro.minecraft.gardentrees.block.tile.TileEntityWoodProxy;
import com.jaquadro.minecraft.gardentrees.core.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemThinLogFence extends ItemBlock
{
    public ItemThinLogFence (Block block) {
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
        if (meta < 0 || meta >= BlockThinLogFence.subNames.length)
            return super.getUnlocalizedName();

        return super.getUnlocalizedName() + "." + BlockThinLogFence.subNames[meta];
    }

    @Override
    public String getItemStackDisplayName (ItemStack itemStack) {
        int meta = itemStack.getItemDamage();
        if (meta < 16)
            return super.getItemStackDisplayName(itemStack);

        Block block = TileEntityWoodProxy.getBlockFromComposedMetadata(meta);
        Item item = Item.getItemFromBlock(block);
        if (item == null)
            return super.getItemStackDisplayName(itemStack);

        String unlocName = item.getUnlocalizedName(new ItemStack(item, 1, TileEntityWoodProxy.getMetaFromComposedMetadata(meta)));

        return ("" + StatCollector.translateToLocal(unlocName + ".name") + " " + StatCollector.translateToLocal(getUnlocalizedName() + ".name")).trim();
    }

    @Override
    public IIcon getIconFromDamage (int meta) {
        return ModBlocks.thinLogFence.getIcon(0, meta);
    }

    @Override
    public int getColorFromItemStack (ItemStack itemStack, int meta) {
        return ModBlocks.thinLogFence.getRenderColor(itemStack.getItemDamage());
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
