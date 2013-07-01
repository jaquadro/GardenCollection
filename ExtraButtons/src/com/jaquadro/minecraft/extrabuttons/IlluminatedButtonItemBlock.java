package com.jaquadro.minecraft.extrabuttons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

public class IlluminatedButtonItemBlock extends ItemBlock
{
    public IlluminatedButtonItemBlock (int id)
    {
        super(id);
        setHasSubtypes(true);
        setItemName("illuminatedButton");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getIconFromDamage(int data)
    {
        IlluminatedButton block = (IlluminatedButton)ExtraButtons.illuminatedButton;
        return block.getBlockTextureFromIndex(IlluminatedButton.getBlockFromDye(data));
    }

    @Override
    public int getMetadata (int damageValue) {
        return damageValue;
    }

    @Override
    public String getItemNameIS (ItemStack itemStack)
    {
        return getItemName() + "." + ItemDye.dyeColorNames[IlluminatedButton.getBlockFromDye(itemStack.getItemDamage())];
    }
}
