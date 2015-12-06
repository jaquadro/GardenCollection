package com.jaquadro.minecraft.gardenstuff.renderer;

import com.jaquadro.minecraft.gardenapi.api.GardenAPI;
import com.jaquadro.minecraft.gardenapi.api.connect.IAttachable;
import com.jaquadro.minecraft.gardencore.client.renderer.support.ModularBoxRenderer;
import com.jaquadro.minecraft.gardencore.util.RenderHelper;
import com.jaquadro.minecraft.gardenstuff.block.BlockLattice;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

import static com.jaquadro.minecraft.gardentrees.core.ModBlocks.*;

public class LatticeRenderer implements ISimpleBlockRenderingHandler
{
    private static final float UN4 = .0625f * -4;
    private static final float U7 = .0625f * 7;
    private static final float U8 = .0625f * 8;
    private static final float U9 = .0625f * 9;
    private static final float U20 = .0625f * 20;

    private ModularBoxRenderer boxRenderer = new ModularBoxRenderer();

    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glRotatef(90, 0, 1, 0);
        GL11.glScalef(1.5f, 1.5f, 1.5f);
        GL11.glTranslatef(-.5f, -.5f, -.5f);

        boxRenderer.setUnit(.0625);
        boxRenderer.setColor(ModularBoxRenderer.COLOR_WHITE);
        boxRenderer.setIcon(block.getIcon(0, metadata));

        boxRenderer.renderSolidBox(null, block, 0, 0, 0, 0, U7 + .01, U7 + .01, 1, U9 - .01, U9 - .01);
        boxRenderer.renderSolidBox(null, block, 0, 0, 0, U7 + .01, 0, U7 + .01, U9 - .01, 1, U9 - .01);

        GL11.glTranslatef(.5f, .5f, .5f);
    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (!(block instanceof BlockLattice))
            return false;

