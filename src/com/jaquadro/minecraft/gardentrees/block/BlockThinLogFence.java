package com.jaquadro.minecraft.gardentrees.block;

import com.jaquadro.minecraft.gardencore.api.WoodRegistry;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import com.jaquadro.minecraft.gardentrees.GardenTrees;
import com.jaquadro.minecraft.gardentrees.block.tile.TileEntityWoodProxy;
import com.jaquadro.minecraft.gardentrees.core.ClientProxy;
import com.jaquadro.minecraft.gardentrees.core.ModCreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemLead;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlockThinLogFence extends BlockContainer
{
    public static final String[] subNames = new String[] { "oak", "spruce", "birch", "jungle", "acacia", "big_oak" };

    @SideOnly(Side.CLIENT)
    IIcon sideIcon;

    public BlockThinLogFence (String blockName) {
        super(Material.wood);

        setCreativeTab(ModCreativeTabs.tabGardenTrees);
        setHardness(1.5f);
        setResistance(5f);
        setLightOpacity(0);
        setStepSound(Block.soundTypeWood);
        setBlockName(blockName);

        setBlockBoundsForItemRender();
    }

    public float getMargin () {
        return 0.25f;
    }

    @Override
    public void setBlockBoundsForItemRender () {
        float margin = getMargin();
        setBlockBounds(margin, 0, margin, 1 - margin, 1, 1 - margin);
    }

    @Override
    public void addCollisionBoxesToList (World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity colliding) {
        boolean connectedZNeg = canConnectFenceTo(world, x, y, z - 1);
        boolean connectedZPos = canConnectFenceTo(world, x, y, z + 1);
        boolean connectedXNeg = canConnectFenceTo(world, x - 1, y, z);
        boolean connectedXPos = canConnectFenceTo(world, x + 1, y, z);

        float margin = getMargin();
        float xs = margin;
        float xe = 1 - margin;
        float zs = margin;
        float ze = 1 - margin;

        if (connectedZNeg)
            zs = 0;
        if (connectedZPos)
            ze = 1;
        if (connectedZNeg || connectedZPos) {
            setBlockBounds(xs, 0, zs, xe, 1.5f, ze);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }

        zs = margin;
        ze = 1 - margin;

        if (connectedXNeg)
            xs = 0;
        if (connectedXPos)
            xe = 1;
        if (connectedXNeg || connectedXPos || (!connectedZNeg && !connectedZPos)) {
            setBlockBounds(xs, 0, zs, xe, 1.5f, ze);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }

        if (connectedZNeg)
            zs = 0;
        if (connectedZPos)
            ze = 1;

        setBlockBounds(xs, 0, zs, xe, 1, ze);
    }

    @Override
    public void setBlockBoundsBasedOnState (IBlockAccess world, int x, int y, int z) {
        boolean connectedZNeg = canConnectFenceTo(world, x, y, z - 1);
        boolean connectedZPos = canConnectFenceTo(world, x, y, z + 1);
        boolean connectedXNeg = canConnectFenceTo(world, x - 1, y, z);
        boolean connectedXPos = canConnectFenceTo(world, x + 1, y, z);

        float margin = getMargin();
        float xs = margin;
        float xe = 1 - margin;
        float zs = margin;
        float ze = 1 - margin;

        if (connectedZNeg)
            zs = 0;
        if (connectedZPos)
            ze = 1;
        if (connectedXNeg)
            xs = 0;
        if (connectedXPos)
            xe = 1;

        setBlockBounds(xs, 0, zs, xe, 1, ze);
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
    public boolean getBlocksMovement (IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public int getRenderType () {
        return ClientProxy.thinLogFenceRenderID;
    }

    @Override
    public boolean canPlaceTorchOnTop (World world, int x, int y, int z) {
        return true;
    }

    public boolean canConnectFenceTo (IBlockAccess world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block != this)
            return (block.getMaterial().isOpaque() && block.renderAsNormalBlock()) ? block.getMaterial() != Material.gourd : false;

        return true;
    }

    @Override
    public boolean shouldSideBeRendered (IBlockAccess world, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public boolean removedByPlayer (World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        if (willHarvest)
            return true;

        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

    @Override
    public void harvestBlock (World world, EntityPlayer player, int x, int y, int z, int meta) {
        super.harvestBlock(world, player, x, y, z, meta);
        world.setBlockToAir(x, y, z);
    }

    @Override
    public int damageDropped (int meta) {
        return meta;
    }

    @Override
    public ArrayList<ItemStack> getDrops (World world, int x, int y, int z, int metadata, int fortune) {
        TileEntityWoodProxy tile = getTileEntity(world, x, y, z);
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        int count = quantityDropped(metadata, fortune, world.rand);
        for(int i = 0; i < count; i++)
        {
            Item item = getItemDropped(metadata, world.rand, fortune);
            if (item != null)
            {
                int damage = damageDropped(metadata);
                if (tile != null && tile.getProtoBlock() != null)
                    damage = TileEntityWoodProxy.composeMetadata(tile.getProtoBlock(), tile.getProtoMeta());

                ItemStack stack = new ItemStack(item, 1, damage);
                ret.add(stack);
            }
        }
        return ret;
    }

    @Override
    public IIcon getIcon (int side, int meta) {
        int protoMeta = TileEntityWoodProxy.getMetaFromComposedMetadata(meta);
        Block protoBlock = TileEntityWoodProxy.getBlockFromComposedMetadata(meta);
        if (protoBlock == null)
            protoBlock = getIconSource(meta);

        return protoBlock.getIcon(side, protoMeta);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon (IBlockAccess blockAccess, int x, int y, int z, int side) {
        TileEntityWoodProxy te = getTileEntity(blockAccess, x, y, z);
        if (te == null || te.getProtoBlock() == null)
            return super.getIcon(blockAccess, x, y, z, side);

        int protoMeta = te.getProtoMeta();
        Block protoBlock = te.getProtoBlock();
        if (protoBlock == null)
            protoBlock = Blocks.log;

        return protoBlock.getIcon(side, protoMeta);
    }

    private TileEntityWoodProxy getTileEntity (IBlockAccess blockAccess, int x, int y, int z) {
        TileEntity te = blockAccess.getTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityWoodProxy)
            return (TileEntityWoodProxy) te;

        return null;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getSideIcon () {
        return sideIcon;
    }

    private Block getIconSource (int meta) {
        switch (meta / 4) {
            case 0:
                return Blocks.log;
            case 1:
                return Blocks.log2;
            default:
                return Blocks.log;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addHitEffects (World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
        TileEntityWoodProxy te = getTileEntity(worldObj, target.blockX, target.blockY, target.blockZ);
        BlockThinLogFence block = (BlockThinLogFence) worldObj.getBlock(target.blockX, target.blockY, target.blockZ);

        if (te == null || block == null)
            return false;

        int protoMeta = te.getProtoMeta();
        Block protoBlock = te.getProtoBlock();
        if (protoBlock == null)
            protoBlock = Blocks.log;

        float f = 0.1F;
        double xPos = target.blockX + worldObj.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (f * 2.0F)) + f + block.getBlockBoundsMinX();
        double yPos = target.blockY + worldObj.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (f * 2.0F)) + f + block.getBlockBoundsMinY();
        double zPos = target.blockZ + worldObj.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (f * 2.0F)) + f + block.getBlockBoundsMinZ();

        if (target.sideHit == 0)
            yPos = target.blockY + block.getBlockBoundsMinY() - f;
        if (target.sideHit == 1)
            yPos = target.blockY + block.getBlockBoundsMaxY() + f;
        if (target.sideHit == 2)
            zPos = target.blockZ + block.getBlockBoundsMinZ() - f;
        if (target.sideHit == 3)
            zPos = target.blockZ + block.getBlockBoundsMaxZ() + f;
        if (target.sideHit == 4)
            xPos = target.blockX + block.getBlockBoundsMinX() - f;
        if (target.sideHit == 5)
            xPos = target.blockX + block.getBlockBoundsMaxX() + f;

        EntityDiggingFX fx = new EntityDiggingFX(worldObj, xPos, yPos, zPos, 0.0D, 0.0D, 0.0D, block, worldObj.getBlockMetadata(target.blockX, target.blockY, target.blockZ));
        fx.applyColourMultiplier(target.blockX, target.blockY, target.blockZ);
        fx.multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F);
        fx.setParticleIcon(block.getIcon(worldObj.rand.nextInt(6), te.composeMetadata(protoBlock, protoMeta)));

        effectRenderer.addEffect(fx);

        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addDestroyEffects (World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        TileEntityWoodProxy te = getTileEntity(world, x, y, z);
        BlockThinLogFence block = (BlockThinLogFence) world.getBlock(x, y, z);

        if (te == null || block == null)
            return false;

        int protoMeta = te.getProtoMeta();
        Block protoBlock = te.getProtoBlock();
        if (protoBlock == null)
            protoBlock = Blocks.log;

        try {
            byte count = 4;
            for (int ix = 0; ix < count; ++ix) {
                for (int iy = 0; iy < count; ++iy) {
                    for (int iz = 0; iz < count; ++iz) {
                        double xOff = (double)x + ((double)ix + 0.5D) / (double)count;
                        double yOff = (double)y + ((double)iy + 0.5D) / (double)count;
                        double zOff = (double)z + ((double)iz + 0.5D) / (double)count;

                        EntityDiggingFX fx = new EntityDiggingFX(world, xOff, yOff, zOff, xOff - (double) x - 0.5D, yOff - (double) y - 0.5D, zOff - (double) z - 0.5D, this, meta);
                        fx.setParticleIcon(block.getIcon(world.rand.nextInt(6), te.composeMetadata(protoBlock, protoMeta)));

                        effectRenderer.addEffect(fx.applyColourMultiplier(x, y, z));
                    }
                }
            }
        }
        catch (Exception e) { }

        return true;
    }

    @Override
    public void getSubBlocks (Item item, CreativeTabs creativeTabs, List blockList) {
        for (int i = 0; i < 6; i++)
            blockList.add(new ItemStack(item, 1, i));

        for (Map.Entry<UniqueMetaIdentifier, Block> entry : WoodRegistry.instance().registeredTypes()) {
            if (entry.getValue() == Blocks.log || entry.getValue() == Blocks.log2)
                continue;

            int id = TileEntityWoodProxy.composeMetadata(entry.getValue(), entry.getKey().meta);
            blockList.add(new ItemStack(item, 1, id));
        }
    }

    @Override
    public void registerBlockIcons (IIconRegister iconRegister) {
        sideIcon = iconRegister.registerIcon(GardenTrees.MOD_ID + ":thinlog_fence_side");
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float vx, float vy, float vz) {
        return world.isRemote ? true : ItemLead.func_150909_a(player, world, x, y, z);
    }

    @Override
    public TileEntity createNewTileEntity (World world, int meta) {
        return new TileEntityWoodProxy();
    }
}
