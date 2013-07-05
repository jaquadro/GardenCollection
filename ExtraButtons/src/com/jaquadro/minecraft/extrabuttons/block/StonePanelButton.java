package com.jaquadro.minecraft.extrabuttons.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.util.Icon;

public class StonePanelButton extends PanelButton
{
    public StonePanelButton (int id)
    {
        super(id, false);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int side, int data)
    {
        return Block.stone.getBlockTextureFromSide(1);
    }
}
