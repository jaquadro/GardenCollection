package com.jaquadro.minecraft.extrabuttons.block;

import net.minecraft.block.BlockButton;
import net.minecraft.world.IBlockAccess;

public class PanelButton extends BlockButton
{
    public PanelButton (int id, int texture)
    {
        super(id, texture, false);
    }

    @Override
    public void setBlockBoundsBasedOnState (IBlockAccess world, int x, int y, int z)
    {
        int data = world.getBlockMetadata(x, y, z);
        this.updateBlockBoundsWithState(data);
    }

    private void updateBlockBoundsWithState (int data)
    {
        int direction = data & 7;
        boolean isPressed = (data & 8) > 0;
        float var4 = 0.075F;
        float var5 = 0.925F;
        float var6 = 0.4375F;
        float depth = 0.125F;

        if (isPressed) {
            depth = 0.0625F;
        }

        if (direction == 1) {
            this.setBlockBounds(0.0F, var4, 0.5F - var6, depth, var5, 0.5F + var6);
        }
        else if (direction == 2) {
            this.setBlockBounds(1.0F - depth, var4, 0.5F - var6, 1.0F, var5, 0.5F + var6);
        }
        else if (direction == 3) {
            this.setBlockBounds(0.5F - var6, var4, 0.0F, 0.5F + var6, var5, depth);
        }
        else if (direction == 4) {
            this.setBlockBounds(0.5F - var6, var4, 1.0F - depth, 0.5F + var6, var5, 1.0F);
        }
    }

    @Override
    public void setBlockBoundsForItemRender ()
    {
        float x = 0.4375F;
        float y = 0.4375F;
        float z = 0.125F;
        this.setBlockBounds(0.5F - x, 0.5F - y, 0.5F - z, 0.5F + x, 0.5F + y, 0.5F + z);
    }
}
