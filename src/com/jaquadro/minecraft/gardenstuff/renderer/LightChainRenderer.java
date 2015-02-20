package com.jaquadro.minecraft.gardenstuff.renderer;

import com.jaquadro.minecraft.gardencore.api.block.IChainAttachable;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardenstuff.block.BlockLightChain;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.src.FMLRenderAccessLibrary;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class LightChainRenderer implements ISimpleBlockRenderingHandler
{
    private static final Vec3[] defaultAttachPoints = new Vec3[] {
        Vec3.createVectorHelper(.03125, 1, .03125), Vec3.createVectorHelper(.03125, 1, 1 - .03125),
        Vec3.createVectorHelper(1 - .03125, 1, .03125), Vec3.createVectorHelper(1 - .03125, 1, 1 - .03125),
    };
    private static final Vec3[] singleAttachPoint = new Vec3[] {
        Vec3.createVectorHelper(.5, 1, .5),
    };

    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (block instanceof BlockLightChain)
            return renderWorldBlock(world, x, y, z, (BlockLightChain)block, modelId, renderer);

        return false;
    }

    private boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, BlockLightChain block, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));

        int color = block.colorMultiplier(world, x, y, z);
        float r = (float)(color >> 16 & 255) / 255f;
        float g = (float)(color >> 8 & 255) / 255f;
        float b = (float)(color & 255) / 255f;

        tessellator.setColorOpaque_F(r, g, b);

        IIcon icon = block.getIcon(0, world.getBlockMetadata(x, y, z));
        if (renderer.hasOverrideBlockTexture())
            icon = renderer.overrideBlockTexture;

        int yMin = block.findMinY(world, x, y, z);
        int yMax = block.findMaxY(world, x, y, z);

        for (Vec3 point : block.getAttachPoints(world, x, y, z)) {
            float height = yMax - yMin + 2 - (float)point.yCoord;

            double cx = .5f;
            double cz = .5f;
            double dx = cx - point.xCoord;
            double dz = cz - point.zCoord;
            double yt = y + 1;
            double yb = y;

            double localYMin = yMin + point.yCoord - 1;

            if (y == yMin)
                yb = y - 1 + point.yCoord;

            double lerpB = 1 - ((yb - localYMin) / height);
            double lerpT = 1 - ((yt - localYMin) / height);

            drawBetween(renderer, icon, x + dx * lerpB + cx, yb, z + dz * lerpB + cz, x + dx * lerpT + cx, yt, z + dz * lerpT + cz);
        }


        /*double lerpB = ((y - yMin) / height) * .5f;
        double lerpT = ((y + 1 - yMin) / height) * .5f;

        drawBetween(renderer, icon, .005 + x + lerpB, y, z + lerpB, x + lerpT, y + 1, z + lerpT);
        drawBetween(renderer, icon, .005 + x + 1 - lerpB, y, z + lerpB, x + 1 - lerpT, y + 1, z + lerpT);
        drawBetween(renderer, icon, .005 + x + lerpB, y, z + 1 - lerpB, x + lerpT, y + 1, z + 1 - lerpT);
        drawBetween(renderer, icon, .005 + x + 1 - lerpB, y, z + 1 - lerpB, x + 1 - lerpT, y + 1, z + 1 - lerpT);*/

        FMLRenderAccessLibrary.renderWorldBlock(renderer, world, x, y, z, ModBlocks.gardenProxy, ModBlocks.gardenProxy.getRenderType());

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return false;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.lightChainRenderID;
    }

    private void drawBetween (RenderBlocks renderer, IIcon icon, double x0, double y0, double z0, double x1, double y1, double z1) {
        Tessellator tessellator = Tessellator.instance;

        double minU = icon.getMinU();
        double minV = icon.getMinV();
        double maxU = icon.getMaxU();
        double maxV = icon.getMaxV();

        Vec3 vT = Vec3.createVectorHelper(x1 - x0, y1 - y0, z1 - z0);
        Vec3 vB = Vec3.createVectorHelper(x1 - x0, 0, z1 - z0);
        Vec3 vN = vT.crossProduct(vB);
        Vec3 vU = vT.crossProduct(vN);

        vU = vU.normalize();
        vN = vN.normalize();

        double vUx = vU.xCoord / 2;
        double vUy = vU.yCoord / 2;
        double vUz = vU.zCoord / 2;

        if (vUx == 0 && vUy == 0) {
            vUx = -.5;
            vUz = .5;
        }

        tessellator.addVertexWithUV(x0 + vUx, y0 + vUy, z0 + vUz, maxU, minV);
        tessellator.addVertexWithUV(x0 - vUx, y0 - vUy, z0 - vUz, minU, minV);
        tessellator.addVertexWithUV(x1 - vUx, y1 - vUy, z1 - vUz, minU, maxV);
        tessellator.addVertexWithUV(x1 + vUx, y1 + vUy, z1 + vUz, maxU, maxV);

        tessellator.addVertexWithUV(x1 + vUx, y1 + vUy, z1 + vUz, maxU, maxV);
        tessellator.addVertexWithUV(x1 - vUx, y1 - vUy, z1 - vUz, minU, maxV);
        tessellator.addVertexWithUV(x0 - vUx, y0 - vUy, z0 - vUz, minU, minV);
        tessellator.addVertexWithUV(x0 + vUx, y0 + vUy, z0 + vUz, maxU, minV);

        double vNx = vN.xCoord / 2;
        double vNy = vN.yCoord / 2;
        double vNz = vN.zCoord / 2;

        if (vNx == 0 && vNy == 0) {
            vNx = .5;
            vNz = .5;
        }

        tessellator.addVertexWithUV(x0 + vNx, y0 + vNy, z0 + vNz, maxU, minV);
        tessellator.addVertexWithUV(x0 - vNx, y0 - vNy, z0 - vNz, minU, minV);
        tessellator.addVertexWithUV(x1 - vNx, y1 - vNy, z1 - vNz, minU, maxV);
        tessellator.addVertexWithUV(x1 + vNx, y1 + vNy, z1 + vNz, maxU, maxV);

        tessellator.addVertexWithUV(x1 + vNx, y1 + vNy, z1 + vNz, maxU, maxV);
        tessellator.addVertexWithUV(x1 - vNx, y1 - vNy, z1 - vNz, minU, maxV);
        tessellator.addVertexWithUV(x0 - vNx, y0 - vNy, z0 - vNz, minU, minV);
        tessellator.addVertexWithUV(x0 + vNx, y0 + vNy, z0 + vNz, maxU, minV);
    }
}
