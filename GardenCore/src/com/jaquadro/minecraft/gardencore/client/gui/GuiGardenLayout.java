package com.jaquadro.minecraft.gardencore.client.gui;

import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.inventory.ContainerGarden;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiGardenLayout extends GuiContainer
{
    private static final ResourceLocation guiTextures = new ResourceLocation("gardencore", "textures/gui/gardenLayout.png");
    private TileEntityGarden tileGarden;

    private Slot hoveredSlot;

    private static final int slotBaseX = 44;
    private static final int slotBaseY = 18;
    private static final int smDisabledX = 176;
    private static final int smDisabledY = 0;

    public GuiGardenLayout (InventoryPlayer inventory, TileEntityGarden tileEntity) {
        super(new ContainerGarden(inventory, tileEntity));
        tileGarden = tileEntity;

        xSize = 176;
        ySize = 204;
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
    protected void drawGuiContainerForegroundLayer (int p_146979_1_, int p_146979_2_) {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        String name = tileGarden.hasCustomInventoryName() ? tileGarden.getInventoryName() : I18n.format(tileGarden.getInventoryName(), new Object[0]);
        fontRendererObj.drawString(name, 8, 6, 4210752);
        fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, ySize - 96 + 2, 4210752);

        for (int i = 0, n = inventorySlots.inventorySlots.size(); i < n; i++)
            drawSlotHighlight(inventorySlots.getSlot(i));

        GL11.glPopAttrib();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer (float var1, int var2, int var3) {
        GL11.glColor4f(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(guiTextures);

        int guiX = (width - xSize) / 2;
        int guiY = (height - ySize) / 2;
        drawTexturedModalRect(guiX, guiY, 0, 0, xSize, ySize);

        if (!tileGarden.isAttachedNeighbor(tileGarden.xCoord - 1, tileGarden.yCoord, tileGarden.zCoord - 1))
            drawTexturedModalRect(guiX + slotBaseX, guiY + slotBaseY, smDisabledX, smDisabledY, 16, 16);
        if (!tileGarden.isAttachedNeighbor(tileGarden.xCoord, tileGarden.yCoord, tileGarden.zCoord - 1))
            drawTexturedModalRect(guiX + slotBaseX + 18 * 2, guiY + slotBaseY, smDisabledX, smDisabledY, 16, 16);
        if (!tileGarden.isAttachedNeighbor(tileGarden.xCoord + 1, tileGarden.yCoord, tileGarden.zCoord - 1))
            drawTexturedModalRect(guiX + slotBaseX + 18 * 4, guiY + slotBaseY, smDisabledX, smDisabledY, 16, 16);
        if (!tileGarden.isAttachedNeighbor(tileGarden.xCoord + 1, tileGarden.yCoord, tileGarden.zCoord))
            drawTexturedModalRect(guiX + slotBaseX + 18 * 4, guiY + slotBaseY + 18 * 2, smDisabledX, smDisabledY, 16, 16);
        if (!tileGarden.isAttachedNeighbor(tileGarden.xCoord + 1, tileGarden.yCoord, tileGarden.zCoord + 1))
            drawTexturedModalRect(guiX + slotBaseX + 18 * 4, guiY + slotBaseY + 18 * 4, smDisabledX, smDisabledY, 16, 16);
        if (!tileGarden.isAttachedNeighbor(tileGarden.xCoord, tileGarden.yCoord, tileGarden.zCoord + 1))
            drawTexturedModalRect(guiX + slotBaseX + 18 * 2, guiY + slotBaseY + 18 * 4, smDisabledX, smDisabledY, 16, 16);
        if (!tileGarden.isAttachedNeighbor(tileGarden.xCoord - 1, tileGarden.yCoord, tileGarden.zCoord + 1))
            drawTexturedModalRect(guiX + slotBaseX, guiY + slotBaseY + 18 * 4, smDisabledX, smDisabledY, 16, 16);
        if (!tileGarden.isAttachedNeighbor(tileGarden.xCoord - 1, tileGarden.yCoord, tileGarden.zCoord))
            drawTexturedModalRect(guiX + slotBaseX, guiY + slotBaseY + 18 * 2, smDisabledX, smDisabledY, 16, 16);
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
