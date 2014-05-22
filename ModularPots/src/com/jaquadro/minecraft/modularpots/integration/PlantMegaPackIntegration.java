package com.jaquadro.minecraft.modularpots.integration;

import com.jaquadro.minecraft.modularpots.ModularPots;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.oredict.OreDictionary;

public class PlantMegaPackIntegration
{
    public static final String MOD_ID = "plantmegapack";

    private static final String[] blacklist = new String[] {
        "waterKelpGiantGRN", "waterKelpGiantYEL", "waterCryptWendtii", "waterDwarfHairGrass", "waterMondoGrass",
        "waterBambooCoralYEL", "waterPulsingXenia", "waterPurpleSeaWhip", "waterRedSeaFan"
    };

    private static final String[] bonemealBlacklist = new String[] {
        "bambooAsper", "bambooFargesiaRobusta", "bambooGiantTimber", "bambooGolden", "bambooMoso",
        "bambooShortTassled", "bambooTimorBlack", "bambooTropicalBlue", "bambooWetForest"
    };

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;

        NBTTagList blacklistTags = new NBTTagList();
        for (String itemName : blacklist) {
            Item item = GameRegistry.findItem(MOD_ID, itemName);
            if (item == null)
                continue;

            ItemStack itemStack = new ItemStack(item, 1, 0);
            NBTTagCompound itemTag = new NBTTagCompound();
            itemStack.writeToNBT(itemTag);

            blacklistTags.appendTag(itemTag);
        }

        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("items", blacklistTags);

        FMLInterModComms.sendMessage(ModularPots.MOD_ID, "plantBlacklist", tag);

        NBTTagList bonemealBlacklistTags = new NBTTagList();
        for (String itemName : bonemealBlacklist) {
            Item item = GameRegistry.findItem(MOD_ID, itemName);
            if (item == null)
                continue;

            ItemStack itemStack = new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
            NBTTagCompound itemTag = new NBTTagCompound();
            itemStack.writeToNBT(itemTag);

            bonemealBlacklistTags.appendTag(itemTag);
        }

        tag = new NBTTagCompound();
        tag.setTag("items", bonemealBlacklistTags);

        FMLInterModComms.sendMessage(ModularPots.MOD_ID, "bonemealBlacklist", tag);
    }
}
