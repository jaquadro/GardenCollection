package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardencore.api.block.IChain;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardencore.util.RenderHelper;
import com.jaquadro.minecraft.gardencore.util.RenderHelperState;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityCandelabra;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

public class BlockCandelabra extends BlockContainer
{
    @SideOnly(Side.CLIENT)
    private IIcon iconArm;
    @SideOnly(Side.CLIENT)
    private IIcon iconArmExt;
    @SideOnly(Side.CLIENT)
    private IIcon iconPlate;
    @SideOnly(Side.CLIENT)
    private IIcon iconCandle;
    @SideOnly(Side.CLIENT)
    private IIcon iconBase;
    @SideOnly(Side.CLIENT)
    private IIcon iconHang;
    @SideOnly(Side.CLIENT)
    private IIcon iconCandleSide;
    @SideOnly(Side.CLIENT)
    private IIcon iconCandleTop;
    @SideOnly(Side.CLIENT)
    private IIcon iconHolderSide;

    public BlockCandelabra (String blockName) {
        super(Material.iron);

        setBlockName(blockName);
        setTickRandomly(true);
        setHardness(2.5f);
        setResistance(5);
        setLightLevel(0);
        setBlockTextureName(GardenStuff.MOD_ID + ":candelabra");
        setCreativeTab(ModCreativeTabs.tabGardenCore);
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
        return ClientProxy.sconceRenderID;
    }

