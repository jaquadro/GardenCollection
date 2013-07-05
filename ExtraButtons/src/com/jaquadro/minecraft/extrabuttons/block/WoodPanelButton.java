package com.jaquadro.minecraft.extrabuttons.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.util.Icon;

public class WoodPanelButton extends PanelButton
{
    public WoodPanelButton (int id)
    {
        super(id, true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int side, int data)
    {
        return Block.planks.getBlockTextureFromSide(1);
    }
}
