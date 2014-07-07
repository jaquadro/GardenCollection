package com.jaquadro.minecraft.gardencontainers.client.gui;

import com.jaquadro.minecraft.gardencontainers.GardenContainers;
import com.jaquadro.minecraft.gardencontainers.block.BlockLargePot;
import com.jaquadro.minecraft.gardencontainers.block.tile.TileEntityPotteryTable;
import com.jaquadro.minecraft.gardencontainers.core.ModBlocks;
import com.jaquadro.minecraft.gardencontainers.core.ModItems;
import com.jaquadro.minecraft.gardencontainers.inventory.ContainerPotteryTable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiPotteryTable extends GuiContainer
{
    private static final ResourceLocation guiTextures = new ResourceLocation(GardenContainers.MOD_ID, "textures/gui/potteryTable.png");
    private TileEntityPotteryTable tilePotteryTable;

    public GuiPotteryTable (InventoryPlayer inventory, TileEntityPotteryTable tileEntity) {
        super(new ContainerPotteryTable(inventory, tileEntity));
        tilePotteryTable = tileEntity;

        xSize = 180;
        ySize = 222;
    }

    @Override
    protected void drawGuiContainerForegroundLayer (int p_146979_1_, int p_146979_2_) {
        String name = tilePotteryTable.hasCustomInventoryName() ? tilePotteryTable.getInventoryName() : I18n.format(tilePotteryTable.getInventoryName(), new Object[0]);
        fontRendererObj.drawString(name, 8, 6, 4210752);
        fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, ySize - 96 + 2, 4210752);

        DrawPatternColumn(0, 6, -18, 0);
        DrawPatternColumn(6, 12, 18, 0);
    }

    private void DrawPatternColumn (int start, int end, int offsetX, int offsetY) {
        zLevel = 100.0F;
        itemRender.zLevel = 100.0F;

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        for (int i = start; i < end; i++) {
            Slot slot = inventorySlots.getSlot(3 + i);
            if (slot != null && slot.getStack() != null) {
                ItemStack item = slot.getStack();
                if (item.getItem() == ModItems.potteryPattern) {
                    BlockLargePot largePot = ModBlocks.largePot;

                    IIcon pattern = largePot.getOverlayIcon(item.getItemDamage());
                    if (pattern != null)
                        drawTexturedModelRectFromIcon(slot.xDisplayPosition + offsetX, slot.yDisplayPosition + offsetY, pattern, 16, 16);
                }
            }
        }

        GL11.glEnable(GL11.GL_LIGHTING);

        itemRender.zLevel = 0.0F;
        zLevel = 0.0F;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer (float x, int y, int z) {
        GL11.glColor4f(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(guiTextures);

        int guiX = (width - xSize) / 2;
        int guiY = (height - ySize) / 2;
        drawTexturedModalRect(guiX, guiY, 0, 0, xSize, ySize);
    }
}
