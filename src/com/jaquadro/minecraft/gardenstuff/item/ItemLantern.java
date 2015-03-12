package com.jaquadro.minecraft.gardenstuff.item;

import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.block.BlockLantern;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityLantern;
import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemLantern extends ItemBlock
{
    public ItemLantern (Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata (int meta) {
        return meta;
    }

    @Override
    public boolean placeBlockAt (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        if (!super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata & 15))
            return false;

        TileEntityLantern tile = (TileEntityLantern) world.getTileEntity(x, y, z);
        if (tile != null) {
            if (ModBlocks.lantern.isGlass(stack))
                tile.setHasGlass(true);
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation (ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        if (ModBlocks.lantern.isGlass(itemStack)) {
            String glassName = Blocks.stained_glass.getUnlocalizedName() + "." + ItemDye.field_150923_a[BlockColored.func_150032_b(itemStack.getItemDamage())];
            list.add(StatCollector.translateToLocal(glassName + ".name"));
        }

        String contents = StatCollector.translateToLocal(ModBlocks.makeName("lanternSource")) + ": " + EnumChatFormatting.YELLOW;
        contents += StatCollector.translateToLocal(ModBlocks.makeName("lanternSource.") + getLanternSourceKey(ModBlocks.lantern.getLightSource(itemStack)));
        list.add(contents);
    }

    public ItemStack makeItemStack (int count, int meta, boolean hasGlass) {
        ItemStack stack = new ItemStack(this, count, meta);

        if (hasGlass) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("glass", true);
            stack.setTagCompound(tag);
        }

        return stack;
    }

    private String getLanternSourceKey (TileEntityLantern.LightSource lightSource) {
        switch (lightSource) {
            case TORCH: return "torch";
            case REDSTONE_TORCH: return "redstoneTorch";
            case GLOWSTONE: return "glowstone";
            case CANDLE: return "candle";
            case FIREFLY: return "firefly";
            default: return "none";
        }
    }
}
