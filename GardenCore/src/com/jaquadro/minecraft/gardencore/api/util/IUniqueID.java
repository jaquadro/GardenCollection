package com.jaquadro.minecraft.gardencore.api.util;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;

/**
 * A unique identifier that carries the owning mod ID of blocks and items, in addition to name and metadata.
 * Serves a similar purpose to the {@link GameRegistry.UniqueIdentifier}.
 */
public interface IUniqueID
{
    /**
     * Gets the string ID of the mod that owns the represented block or item.
     */
    String getModId ();

    /**
     * Gets the registered name of the block or item.  The name is not qualified with the mod ID.
     */
    String getName ();

    /**
     * Gets the meta or damage value associated with the unique block or item state.
     * A value of {@link OreDictionary#WILDCARD_VALUE} may indicate the unique ID represents a class of blocks or items.
     */
    int getMeta ();

    /**
     * Gets the Forge {@link GameRegistry.UniqueIdentifier} representation of the unique ID, which doesn't carry the mod ID.
     */
    GameRegistry.UniqueIdentifier getUniqueIdentifier ();

    /**
     * Gets the block instance associated with the unique ID.
     *
     * @return null if there is no block registered with the given mod ID and name.
     */
    Block getBlock ();
}
