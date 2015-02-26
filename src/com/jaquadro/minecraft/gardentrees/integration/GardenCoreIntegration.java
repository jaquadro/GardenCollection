package com.jaquadro.minecraft.gardentrees.integration;

import com.jaquadro.minecraft.gardencore.api.GardenCoreAPI;
import com.jaquadro.minecraft.gardencore.api.IBonemealHandler;
import com.jaquadro.minecraft.gardencore.api.SaplingRegistry;
import com.jaquadro.minecraft.gardencore.api.WoodRegistry;
import com.jaquadro.minecraft.gardencore.block.BlockGarden;
import com.jaquadro.minecraft.gardentrees.core.ModBlocks;
import com.jaquadro.minecraft.gardentrees.world.gen.WorldGenStandardOrnTree;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class GardenCoreIntegration
{
    public static final String MOD_ID = "GardenCore";

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;

        SaplingRegistry saplingReg = SaplingRegistry.instance();

        Item sapling = Item.getItemFromBlock(Blocks.sapling);

        saplingReg.putExtendedData(sapling, 0, "sm_generator",
            WorldGenStandardOrnTree.SmallOakTree.FACTORY.create(Blocks.log, 0, Blocks.leaves, 0));
        saplingReg.putExtendedData(sapling, 1, "sm_generator",
            WorldGenStandardOrnTree.SmallSpruceTree.FACTORY.create(Blocks.log, 1, Blocks.leaves, 1));
        saplingReg.putExtendedData(sapling, 2, "sm_generator",
            WorldGenStandardOrnTree.SmallOakTree.FACTORY.create(Blocks.log, 2, Blocks.leaves, 2));
        saplingReg.putExtendedData(sapling, 3, "sm_generator",
            WorldGenStandardOrnTree.SmallJungleTree.FACTORY.create(Blocks.log, 3, Blocks.leaves, 3));
        saplingReg.putExtendedData(sapling, 4, "sm_generator",
            WorldGenStandardOrnTree.SmallAcaciaTree.FACTORY.create(Blocks.log2, 0, Blocks.leaves2, 0));
        saplingReg.putExtendedData(sapling, 5, "sm_generator",
            WorldGenStandardOrnTree.SmallOakTree.FACTORY.create(Blocks.log2, 1, Blocks.leaves2, 1));

        Item extSapling = Item.getItemFromBlock(ModBlocks.sapling);

        saplingReg.registerSapling(extSapling, 0, Blocks.log, 1, Blocks.leaves, 1);
        saplingReg.registerSapling(extSapling, 1, Blocks.log, 0, Blocks.leaves, 0);
        saplingReg.registerSapling(extSapling, 2, Blocks.log, 2, Blocks.leaves, 2);

        saplingReg.putExtendedData(extSapling, 0, "sm_generator",
            WorldGenStandardOrnTree.SmallSpruceTree.FACTORY.create(Blocks.log, 1, Blocks.leaves, 1));
        saplingReg.putExtendedData(extSapling, 1, "sm_generator",
            WorldGenStandardOrnTree.SmallOakTree.FACTORY.create(Blocks.log, 0, Blocks.leaves, 0));
        saplingReg.putExtendedData(extSapling, 2, "sm_generator",
            WorldGenStandardOrnTree.SmallOakTree.FACTORY.create(Blocks.log, 2, Blocks.leaves, 2));

        GardenCoreAPI.instance().registerBonemealHandler(new GardenBonemealHandler());

        WoodRegistry woodRegistry = WoodRegistry.instance();

        List<ItemStack> woodList = OreDictionary.getOres("treeWood");
        for (ItemStack item : woodList) {
            if (item == null)
                continue;

            Block block = Block.getBlockFromItem(item.getItem());
            woodRegistry.registerWoodType(block, item.getItemDamage());
        }
    }

    private static class GardenBonemealHandler implements IBonemealHandler
    {
        @Override
        public boolean applyBonemeal (World world, int x, int y, int z, BlockGarden hostBlock, int slot) {
            Block plantBlock = hostBlock.getPlantBlockFromSlot(world, x, y, z, slot);
            if (plantBlock == null)
                return false;

            Item sapling = Item.getItemFromBlock(plantBlock);
            int saplingMeta = hostBlock.getPlantMetaFromSlot(world, x, y, z, slot);

            WorldGenerator generator = (WorldGenerator) SaplingRegistry.instance().getExtendedData(sapling, saplingMeta, "sm_generator");
            if (generator == null)
                return false;

            NBTTagCompound storedTE = hostBlock.saveAndClearPlantedContents(world, x, y, z);

            if (!generator.generate(world, world.rand, x, y + 1, z))
                hostBlock.restorePlantedContents(world, x, y, z, storedTE);

            return true;
        }
    }
}
