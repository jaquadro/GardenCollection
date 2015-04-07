package com.jaquadro.minecraft.gardenapi.internal.registry;

import com.jaquadro.minecraft.gardenapi.api.machine.ICompostMaterial;
import com.jaquadro.minecraft.gardenapi.api.machine.ICompostRegistry;
import com.jaquadro.minecraft.gardenapi.api.machine.StandardCompostMaterial;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.BlockVine;
import net.minecraft.block.IGrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

public class CompostRegistry implements ICompostRegistry
{
    private static ICompostMaterial defaultMaterial = new StandardCompostMaterial();

    private UniqueMetaRegistry<ICompostMaterial> itemRegistry;
    private Map<String, ICompostMaterial> oreDictRegistry;
    private Map<Class, ICompostMaterial> classRegistry;

    public CompostRegistry () {
        itemRegistry = new UniqueMetaRegistry<ICompostMaterial>();
        oreDictRegistry = new HashMap<String, ICompostMaterial>();
        classRegistry = new HashMap<Class, ICompostMaterial>();

        init();
    }

    private void init () {
        registerCompostMaterial(new ItemStack(Blocks.melon_block), defaultMaterial);
        registerCompostMaterial(new ItemStack(Blocks.pumpkin), defaultMaterial);
        registerCompostMaterial(new ItemStack(Blocks.hay_block), defaultMaterial);
        registerCompostMaterial(new ItemStack(Items.string), new StandardCompostMaterial(100, 0.0625f));
        registerCompostMaterial(new ItemStack(Items.wheat), new StandardCompostMaterial(100, 0.125f));
        registerCompostMaterial(new ItemStack(Items.reeds), new StandardCompostMaterial(150, 0.125f));
        registerCompostMaterial(new ItemStack(Items.feather), new StandardCompostMaterial(50, 0.0625f));
        registerCompostMaterial(new ItemStack(Items.rotten_flesh), new StandardCompostMaterial(150, 0.125f));
        registerCompostMaterial(new ItemStack(Items.leather), new StandardCompostMaterial(150, 0.125f));

        registerCompostMaterial("treeWood", new StandardCompostMaterial(300, 0.25f));
        registerCompostMaterial("logWood", new StandardCompostMaterial(300, 0.25f));
        registerCompostMaterial("treeLeaves", defaultMaterial);
        registerCompostMaterial("treeSapling", defaultMaterial);
        registerCompostMaterial("stickWood", defaultMaterial);

        registerCompostMaterial(IPlantable.class, defaultMaterial);
        registerCompostMaterial(IGrowable.class, defaultMaterial);
        registerCompostMaterial(BlockLeavesBase.class, defaultMaterial);
        registerCompostMaterial(BlockVine.class, defaultMaterial);
        registerCompostMaterial(ItemFood.class, defaultMaterial);
    }

    @Override
    public void registerCompostMaterial (String modId, String itemId, int meta, ICompostMaterial materialInfo) {
        if (modId != null && itemId != null && materialInfo != null) {
            UniqueMetaIdentifier id = new UniqueMetaIdentifier(modId, itemId, meta);
            itemRegistry.register(id, materialInfo);
        }
    }

    @Override
    public void registerCompostMaterial (String modId, String itemId, ICompostMaterial materialInfo) {
        if (modId != null && itemId != null && materialInfo != null) {
            UniqueMetaIdentifier id = new UniqueMetaIdentifier(modId, itemId);
            itemRegistry.register(id, materialInfo);
        }
    }

    @Override
    public void registerCompostMaterial (ItemStack itemStack, ICompostMaterial materialInfo) {
        UniqueMetaIdentifier id = UniqueMetaIdentifier.createFor(itemStack);
        if (id != null && materialInfo != null)
            itemRegistry.register(id, materialInfo);
    }

    @Override
    public void registerCompostMaterial (String oreDictionaryKey, ICompostMaterial materialInfo) {
        if (oreDictionaryKey != null && materialInfo != null)
            oreDictRegistry.put(oreDictionaryKey, materialInfo);
    }

    @Override
    public void registerCompostMaterial (Class clazz, ICompostMaterial materialInfo) {
        if (clazz != null && materialInfo != null)
            classRegistry.put(clazz, materialInfo);
    }

    @Override
    public void removeCompostMaterial (ItemStack itemStack) {
        itemRegistry.remove(UniqueMetaIdentifier.createFor(itemStack));
    }

    @Override
    public void removeCompostMaterial (String oreDictionaryKey) {
        if (oreDictionaryKey != null)
            oreDictRegistry.remove(oreDictionaryKey);
    }

    @Override
    public void removeCompostMaterial (Class clazz) {
        if (clazz != null)
            classRegistry.remove(clazz);
    }

    @Override
    public void clear () {
        itemRegistry.clear();
        oreDictRegistry.clear();
        classRegistry.clear();
    }

    @Override
    public ICompostMaterial getCompostMaterialInfo (ItemStack itemStack) {
        if (itemStack == null)
            return null;

        UniqueMetaIdentifier id = UniqueMetaIdentifier.createFor(itemStack);
        if (id != null) {
            ICompostMaterial entry = itemRegistry.getEntry(id);
            if (entry != null)
                return entry;
        }

        for (int oreId : OreDictionary.getOreIDs(itemStack)) {
            String oreEntry = OreDictionary.getOreName(oreId);
            if (oreEntry == null)
                continue;

            ICompostMaterial entry = oreDictRegistry.get(oreEntry);
            if (entry != null)
                return entry;
        }

        if (itemStack.getItem() instanceof ItemBlock) {
            Block block = Block.getBlockFromItem(itemStack.getItem());
            Class clazz = block.getClass();

            while (clazz != null) {
                if (classRegistry.containsKey(clazz))
                    return classRegistry.get(clazz);

                for (Class iface : clazz.getInterfaces()) {
                    if (classRegistry.containsKey(iface))
                        return classRegistry.get(iface);
                }

                clazz = clazz.getSuperclass();
            }
        }
        else if (itemStack.getItem() != null) {
            Class clazz = itemStack.getItem().getClass();

            while (clazz != null) {
                if (classRegistry.containsKey(clazz))
                    return classRegistry.get(clazz);

                for (Class iface : clazz.getInterfaces()) {
                    if (classRegistry.containsKey(iface))
                        return classRegistry.get(iface);
                }

                clazz = clazz.getSuperclass();
            }
        }

        return null;
    }
}
