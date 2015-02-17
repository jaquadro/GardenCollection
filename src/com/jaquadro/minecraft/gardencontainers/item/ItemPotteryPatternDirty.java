package com.jaquadro.minecraft.gardencontainers.item;

import com.jaquadro.minecraft.gardencontainers.GardenContainers;
import com.jaquadro.minecraft.gardencontainers.config.PatternConfig;
import com.jaquadro.minecraft.gardencontainers.core.ModItems;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemPotteryPatternDirty extends Item
{
    public ItemPotteryPatternDirty (String unlocalizedName) {
        setUnlocalizedName(unlocalizedName);
        setMaxStackSize(64);
        setHasSubtypes(false);
        setTextureName(GardenContainers.MOD_ID + ":pottery_pattern_dirt");
        setCreativeTab(ModCreativeTabs.tabGardenCore);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation (ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        for (String s : StatCollector.translateToLocal("item.potteryPatternDirty.description").split("\\\\n"))
            list.add(s);
    }

    @Override
    public boolean onItemUse (ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (side != 1)
            return false;

        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        if (block instanceof BlockCauldron) {
            int waterLevel = BlockCauldron.func_150027_b(meta);
            if (waterLevel == 0)
                return false;

            int index = getPatternIndex(world);
            if (index == -1)
                return false;

            PatternConfig pattern = GardenContainers.config.getPattern(index);
            if (pattern == null)
                return false;

            ItemStack stamp = new ItemStack(ModItems.potteryPattern, 1, pattern.getId());

            itemStack.stackSize--;

            world.spawnEntityInWorld(new EntityItem(world, x + .5, y + 1.5, z + .5, stamp));

            return true;
        }

        return false;
    }

    private int getPatternIndex (World world) {
        int count = GardenContainers.config.getPatternCount();
        int[] accumWeights = new int[count + 1];

        if (count == 0)
            return -1;

        for (int i = 1; i <= count; i++) {
            PatternConfig pattern = GardenContainers.config.getPattern(i);
            accumWeights[i] = accumWeights[i - 1] + pattern.getWeight();
        }

        int maxWeight = accumWeights[accumWeights.length - 1];
        if (maxWeight == 0)
            return -1;

        int pick = world.rand.nextInt(maxWeight);
        int index = 1;
        for (int i = 1; i <= count; i++) {
            if (pick >= accumWeights[i - 1])
                index = i;
        }

        return index;
    }
}
