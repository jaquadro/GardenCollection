package com.jaquadro.minecraft.modularpots.client.gui.inventory;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.block.LargePot;
import com.jaquadro.minecraft.modularpots.inventory.ContainerPotteryTable;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityPotteryTable;
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
    private static final ResourceLocation guiTextures = new ResourceLocation(ModularPots.MOD_ID, "textures/gui/container/potteryTable.png");
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

        for (int i = 0; i < 6; i++) {
            Slot slot = inventorySlots.getSlot(3 + i);
            if (slot != null && slot.getStack() != null) {
                ItemStack item = slot.getStack();
                if (item.getItem() == ModularPots.potteryPattern) {
                    LargePot largePot = (LargePot) ModularPots.largePot;
                    IIcon pattern = largePot.getOverlayIcon(item.getItemDamage());

                    if (pattern != null) {
                        GL11.glDisable(GL11.GL_LIGHTING);
                        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                        drawTexturedModelRectFromIcon(slot.xDisplayPosition - 18, slot.yDisplayPosition, pattern, 16, 16);
                        GL11.glEnable(GL11.GL_LIGHTING);
                    }
                }
            }
        }
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
