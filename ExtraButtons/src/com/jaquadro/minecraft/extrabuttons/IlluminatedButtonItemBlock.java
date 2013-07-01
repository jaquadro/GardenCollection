package com.jaquadro.minecraft.extrabuttons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
        int id = block.getBlockTextureFromIndex(IlluminatedButton.getBlockFromDye(data));
        System.out.println("IconFromDamage: data=" + data + " id=" + id);
        return id;
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

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        if (!super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata))
            return false;

        TileEntityButton te = (TileEntityButton)world.getBlockTileEntity(x, y, z);
        if (te != null)
            te.colorIndex = (byte)stack.getItemDamage();

        return true;
    }
}
