package com.jaquadro.minecraft.modularpots.client.gui.inventory;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.inventory.ContainerGarden;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityGarden;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiGardenLayout extends GuiContainer
{
    private static final ResourceLocation guiTextures = new ResourceLocation(ModularPots.MOD_ID, "textures/gui/container/gardenLayout.png");
    private TileEntityGarden tileGarden;

    public GuiGardenLayout (InventoryPlayer inventory, TileEntityGarden tileEntity) {
        super(new ContainerGarden(inventory, tileEntity));
        tileGarden = tileEntity;

        xSize = 176;
        ySize = 204;
    }

    @Override
    protected void drawGuiContainerForegroundLayer (int p_146979_1_, int p_146979_2_) {
        String name = tileGarden.hasCustomInventoryName() ? tileGarden.getInventoryName() : I18n.format(tileGarden.getInventoryName(), new Object[0]);
        fontRendererObj.drawString(name, 8, 6, 4210752);
        fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer (float var1, int var2, int var3) {
        GL11.glColor4f(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(guiTextures);

        int guiX = (width - xSize) / 2;
        int guiY = (height - ySize) / 2;
        drawTexturedModalRect(guiX, guiY, 0, 0, xSize, ySize);
    }
}
