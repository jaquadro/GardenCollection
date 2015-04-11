package com.jaquadro.minecraft.gardencore.integration;

import com.jaquadro.minecraft.gardencore.api.*;
import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import com.jaquadro.minecraft.gardencore.api.plant.SimplePlantInfo;
import com.jaquadro.minecraft.gardencore.block.BlockGarden;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WitcheryIntegration
{
    public static final String MOD_ID = "witchery";

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;

        initWood();

        GardenCoreAPI.instance().registerBonemealHandler(new BonemealHandler());

        PlantRegistry plantReg = PlantRegistry.instance();

        plantReg.registerPlantInfo(MOD_ID, "embermoss", new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));
        plantReg.registerPlantInfo(MOD_ID, "leapinglily", new SimplePlantInfo(PlantType.AQUATIC_COVER, PlantSize.LARGE));
        plantReg.registerPlantInfo(MOD_ID, "spanishmoss", new SimplePlantInfo(PlantType.HANGING_SIDE, PlantSize.LARGE));
        plantReg.registerPlantInfo(MOD_ID, "bramble", new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));
        plantReg.registerPlantInfo(MOD_ID, "glintweed", new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));
        plantReg.registerPlantInfo(MOD_ID, "voidbramble", new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));

        plantReg.registerPlantInfo(MOD_ID, "seedsartichoke", 0, new SimplePlantInfo(PlantType.AQUATIC_SURFACE, PlantSize.LARGE));
        plantReg.registerPlantInfo(MOD_ID, "seedswormwood", 0, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));
        plantReg.registerPlantInfo(MOD_ID, "seedsmandrake", 0, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));
        plantReg.registerPlantInfo(MOD_ID, "seedswolfsbane", 0, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));
        plantReg.registerPlantInfo(MOD_ID, "seedsbelladonna", 0, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));
        plantReg.registerPlantInfo(MOD_ID, "seedssnowbell", 0, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));
        plantReg.registerPlantInfo(MOD_ID, "garlic", 0, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));
    }

    private static void initWood () {
        Block log = GameRegistry.findBlock(MOD_ID, "witchlog");
        Block leaf = GameRegistry.findBlock(MOD_ID, "witchleaves");
        Item sapling = GameRegistry.findItem(MOD_ID, "witchsapling");

        WoodRegistry woodReg = WoodRegistry.instance();

        for (int i = 0; i < 3; i++)
            woodReg.registerWoodType(log, i);

        SaplingRegistry saplingReg = SaplingRegistry.instance();

        for (int i = 0; i < 3; i++)
            saplingReg.registerSapling(sapling, i, log, i, leaf, i);
    }

    private static class BonemealHandler implements IBonemealHandler
    {
        @Override
        public boolean applyBonemeal (World world, int x, int y, int z, BlockGarden hostBlock, int slot) {
            TileEntityGarden te = hostBlock.getTileEntity(world, x, y, z);

            ItemStack plantItem = te.getPlantInSlot(slot);
            if (plantItem == null)
                return false;

            if (plantItem.getItemDamage() < 4 && (
                plantItem.getItem() == GameRegistry.findItem(MOD_ID, "seedsbelladonna") ||
                plantItem.getItem() == GameRegistry.findItem(MOD_ID, "seedsartichoke") ||
                plantItem.getItem() == GameRegistry.findItem(MOD_ID, "seedswormwood") ||
                plantItem.getItem() == GameRegistry.findItem(MOD_ID, "seedsmandrake") ||
                plantItem.getItem() == GameRegistry.findItem(MOD_ID, "seedssnowbell"))
                ) {
                ItemStack upgrade = new ItemStack(plantItem.getItem(), 1, plantItem.getItemDamage() + 1);
                if (hostBlock.isPlantValidForSlot(world, x, y, z, slot, PlantItem.getForItem(world, upgrade))) {
                    te.setInventorySlotContents(slot, upgrade);
                    return true;
                }
            }
            else if (plantItem.getItemDamage() < 5 && (plantItem.getItem() == GameRegistry.findItem(MOD_ID, "garlic"))) {
                ItemStack upgrade = new ItemStack(plantItem.getItem(), 1, plantItem.getItemDamage() + 1);
                if (hostBlock.isPlantValidForSlot(world, x, y, z, slot, PlantItem.getForItem(world, upgrade))) {
                    te.setInventorySlotContents(slot, upgrade);
                    return true;
                }
            }
            else if (plantItem.getItemDamage() < 7 && (plantItem.getItem() == GameRegistry.findItem(MOD_ID, "seedswolfsbane"))) {
                ItemStack upgrade = new ItemStack(plantItem.getItem(), 1, plantItem.getItemDamage() + 1);
                if (hostBlock.isPlantValidForSlot(world, x, y, z, slot, PlantItem.getForItem(world, upgrade))) {
                    te.setInventorySlotContents(slot, upgrade);
                    return true;
                }
            }

            return false;
        }
    }
}
