package com.jaquadro.minecraft.modularpots.item;

import com.jaquadro.minecraft.modularpots.ModularPots;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class ItemSoilKit extends Item
{
    @SideOnly(Side.CLIENT)
    private Icon icon;

    public ItemSoilKit (int id) {
        super(id);
        setMaxStackSize(64);
        setCreativeTab(ModularPots.tabModularPots);
    }

    @Override
    public boolean onItemUse (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (player.inventory.getFirstEmptyStack() == -1 && player.inventory.getCurrentItem().stackSize > 1)
            return false;

        BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
        int temperature = (int)(Math.min(1, Math.max(0, biome.temperature)) * 255) & 255;
        int rainfall = (int)(Math.min(1, Math.max(0, biome.rainfall)) * 255) & 255;

        ItemStack usedKit = new ItemStack(ModularPots.soilTestKitUsed, 1, rainfall << 8 | temperature);

        world.playSoundAtEntity(player, "step.grass", 1.0f, 1.0f);

        if (player.inventory.getCurrentItem().stackSize == 1)
            player.inventory.setInventorySlotContents(player.inventory.currentItem, usedKit);
        else {
            stack.stackSize--;
            player.inventory.setInventorySlotContents(player.inventory.getFirstEmptyStack(), usedKit);
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage (int par1) {
        return icon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons (IconRegister iconRegister) {
        icon = iconRegister.registerIcon(ModularPots.MOD_ID + ":soil_test_kit");
    }
}
