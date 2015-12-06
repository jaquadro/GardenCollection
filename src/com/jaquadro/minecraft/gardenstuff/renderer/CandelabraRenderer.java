package com.jaquadro.minecraft.gardenstuff.renderer;

import com.jaquadro.minecraft.gardencore.api.block.IChain;
import com.jaquadro.minecraft.gardencore.util.RenderHelper;
import com.jaquadro.minecraft.gardencore.util.RenderHelperState;
import com.jaquadro.minecraft.gardenstuff.block.BlockCandelabra;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityCandelabra;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class CandelabraRenderer implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {
        if (!(block instanceof BlockCandelabra))
            return;

        GL11.glRotatef(90, 0, 1, 0);
        GL11.glTranslatef(-.5f, -.5f, -.5f);

        renderInventoryBlock((BlockCandelabra) block, metadata, modelId, renderer);

        GL11.glTranslatef(.5f, .5f, .5f);
    }

    private void renderInventoryBlock (BlockCandelabra block, int metadata, int modelId, RenderBlocks renderer) {
        int level = metadata;

        // Candelsticks
        RenderHelper.instance.setRenderBounds(0, 0, 0, 1, 1, 1);
        RenderHelper.instance.state.setRenderOffset(0, 0, 0);
        RenderHelper.instance.renderCrossedSquares(block, metadata, block.getIconCandle());

        if (level >= 1) {
            RenderHelper.instance.state.setRenderOffset(-.34375f, 0, 0);
            RenderHelper.instance.renderCrossedSquares(block, metadata, block.getIconCandle());
            RenderHelper.instance.state.setRenderOffset(.34375f, 0, 0);
            RenderHelper.instance.renderCrossedSquares(block, metadata, block.getIconCandle());
        }
        if (level >= 2) {
            RenderHelper.instance.state.setRenderOffset(0, 0, -.34375f);
            RenderHelper.instance.renderCrossedSquares(block, metadata, block.getIconCandle());
            RenderHelper.instance.state.setRenderOffset(0, 0, .34375f);
            RenderHelper.instance.renderCrossedSquares(block, metadata, block.getIconCandle());
        }

        // Base
        RenderHelper.instance.state.setRenderOffset(0, 0, 0);
        RenderHelper.instance.renderCrossedSquares(block, metadata, block.getIconBase());

        // Arms
        if (level >= 1) {
            RenderHelper.instance.state.setRenderOffset(0, 0, 0);
            RenderHelper.instance.setRenderBounds(.5f, 0, .5f, 1, 1, 1);
            RenderHelper.instance.renderFace(RenderHelper.ZNEG, renderer.blockAccess, block, block.getIconArmExt(), metadata);

            RenderHelper.instance.state.setRenderOffset(.5f, 0, 0);
            RenderHelper.instance.setRenderBounds(0, 0, 0, .5f, 1, .5f);
            RenderHelper.instance.renderFace(RenderHelper.ZPOS, renderer.blockAccess, block, block.getIconArmExt(), metadata);

            RenderHelper.instance.state.setRenderOffset(0, 0, 0);
            RenderHelper.instance.setRenderBounds(0, 0, .5f, .5f, 1, 1);
            RenderHelper.instance.renderFace(RenderHelper.ZNEG, renderer.blockAccess, block, block.getIconArmExt(), metadata);

            RenderHelper.instance.state.setRenderOffset(-.5f, 0, 0);
            RenderHelper.instance.setRenderBounds(.5f, 0, 0, 1, 1, .5f);
            RenderHelper.instance.renderFace(RenderHelper.ZPOS, renderer.blockAccess, block, block.getIconArmExt(), metadata);

        }
        if (level >= 2) {
            RenderHelper.instance.state.setRenderOffset(0, 0, -.5f);
            RenderHelper.instance.setRenderBounds(.5f, 0, .5f, 1, 1, 1);
            RenderHelper.instance.renderFace(RenderHelper.XNEG, renderer.blockAccess, block, block.getIconArmExt(), metadata);

            RenderHelper.instance.state.setRenderOffset(0, 0, 0);
            RenderHelper.instance.setRenderBounds(0, 0, 0, .5f, 1, .5f);
            RenderHelper.instance.renderFace(RenderHelper.XPOS, renderer.blockAccess, block, block.getIconArmExt(), metadata);

            RenderHelper.instance.state.setRenderOffset(0, 0, .5f);
            RenderHelper.instance.setRenderBounds(.5f, 0, 0, 1, 1, .5f);
            RenderHelper.instance.renderFace(RenderHelper.XNEG, renderer.blockAccess, block, block.getIconArmExt(), metadata);

            RenderHelper.instance.state.setRenderOffset(0, 0, 0);
            RenderHelper.instance.setRenderBounds(0, 0, .5f, .5f, 1, 1);
            RenderHelper.instance.renderFace(RenderHelper.XPOS, renderer.blockAccess, block, block.getIconArmExt(), metadata);
        }

        RenderHelper.instance.state.clearRenderOffset();
    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (!(block instanceof BlockCandelabra))
            return false;

        return renderWorldBlock(world, x, y, z, (BlockCandelabra) block, modelId, renderer);
    }

    private boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, BlockCandelabra block, int modelId, RenderBlocks renderer) {
        TileEntityCandelabra tile = (TileEntityCandelabra)world.getTileEntity(x, y, z);
        if (tile == null)
            return false;

        RenderHelper.instance.setColorAndBrightness(world, block, x, y, z);
        RenderHelper.instance.state.setRotateTransform(RenderHelper.ZNEG, tile.getDirection());

        if (tile.isSconce())
            renderSconce(world, x, y, z, block, tile.getLevel());
        else
            renderCandelabra(world, x, y, z, block, tile.getLevel());

        RenderHelper.instance.state.clearRotateTransform();

        return true;
    }

    private void renderCandelabra (IBlockAccess world, int x, int y, int z, BlockCandelabra block, int level) {
        Block blockUpper = world.getBlock(x, y + 1, z);
        boolean hanging = level > 0 && (blockUpper instanceof IChain || blockUpper.isSideSolid(world, x, y + 1, z, ForgeDirection.DOWN));

        // Candelsticks
        RenderHelper.instance.setRenderBounds(0, 0, 0, 1, 1, 1);

        if (!hanging) {
            RenderHelper.instance.state.setRenderOffset(0, .0625f, 0);
            RenderHelper.instance.drawCrossedSquares(block.getIconCandle(), x, y, z, 1);
        }

        if (level >= 1) {
            RenderHelper.instance.state.setRenderOffset(-.34375f, 0, 0);
            RenderHelper.instance.drawCrossedSquares(block.getIconCandle(), x, y, z, 1);
            RenderHelper.instance.state.setRenderOffset(.34375f, 0, 0);
            RenderHelper.instance.drawCrossedSquares(block.getIconCandle(), x, y, z, 1);
        }
        if (level >= 2) {
            RenderHelper.instance.state.setRenderOffset(0, 0, -.34375f);
            RenderHelper.instance.drawCrossedSquares(block.getIconCandle(), x, y, z, 1);
            RenderHelper.instance.state.setRenderOffset(0, 0, .34375f);
            RenderHelper.instance.drawCrossedSquares(block.getIconCandle(), x, y, z, 1);
        }

        // Hanger / Base
        if (hanging) {
            RenderHelper.instance.state.setRenderOffset(0, 0, 0);
            RenderHelper.instance.drawCrossedSquares(block.getIconHang(), x, y, z, 1);
        }
        else {
            RenderHelper.instance.state.setRenderOffset(0, 0, 0);
            RenderHelper.instance.drawCrossedSquares(block.getIconBase(), x, y, z, 1);
        }

        // Arms
        if (level >= 1) {
            RenderHelper.instance.state.setRenderOffset(0, 0, 0);
            RenderHelper.instance.setRenderBounds(.5f, 0, .5f, 1, 1, 1);
            RenderHelper.instance.renderFace(RenderHelper.ZNEG, world, block, x, y, z, block.getIconArmExt());

            RenderHelper.instance.state.setRenderOffset(.5f, 0, 0);
            RenderHelper.instance.setRenderBounds(0, 0, 0, .5f, 1, .5f);
            RenderHelper.instance.renderFace(RenderHelper.ZPOS, world, block, x, y, z, block.getIconArmExt());

            RenderHelper.instance.state.setRenderOffset(0, 0, 0);
            RenderHelper.instance.setRenderBounds(0, 0, .5f, .5f, 1, 1);
            RenderHelper.instance.renderFace(RenderHelper.ZNEG, world, block, x, y, z, block.getIconArmExt());

            RenderHelper.instance.state.setRenderOffset(-.5f, 0, 0);
            RenderHelper.instance.setRenderBounds(.5f, 0, 0, 1, 1, .5f);
            RenderHelper.instance.renderFace(RenderHelper.ZPOS, world, block, x, y, z, block.getIconArmExt());

        }
        if (level >= 2) {
            RenderHelper.instance.state.setRenderOffset(0, 0, -.5f);
            RenderHelper.instance.setRenderBounds(.5f, 0, .5f, 1, 1, 1);
            RenderHelper.instance.renderFace(RenderHelper.XNEG, world, block, x, y, z, block.getIconArmExt());

            RenderHelper.instance.state.setRenderOffset(0, 0, 0);
            RenderHelper.instance.setRenderBounds(0, 0, 0, .5f, 1, .5f);
            RenderHelper.instance.renderFace(RenderHelper.XPOS, world, block, x, y, z, block.getIconArmExt());

            RenderHelper.instance.state.setRenderOffset(0, 0, .5f);
            RenderHelper.instance.setRenderBounds(.5f, 0, 0, 1, 1, .5f);
            RenderHelper.instance.renderFace(RenderHelper.XNEG, world, block, x, y, z, block.getIconArmExt());

            RenderHelper.instance.state.setRenderOffset(0, 0, 0);
            RenderHelper.instance.setRenderBounds(0, 0, .5f, .5f, 1, 1);
            RenderHelper.instance.renderFace(RenderHelper.XPOS, world, block, x, y, z, block.getIconArmExt());
        }

        RenderHelper.instance.state.clearRenderOffset();
    }

    private void renderSconce (IBlockAccess world, int x, int y, int z, BlockCandelabra block, int level) {
        IIcon centerArmIcon = (level == 0) ? block.getIconArm() : block.getIconArmExt();

        RenderHelper.instance.setRenderBounds(0, 0, 0, 1, 1, 1);

        // Candlesticks
        if (level == 0) {
            RenderHelper.instance.state.setRenderOffset(0, 0, -.25f);
            RenderHelper.instance.drawCrossedSquares(block.getIconCandle(), x, y, z, 1);
        }
        if (level == 1 || level == 2) {
            RenderHelper.instance.state.setRenderOffset(-.25f, 0, -.25f);
            RenderHelper.instance.drawCrossedSquares(block.getIconCandle(), x, y, z, 1);
            RenderHelper.instance.state.setRenderOffset(.25f, 0, -.25f);
            RenderHelper.instance.drawCrossedSquares(block.getIconCandle(), x, y, z, 1);
        }
        if (level == 2) {
            RenderHelper.instance.state.setRenderOffset(0, 0, -.125f);
            RenderHelper.instance.drawCrossedSquares(block.getIconCandle(), x, y, z, 1);
        }

        // Angled arms
        if (level == 1 || level == 2) {
            RenderHelper.instance.setRenderBounds(0, 0, 0, .5f, 1, .5f);
            RenderHelper.instance.state.setRenderOffset(.5f, 0, 0);
            RenderHelper.instance.drawCrossedSquaresBounded(block.getIconArm(), x, y, z, 1);
            RenderHelper.instance.setRenderBounds(.5f, 0, 0, 1, 1, .5f);
            RenderHelper.instance.state.setRenderOffset(-.5f, 0, 0);
            RenderHelper.instance.drawCrossedSquaresBounded(block.getIconArm(), x, y, z, 1);
        }

        // Center arm
        if (level == 0 || level == 2) {
            RenderHelper.instance.setRenderBounds(.5f, 0, 0, 1, 1, .5f);
            RenderHelper.instance.state.setRenderOffset(0, 0, .03125f);
            RenderHelper.instance.renderFace(RenderHelper.XNEG, world, block, x, y, z, centerArmIcon);

            RenderHelper.instance.setRenderBounds(0, 0, 0, .5f, 1, .5f);
            RenderHelper.instance.state.setRenderOffset(0, 0, .03125f);
            RenderHelper.instance.state.flipTexture = true;
            RenderHelper.instance.renderFace(RenderHelper.XPOS, world, block, x, y, z, centerArmIcon);
            RenderHelper.instance.state.flipTexture = false;
        }

        // Plate
        RenderHelper.instance.setRenderBounds(.375f, .0625f, 0, 1 - .375f, .375f, .0625f);
        RenderHelper.instance.state.clearRenderOffset();
        RenderHelper.instance.renderBlock(world, ModBlocks.metalBlock, x, y, z);

        RenderHelper.instance.state.clearRenderOffset();
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return true;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.sconceRenderID;
    }
}
