package com.jaquadro.minecraft.gardentrees.block;

import com.jaquadro.minecraft.gardentrees.GardenTrees;
import com.jaquadro.minecraft.gardentrees.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardentrees.core.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IShearable;

import java.util.ArrayList;

public class BlockCandelilla extends BlockCrops implements IShearable
{
    @SideOnly(Side.CLIENT)
    IIcon[] icons;

    private boolean shearScratch;

    public BlockCandelilla (String blockName) {
        setBlockTextureName(GardenTrees.MOD_ID + ":candelilla");
        setBlockName(blockName);
        setHardness(0);
        setStepSound(BlockBush.soundTypeGrass);
        setCreativeTab(ModCreativeTabs.tabGardenTrees);
    }

    @Override
    public int getRenderType () {
        return 6;
    }

    @Override
    public boolean isShearable (ItemStack item, IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> onSheared (ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
        shearScratch = true;
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z)));
        return ret;
    }

    @Override
    public boolean canHarvestBlock (EntityPlayer player, int meta) {
        if (shearScratch) {
            shearScratch = false;
            return false;
        }

        return super.canHarvestBlock(player, meta);
    }

    @Override
    public IIcon getIcon (int side, int meta) {
        switch (meta) {
            case 0:
                return icons[0];
            case 1:
                return icons[1];
            case 2:
                return icons[2];
            case 3:
            case 4:
                return icons[3];
            case 5:
            case 6:
                return icons[4];
            case 7:
            default:
                return icons[5];
        }
    }

    @Override
    protected Item func_149866_i()
    {
        return ModItems.candelilla_seeds;
    }

    @Override
    protected Item func_149865_P()
    {
        return ModItems.candelilla;
    }

    @Override
    public EnumPlantType getPlantType (IBlockAccess world, int x, int y, int z) {
        return EnumPlantType.Crop;
    }

    @Override
    public boolean canBlockStay (World world, int x, int y, int z) {
        if (super.canBlockStay(world, x, y, z))
            return true;

        return canPlaceBlockOn(world.getBlock(x, y - 1, z));
    }

    @Override
    protected boolean canPlaceBlockOn (Block block) {
        return block == Blocks.grass || block == Blocks.dirt || block == Blocks.farmland || block == Blocks.sand;
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor () {
        return ColorizerGrass.getGrassColor(0.5, 1.0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor (int meta) {
        return ColorizerGrass.getGrassColor(0.5, 1.0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier (IBlockAccess blockAccess, int x, int y, int z) {
        return blockAccess.getBiomeGenForCoords(x, z).getBiomeGrassColor(x, y, z);
    }*/

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons (IIconRegister register) {
        icons = new IIcon[6];

        for (int i = 0; i < icons.length; i++)
            icons[i] = register.registerIcon(getTextureName() + "_stage_" + i);
    }
}
