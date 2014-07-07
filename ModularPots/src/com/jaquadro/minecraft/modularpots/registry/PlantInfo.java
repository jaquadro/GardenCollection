package com.jaquadro.minecraft.modularpots.registry;

import com.jaquadro.minecraft.modularpots.block.support.UniqueMetaIdentifier;
import net.minecraft.block.Block;

/**
 * Created by Justin on 5/26/2014.
 */
public class PlantInfo
{
    public UniqueMetaIdentifier id;


    public Block getBlock () {
        return id.getBlock();
    }


}
