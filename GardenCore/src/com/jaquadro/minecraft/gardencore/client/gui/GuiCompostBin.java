package com.jaquadro.minecraft.gardencore.client.gui;

import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityCompostBin;
import com.jaquadro.minecraft.gardencore.inventory.ContainerCompostBin;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCompostBin extends GuiContainer
{
    private static final ResourceLocation compostBinGuiTextures = new ResourceLocation(GardenCore.MOD_ID, "textures/gui/compostBin.png");
    private TileEntityCompostBin tileCompost;

    public GuiCompostBin (InventoryPlayer inventory, TileEntityCompostBin tileEntity) {
        super(new ContainerCompostBin(inventory, tileEntity));
        tileCompost = tileEntity;
    }

    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        String name = this.tileCompost.hasCustomInventoryName() ? this.tileCompost.getInventoryName() : I18n.format(this.tileCompost.getInventoryName(), new Object[0]);
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer (float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(compostBinGuiTextures);
        int halfW = (this.width - this.xSize) / 2;
        int halfH = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(halfW, halfH, 0, 0, this.xSize, this.ySize);

        if (tileCompost.binDecomposeTime > 0 || tileCompost.itemDecomposeCount > 0) {
            int timeRemaining = tileCompost.getDecomposeTimeRemainingScaled(24);
            drawTexturedModalRect(halfW + 89, halfH + 34, 176, 0, 24 - timeRemaining + 1, 16);
        }
    }
}
