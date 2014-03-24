package com.jaquadro.minecraft.modularpots.integration;

import com.jaquadro.minecraft.modularpots.ModBlocks;
import com.jaquadro.minecraft.modularpots.ModularPots;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TreecapitatorIntegration
{
    public static final String TREECAPITATOR_ID = "TreeCapitator";

    public static void init () {
        if (!Loader.isModLoaded(TREECAPITATOR_ID))
            return;

        String thinLogs1 = ModBlocks.getQualifiedName(ModBlocks.thinLog);
        String mcLeaves1 = "18";
        String mcLeaves2 = "161";

        NBTTagCompound tpModCfg = new NBTTagCompound();
        tpModCfg.setString("modID", ModularPots.MOD_ID);

        NBTTagList treeList = new NBTTagList();

        NBTTagCompound tree = new NBTTagCompound();
        tree.setString("treeName", "modpots_small_oak");
        tree.setString("logs", String.format("%s,0", thinLogs1));
        tree.setString("leaves", String.format("%s,0", mcLeaves1));
        tree.setBoolean("requireLeafDecayCheck", false);
        treeList.appendTag(tree);

        tree = new NBTTagCompound();
        tree.setString("treeName", "modpots_small_spruce");
        tree.setString("logs", String.format("%s,1", thinLogs1));
        tree.setString("leaves", String.format("%s,1", mcLeaves1));
        tree.setBoolean("requireLeafDecayCheck", false);
        treeList.appendTag(tree);

        tree = new NBTTagCompound();
        tree.setString("treeName", "modpots_small_birch");
        tree.setString("logs", String.format("%s,2", thinLogs1));
        tree.setString("leaves", String.format("%s,2", mcLeaves1));
        tree.setBoolean("requireLeafDecayCheck", false);
        treeList.appendTag(tree);

        tree = new NBTTagCompound();
        tree.setString("treeName", "modpots_small_jungle");
        tree.setString("logs", String.format("%s,3", thinLogs1));
        tree.setString("leaves", String.format("%s,3", mcLeaves1));
        tree.setBoolean("requireLeafDecayCheck", false);
        treeList.appendTag(tree);

        tree = new NBTTagCompound();
        tree.setString("treeName", "modpots_small_acacia");
        tree.setString("logs", String.format("%s,5", thinLogs1));
        tree.setString("leaves", String.format("%s,0", mcLeaves2));
        tree.setBoolean("requireLeafDecayCheck", false);
        treeList.appendTag(tree);

        tree = new NBTTagCompound();
        tree.setString("treeName", "modpots_small_darkoak");
        tree.setString("logs", String.format("%s,6", thinLogs1));
        tree.setString("leaves", String.format("%s,1", mcLeaves2));
        tree.setBoolean("requireLeafDecayCheck", false);
        treeList.appendTag(tree);

        tpModCfg.setTag("trees", treeList);

        FMLInterModComms.sendMessage(TREECAPITATOR_ID, "ThirdPartyModConfig", tpModCfg);
    }
}
