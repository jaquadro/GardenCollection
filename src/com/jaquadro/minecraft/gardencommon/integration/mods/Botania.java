package com.jaquadro.minecraft.gardencommon.integration.mods;

import com.jaquadro.minecraft.gardencommon.integration.IntegrationModule;
import com.jaquadro.minecraft.gardencore.api.*;
import com.jaquadro.minecraft.gardencore.api.plant.*;
import com.jaquadro.minecraft.gardencore.block.BlockGarden;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.client.renderer.plant.DoublePlantRenderer;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Botania extends IntegrationModule
{
    public static final String MOD_ID = "Botania";

    private static Block flower1;
    private static Block flower2;

    @Override
    public String getModID () {
        return MOD_ID;
    }

    @Override
    public void init () throws Throwable {
        flower1 = GameRegistry.findBlock(MOD_ID, "doubleFlower1");
        flower2 = GameRegistry.findBlock(MOD_ID, "doubleFlower2");

        PlantRegistry plantReg = PlantRegistry.instance();

        IPlantRenderer doubleFlowerRender = new DoubleFlowerRenderer();
        plantReg.registerPlantRenderer(MOD_ID, "flower", PlantRegistry.CROSSED_SQUARES_RENDERER);
        plantReg.registerPlantRenderer(MOD_ID, "shinyFlower", PlantRegistry.CROSSED_SQUARES_RENDERER);
        plantReg.registerPlantRenderer(MOD_ID, "doubleFlower1", doubleFlowerRender);
        plantReg.registerPlantRenderer(MOD_ID, "doubleFlower2", doubleFlowerRender);

        IPlantInfo doubleFlowerInfo = new DoubleFlowerPlantInfo();
        plantReg.registerPlantInfo(MOD_ID, "doubleFlower1", doubleFlowerInfo);
        plantReg.registerPlantInfo(MOD_ID, "doubleFlower2", doubleFlowerInfo);

        GardenCoreAPI.instance().registerBonemealHandler(new BonemealHandler());

        for (int i : new int[] { 2, 3, 6, 9, 15 }) {
            plantReg.registerPlantInfo(MOD_ID, "flower", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL));
            plantReg.registerPlantInfo(MOD_ID, "shinyFlower", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL));
        }
        for (int i : new int[] { 4 }) {
            plantReg.registerPlantInfo(MOD_ID, "flower", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));
            plantReg.registerPlantInfo(MOD_ID, "shinyFlower", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));
        }

        plantReg.registerPlantInfo(MOD_ID, "specialFlower", new SimplePlantInfo(PlantType.INVALID, PlantSize.MEDIUM));
    }

    @Override
    public void postInit () throws Throwable {

    }

    private static class BonemealHandler implements IBonemealHandler
    {
        @Override
        public boolean applyBonemeal (World world, int x, int y, int z, BlockGarden hostBlock, int slot) {
            TileEntityGarden te = hostBlock.getTileEntity(world, x, y, z);

            Block block = hostBlock.getPlantBlockFromSlot(world, x, y, z, slot);
            int meta = hostBlock.getPlantMetaFromSlot(world, x, y, z, slot);

            Block flower = GameRegistry.findBlock(MOD_ID, "flower");

            if (block == flower) {
                ItemStack upgrade = ((meta & 0x8) == 0) ? new ItemStack(flower1, 1, meta) : new ItemStack(flower2, 1, meta & 0x7);
                if (hostBlock.isPlantValidForSlot(world, x, y, z, slot, PlantItem.getForItem(upgrade))) {
                    te.setInventorySlotContents(slot, upgrade);
                    return true;
                }
            }

            return false;
        }
    }

    private static class DoubleFlowerPlantInfo extends DefaultPlantInfo
    {
        @Override
        public int getPlantHeight (Block block, int meta) {
            if (block == flower1 || block == flower2)
                return  2;

            return 1;
        }

        @Override
        public int getPlantSectionMeta (Block block, int meta, int section) {
            if (block == flower1 || block == flower2) {
                switch (section) {
                    case 2: return meta & 0x7;
                    case 1: return meta | 0x8;
                }
            }

            return meta;
        }
    }

    private static class DoubleFlowerRenderer extends DoublePlantRenderer
    {
        @Override
        public IIcon getIcon (Block block, IBlockAccess blockAccess, int meta) {
            return block.getIcon(0, meta);
        }
    }
}
