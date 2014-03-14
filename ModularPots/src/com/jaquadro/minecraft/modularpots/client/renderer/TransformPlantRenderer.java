package com.jaquadro.minecraft.modularpots.client.renderer;

import com.jaquadro.minecraft.modularpots.block.LargePotPlantProxy;
import com.jaquadro.minecraft.modularpots.client.ClientProxy;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityLargePot;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TransformPlantRenderer implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (!(block instanceof LargePotPlantProxy))
            return false;

        return renderWorldBlock(world, x, y, z, (LargePotPlantProxy) block, modelId, renderer);
    }

    private boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, LargePotPlantProxy block, int modelId, RenderBlocks renderer) {
        World worldFull = renderer.minecraftRB.theWorld.provider.worldObj;

        //Item item = block.getItemBlock(world, x, y, z);
        //Block itemBlock = (item instanceof IPlantable) ? ((IPlantable)item).getPlant(world, x, y, z) : Block.getBlockFromItem(item);
        Block itemBlock = block.getItemBlock(worldFull, x, y, z);
        if (itemBlock == null || world.isAirBlock(x, y, z))
            return true;

        int itemRenderType = itemBlock.getRenderType();

        Tessellator tessellator = Tessellator.instance;
        tessellator.addTranslation(0, -.0625f, 0);

        int color = block.colorMultiplier(world, x, y, z);
        if (color != 16777215) {
            float r = (color >> 16 & 255) / 255f;
            float g = (color >> 8 & 255) / 255f;
            float b = (color & 255) / 255f;
            tessellator.setColorOpaque_F(r, g, b);
        }

        TileEntityLargePot potData = block.getAttachedPotEntity(world, x, y, z);
        if (potData == null)
            potData = new TileEntityLargePot();
        if (itemRenderType == 1)
            renderCrossedSquares(world, renderer, itemBlock, x, y, z, potData);
        else
            renderer.renderBlockByRenderType(itemBlock, x, y, z);

        tessellator.addTranslation(0, +.0625f, 0);

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory () {
        return false;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.transformPlantRenderID;
    }

    private boolean renderCrossedSquares(IBlockAccess world, RenderBlocks renderer, Block block, int x, int y, int z, TileEntityLargePot potData)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
        int l = block.colorMultiplier(renderer.blockAccess, x, y, z);
        if (l == world.getBiomeGenForCoords(x, z).getBiomeGrassColor())
            l = ColorizerGrass.getGrassColor(potData.getBiomeTemperature(), potData.getBiomeHumidity());

        float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        tessellator.setColorOpaque_F(f, f1, f2);
        double d1 = (double)x;
        double d2 = (double)y;
        double d0 = (double)z;

        renderer.drawCrossedSquares(block, renderer.blockAccess.getBlockMetadata(x, y, z), d1, d2, d0, 1.0F);
        return true;
    }
}
