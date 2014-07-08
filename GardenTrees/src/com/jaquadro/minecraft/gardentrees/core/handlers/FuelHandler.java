package com.jaquadro.minecraft.gardentrees.core.handlers;

import com.jaquadro.minecraft.gardentrees.item.ItemThinLog;
import com.jaquadro.minecraft.gardentrees.item.ItemThinLogFence;
import cpw.mods.fml.common.IFuelHandler;
import net.minecraft.item.ItemStack;

public class FuelHandler implements IFuelHandler
{
    @Override
    public int getBurnTime (ItemStack fuel) {
        if (fuel.getItem() instanceof ItemThinLog)
            return 150;
        if (fuel.getItem() instanceof ItemThinLogFence)
            return 150;

        return 0;
    }
}
