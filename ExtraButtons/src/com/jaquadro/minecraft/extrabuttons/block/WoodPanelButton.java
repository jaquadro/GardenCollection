package com.jaquadro.minecraft.extrabuttons.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class WoodPanelButton extends PanelButton
{
    public WoodPanelButton ()
    {
        super(true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int data)
    {
        return Blocks.planks.getBlockTextureFromSide(1);
    }
}
