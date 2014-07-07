package com.jaquadro.minecraft.extrabuttons.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class StonePanelButton extends PanelButton
{
    public StonePanelButton ()
    {
        super(false);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int data)
    {
        return Blocks.stone.getBlockTextureFromSide(1);
    }
}
