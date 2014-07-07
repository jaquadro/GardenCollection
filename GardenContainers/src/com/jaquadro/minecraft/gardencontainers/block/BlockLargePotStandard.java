package com.jaquadro.minecraft.gardencontainers.block;

import com.jaquadro.minecraft.gardencontainers.GardenContainers;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class BlockLargePotStandard extends BlockLargePot
{
    public static final String[] subTypes = new String[] { "default", "raw" };

    @SideOnly(Side.CLIENT)
    private IIcon iconSide;

    public BlockLargePotStandard (String blockName) {
        super(blockName);
    }

    @Override
    public String[] getSubTypes () {
        return subTypes;
    }

    @Override
    protected void applySubstrate (World world, int x, int y, int z, TileEntityGarden tileEntity, EntityPlayer player) {
        if (world.getBlockMetadata(x, y, z) == 1) {
            world.setBlockToAir(x, y, z);
            world.playSoundAtEntity(player, "dig.sand", 1.0f, 1.0f);

            for (int i = 0; i < 4; i++)
                dropBlockAsItem(world, x, y, z, new ItemStack(Items.clay_ball));

            return;
        }

        super.applySubstrate(world, x, y, z, tileEntity, player);
    }

    @Override
    public void getSubBlocks (Item item, CreativeTabs creativeTabs, List blockList) {
        blockList.add(new ItemStack(item, 1, 0));
        blockList.add(new ItemStack(item, 1, 1));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon (int side, int data) {
        switch (data) {
            case 1:
                return Blocks.clay.getIcon(side, 0);
            default:
                return iconSide;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister iconRegister) {
        iconSide = iconRegister.registerIcon(GardenContainers.MOD_ID + ":large_pot");

        super.registerBlockIcons(iconRegister);
    }
}
