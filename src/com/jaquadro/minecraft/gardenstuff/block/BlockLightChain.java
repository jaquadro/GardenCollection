package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardenapi.api.GardenAPI;
import com.jaquadro.minecraft.gardenapi.api.connect.IAttachable;
import com.jaquadro.minecraft.gardencore.api.IPlantProxy;
import com.jaquadro.minecraft.gardencore.api.block.IChain;
import com.jaquadro.minecraft.gardencore.api.block.IChainAttachable;
import com.jaquadro.minecraft.gardenapi.api.connect.IChainSingleAttachable;
import com.jaquadro.minecraft.gardencore.block.BlockGarden;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockLightChain extends Block implements IPlantProxy, IChain
{
    public static final String[] types = new String[] { "iron", "gold", "rope", "rust", "wrought_iron", "moss" };

    @SideOnly(Side.CLIENT)
    private static IIcon[] icons;

    private static final Vec3[] defaultAttachPoints = new Vec3[] {
        Vec3.createVectorHelper(.03125, 1, .03125), Vec3.createVectorHelper(.03125, 1, 1 - .03125),
        Vec3.createVectorHelper(1 - .03125, 1, .03125), Vec3.createVectorHelper(1 - .03125, 1, 1 - .03125),
    };
    private static final Vec3[] singleAttachPoint = new Vec3[] {
        Vec3.createVectorHelper(.5, 1, .5),
    };

    public BlockLightChain (String blockName) {
        super(Material.iron);

        setBlockName(blockName);
        setHardness(2.5f);
        setResistance(5f);
        setStepSound(Block.soundTypeMetal);
        setBlockBounds(.5f - .0625f, 0, .5f - .0625f, .5f + .0625f, 1, .5f + .0625f);
        setBlockTextureName(GardenStuff.MOD_ID + ":chain_light");
        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setBlockBounds(0, 0, 0, 1, 1, 1);
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
        return ClientProxy.lightChainRenderID; // Crossed Squares
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool (World world, int x, int y, int z) {
        return null;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool (World world, int x, int y, int z) {
        float minX = 1;
        float minZ = 1;
        float maxX = 0;
        float maxZ = 0;
        for (Vec3 point : getAttachPoints(world, x, y, z)) {
            if (point.xCoord < minX)
                minX = (float) point.xCoord;
            if (point.zCoord < minZ)
                minZ = (float) point.zCoord;
            if (point.xCoord > maxX)
                maxX = (float) point.xCoord;
            if (point.zCoord > maxZ)
                maxZ = (float) point.zCoord;
        }

        if (maxX - minX < .125) {
            minX = .5f - .0625f;
            maxX = .5f + .0625f;
        }
        if (maxZ - minZ < .125) {
            minZ = .5f - .0625f;
            maxZ = .5f + .0625f;
        }

        return AxisAlignedBB.getBoundingBox(x + minX, y + 0, z + minZ, x + maxX, y + 1, z + maxZ);
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float vx, float vy, float vz) {
        BlockGarden block = getGardenBlock(world, x, y, z);
        if (block != null) {
            y = getBaseBlockYCoord(world, x, y, z);
            return block.applyItemToGarden(world, x, y, z, player, null);
        }

        return super.onBlockActivated(world, x, y, z, player, side, vx, vy, vz);
    }

    @Override
    public boolean applyBonemeal (World world, int x, int y, int z) {
        return ModBlocks.gardenProxy.applyBonemeal(world, x, y, z);
    }

    @Override
    public TileEntityGarden getGardenEntity (IBlockAccess blockAccess, int x, int y, int z) {
        return ModBlocks.gardenProxy.getGardenEntity(blockAccess, x, y, z);
    }

    public int findMinY (IBlockAccess world, int x, int y, int z) {
        while (y > 0) {
            if (world.getBlock(x, --y, z) != this)
                return y + 1;
        }

        return y;
    }

    public int findMaxY (IBlockAccess world, int x, int y, int z) {
        while (y < world.getHeight() - 1) {
            if (world.getBlock(x, ++y, z) != this)
                return y - 1;
        }

        return y;
    }

    public Vec3[] getAttachPoints (IBlockAccess world, int x, int y, int z) {
        int yMin = findMinY(world, x, y, z);
        Block bottomBlock = world.getBlock(x, yMin - 1, z);

        IAttachable attachable = GardenAPI.instance().registries().attachable().getAttachable(bottomBlock, world.getBlockMetadata(x, y - 1, z));

        Vec3[] attachPoints = singleAttachPoint;
        if (bottomBlock instanceof IChainAttachable)
            attachPoints = ((IChainAttachable) bottomBlock).getChainAttachPoints(1);
        else if (attachable != null && attachable.isAttachable(world, x, y - 1, z, 1))
            attachPoints = new Vec3[] { Vec3.createVectorHelper(.5, attachable.getAttachDepth(world, x, y - 1, z, 1), .5) };
        else if (bottomBlock instanceof IChainSingleAttachable) {
            Vec3 attachPoint = ((IChainSingleAttachable) bottomBlock).getChainAttachPoint(world, x, y, z, 1);
            if (attachPoint != null)
                attachPoints = new Vec3[] { attachPoint };
        }
        else if (bottomBlock.renderAsNormalBlock() && bottomBlock.getMaterial() != Material.air)
            attachPoints = defaultAttachPoints;

        return attachPoints;
    }

    @Override
    public int damageDropped (int meta) {
        return MathHelper.clamp_int(meta, 0, types.length - 1);
    }

    @Override
    public void getSubBlocks (Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        //list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 3));
        list.add(new ItemStack(item, 1, 4));
        list.add(new ItemStack(item, 1, 5));
    }

    @Override
    public IIcon getIcon (int side, int meta) {
        return icons[MathHelper.clamp_int(meta, 0, types.length - 1)];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons (IIconRegister register) {
        icons = new IIcon[types.length];

        for (int i = 0; i < types.length; i++)
            icons[i] = register.registerIcon(getTextureName() + "_" + types[i]);
    }

    private int getBaseBlockYCoord (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return 0;

        Block underBlock = world.getBlock(x, --y, z);
        while (y > 0 && underBlock instanceof IPlantProxy)
            underBlock = world.getBlock(x, --y, z);

        return y;
    }

    public BlockGarden getGardenBlock (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return null;

        y = getBaseBlockYCoord(world, x, y, z);
        Block underBlock = world.getBlock(x, y, z);

        if (!(underBlock instanceof BlockGarden))
            return null;

        return (BlockGarden) underBlock;
    }

    @Override
    public boolean isMultiAttach () {
        return true;
    }
}
