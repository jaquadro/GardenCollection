package com.jaquadro.minecraft.modularpots.integration;

import com.jaquadro.minecraft.modularpots.core.ModBlocks;
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
        String mcLeaves1 = "minecraft:leaves";
        String mcLeaves2 = "minecraft:leaves2";

        NBTTagCompound tpModCfg = new NBTTagCompound();
        tpModCfg.setString("modID", ModularPots.MOD_ID);

        NBTTagList treeList = new NBTTagList();

        NBTTagCompound tree = new NBTTagCompound();
        tree.setString("treeName", "small_oak");
        tree.setString("logs", String.format("%s,0", thinLogs1));
        tree.setString("leaves", String.format("%s,0; %s,8", mcLeaves1, mcLeaves1));
        tree.setBoolean("requireLeafDecayCheck", false);
        treeList.appendTag(tree);

        tree = new NBTTagCompound();
        tree.setString("treeName", "small_spruce");
        tree.setString("logs", String.format("%s,1", thinLogs1));
        tree.setString("leaves", String.format("%s,1; %s,9", mcLeaves1, mcLeaves1));
        tree.setBoolean("requireLeafDecayCheck", false);
        treeList.appendTag(tree);

        tree = new NBTTagCompound();
        tree.setString("treeName", "small_birch");
        tree.setString("logs", String.format("%s,2", thinLogs1));
        tree.setString("leaves", String.format("%s,2; %s,10", mcLeaves1, mcLeaves1));
        tree.setBoolean("requireLeafDecayCheck", false);
        treeList.appendTag(tree);

        tree = new NBTTagCompound();
        tree.setString("treeName", "small_jungle");
        tree.setString("logs", String.format("%s,3", thinLogs1));
        tree.setString("leaves", String.format("%s,3; %s,11", mcLeaves1, mcLeaves1));
        tree.setBoolean("requireLeafDecayCheck", false);
        treeList.appendTag(tree);

        tree = new NBTTagCompound();
        tree.setString("treeName", "small_acacia");
        tree.setString("logs", String.format("%s,5", thinLogs1));
        tree.setString("leaves", String.format("%s,0; %s,8", mcLeaves2, mcLeaves2));
        tree.setBoolean("requireLeafDecayCheck", false);
        treeList.appendTag(tree);

        tree = new NBTTagCompound();
        tree.setString("treeName", "small_darkoak");
        tree.setString("logs", String.format("%s,6", thinLogs1));
        tree.setString("leaves", String.format("%s,1; %s,9", mcLeaves2, mcLeaves2));
        tree.setBoolean("requireLeafDecayCheck", false);
        treeList.appendTag(tree);

        tpModCfg.setTag("trees", treeList);

        FMLInterModComms.sendMessage(TREECAPITATOR_ID, "ThirdPartyModConfig", tpModCfg);
    }
}
