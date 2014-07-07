package com.jaquadro.minecraft.gardencore.block;

import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.api.GardenCoreAPI;
import com.jaquadro.minecraft.gardencore.core.ClientProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSmallFire extends Block
{
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public BlockSmallFire () {
        super(Material.fire);

        setHardness(0);
        setLightLevel(1);
        setBlockName("smallFire");
        setBlockTextureName("fire");
        disableStats();
        //setTickRandomly(true);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool (World world, int x, int y, int z) {
        return null;
    }

    @Override
    public boolean isOpaqueCube () {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock () {
        return false;
    }

    @Override
    public int getRenderType () {
        return ClientProxy.smallFireRenderID;
    }

    @Override
    public int quantityDropped (Random random) {
        return 0;
    }

    //@Override
    //public int tickRate (World world) {
    //    return 30;
    //}

    @Override
    public boolean func_149698_L () {
        return false;
    }

    @Override
    public boolean isCollidable () {
        return false;
    }

    private static boolean blockCanHostSmallFlame (World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        return GardenCoreAPI.instance().blockCanHostSmallFlame(block, metadata);
    }

    @Override
    public boolean canPlaceBlockAt (World world, int x, int y, int z) {
        return blockCanHostSmallFlame(world, x, y - 1, z);
    }

    @Override
    public void onNeighborBlockChange (World world, int x, int y, int z, Block block) {
        if (!blockCanHostSmallFlame(world, x, y - 1, z))
            world.setBlockToAir(x, y, z);
    }

    @Override
    public void onBlockAdded (World world, int x, int y, int z) {
        if (!blockCanHostSmallFlame(world, x, y - 1, z))
            world.setBlockToAir(x, y, z);
        else
            world.scheduleBlockUpdate(x, y, z, this, tickRate(world) + world.rand.nextInt(10));
    }

    @Override
    public void onEntityCollidedWithBlock (World world, int x, int y, int z, Entity entity) {
        entity.setFire(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick (World world, int x, int y, int z, Random random) {
        if (random.nextInt(64) == 0)
            world.playSound(x + .5f, y + .5f, z + .5f, "fire.fire", .3f + random.nextFloat() * .5f, random.nextFloat() * .5f + .2f, false);

        for (int i = 0; i < 3; i++) {
            float spawnX = x + random.nextFloat();
            float spawnY = y + random.nextFloat() * .5f + .2f;
            float spawnZ = z + random.nextFloat();
            world.spawnParticle("smoke", spawnX, spawnY, spawnZ, 0, 0, 0);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon (int side, int meta) {
        return Blocks.fire.getIcon(side, meta);
    }

    @Override
    public MapColor getMapColor (int meta) {
        return MapColor.tntColor;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons (IIconRegister register) {
        icons = new IIcon[] {
            register.registerIcon(GardenCore.MOD_ID + ":" + getTextureName() + "_layer_0"),
            register.registerIcon(GardenCore.MOD_ID + ":" + getTextureName() + "_layer_1"),
        };
    }

    @SideOnly(Side.CLIENT)
    public IIcon getFireIcon (int layer) {
        return icons[layer];
    }
}