    @Override
    public boolean shouldSideBeRendered (IBlockAccess world, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public void setBlockBoundsBasedOnState (IBlockAccess world, int x, int y, int z) {
        TileEntityCandelabra tile = getTileEntity(world, x, y, z);

        float yMin = 0;
        float yMax = 1;
        float xMin = 0;
        float xMax = 1;
        float zMin = 0;
        float zMax = 1;

        float depth = (tile.getLevel() == 2) ? .5f : .390625f;
        float hwidth = (tile.getLevel() == 0) ? .125f : .390625f;

        if (tile.isSconce()) {
            yMin = .0625f;
            yMax = .875f;
            switch (tile.getDirection()) {
                case 2:
                    zMax = depth;
                    xMin = .5f - hwidth;
                    xMax = .5f + hwidth;
                    break;
                case 3:
                    zMin = 1 - depth;
                    xMin = .5f - hwidth;
                    xMax = .5f + hwidth;
                    break;
                case 4:
                    xMax = depth;
                    zMin = .5f - hwidth;
                    zMax = .5f + hwidth;
                    break;
                case 5:
                    xMin = 1 - depth;
                    zMin = .5f - hwidth;
                    zMax = .5f + hwidth;
                    break;
            }
        }
        else {
            yMax = .0625f * 15f;
            xMin = zMin = .0625f * 5.75f;
            xMax = zMax = .0625f * 10.25f;

            switch (tile.getLevel()) {
                case 0:
                    yMax = .0625f * 15f;
                    break;
                case 1:
                    if (tile.getDirection() == 2 || tile.getDirection() == 3) {
                        xMin = .0625f * .5f;
                        xMax = .0625f * 15.5f;
                    }
                    else {
                        zMin = .0625f * .5f;
                        zMax = .0625f * 15.5f;
                    }
                    break;
                case 2:
                    xMin = zMin = .0625f * .5f;
                    xMax = zMax = .0625f * 15.5f;
                    break;
            }
        }

        setBlockBounds(xMin, yMin, zMin, xMax, yMax, zMax);
    }

    @Override
    public void addCollisionBoxesToList (World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
        setBlockBoundsBasedOnState(world, x, y, z);
        setBlockBounds((float)minX, (float)minY, (float)minZ, (float)maxX, (float)maxY - (.0625f * 1.5f), (float)maxZ);
        super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
    }

    @Override
    public void onBlockPlacedBy (World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        TileEntityCandelabra te = (TileEntityCandelabra) world.getTileEntity(x, y, z);
        if (te == null || te.isDirectionInitialized())
            return;

        int quadrant = MathHelper.floor_double((entity.rotationYaw * 4f / 360f) + .5) & 3;
        switch (quadrant) {
            case 0:
                te.setDirection(3);
                break;
            case 1:
                te.setDirection(4);
                break;
            case 2:
                te.setDirection(2);
                break;
            case 3:
                te.setDirection(5);
                break;
        }

        if (world.isRemote) {
            te.invalidate();
            world.markBlockForUpdate(x, y, z);
        }
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        TileEntityCandelabra tile = getTileEntity(world, x, y, z);
        if (tile == null)
            return;

        double[] c = new double[3];
        int level = tile.getLevel();
        int dir = tile.getDirection();

        float flameDepth = 0.96875f;

        if (tile.isSconce()) {
            if (level == 0) {
                RenderHelper.instance.state.transformCoord(.5f, flameDepth, .25f, c, RenderHelperState.ROTATION_BY_FACE_FACE[2][dir]);
                renderParticleAt(world, x + c[0], y + c[1], z + c[2]);
            }
            if (level == 1 || level == 2) {
                RenderHelper.instance.state.transformCoord(.25f, flameDepth, .25f, c, RenderHelperState.ROTATION_BY_FACE_FACE[2][dir]);
                renderParticleAt(world, x + c[0], y + c[1], z + c[2]);
                RenderHelper.instance.state.transformCoord(.75f, flameDepth, .25f, c, RenderHelperState.ROTATION_BY_FACE_FACE[2][dir]);
                renderParticleAt(world, x + c[0], y + c[1], z + c[2]);
            }
            if (level == 2) {
                RenderHelper.instance.state.transformCoord(.5f, flameDepth, .375f, c, RenderHelperState.ROTATION_BY_FACE_FACE[2][dir]);
                renderParticleAt(world, x + c[0], y + c[1], z + c[2]);
            }
        }
        else {
            Block blockUpper = world.getBlock(x, y + 1, z);
            boolean hanging = level > 0 && (blockUpper instanceof IChain || blockUpper.isSideSolid(world, x, y + 1, z, ForgeDirection.DOWN));

            if (level >= 0 && !hanging) {
                RenderHelper.instance.state.transformCoord(.5f, flameDepth + .0625f, .5f, c, RenderHelperState.ROTATION_BY_FACE_FACE[2][dir]);
                renderParticleAt(world, x + c[0], y + c[1], z + c[2]);
            }
            if (level >= 1) {
                RenderHelper.instance.state.transformCoord(.15625f, flameDepth, .5f, c, RenderHelperState.ROTATION_BY_FACE_FACE[2][dir]);
                renderParticleAt(world, x + c[0], y + c[1], z + c[2]);
                RenderHelper.instance.state.transformCoord(.84375, flameDepth, .5f, c, RenderHelperState.ROTATION_BY_FACE_FACE[2][dir]);
                renderParticleAt(world, x + c[0], y + c[1], z + c[2]);
            }
            if (level >= 2) {
                RenderHelper.instance.state.transformCoord(.5f, flameDepth, .15625f, c, RenderHelperState.ROTATION_BY_FACE_FACE[2][dir]);
                renderParticleAt(world, x + c[0], y + c[1], z + c[2]);
                RenderHelper.instance.state.transformCoord(.5f, flameDepth, .84375, c, RenderHelperState.ROTATION_BY_FACE_FACE[2][dir]);
                renderParticleAt(world, x + c[0], y + c[1], z + c[2]);
            }
        }
    }

    private void renderParticleAt (World world, double x, double y, double z) {
        //world.spawnParticle("smoke", x, y, z, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("flame", x, y, z, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public int getLightValue (IBlockAccess world, int x, int y, int z) {
        TileEntityCandelabra tile = getTileEntity(world, x, y, z);
        if (tile == null)
            return 0;

        switch (tile.getLevel()) {
            case 0:
                return 13;
            case 1:
                return 14;
            case 2:
                return 15;
        }

        return 0;
    }

    @Override
    public void getSubBlocks (Item item, CreativeTabs creativeTabs, List blockList) {
        for (int i = 0; i < 3; i++)
            blockList.add(new ItemStack(item, 1, i));
    }

    @Override
    public TileEntity createNewTileEntity (World p_149915_1_, int p_149915_2_) {
        return new TileEntityCandelabra();
    }

    public TileEntityCandelabra getTileEntity (IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return (te != null && te instanceof TileEntityCandelabra) ? (TileEntityCandelabra) te : null;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconArm () {
        return iconArm;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconArmExt () {
        return iconArmExt;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconCandle () {
        return iconCandle;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconBase () {
        return iconBase;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconHang () {
        return iconHang;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconCandleSide () {
        return iconCandleSide;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconCandleTop () {
        return iconCandleTop;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconHolderSide () {
        return iconHolderSide;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister register) {
        iconCandle = register.registerIcon(getTextureName() + "_candle");
        iconArm = register.registerIcon(getTextureName() + "_arm");
        iconArmExt = register.registerIcon(getTextureName() + "_arm_ext");
        iconBase = register.registerIcon(getTextureName() + "_base");
        iconHang = register.registerIcon(getTextureName() + "_hang");
        iconCandleSide = register.registerIcon(getTextureName() + "_candle_side");
        iconCandleTop = register.registerIcon(getTextureName() + "_candle_top");
        iconHolderSide = register.registerIcon(getTextureName() + "_holder_side");
    }
}