        return renderWorldBlock(world, x, y, z, (BlockLattice) block, modelId, renderer);
    }

    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, BlockLattice block, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));

        boxRenderer.setUnit(.0625);
        boxRenderer.setColor(ModularBoxRenderer.COLOR_WHITE);
        boxRenderer.setIcon(block.getIcon(world, x, y, z, 0));

        int connectFlags = block.calcConnectionFlags(world, x, y, z);

        boolean connectYNeg = (connectFlags & 1) != 0;
        boolean connectYPos = (connectFlags & 2) != 0;
        boolean connectZNeg = (connectFlags & 4) != 0;
        boolean connectZPos = (connectFlags & 8) != 0;
        boolean connectXNeg = (connectFlags & 16) != 0;
        boolean connectXPos = (connectFlags & 32) != 0;

        boolean extYNeg = (connectFlags & 64) != 0;
        boolean extYPos = (connectFlags & 128) != 0;
        boolean extZNeg = (connectFlags & 256) != 0;
        boolean extZPos = (connectFlags & 512) != 0;
        boolean extXNeg = (connectFlags & 1024) != 0;
        boolean extXPos = (connectFlags & 2048) != 0;

        boxRenderer.renderSolidBox(world, block, x, y, z, U7, U7, U7, U9, U9, U9);

        float yMin = extYNeg ? UN4 : (connectYNeg ? 0 : U7);
        float yMax = extYPos ? U20 : (connectYPos ? 1 : U9);

        IAttachable attachableYN = GardenAPI.instance().registries().attachable().getAttachable(world.getBlock(x, y - 1, z), world.getBlockMetadata(x, y - 1, z));
        if (attachableYN != null && attachableYN.isAttachable(world, x, y - 1, z, 1))
            yMin = (float)attachableYN.getAttachDepth(world, x, y - 1, z, 1) - 1;
        IAttachable attachableYP = GardenAPI.instance().registries().attachable().getAttachable(world.getBlock(x, y + 1, z), world.getBlockMetadata(x, y + 1, z));
        if (attachableYP != null && attachableYP.isAttachable(world, x, y + 1, z, 0))
            yMax = (float)attachableYP.getAttachDepth(world, x, y + 1, z, 0) + 1;

        if (yMin < U7)
            boxRenderer.renderSolidBox(world, block, x, y, z, U7, yMin, U7, U9, U7, U9);
        if (yMax > U9)
            boxRenderer.renderSolidBox(world, block, x, y, z, U7, U9, U7, U9, yMax, U9);

        float zMin = extZNeg ? UN4 : (connectZNeg ? 0 : U7);
        float zMax = extZPos ? U20 : (connectZPos ? 1 : U9);

        if (zMin < U7)
            boxRenderer.renderSolidBox(world, block, x, y, z, U7, U7, zMin, U9, U9, U7);
        if (zMax > U9)
            boxRenderer.renderSolidBox(world, block, x, y, z, U7, U7, U9, U9, U9, zMax);

        float xMin = extXNeg ? UN4 : (connectXNeg ? 0 : U7);
        float xMax = extXPos ? U20 : (connectXPos ? 1 : U9);

        if (xMin < U7)
            boxRenderer.renderSolidBox(world, block, x, y, z, xMin, U7, U7, U7, U9, U9);
        if (xMax > U9)
            boxRenderer.renderSolidBox(world, block, x, y, z, U9, U7, U7, xMax, U9, U9);

        IIcon vineIcon = Blocks.vine.getIcon(0, 0);

        /*boxRenderer.setUnit(0);
        boxRenderer.flipOpposite = true;
        boxRenderer.setIcon(Blocks.glass_pane.getIcon(4, 0));

        int cutX = ModularBoxRenderer.CUT_YNEG | ModularBoxRenderer.CUT_YPOS | ModularBoxRenderer.CUT_ZNEG | ModularBoxRenderer.CUT_ZPOS;
        RenderHelper.instance.setTextureOffset(.5, .5);

        if (connectZNeg) {
            if (connectYPos)
                boxRenderer.renderExterior(renderer, Blocks.glass_pane, x, y, z, U7, U9, 0, U9, 1, U7, 0, cutX);
            if (connectYNeg)
                boxRenderer.renderExterior(renderer, Blocks.glass_pane, x, y, z, U7, 0, 0, U9, U7, U7, 0, cutX);
        }
        if (connectZPos) {
            if (connectYPos)
                boxRenderer.renderExterior(renderer, Blocks.glass_pane, x, y, z, U7, U9, U9, U9, 1, 1, 0, cutX);
            if (connectYNeg)
                boxRenderer.renderExterior(renderer, Blocks.glass_pane, x, y, z, U7, 0, U9, U9, U7, 1, 0, cutX);
        }
        RenderHelper.instance.clearTextureOffset();

        int cutZ = ModularBoxRenderer.CUT_YNEG | ModularBoxRenderer.CUT_YPOS | ModularBoxRenderer.CUT_XNEG | ModularBoxRenderer.CUT_XPOS;
        RenderHelper.instance.setTextureOffset(.5, .5);
        if (connectXNeg) {
            if (connectYPos)
                boxRenderer.renderExterior(renderer, Blocks.glass_pane, x, y, z, 0, U9, U7, U7, 1, U9, 0, cutZ);
            if (connectYNeg)
                boxRenderer.renderExterior(renderer, Blocks.glass_pane, x, y, z, 0, 0, U7, U7, U7, U9, 0, cutZ);
        }
        if (connectXPos) {
            if (connectYPos)
                boxRenderer.renderExterior(renderer, Blocks.glass_pane, x, y, z, U9, U9, U7, 1, 1, U9, 0, cutZ);
            if (connectYNeg)
                boxRenderer.renderExterior(renderer, Blocks.glass_pane, x, y, z, U9, 0, U7, 1, U7, U9, 0, cutZ);
        }
        RenderHelper.instance.clearTextureOffset();

        boxRenderer.flipOpposite = false;*/

        /*RenderHelper.instance.state.colorMultYNeg = 0.8f;

        renderer.setRenderBounds(0, .40625, 0, 1, .59375, 1);
        RenderHelper.instance.renderFace(RenderHelper.YNEG, renderer, ivy, x, y, z, vineIcon);
        RenderHelper.instance.renderFace(RenderHelper.YPOS, renderer, ivy, x, y, z, vineIcon);

        renderer.setRenderBounds(0, .59375, 0, 1, 1, 1);
        RenderHelper.instance.setTextureOffset(.5, .5);
        RenderHelper.instance.renderFace(RenderHelper.YNEG, renderer, ivy, x, y, z, vineIcon);
        RenderHelper.instance.renderFace(RenderHelper.YPOS, renderer, ivy, x, y, z, vineIcon);
        RenderHelper.instance.clearTextureOffset();

        vineIcon = ModBlocks.rootCover.getIcon(world, x, y, z, 0); // Blocks.vine.getIcon(0, 0);

        renderer.setRenderBounds(0, .375, 0, 1, .625, 1);
        RenderHelper.instance.renderFace(RenderHelper.YNEG, renderer, ModBlocks.rootCover, x, y, z, vineIcon);
        RenderHelper.instance.renderFace(RenderHelper.YPOS, renderer, ModBlocks.rootCover, x, y, z, vineIcon);

        vineIcon = ModBlocks.rootCover.getIcon(world, x + 1, y, z + 1, 0);

        renderer.setRenderBounds(0, .625, 0, 1, 1, 1);
        RenderHelper.instance.renderFace(RenderHelper.YNEG, renderer, ModBlocks.rootCover, x, y, z, vineIcon);
        //RenderHelper.instance.renderFace(RenderHelper.YPOS, renderer, ModBlocks.rootCover, x, y, z, vineIcon);

        RenderHelper.instance.state.resetColorMult();*/

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return true;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.latticeRenderID;
    }
}
