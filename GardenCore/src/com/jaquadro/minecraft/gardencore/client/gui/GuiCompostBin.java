package com.jaquadro.minecraft.gardencore.client.gui;

import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityCompostBin;
import com.jaquadro.minecraft.gardencore.inventory.ContainerCompostBin;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCompostBin extends GuiContainer
{
    private static final ResourceLocation compostBinGuiTextures = new ResourceLocation("gardencore", "textures/gui/compostBin.png");
    private TileEntityCompostBin tileCompost;

    private Slot hoveredSlot;

    public GuiCompostBin (InventoryPlayer inventory, TileEntityCompostBin tileEntity) {
        super(new ContainerCompostBin(inventory, tileEntity));
        tileCompost = tileEntity;
    }

    @Override
    public void drawScreen (int mouseX, int mouseY, float dt) {
        hoveredSlot = null;
        for (int i = 0, n = inventorySlots.inventorySlots.size(); i < n; i++) {
            Slot slot = inventorySlots.getSlot(i);
            if (func_146978_c(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, mouseX, mouseY))
                hoveredSlot = slot;
        }

        super.drawScreen(mouseX, mouseY, dt);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        String name = this.tileCompost.hasCustomInventoryName() ? this.tileCompost.getInventoryName() : I18n.format(this.tileCompost.getInventoryName(), new Object[0]);
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);

        for (int i = 0, n = inventorySlots.inventorySlots.size(); i < n; i++)
            drawSlotHighlight(inventorySlots.getSlot(i));

        GL11.glPopAttrib();
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

    protected void drawSlotHighlight (Slot slot) {
        if (hoveredSlot == null || isInPlayerInventory(hoveredSlot) || slot == hoveredSlot)
            return;

        if (mc.thePlayer.inventory.getItemStack() == null) {
            if (slot != null && slot.getHasStack() && hoveredSlot.isItemValid(slot.getStack())) {
                zLevel += 100;
                drawGradientRect(slot.xDisplayPosition, slot.yDisplayPosition, slot.xDisplayPosition + 16, slot.yDisplayPosition + 16, 0x80ffffff, 0x80ffffff);
                zLevel -= 100;
            }
        }
    }

    private boolean isInPlayerInventory (Slot slot) {
        return slot.inventory == mc.thePlayer.inventory;
    }
}
