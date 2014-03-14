package com.jaquadro.minecraft.modularpots.block;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.client.ClientProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ThinLog extends Block
{
    public static final String[] subNames = new String[] { "oak", "spruce", "birch", "jungle" };

    // Scratch state variable for rendering purposes
    // 0 = Y, 1 = Z, 2 = X, 3 = BARK
    private int orientation;

    public ThinLog (int id) {
        super(id, Material.wood);
        this.setCreativeTab(ModularPots.tabModularPots);

        setBlockBoundsForItemRender();
    }

    public float getMargin () {
        return 0.25f;
    }

    public void setOrientation (int orientation) {
        this.orientation = orientation;
    }

    @Override
    public void setBlockBoundsForItemRender () {
        float margin = getMargin();
        setBlockBounds(margin, 0, margin, 1 - margin, 1, 1 - margin);
    }

    @Override
    public void addCollisionBoxesToList (World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity colliding) {
        int connectFlags = calcConnectionFlags(world, x, y, z);

        float margin = getMargin();
        float ys = (connectFlags & 1) != 0 ? 0 : margin;
        float ye = (connectFlags & 2) != 0 ? 1 : 1 - margin;
        float zs = (connectFlags & 4) != 0 ? 0 : margin;
        float ze = (connectFlags & 8) != 0 ? 1 : 1 - margin;
        float xs = (connectFlags & 16) != 0 ? 0 : margin;
        float xe = (connectFlags & 32) != 0 ? 1 : 1 - margin;

        setBlockBounds(xs, ys, zs, xe, ye, ze);
        super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
    }

    @Override
    public void setBlockBoundsBasedOnState (IBlockAccess world, int x, int y, int z) {
        int connectFlags = calcConnectionFlags(world, x, y, z);

        float margin = getMargin();
        float ys = (connectFlags & 1) != 0 ? 0 : margin;
        float ye = (connectFlags & 2) != 0 ? 1 : 1 - margin;
        float zs = (connectFlags & 4) != 0 ? 0 : margin;
        float ze = (connectFlags & 8) != 0 ? 1 : 1 - margin;
        float xs = (connectFlags & 16) != 0 ? 0 : margin;
        float xe = (connectFlags & 32) != 0 ? 1 : 1 - margin;

        setBlockBounds(xs, ys, zs, xe, ye, ze);
    }

    @Override
    public int quantityDropped (Random random) {
        return 1;
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
    public boolean canPlaceTorchOnTop (World world, int x, int y, int z) {
        return true;
    }

    @Override
    public int getRenderType () {
        return ClientProxy.thinLogRenderID;
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, int blockId, int meta) {
        byte range = 4;
        int height = range + 1;

        if (world.checkChunksExist(x - height, y - height, z - height, x + height, y + height, z + height)) {
            for (int dx = -range; dx <= range; dx++) {
                for (int dy = -range; dy <= range; dy++) {
                    for (int dz = -range; dz <= range; dz++) {
                        Block leaf = Block.blocksList[world.getBlockId(x + dx, y + dy, z + dz)];
                        if (leaf != null && leaf.isLeaves(world, x + dx, y + dy, z + dz))
                            leaf.beginLeavesDecay(world, x + dx, y + dy, z + dz);
                    }
                }
            }
        }
    }

    public int calcConnectionFlags (IBlockAccess world, int x, int y, int z) {
        int flagsY = calcConnectYFlags(world, x, y, z);
        int flagsZNeg = calcConnectYFlags(world, x, y, z - 1);
        int flagsZPos = calcConnectYFlags(world, x, y, z + 1);
        int flagsXNeg = calcConnectYFlags(world, x - 1, y, z);
        int flagsXPos = calcConnectYFlags(world, x + 1, y, z);

        int connectFlagsY = flagsY & 3;
        int connectFlagsZNeg = flagsZNeg & 3;
        int connectFlagsZPos = flagsZPos & 3;
        int connectFlagsXNeg = flagsXNeg & 3;
        int connectFlagsXPos = flagsXPos & 3;

        Block blockZNeg = Block.blocksList[world.getBlockId(x, y, z - 1)];
        Block blockZPos = Block.blocksList[world.getBlockId(x, y, z + 1)];
        Block blockXNeg = Block.blocksList[world.getBlockId(x - 1, y, z)];
        Block blockXPos = Block.blocksList[world.getBlockId(x + 1, y, z)];

        boolean hardZNeg = isNeighborHardConnection(blockZNeg) || blockZNeg instanceof BlockTorch;
        boolean hardZPos = isNeighborHardConnection(blockZPos) || blockZPos instanceof BlockTorch;
        boolean hardXNeg = isNeighborHardConnection(blockXNeg) || blockXNeg instanceof BlockTorch;
        boolean hardXPos = isNeighborHardConnection(blockXPos) || blockXPos instanceof BlockTorch;

        boolean hardConnection = (flagsY & 4) != 0;
        boolean hardConnectionZNeg = hardConnection && (flagsZNeg & 4) != 0;
        boolean hardConnectionZPos = hardConnection && (flagsZPos & 4) != 0;
        boolean hardConnectionXNeg = hardConnection && (flagsXNeg & 4) != 0;
        boolean hardConnectionXPos = hardConnection && (flagsXPos & 4) != 0;

        boolean connectZNeg = (connectFlagsY == 0 && hardZNeg)
            || (blockZNeg == this && !hardConnectionZNeg && (connectFlagsY != 3 || connectFlagsZNeg != 3));
        boolean connectZPos = (connectFlagsY == 0 && hardZPos)
            || (blockZPos == this && !hardConnectionZPos && (connectFlagsY != 3 || connectFlagsZPos != 3));
        boolean connectXNeg = (connectFlagsY == 0 && hardXNeg)
            || (blockXNeg == this && !hardConnectionXNeg && (connectFlagsY != 3 || connectFlagsXNeg != 3));
        boolean connectXPos = (connectFlagsY == 0 && hardXPos)
            || (blockXPos == this && !hardConnectionXPos && (connectFlagsY != 3 || connectFlagsXPos != 3));

        if (!(connectZNeg | connectZPos | connectXNeg | connectXPos))
            connectFlagsY = 3;

        return connectFlagsY | (connectZNeg ? 4 : 0) | (connectZPos ? 8 : 0) |
            (connectXNeg ? 16 : 0) | (connectXPos ? 32 : 0);
    }

    private int calcConnectYFlags (IBlockAccess world, int x, int y, int z) {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        if (block != this)
            return 0;

        Block blockYNeg = Block.blocksList[world.getBlockId(x, y - 1, z)];
        boolean hardYNeg = isNeighborHardConnectionY(blockYNeg);
        boolean connectYNeg = hardYNeg || blockYNeg == this;

        Block blockYPos = Block.blocksList[world.getBlockId(x, y + 1, z)];
        boolean hardYPos = isNeighborHardConnectionY(blockYPos);
        boolean connectYPos = hardYPos || blockYPos == this|| blockYPos instanceof BlockTorch;

        return (connectYNeg ? 1 : 0) | (connectYPos ? 2 : 0) | (hardYNeg ? 4 : 0) | (hardYPos ? 8 : 0);
    }

    private boolean isNeighborHardConnection (Block block) {
        if (block == null || block.blockMaterial == null)
            return false;
        if (block.blockMaterial.isOpaque() && block.renderAsNormalBlock())
            return true;
        if (block == ModularPots.largePot)
            return true;
        return false;
    }

    private boolean isNeighborHardConnectionY (Block block) {
        if (isNeighborHardConnection(block))
            return true;

        return block instanceof BlockLeavesBase
            || block == ModularPots.thinLogFence;
    }

    @Override
    public void getSubBlocks (int itemId, CreativeTabs creativeTabs, List blockList) {
        for (int i = 0; i < 4; i++)
            blockList.add(new ItemStack(itemId, 1, i));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon (int side, int meta) {
        int superMeta = meta % 4;
        if (orientation == 1)
            superMeta |= 8;
        else if (orientation == 2)
            superMeta |= 4;
        else if (orientation == 3)
            superMeta |= 12;

        return getIconSource(meta).getIcon(side, superMeta);
    }

    private Block getIconSource (int meta) {
        switch (meta / 4) {
            case 0:
                return Block.wood;
            default:
                return Block.wood;
        }
    }

    @Override
    public boolean canSustainLeaves (World world, int x, int y, int z) {
        return true;
    }
}
