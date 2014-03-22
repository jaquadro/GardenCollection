package com.jaquadro.minecraft.modularpots.item;

import com.jaquadro.minecraft.modularpots.ModBlocks;
import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.block.BlockLargePot;
import com.jaquadro.minecraft.modularpots.config.PatternConfig;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityLargePot;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemLargePot extends ItemMultiTexture
{
    public ItemLargePot (Block block) {
        super(block, block, getSubTypes(block));
    }

    private static String[] getSubTypes (Block block) {
        if (block == ModBlocks.largePot)
            return BlockLargePot.subTypes;

        return new String[0];
    }

    @Override
    public String getUnlocalizedName (ItemStack itemStack) {
        return super.getUnlocalizedName() + "." + BlockLargePot.subTypes[itemStack.getItemDamage() % 2];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation (ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        PatternConfig pattern = ModularPots.config.getPattern((itemStack.getItemDamage() >> 8) & 255);
        if (pattern != null && pattern.getName() != null)
            list.add(pattern.getName());
    }

    @Override
    public boolean placeBlockAt (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        if (!super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata & 15))
            return false;

        TileEntityLargePot largePot = (TileEntityLargePot) world.getTileEntity(x, y, z);
        if (largePot != null)
            largePot.setCarving((metadata >> 8) & 255);

        return true;
    }
}
