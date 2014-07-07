package com.jaquadro.minecraft.modularpots.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

/**
 * Created by Justin on 5/26/2014.
 */
public class PlacementGrid
{
    public void render (EntityPlayer player, MovingObjectPosition target, float partialTick) {
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(0, 0, 0, .6f);
        GL11.glLineWidth(2);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);

        float expansion = 0.002f;
        Block block = player.worldObj.getBlock(target.blockX, target.blockY, target.blockZ);

        if (block.getMaterial() != Material.air) {
            block.setBlockBoundsBasedOnState(player.worldObj, target.blockX, target.blockY, target.blockZ);
            double adjPlayerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTick;
            double adjPlayerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTick;
            double adjPlayerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTick;
            AxisAlignedBB bound = block.getSelectedBoundingBoxFromPool(player.worldObj, target.blockX, target.blockY, target.blockZ)
                .expand(expansion, expansion, expansion)
                .getOffsetBoundingBox(-adjPlayerX, -adjPlayerY, -adjPlayerZ);
            drawLines(bound);
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected void drawLines (AxisAlignedBB bound) {
        Tessellator tessellator = Tessellator.instance;

        double midX = (bound.maxX + bound.minX) / 2;
        double midZ = (bound.maxZ + bound.minZ) / 2;
        double insetX = (bound.maxX - bound.minX) * (5 / 16f);
        double insetZ = (bound.maxZ - bound.minZ) * (5 / 16f);

        // Draw perimeter outline

        tessellator.startDrawing(3);
        tessellator.addVertex(bound.minX, bound.maxY, bound.minZ);
        tessellator.addVertex(bound.maxX, bound.maxY, bound.minZ);
        tessellator.addVertex(bound.maxX, bound.maxY, bound.maxZ);
        tessellator.addVertex(bound.minX, bound.maxY, bound.maxZ);
        tessellator.addVertex(bound.minX, bound.maxY, bound.minZ);
        tessellator.draw();

        // Draw interior diamond

        tessellator.startDrawing(3);
        tessellator.addVertex(midX, bound.maxY, midZ + insetZ);
        tessellator.addVertex(midX + insetX, bound.maxY, midZ);
        tessellator.addVertex(midX, bound.maxY, midZ - insetZ);
        tessellator.addVertex(midX - insetX, bound.maxY, midZ);
        tessellator.addVertex(midX, bound.maxY, midZ + insetZ);
        tessellator.draw();

        // Draw interior connectors

        tessellator.startDrawing(1);
        tessellator.addVertex(midX, bound.maxY, bound.minZ);
        tessellator.addVertex(midX, bound.maxY, midZ - insetZ);
        tessellator.addVertex(midX, bound.maxY, midZ + insetZ);
        tessellator.addVertex(midX, bound.maxY, bound.maxZ);
        tessellator.addVertex(bound.minX, bound.maxY, midZ);
        tessellator.addVertex(midX - insetX, bound.maxY, midZ);
        tessellator.addVertex(midX + insetX, bound.maxY, midZ);
        tessellator.addVertex(bound.maxX, bound.maxY, midZ);
        tessellator.draw();
    }
}
