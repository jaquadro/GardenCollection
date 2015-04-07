package com.jaquadro.minecraft.gardenapi.api.machine;

import net.minecraft.item.ItemStack;

public interface ICompostRegistry
{
    void registerCompostMaterial (String modId, String itemId, int meta, ICompostMaterial materialInfo);

    void registerCompostMaterial (String modId, String itemId, ICompostMaterial materialInfo);

    void registerCompostMaterial (ItemStack itemStack, ICompostMaterial materialInfo);

    void registerCompostMaterial (String oreDictionaryKey, ICompostMaterial materialInfo);

    void registerCompostMaterial (Class clazz, ICompostMaterial materialInfo);

    void removeCompostMaterial (ItemStack itemStack);

    void removeCompostMaterial (String oreDictionaryKey);

    void removeCompostMaterial (Class clazz);

    void clear ();

    ICompostMaterial getCompostMaterialInfo (ItemStack itemStack);
}
