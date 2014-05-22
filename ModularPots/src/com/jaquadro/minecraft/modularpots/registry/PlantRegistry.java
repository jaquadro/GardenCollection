package com.jaquadro.minecraft.modularpots.registry;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.block.support.UniqueMetaIdentifier;
import com.jaquadro.minecraft.modularpots.core.ModItems;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public class PlantRegistry
{
    private static PlantRegistry instance;

    private List<UniqueMetaIdentifier> blacklist;

    public static PlantRegistry instance () {
        if (instance == null)
            new PlantRegistry();
        return instance;
    }

    protected PlantRegistry () {
        instance = this;

        blacklist = new ArrayList<UniqueMetaIdentifier>();
    }

    public boolean isBlacklisted (UniqueMetaIdentifier id) {
        return blacklist.contains(id);
    }

    public boolean isBlacklisted (ItemStack itemStack) {
        return isBlacklisted(ModItems.getUniqueMetaID(itemStack));
    }

    public void addToBlacklist (ItemStack itemStack) {
        UniqueMetaIdentifier id = ModItems.getUniqueMetaID(itemStack);
        if (!blacklist.contains(id)) {
            blacklist.add(id);
            FMLLog.log(ModularPots.MOD_ID, Level.INFO, "Blacklisting plant " + id.toString());
        }
    }

    public void addToBlacklist (NBTTagCompound tagData) {
        if (tagData.hasKey("items", Constants.NBT.TAG_LIST)) {
            NBTTagList list = tagData.getTagList("items", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.tagCount(); i++)
                addToBlacklist(list.getCompoundTagAt(i));
            return;
        }

        try {
            ItemStack item = ItemStack.loadItemStackFromNBT(tagData);
            if (item != null)
                addToBlacklist(item);
        }
        catch (Exception e) { }
    }
}
