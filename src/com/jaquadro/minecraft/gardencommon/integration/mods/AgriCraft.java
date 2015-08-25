package com.jaquadro.minecraft.gardencommon.integration.mods;

import com.InfinityRaider.AgriCraft.api.API;
import com.InfinityRaider.AgriCraft.api.APIBase;
import com.InfinityRaider.AgriCraft.api.APIStatus;
import com.InfinityRaider.AgriCraft.api.v1.APIv1;
import com.InfinityRaider.AgriCraft.api.v1.BlockWithMeta;
import com.jaquadro.minecraft.gardencommon.integration.IntegrationModule;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;

public class AgriCraft extends IntegrationModule
{
    @Override
    public String getModID () {
        return "AgriCraft";
    }

    @Override
    public void init () throws Throwable {
        APIBase api = API.getAPI(1);
        if (api.getStatus() == APIStatus.OK && api.getVersion() == 1) {
            APIv1 agricraft = (APIv1)api;

            agricraft.registerDefaultSoil(new BlockWithMeta(ModBlocks.gardenFarmland));
        }
    }

    @Override
    public void postInit () throws Throwable { }
}
