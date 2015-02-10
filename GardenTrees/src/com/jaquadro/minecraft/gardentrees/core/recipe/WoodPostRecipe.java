package com.jaquadro.minecraft.gardentrees.core.recipe;

import com.jaquadro.minecraft.gardencore.api.WoodRegistry;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import com.jaquadro.minecraft.gardentrees.block.tile.TileEntityWoodProxy;
import com.jaquadro.minecraft.gardentrees.core.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class WoodPostRecipe implements IRecipe
{
    private static final Item[] axeList = new Item[] {
        Items.wooden_axe, Items.stone_axe, Items.iron_axe, Items.golden_axe, Items.diamond_axe
    };

    private UniqueMetaIdentifier woodType;

    public WoodPostRecipe (UniqueMetaIdentifier uid) {
        woodType = uid;
    }

    @Override
    public boolean matches (InventoryCrafting inventory, World world) {
        return getCraftingResult(inventory) != null;
    }

    @Override
    public ItemStack getCraftingResult (InventoryCrafting inventory) {
        int size = getGridSize(inventory);
        for (int row = 0; row < size - 1; row++) {
            for (int col = 0; col < size; col++) {
                ItemStack axe = inventory.getStackInRowAndColumn(col, row);
                if (!isValidAxe(axe))
                    continue;

                ItemStack wood = inventory.getStackInRowAndColumn(col, row + 1);
                if (wood == null)
                    continue;

                Block woodBlock = Block.getBlockFromItem(wood.getItem());
                int woodMeta = wood.getItemDamage();

                if (!WoodRegistry.instance().contains(woodBlock, woodMeta))
                    continue;

                if (woodBlock == woodType.getBlock() && woodMeta == woodType.meta)
                    return new ItemStack(ModBlocks.thinLog, 4, TileEntityWoodProxy.composeMetadata(woodBlock, woodMeta));
            }
        }

        return null;
    }

    @Override
    public int getRecipeSize () {
        return 4;
    }

    @Override
    public ItemStack getRecipeOutput () {
        return new ItemStack(ModBlocks.thinLog, 4, TileEntityWoodProxy.composeMetadata(woodType.getBlock(), woodType.meta));
    }

    private int getGridSize (InventoryCrafting inventory) {
        return (int)Math.sqrt(inventory.getSizeInventory());
    }

    private boolean isValidAxe (ItemStack itemStack) {
        if (itemStack == null)
            return false;

        Item item = itemStack.getItem();
        for (Item axe : axeList) {
            if (item == axe)
                return true;
        }

        return false;
    }
}
