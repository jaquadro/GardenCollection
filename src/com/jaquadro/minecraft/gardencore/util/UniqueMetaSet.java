package com.jaquadro.minecraft.gardencore.util;

import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;
import java.util.Set;

public class UniqueMetaSet<E>
{
    private Set<UniqueMetaIdentifier> registry;

    public UniqueMetaSet () {
        registry = new HashSet<UniqueMetaIdentifier>();
    }

    public void register (UniqueMetaIdentifier id) {
        registry.add(id);
    }

    public boolean contains (UniqueMetaIdentifier id) {
        if (registry.contains(id))
            return true;

        if (id.meta != OreDictionary.WILDCARD_VALUE) {
            id = new UniqueMetaIdentifier(id.modId, id.name, OreDictionary.WILDCARD_VALUE);
            if (registry.contains(id))
                return true;
        }

        return false;
    }
}