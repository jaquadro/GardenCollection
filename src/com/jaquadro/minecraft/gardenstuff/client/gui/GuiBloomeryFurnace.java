package com.jaquadro.minecraft.gardenstuff.client.gui;

import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityBloomeryFurnace;
import com.jaquadro.minecraft.gardenstuff.container.ContainerBloomeryFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiBloomeryFurnace extends GuiContainer
{
    private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("gardenstuff", "textures/gui/bloomery_furnace.png");
    private TileEntityBloomeryFurnace tileFurnace;

    public GuiBloomeryFurnace (InventoryPlayer inventory, TileEntityBloomeryFurnace tile) {
        super(new ContainerBloomeryFurnace(inventory, tile));
        tileFurnace = tile;
    }

    @Override
    protected void drawGuiContainerForegroundLayer (int p_146979_1_, int p_146979_2_) {
        String name = this.tileFurnace.hasCustomInventoryName() ? this.tileFurnace.getInventoryName() : I18n.format(this.tileFurnace.getInventoryName(), new Object[0]);
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer (float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(furnaceGuiTextures);

        int halfW = (this.width - this.xSize) / 2;
        int halfH = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(halfW, halfH, 0, 0, this.xSize, this.ySize);

        if (this.tileFurnace.isBurning())
        {
            int i1 = this.tileFurnace.getBurnTimeRemainingScaled(13);
            this.drawTexturedModalRect(halfW + 56, halfH + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 1);
            i1 = this.tileFurnace.getCookProgressScaled(24);
            this.drawTexturedModalRect(halfW + 79, halfH + 34, 176, 14, i1 + 1, 16);
        }
    }
}
